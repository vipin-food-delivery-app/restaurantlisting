package com.vipin.restaurantlisting.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.vipin.restaurantlisting.dto.RestaurantDTO;
import com.vipin.restaurantlisting.service.RestaurantService;

public class RestaurantControllerTest {

    @InjectMocks 
    RestaurantController restaurantController;

    @Mock 
    RestaurantService restaurantService;

    @BeforeEach  
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test 
    public void testFetchAllRestaurants(){
        List<RestaurantDTO> mockRestaurants=Arrays.asList(
            new RestaurantDTO(1,"Restaurant 1","Address 1","city 1","Description 1"),
            new RestaurantDTO(2,"Restaurant 2","Address 2","city 2","Description 2")
        );
        when(restaurantService.findAllRestaurants()).thenReturn(mockRestaurants);

        ResponseEntity<List<RestaurantDTO>> response=restaurantController.fetchAllRestaurants();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockRestaurants, response.getBody());

        verify(restaurantService, times(1)).findAllRestaurants();

    }

    @Test 
    public void testSaveRestaurant(){
        RestaurantDTO mockRestaurant=new RestaurantDTO(2,"Restaurant 2","Address 2","city 2","Description 2");
        when(restaurantService.addRestaurantInDB(mockRestaurant)).thenReturn(mockRestaurant);

        ResponseEntity<RestaurantDTO> response=restaurantController.saveRestaurant(mockRestaurant);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockRestaurant, response.getBody());

        verify(restaurantService, times(1)).addRestaurantInDB(mockRestaurant);

    }

    @Test 
    public void testFindRestaurantById(){
        Integer mockRestaurantId = 1;
        RestaurantDTO mockRestaurant=new RestaurantDTO(2,"Restaurant 2","Address 2","city 2","Description 2");
       
        when(restaurantService.fetchRestaurantById(mockRestaurantId)).thenReturn(new ResponseEntity<>(mockRestaurant,HttpStatus.OK));

        ResponseEntity<RestaurantDTO> response=restaurantController.findRestaurantById(mockRestaurantId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockRestaurant, response.getBody());

        verify(restaurantService, times(1)).fetchRestaurantById(mockRestaurantId);

    }
}
