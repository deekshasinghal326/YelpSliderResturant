package com.example.homework.ui

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.homework.R
import com.example.homework.adapter.RestaurantListAdapter
import com.example.homework.base.BaseActivity
import com.example.homework.utils.UiUtils
import com.example.homework.viewmodel.RestaurantListViewModel
import kotlinx.android.synthetic.main.activity_restaurant_list.*

class RestaurantListActivity : BaseActivity() {

    private val viewModel: RestaurantListViewModel by viewModels()
    private val adapter by lazy { RestaurantListAdapter() }
    private val userLocation = "New York City"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRecyclerview()
        observe()
        fetchData()
        srl_refresh.setOnRefreshListener { fetchData() }
        radiusSlider.addOnChangeListener { _, value, _ ->
            tvRadius.text = changeDistanceInKM(value)
            tvStartRadius.text = changeDistanceInKM(value)
            viewModel.sliderRadius.value = value.toString().substringBefore(".").toInt()
            fetchData()
        }

    }

    private fun fetchData() {
        viewModel.fetchRestaurant(
            userLocation,
            viewModel.sliderRadius.value,
            15
        )
    }

    private fun setupRecyclerview() {
        rv_content.adapter = adapter
        rv_content.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                val gap = UiUtils.dp2px(view.context, 8)
                outRect.left = gap
                outRect.right = gap
                outRect.top = gap
                outRect.bottom = gap
            }
        })
    }

    /**
     * observe the fetch result
     */
    private fun observe() {
        viewModel.restaurantResult.observe(this) { result ->
            if (result == null) {
                Toast.makeText(this, R.string.failed_fetch, Toast.LENGTH_LONG).show()
            } else {
                adapter.setDatas(result)
            }
        }
        viewModel.refresh.observe(this) {
            srl_refresh.post {
                // Causes the Runnable to be added to the message queue.
                // The runnable will be run on the user interface thread.
                srl_refresh.isRefreshing = it
            }
        }
        viewModel.sliderRadius.observe(this) {
           fetchData()
        }
    }

    private fun changeDistanceInKM(value: Float): String {
        val distance = (value / 1000)
        return "$distance KM"
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_restaurant_list
    }

    override fun setTitle(): String {
        return "Restaurant List"
    }
}