package com.vipin.restaurantlisting.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.vipin.restaurantlisting.dto.RestaurantDTO;
import com.vipin.restaurantlisting.entity.Restaurant;

@Mapper 
public interface RestaurantMapper {

    RestaurantMapper INSTANCE =Mappers.getMapper(RestaurantMapper.class);

    Restaurant mapRestaurantDTOToRestaurant(RestaurantDTO restaurantDTO);
    
    RestaurantDTO mapRestaurantToRestaurantDTO(Restaurant restaurant);
}
