package com.example.foodappwithmvi.ui.list.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.foodappwithmvi.data.model.ResponseFoodsList
import com.example.foodappwithmvi.databinding.ItemRecyclerFoodlistBinding
import javax.inject.Inject

class FoodListAdapter@Inject constructor():RecyclerView.Adapter<FoodListAdapter.FoodListViewHolder>() {

    lateinit var binding: ItemRecyclerFoodlistBinding

    private var foodList= emptyList<ResponseFoodsList.Meal>()
    inner class FoodListViewHolder(item: View):RecyclerView.ViewHolder(item){


        fun onBind(oneItem:ResponseFoodsList.Meal){

binding.itemFoodsImg.load(oneItem.strMealThumb){
    crossfade(true)
    crossfade(500)
}
            binding.itemFoodsTitle.text=oneItem.strMeal

            if (oneItem.strCategory.isNullOrEmpty().not()){

                binding.itemFoodsCategory.text=oneItem.strCategory
                binding.itemFoodsCategory.visibility=View.VISIBLE


            }else{
                binding.itemFoodsCategory.visibility=View.GONE

            }


            if (oneItem.strArea.isNullOrEmpty().not()){


                binding.itemFoodsArea.text=oneItem.strArea

                binding.itemFoodsArea.visibility=View.VISIBLE

            }else{
                binding.itemFoodsArea.visibility=View.GONE

            }

            if (oneItem.strSource!=null){

                binding.itemFoodsCount.visibility=View.VISIBLE


            }else{

                binding.itemFoodsCount.visibility=View.GONE

            }


            binding.root.setOnClickListener {

              onItemClick?.let {

                  it(oneItem)


              }




            }

        }




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodListViewHolder {
        binding= ItemRecyclerFoodlistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    return FoodListViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: FoodListViewHolder, position: Int) {
 holder.onBind(foodList[position])
     holder.setIsRecyclable(false)


    }

    override fun getItemCount()=foodList.size




    private var onItemClick:((ResponseFoodsList.Meal)->Unit)?=null

    fun setOnItemClickListener(listener:(ResponseFoodsList.Meal)->Unit){

        onItemClick=listener






    }


    fun setData(data:List<ResponseFoodsList.Meal>){

        val diff=FoodDiffUtil(foodList,data)

        val differ=DiffUtil.calculateDiff(diff)
foodList=data
        differ.dispatchUpdatesTo(this)



    }



    class FoodDiffUtil(private val oldItem:List<ResponseFoodsList.Meal>,private val newItem:List<ResponseFoodsList.Meal>):DiffUtil.Callback(){
        override fun getOldListSize(): Int {
            return oldItem.size
        }

        override fun getNewListSize(): Int {
            return newItem.size        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return newItem[newItemPosition]===  oldItem[oldItemPosition]      }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return newItem[newItemPosition]===  oldItem[oldItemPosition]          }
    }

}