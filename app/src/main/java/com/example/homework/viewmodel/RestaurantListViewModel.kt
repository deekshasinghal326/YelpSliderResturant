package com.example.homework.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import com.example.homework.entity.Restaurant
import com.example.homework.repository.RestaurantRepository
import kotlinx.coroutines.launch

class RestaurantListViewModel : ViewModel() {
    private val restaurantRepository = RestaurantRepository
    val restaurantResult = MutableLiveData<List<Restaurant?>?>()
    val refresh = MutableLiveData(false)
    val sliderRadius= MutableLiveData(0)

    /**
     * fetch event trigger then change value of [restaurantResult]
     */
    fun fetchRestaurant(location: String?,radius: Int?,limit :Int ) {
        refresh.value = true
        viewModelScope.launch {
            restaurantRepository.fetchRestaurants(location, radius, limit).collect {
                refresh.value = false
                when (it) {
                    is RestaurantRepository.Callback.SuccessFetch -> {
                        restaurantResult.value = it.restaurant
                    }
                    else -> {
                        restaurantResult.value = null
                    }
                }
            }
        }
    }
}