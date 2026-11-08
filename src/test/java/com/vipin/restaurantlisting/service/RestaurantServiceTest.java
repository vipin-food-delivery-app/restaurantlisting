package com.vipin.restaurantlisting.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.vipin.restaurantlisting.dto.RestaurantDTO;
import com.vipin.restaurantlisting.entity.Restaurant;
import com.vipin.restaurantlisting.mapper.RestaurantMapper;
import com.vipin.restaurantlisting.repo.RestaurantRepo;

public class RestaurantServiceTest {

        @Mock 
        RestaurantRepo restaurantRepo;

        @InjectMocks 
        RestaurantService restaurantService;

        @BeforeEach 
        public void setUp(){
            MockitoAnnotations.openMocks(this);
        }

        @Test 
        public void testFindAllRestaurants(){
            List<Restaurant> mockRestaurants=Arrays.asList(
            new Restaurant(1,"Restaurant 1","Address 1","city 1","Description 1"),
            new Restaurant(2,"Restaurant 2","Address 2","city 2","Description 2")
        );
        when(restaurantRepo.findAll()).thenReturn(mockRestaurants);

        List<RestaurantDTO> response=restaurantService.findAllRestaurants();

        assertEquals(mockRestaurants.size(), response.size());

        for (int i = 0; i < mockRestaurants.size(); i++) {
            RestaurantDTO expectedDTO=RestaurantMapper.INSTANCE.mapRestaurantToRestaurantDTO(mockRestaurants.get(i));
            assertEquals(expectedDTO, response.get(i));
        }
        verify(restaurantRepo,times(1)).findAll();
        }

        @Test 
        public void testAddRestaurantInDB(){

            //Arrange mocking
            RestaurantDTO restaurantDTO=new RestaurantDTO(1,"Restaurant 1","Address 1","city 1","Description 1");
            Restaurant restaurant=RestaurantMapper.INSTANCE.mapRestaurantDTOToRestaurant(restaurantDTO);
            
            when(restaurantRepo.save(restaurant)).thenReturn(restaurant);

            RestaurantDTO response=restaurantService.addRestaurantInDB(restaurantDTO);
            //verifying the response
            assertEquals(restaurantDTO, response);
            //verifying the mocked up object's callings
            verify(restaurantRepo,times(1)).save(restaurant);
        }

        @Test 
        public void testFetchRestaurantByExistingId(){

            //Arrange mocking
            Integer restaurantId=1;
            RestaurantDTO restaurantDTO=new RestaurantDTO(1,"Restaurant 1","Address 1","city 1","Description 1");
            Restaurant restaurant=RestaurantMapper.INSTANCE.mapRestaurantDTOToRestaurant(restaurantDTO);
            
            when(restaurantRepo.findById(restaurantId)).thenReturn(Optional.of(restaurant));

            ResponseEntity<RestaurantDTO> response=restaurantService.fetchRestaurantById(restaurantId);
            //verifying the response
            assertEquals(restaurantDTO, response.getBody());
            //verifying the mocked up object's callings
            verify(restaurantRepo,times(1)).findById(restaurantId);
        }

         @Test 
        public void testFetchRestaurantByNonExistingId(){

            //Arrange mocking
            RestaurantDTO restaurantDTO=new RestaurantDTO(1,"Restaurant 1","Address 1","city 1","Description 1");
            Restaurant restaurant=RestaurantMapper.INSTANCE.mapRestaurantDTOToRestaurant(restaurantDTO);
            
            when(restaurantRepo.findById(null)).thenReturn(Optional.empty());

            ResponseEntity<RestaurantDTO> response=restaurantService.fetchRestaurantById(null);
            //verifying the response
            assertEquals(null, response.getBody());

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            //verifying the mocked up object's callings
            verify(restaurantRepo,times(1)).findById(null);
        }

}