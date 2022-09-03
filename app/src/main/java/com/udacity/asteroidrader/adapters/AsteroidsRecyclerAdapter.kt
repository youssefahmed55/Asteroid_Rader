package com.udacity.asteroidrader.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidrader.R
import com.udacity.asteroidrader.databinding.ListAsteroidBinding


class AsteroidsRecyclerAdapter : RecyclerView.Adapter<AsteroidsRecyclerAdapter.Holder>() {
    private lateinit var mutableList : MutableList<Asteroid>
    private lateinit var onClickOnItem : OnClickOnItem

    interface OnClickOnItem {
        fun onClick1(asteroid: Asteroid)
    }

    fun setArraylist (mutableListt : MutableList<Asteroid>){
        mutableList = mutableListt
    }

    fun setOnItemClickListener (listener : OnClickOnItem){
        onClickOnItem = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding : ListAsteroidBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.list_asteroid,parent,false)
        return Holder(binding,onClickOnItem,mutableList)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.holderBinding.asteroid = mutableList[position]
    }

    override fun getItemCount(): Int {
        return mutableList.size

    }

    class Holder(binding: ListAsteroidBinding,listener: OnClickOnItem, mutableList: MutableList<Asteroid>) : RecyclerView.ViewHolder(binding.root) {
         val holderBinding : ListAsteroidBinding = binding

        init {
            binding.root.setOnClickListener {
                val position: Int = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // you can trust the adapter position
                    // do whatever you intend to do with this position
                    if (listener != null)
                        listener.onClick1(mutableList[adapterPosition])

                }
            }
        }

    }
}