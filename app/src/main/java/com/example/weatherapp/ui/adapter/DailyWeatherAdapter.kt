package com.example.weatherapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ItemDailyBinding
import com.example.weatherapp.retrofit.model.List
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DailyWeatherAdapter(
    private val listener: (Int) -> Unit,
) : RecyclerView.Adapter<DailyWeatherViewHolder>() {

    private var dailyWeatherListData = ArrayList<List>()

    fun setDailyWeatherList(
        dailyWeatherListData: ArrayList<List>,
    ) {
        this.dailyWeatherListData = dailyWeatherListData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyWeatherViewHolder {
        return DailyWeatherViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return dailyWeatherListData.size
    }

    override fun onBindViewHolder(holder: DailyWeatherViewHolder, position: Int) {
        return holder.bind(
            dailyWeatherListData[position],
            listener,
            position
        )
    }
}

class DailyWeatherViewHolder(val binding: ItemDailyBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(parent: ViewGroup): DailyWeatherViewHolder {
            val rawView: ItemDailyBinding =
                ItemDailyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return DailyWeatherViewHolder(rawView)
        }
    }

    fun bind(
        dailyWeatherListData: List,
        listener: (Int) -> Unit,
        position: Int,
    ) {
        val formatter = SimpleDateFormat("hh:mm aa", Locale.getDefault())

        dailyWeatherListData.dt?.let {
            val newDate = Date(it.toLong() * 1000)
            binding.tvTime.text = formatter.format(newDate)
        }
        binding.tvMaxTemp.text =
            itemView.context.getString(R.string.temp, dailyWeatherListData.main?.tempMax.toString())
        binding.tvMinTemp.text =
            itemView.context.getString(R.string.temp, dailyWeatherListData.main?.tempMin.toString())

        val iconText = dailyWeatherListData.weather[0].icon

        iconText?.let {
            val icon = "http://openweathermap.org/img/w/$iconText.png"
            Glide.with(itemView.context).load(icon).into(binding.ivWeatherIcon)
        }

    }
}