package com.example.foodappwithmvi.view.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodappwithmvi.data.database.FoodEntity
import com.example.foodappwithmvi.data.repository.DetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel@Inject constructor(private val repository: DetailRepository):ViewModel() {



    init {
        handleIntents()
    }

    val intentChannel=Channel<DetailIntent>()

    private val _state=MutableStateFlow<DetailState>(DetailState.LoadProgress)

    val state:StateFlow<DetailState>get() = _state



    private fun handleIntents()=viewModelScope.launch {

        intentChannel.consumeAsFlow().collect{detailIntent->


            when(detailIntent){


                is DetailIntent.FoodDetail->{foodDetail(detailIntent.id)}

                is DetailIntent.SaveFood->{saveFood(detailIntent.food)}


                is DetailIntent.DeleteFood->{deleteFood(detailIntent.food)}

                is DetailIntent.ExistFood->{existFood(detailIntent.id)}

                is DetailIntent.BackToHome->{backToHome()}






            }















        }



    }

    private fun backToHome()=viewModelScope.launch {

        _state.emit(DetailState.BackToHome)
    }

    private fun existFood(id: Int)=viewModelScope.launch {

        repository.existFood(id).collect{

          _state.emit(DetailState.ExistFood(it))


        }



    }

    private fun deleteFood(food: FoodEntity)=viewModelScope.launch {


        _state.emit(DetailState.DeleteFood(repository.deleteFood(food)))
    }

    private fun saveFood(food: FoodEntity)=viewModelScope.launch {

        _state.emit(DetailState.SaveFood(repository.saveFood(food)))




    }

    private fun foodDetail(id: Int)=viewModelScope.launch {

        _state.emit(DetailState.LoadProgress)

        val response=repository.detailFood(id)

        when(response.code()){

            in 200..202->{
                _state.emit(DetailState.FoodDetail(response.body()!!))

            }

            in 400..499->{
                _state.emit(DetailState.Error(""))

            }
            in 500..599->{
                _state.emit(DetailState.Error(""))

            }

        }





    }


}