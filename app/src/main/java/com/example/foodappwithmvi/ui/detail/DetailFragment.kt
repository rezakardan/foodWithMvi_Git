package com.example.foodappwithmvi.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.foodappwithmvi.R
import com.example.foodappwithmvi.data.database.FoodEntity
import com.example.foodappwithmvi.databinding.FragmentDetailBinding
import com.example.foodappwithmvi.utils.network.ConnectionStatus
import com.example.foodappwithmvi.utils.network.NetworkService
import com.example.foodappwithmvi.view.detail.DetailIntent
import com.example.foodappwithmvi.view.detail.DetailState
import com.example.foodappwithmvi.view.detail.DetailViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment() {
    lateinit var binding: FragmentDetailBinding


    private val viewModel: DetailViewModel by viewModels()


    private var foodId = 0


    private val args: DetailFragmentArgs by navArgs()

    @Inject
    lateinit var entity: FoodEntity


    private var isFavorite = false


    @Inject
    lateinit var internet: NetworkService


    enum class PageState { NETWORK, EMPTY, NONE }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        foodId = args.foodId


        binding.detailBack.setOnClickListener {

            lifecycleScope.launch {
                viewModel.intentChannel.send(DetailIntent.BackToHome)
            }
        }

        lifecycleScope.launch {


            viewModel.state.collect { detailState ->


                when (detailState) {

                    is DetailState.FoodDetail -> {

                        checkInternetOrEmpty(false, PageState.NONE)
                        binding.detailLoading.visibility = View.GONE
                        binding.detailContentLay.visibility = View.VISIBLE

                        detailState.food.meals!!.get(0).let { itMeal ->


                            entity.id = foodId
                            entity.img = itMeal.strMealThumb.toString()
                            entity.title = itMeal.strMeal.toString()




                            binding.foodCoverImg.load(itMeal.strMealThumb)



                            binding.foodCategoryTxt.text = itMeal.strCategory.toString()
                            binding.foodAreaTxt.text = itMeal.strArea.toString()
                            binding.foodTitleTxt.text = itMeal.strMeal.toString()

                            binding.foodDescTxt.text = itMeal.strInstructions.toString()











                            if (itMeal.strSource != null) {

                                binding.foodSourceImg.visibility = View.VISIBLE
                                binding.foodSourceImg.setOnClickListener {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(itMeal.strSource))

                                startActivity(intent)


                            }

                            }else {
                                binding.foodSourceImg.visibility = View.GONE



                            }


//Play
                            if (itMeal.strYoutube != null) {
                                binding.foodPlayImg.visibility = View.VISIBLE
                                binding.foodPlayImg.setOnClickListener {

                                    val intent=Intent(Intent.ACTION_VIEW,Uri.parse(itMeal.strYoutube))
                                    startActivity(intent)
                                    }
                                } else {
                                binding.foodPlayImg.visibility = View.GONE
                            }





                            val jsonData = JSONObject(Gson().toJson(detailState.food))

                            val meals = jsonData.getJSONArray("meals")

                            val meal = meals.getJSONObject(0)


                            for (i in 1..15) {

                                val ingredient = meal.getString("strIngredient$i")
                                if (ingredient.isNullOrEmpty().not()) {
                                    binding.ingredientsTxt.append("$ingredient\n")

                                }
                            }

                            for (i in 1..15) {

                                val measure = meal.getString("strMeasure$i")
                                if (measure.isNullOrEmpty().not()) {
                                    binding.measureTxt.append("$measure\n")

                                }
                            }


                        }


                    }


                    is DetailState.BackToHome -> {
                        findNavController().navigateUp()
                    }


                    is DetailState.ExistFood -> {

                        isFavorite = detailState.isOrNo

                        if (detailState.isOrNo) {


                            binding.detailFav.setColorFilter(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.tartOrange
                                )
                            )


                        } else {

                            binding.detailFav.setColorFilter(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.black
                                )
                            )
                        }


                    }


                    is DetailState.Error -> {
                        Toast.makeText(
                            requireContext(),
                            detailState.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                    is DetailState.SaveFood -> {}


                    is DetailState.DeleteFood -> {}


                    is DetailState.LoadProgress -> {


                        binding.detailContentLay.visibility = View.GONE
                        binding.detailLoading.visibility = View.VISIBLE
                    }

                }


            }


        }

        lifecycleScope.launch {
            internet.observe().collect {

                when (it) {


                    ConnectionStatus.Status.Available -> {
                        viewModel.intentChannel.send(DetailIntent.FoodDetail(foodId))

                        viewModel.intentChannel.send(DetailIntent.ExistFood(foodId))


                    }

                    ConnectionStatus.Status.UnAvailable -> {}

                    ConnectionStatus.Status.Losing -> {}

                    ConnectionStatus.Status.Lost -> {


                        checkInternetOrEmpty(true, PageState.NETWORK)

                    }
                }


            }

        }





        binding.detailFav.setOnClickListener {


            if (!isFavorite) {

                lifecycleScope.launch {

                    viewModel.intentChannel.send(DetailIntent.SaveFood(entity))

                }


            } else {

                lifecycleScope.launch {

                    viewModel.intentChannel.send(DetailIntent.DeleteFood(entity))

                }
            }


        }


    }


    private fun checkInternetOrEmpty(showErrorOrNo: Boolean, state: PageState) {

        if (showErrorOrNo) {

            binding.detailContentLay.visibility = View.GONE
            binding.homeDisLay.visibility = View.VISIBLE




            when (state) {


                PageState.EMPTY -> {

                    binding.statusLay.disImg.setImageResource(R.drawable.box)
                    binding.statusLay.disTxt.text = getString(R.string.emptyList)
                }

                PageState.NETWORK -> {
                    binding.statusLay.disImg.setImageResource(R.drawable.disconnect)
                    binding.statusLay.disTxt.text = getString(R.string.checkInternet)
                }

                PageState.NONE -> {}


            }


        } else {
            binding.detailContentLay.visibility = View.VISIBLE
            binding.homeDisLay.visibility = View.GONE

        }


    }

}