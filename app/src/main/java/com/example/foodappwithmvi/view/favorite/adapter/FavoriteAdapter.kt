package com.example.foodappwithmvi.view.favorite.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.foodappwithmvi.data.database.FoodEntity
import com.example.foodappwithmvi.databinding.ItemRecyclerFoodlistBinding
import javax.inject.Inject

class FavoriteAdapter@Inject constructor():RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    lateinit var binding: ItemRecyclerFoodlistBinding


    private var foodList= emptyList<FoodEntity>()

    inner class FavoriteViewHolder(item:View):RecyclerView.ViewHolder(item){


fun onBind(oneItem:FoodEntity){

    binding.itemFoodsTitle.text=oneItem.title
    binding.itemFoodsImg.load(oneItem.img){
        crossfade(true)
        crossfade(500)
    }
    binding.itemFoodsArea.visibility=View.GONE
    binding.itemFoodsCount.visibility=View.GONE
    binding.itemFoodsCategory.visibility=View.GONE

binding.root.setOnClickListener {

    onItemClickListener?.let {

        it(oneItem)


    }




}


}





    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
binding=ItemRecyclerFoodlistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    return FavoriteViewHolder((binding.root))
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
holder. onBind(foodList[position])
    holder.setIsRecyclable(false)
    }

    override fun getItemCount()=foodList.size


    private var onItemClickListener:((FoodEntity)->Unit)?=null

    fun setOnItemClickListener(listener:(FoodEntity)->Unit){

        onItemClickListener=listener



    }





    fun setFavoriteData( data:List<FoodEntity>){

        val diff=FavoriteDiffUtils(foodList,data)

        val diffuti=DiffUtil.calculateDiff(diff)

        foodList=data

        diffuti.dispatchUpdatesTo(this)

    }


    class FavoriteDiffUtils(private val oldItem:List<FoodEntity>,private val newItem:List<FoodEntity>):DiffUtil.Callback(){
        override fun getOldListSize(): Int {
            return oldItem.size
        }

        override fun getNewListSize(): Int {
return newItem.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
return oldItem[oldItemPosition]===newItem[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition]===newItem[newItemPosition]
        }


    }


}