package com.example.foodappwithmvi.ui.list.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.foodappwithmvi.R
import com.example.foodappwithmvi.data.model.ResponseCategoriesList
import com.example.foodappwithmvi.databinding.ItemRecyclerCategoryBinding
import javax.inject.Inject

class AdapterCategory @Inject constructor() :
    RecyclerView.Adapter<AdapterCategory.CategoryViewHolder>() {

    lateinit var binding: ItemRecyclerCategoryBinding

    private var categoryList = emptyList<ResponseCategoriesList.Category>()

    private var itemSelected=-1

    inner class CategoryViewHolder(item: View) : RecyclerView.ViewHolder(item) {


        fun onBind(oneItem: ResponseCategoriesList.Category) {


            binding.categoryImg.load(oneItem.strCategoryThumb) {
                crossfade(true)
                crossfade(500)
            }

            binding.categoryName.text = oneItem.strCategory.toString()




            binding.root.setOnClickListener {

                itemSelected=adapterPosition
notifyDataSetChanged()

                onItemClick?.let {

                    it(oneItem)


                }




            }

            if (itemSelected==adapterPosition){

                binding.root.setBackgroundResource(R.drawable.bg_rounded_selcted)



            }else{
                binding.root.setBackgroundResource(R.drawable.bg_rounded_white)
            }



        }







    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        binding =
            ItemRecyclerCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding.root)

    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.onBind(categoryList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = categoryList.size


    private var onItemClick:((ResponseCategoriesList.Category)->Unit)?=null

    fun setOnItemClickListener(listener:((ResponseCategoriesList.Category)->Unit)){


        onItemClick=listener



    }


    fun setData(data: List<ResponseCategoriesList.Category>) {

        val diff = DiffUtilCategory(categoryList, data)

        val differ = DiffUtil.calculateDiff(diff)

        categoryList = data

        differ.dispatchUpdatesTo(this)


    }

    class DiffUtilCategory(
        private val oldItem: List<ResponseCategoriesList.Category>,
        private val newItem: List<ResponseCategoriesList.Category>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldItem.size
        }

        override fun getNewListSize(): Int {
            return newItem.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }
    }
}