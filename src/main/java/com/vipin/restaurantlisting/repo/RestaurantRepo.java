package com.vipin.restaurantlisting.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vipin.restaurantlisting.entity.Restaurant;

public interface RestaurantRepo extends JpaRepository<Restaurant,Integer>{

}
