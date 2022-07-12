package com.example.homework.holder

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.homework.entity.Restaurant
import com.example.homework.utils.FrescoLoader
import kotlinx.android.synthetic.main.item_restaurant_list.view.*
import kotlin.math.roundToInt

class RestaurantViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private var data: Restaurant? = null

    /**
     * bind data from adapter
     */
    @SuppressLint("SetTextI18n")
    fun bindData(restaurant: Restaurant?) {
        data = restaurant
        FrescoLoader.load(restaurant?.image_url, itemView.restLogo)
        itemView.tvRestroName.text = restaurant?.name?: ""
        itemView.tvAddress.text = restaurant?.distance?.toString()?.substringBefore(".")+
                ", " + restaurant?.location?.getAddress()
        itemView.tvOpenStatus.text = "Currently " + if(restaurant?.is_closed==true)"Closed"
        else "Open"
        itemView.tvDistance.text = restaurant?.rating
    }
}
