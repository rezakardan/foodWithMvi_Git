package com.example.foodappwithmvi.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.foodappwithmvi.R
import com.example.foodappwithmvi.databinding.FragmentFoodListBinding
import com.example.foodappwithmvi.ui.list.adapters.AdapterCategory
import com.example.foodappwithmvi.ui.list.adapters.FoodListAdapter
import com.example.foodappwithmvi.utils.network.ConnectionStatus
import com.example.foodappwithmvi.utils.network.NetworkService
import com.example.foodappwithmvi.view.list.ListIntent
import com.example.foodappwithmvi.view.list.ListState
import com.example.foodappwithmvi.view.list.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class FoodListFragment : Fragment() {


    lateinit var binding: FragmentFoodListBinding

    private val viewModel: ListViewModel by viewModels()


    @Inject
    lateinit var categoryAdapter: AdapterCategory

    @Inject
    lateinit var foodListAdapter: FoodListAdapter

    var spinner = ""


    @Inject
    lateinit var network:NetworkService



    enum class PageState { NETWORK, EMPTY, NONE }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFoodListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launch {





            viewModel.state.collect { listState ->


                when (listState) {

                    is ListState.Idle -> {}
                    is ListState.SpinnerList -> {

                        spinnersList(listState.list)


                    }
                    is ListState.FoodRandom -> {


                        binding.headerImg.load(listState.food!!.strMealThumb)


                    }

                    is ListState.Error -> {
                        Toast.makeText(
                            requireContext(),
                            listState.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is ListState.CategoryList -> {

                        binding.categoryList.visibility = View.VISIBLE
                        binding.homeCategoryLoading.visibility = View.GONE

                        categoryAdapter.setData(listState.list)

                        binding.categoryList.adapter = categoryAdapter
                        binding.categoryList.layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )



                        categoryAdapter.setOnItemClickListener {


                            lifecycleScope.launchWhenCreated {

                                viewModel.intentChannel.send(ListIntent.ChooseFoodByCategory(it.strCategory!!))

                            }


                        }


                    }

                    is ListState.LoadCategoryProgress -> {
                        binding.categoryList.visibility = View.GONE
                        binding.homeCategoryLoading.visibility = View.VISIBLE
                    }


                    is ListState.EmptyList -> {
                        checkNetworkOrEmpty(true, PageState.EMPTY)

                        binding.foodsList.visibility = View.GONE
                        binding.homeFoodsLoading.visibility = View.VISIBLE

                    }

                    is ListState.FoodList -> {


                        checkNetworkOrEmpty(false, PageState.NONE)
                        binding.foodsList.visibility = View.VISIBLE
                        binding.homeFoodsLoading.visibility = View.GONE

                        foodListAdapter.setData(listState.foodList)


                        binding.foodsList.adapter = foodListAdapter

                        binding.foodsList.layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )




                        foodListAdapter.setOnItemClickListener {

                            val direction=FoodListFragmentDirections.actionToDetailFragment(it.idMeal!!.toInt())

                            findNavController().navigate(direction)

                        }


                    }

                    is ListState.LoadFoodListProgress -> {

                        binding.foodsList.visibility = View.GONE
                        binding.homeFoodsLoading.visibility = View.VISIBLE
                    }


                }


            }


        }

lifecycleScope.launchWhenCreated {


    network.observe().collect{


       when(it){

           ConnectionStatus.Status.Available->{


               checkNetworkOrEmpty(false,PageState.NONE)

               viewModel.intentChannel.send(ListIntent.SpinnerList)
               viewModel.intentChannel.send(ListIntent.FoodRandom)


               viewModel.intentChannel.send(ListIntent.CategoryList)

               viewModel.intentChannel.send(ListIntent.FoodList("A"))

           }

           ConnectionStatus.Status.UnAvailable->{}


           ConnectionStatus.Status.Losing->{}


           ConnectionStatus.Status.Lost->{



               checkNetworkOrEmpty(true,PageState.NETWORK)

           }

       }






    }

}





        binding.searchEdt.addTextChangedListener {

            val search = it.toString()

            if (search.length > 2) {

                lifecycleScope.launchWhenCreated {
                    viewModel.intentChannel.send(ListIntent.SearchFood(search))

                }


            }

        }


    }


    private fun spinnersList(list: List<Char>) {

        val spinnerList = list

        val adapter = ArrayAdapter(requireContext(), R.layout.item_spinner, spinnerList)


        adapter.setDropDownViewResource(R.layout.item_spinner_list)

        binding.filterSpinner.adapter = adapter


        binding.filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                spinner = spinnerList[p2].toString()

                lifecycleScope.launchWhenCreated {

                    viewModel.intentChannel.send(ListIntent.FoodList(spinner))

                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }


        }


    }


    private fun checkNetworkOrEmpty(showErrorOrNo: Boolean, state: PageState) {

        if (showErrorOrNo) {

            binding.homeDisLay.visibility = View.VISIBLE

            binding.homeContent.visibility = View.GONE

            when (state) {


                PageState.NETWORK -> {

                    binding.statusLay.disImg.setImageResource(R.drawable.disconnect)

                    binding.statusLay.disTxt.text = getString(R.string.checkInternet)


                }


                PageState.EMPTY -> {

                    binding.statusLay.disImg.setImageResource(R.drawable.box)

                    binding.statusLay.disTxt.text = getString(R.string.emptyList)


                }

                PageState.NONE -> {}
            }


        } else {
            binding.homeDisLay.visibility = View.GONE

            binding.homeContent.visibility = View.VISIBLE
        }


    }


}