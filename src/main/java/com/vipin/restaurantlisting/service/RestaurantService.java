package com.vipin.restaurantlisting.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.vipin.restaurantlisting.dto.RestaurantDTO;
import com.vipin.restaurantlisting.entity.Restaurant;
import com.vipin.restaurantlisting.mapper.RestaurantMapper;
import com.vipin.restaurantlisting.repo.RestaurantRepo;


@Service 
public class RestaurantService {
     final RestaurantRepo restaurantRepo;

    RestaurantService(RestaurantRepo restaurantRepo) {
        this.restaurantRepo = restaurantRepo;
    }
    public List<RestaurantDTO> findAllRestaurants() {

        List<Restaurant> restaurants= restaurantRepo.findAll();
        //map it to DTO's
        List<RestaurantDTO> restaurantDTO= restaurants.stream()
        .map(restaurant->RestaurantMapper.INSTANCE.mapRestaurantToRestaurantDTO((restaurant))).collect(Collectors.toList());
        return restaurantDTO;

    }

    public RestaurantDTO addRestaurantInDB(RestaurantDTO restaurantDTO) {
        Restaurant savedRestaurant=restaurantRepo.save(RestaurantMapper.INSTANCE.mapRestaurantDTOToRestaurant(restaurantDTO));
        return RestaurantMapper.INSTANCE.mapRestaurantToRestaurantDTO(savedRestaurant);
    }

    public ResponseEntity<RestaurantDTO> fetchRestaurantById(Integer id) {
        Optional<Restaurant> restaurant=restaurantRepo.findById(id);
        if(restaurant.isPresent()){
            return new ResponseEntity<>(RestaurantMapper.INSTANCE.mapRestaurantToRestaurantDTO(restaurant.get()),HttpStatus.OK);
        }
        return ResponseEntity.<RestaurantDTO>status(HttpStatus.NOT_FOUND).build();
    }
}
