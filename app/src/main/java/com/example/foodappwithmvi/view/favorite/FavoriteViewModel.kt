package com.example.foodappwithmvi.view.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodappwithmvi.data.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavoriteViewModel@Inject constructor(private val repository: FavoriteRepository):ViewModel() {


    init {
        handleIntents()
    }

    val favoriteChannel=Channel<FavoriteIntent>()


    private val _state=MutableStateFlow<FavoriteState>(FavoriteState.EmptyList)

    val state:StateFlow<FavoriteState>get() = _state


    private fun handleIntents()=viewModelScope.launch {

        favoriteChannel.consumeAsFlow().collect{favoriteIntent->


            when(favoriteIntent){


                is FavoriteIntent.GetAllFoods->{getAllFoods()}




            }







        }







    }

    private fun getAllFoods()=viewModelScope.launch {
        repository.getAllFoods().collect{


            if (it.isNotEmpty()) {

                _state.value=FavoriteState.GetAllFoods(it)


            }else{

                _state.value=FavoriteState.EmptyList
            }


        }
    }

}