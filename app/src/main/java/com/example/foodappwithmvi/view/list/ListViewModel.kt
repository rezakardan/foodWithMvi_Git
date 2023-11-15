package com.example.foodappwithmvi.view.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodappwithmvi.data.repository.ListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val repository: ListRepository) : ViewModel() {



    init {
        handleIntents()
    }


    val intentChannel = Channel<ListIntent>()

    private val _state = MutableStateFlow<ListState>(ListState.Idle)

    val state: StateFlow<ListState> get() = _state


    private fun handleIntents() = viewModelScope.launch {


        intentChannel.consumeAsFlow().collect { listIntent ->

            when (listIntent) {


                is ListIntent.SpinnerList -> {
                    spinnersData()
                }

                is ListIntent.FoodRandom -> {
                    foodRandom()
                }

                is ListIntent.CategoryList -> {
                    categoryList()
                }

                is ListIntent.FoodList -> {
                    foodList(listIntent.letter)
                }

                is ListIntent.SearchFood -> {
                    searchFood(listIntent.letter)
                }


                is ListIntent.ChooseFoodByCategory -> {
                    chooseByCategory(listIntent.letter)
                }


            }


        }


    }

    private fun chooseByCategory(letter: String) = viewModelScope.launch {

        val response = repository.foodByCategory(letter)

        when (response.code()) {

            in 200..202 -> {


                _state.emit(ListState.FoodList(response.body()!!.meals!!))


            }
            in 400..499 -> {

                _state.emit(ListState.Error(""))
            }

            in 500..599 -> {
                _state.emit(ListState.Error(""))
            }


        }


    }

    private fun searchFood(letter: String) = viewModelScope.launch {


        _state.emit(ListState.LoadFoodListProgress)
        val response = repository.searchFood(letter)

        when (response.code()) {

            in 200..202 -> {
                _state.value = if (response.body()?.meals != null) {

                    ListState.FoodList(response.body()?.meals!!)


                } else {

                    ListState.EmptyList


                }
            }
            in 400..499 -> {

                _state.emit(ListState.Error(""))
            }

            in 500..599 -> {
                _state.emit(ListState.Error(""))
            }


        }


    }

    private fun foodList(letter: String) = viewModelScope.launch {

        _state.emit(ListState.LoadFoodListProgress)
        val response = repository.foodList(letter)

        when (response.code()) {

            in 200..202 -> {


                if (response.body()?.meals != null) {

                    _state.emit(ListState.FoodList(response.body()?.meals!!))


                } else {

                    _state.emit(ListState.EmptyList)
                }

            }

            in 400..499 -> {

                _state.emit(ListState.Error(""))
            }

            in 500..599 -> {
                _state.emit(ListState.Error(""))
            }


        }


    }

    private fun categoryList() = viewModelScope.launch {
        val response = repository.categoryFood()


        _state.emit(ListState.LoadCategoryProgress)

        when (response.code()) {

            in 200..202 -> {

                _state.emit(ListState.CategoryList(response.body()?.categories!!))
            }


            in 400..499 -> {
                _state.emit(ListState.Error(""))
            }

            in 500..599 -> {
                _state.emit(ListState.Error(""))
            }


        }


    }

    private fun foodRandom() = viewModelScope.launch {

        val response = repository.randomFood()


        when (response.code()) {

            in 200..202 -> {


                _state.emit(ListState.FoodRandom(response.body()?.meals!!.get(0)))


            }


            in 400..499 -> {


                _state.emit(ListState.Error(""))
            }


            in 500..599 -> {


                _state.emit(ListState.Error(""))
            }


        }


    }

    private fun spinnersData() = viewModelScope.launch {
        val data = listOf('A'..'Z').flatten()

        _state.emit(ListState.SpinnerList(data))


    }


}