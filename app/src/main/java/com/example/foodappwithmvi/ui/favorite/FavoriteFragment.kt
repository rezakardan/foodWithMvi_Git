package com.example.foodappwithmvi.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodappwithmvi.R
import com.example.foodappwithmvi.databinding.FragmentFavoriteBinding
import com.example.foodappwithmvi.view.favorite.FavoriteIntent
import com.example.foodappwithmvi.view.favorite.FavoriteState
import com.example.foodappwithmvi.view.favorite.FavoriteViewModel
import com.example.foodappwithmvi.view.favorite.adapter.FavoriteAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

lateinit var binding: FragmentFavoriteBinding


@Inject
lateinit var adapter:FavoriteAdapter

private val viewModel:FavoriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentFavoriteBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)








        lifecycleScope.launch {

            viewModel.favoriteChannel.send(FavoriteIntent.GetAllFoods)

            viewModel.state.collect{favoriteState->



                when(favoriteState){



                    is FavoriteState.GetAllFoods->{


                        binding.favoriteList.visibility=View.VISIBLE
                        binding.emptyLay.visibility=View.GONE

                        adapter.setFavoriteData(favoriteState.foodList)

                        binding.favoriteList.adapter=adapter
                        binding.favoriteList.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)





                        adapter.setOnItemClickListener {

                            val direction=FavoriteFragmentDirections.actionToDetailFragment(it.id)

                            findNavController().navigate(direction)




                        }



                    }

                    is FavoriteState.EmptyList->{


                        binding.favoriteList.visibility=View.GONE

                        binding.emptyLay.visibility=View.VISIBLE


                        binding.statusLay.disTxt.text=getString(R.string.emptyList)

                        binding.statusLay.disImg.setImageResource(R.drawable.empty)



                    }


            }







            }
        }












    }


}