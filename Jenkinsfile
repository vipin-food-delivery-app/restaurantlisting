pipeline{
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('DOCKER_HUB_CREDENTIAL')
        VERSION = "${env.BUILD_ID}"
    }

    tools {
        maven "Maven"
    }

    stages {
        stage('Maven Build'){
            steps{
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Run Tests'){
            steps{
                sh 'mvn test'
            }
        }


        stage('SonarQube Analysis'){
            steps{
                sh 'mvn clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
  -Dsonar.host.url=http://192.168.1.35:9000 \
  -Dsonar.projectKey=restaurantlisting \
  -Dsonar.token="squ_64a3467f5fbfb39728ef1855df57104180e565a5" \
  -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml'
            }
        }

        stage('Check code coverage'){
            steps{
                script{
                    def token = "squ_64a3467f5fbfb39728ef1855df57104180e565a5"
                    def sonarQuebeUrl = "http://192.168.1.35:9000"

                    def componentKey = "restaurantlisting"
                    def coverageThreshold = 80.0

                    def response = sh (
                        //script: "curl -H 'Authorization: Bearer ${token}' '${sonarQuebeUrl}/api/components/search?component=${componentKey}&metricKey=coverage'",
                        script: "curl -H 'Authorization: Bearer ${token}' '${sonarQuebeUrl}/api/measures/component?component=${componentKey}&metricKeys=coverage'",

                        returnStdout: true
                    ).trim()

                    // def coverage = sh(
                    //     script:"echo '${response}' | jq .components.measures[0].value",
                    //     returnStdout: true
                    // ).trim().toDouble()
                    // Parse using native Groovy JsonSlurper
                def json = new groovy.json.JsonSlurper().parseText(response)
                //def coverage = json.component.measures[0].value.doubleValue()
                def coverage = Double.parseDouble(json.component.measures[0].value.toString())

                    echo "Coverage: ${coverage}"

                    if(coverage<coverageThreshold){
                        error "Code coverage is less than the threshold ${coverageThreshold}%.Aborting the pipeline."
                    }

                    echo "Code coverage is above the threshold ${coverageThreshold}%"
                }
            }
        }

        stage('Docker Build and Push'){
            steps {
                sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
                sh 'docker build -t vipiiin/restaurantlisting-server:${VERSION} .'
                sh 'docker push vipiiin/restaurantlisting-server:${VERSION}'
            }
        }

        stage('Cleanup Workspace'){
            steps {
                deleteDir()
            }
        }
        stage('Update Image tag in GitOps'){
            steps {
                checkout scmGit(branches: [[name: '*/master']],extensions:[],userRemoteConfig: [[credentialsId: 'git-ssh', url: 'git@github.com:vipin-food-delivery-app/deployment-folder.git']])
                script {
                    sh '''
                    sed -i "s|image:.*|image: vipiiin/restaurantlisting-server:${VERSION}|g" aws/restaurant-manifest.yml\
                    '''
                    sh 'git checkout master'
                    sh 'git add .'
                    sh 'git commit -m "Update image tag to ${VERSION}"'
                    sshagent(['git-ssh'])
                    {
                        sh 'git push'
                    }
                }
            }
        }

    }
}