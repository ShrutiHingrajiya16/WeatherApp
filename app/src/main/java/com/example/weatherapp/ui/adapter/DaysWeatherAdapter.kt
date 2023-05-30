package com.example.weatherapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ItemDaysBinding
import com.example.weatherapp.retrofit.model.List
import com.example.weatherapp.retrofit.model.WeatherListData
import java.text.SimpleDateFormat
import java.util.Locale

class DaysWeatherAdapter(
    private val listener: (Int, ArrayList<List>) -> Unit,
) : RecyclerView.Adapter<DaysWeatherViewHolder>() {

    private var titleArray = listOf<String>()
    private var compareArray = listOf<String>()
    private var daysWeatherListData: WeatherListData? = null
    private var selectedDayPosition = 0

    fun setDaysList(
        titleArray: ArrayList<String>,
        compareArray: ArrayList<String>,
        daysWeatherListData: WeatherListData?,
    ) {
        this.titleArray = titleArray
        this.compareArray = compareArray
        this.daysWeatherListData = daysWeatherListData
    }

    fun setSelection(selectedDayPosition: Int) {
        this.selectedDayPosition = selectedDayPosition
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysWeatherViewHolder {
        return DaysWeatherViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return compareArray.size
    }

    override fun onBindViewHolder(holder: DaysWeatherViewHolder, position: Int) {
        return holder.bind(
            titleArray[position],
            compareArray[position],
            daysWeatherListData,
            listener,
            position,
            selectedDayPosition
        )
    }
}

class DaysWeatherViewHolder(val binding: ItemDaysBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(parent: ViewGroup): DaysWeatherViewHolder {
            val rawView: ItemDaysBinding =
                ItemDaysBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return DaysWeatherViewHolder(rawView)
        }
    }

    fun bind(
        itemTitle: String,
        itemCompare: String,
        daysWeatherListData: WeatherListData?,
        listener: (Int, ArrayList<List>) -> Unit,
        position: Int,
        selectedDayPosition: Int,
    ) {
        val arrayListData = ArrayList<List>()

        daysWeatherListData?.list?.map {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

            it.dt?.let { a ->

                if (sdf.format(a * 1000.toLong()).contains(itemCompare)) {
                    arrayListData.add(it)
                }
            }
        }

        if (selectedDayPosition == position) {
            binding.tvDay.backgroundTintList = ContextCompat.getColorStateList(
                itemView.context,
                R.color.dark_blue
            )
            binding.tvDay.setTextColor(
                ContextCompat.getColorStateList(
                    itemView.context,
                    R.color.white
                )
            )
        } else {
            binding.tvDay.backgroundTintList = ContextCompat.getColorStateList(
                itemView.context,
                R.color.grey
            )
            binding.tvDay.setTextColor(
                ContextCompat.getColorStateList(itemView.context, R.color.text_color)
            )
        }

        binding.tvDay.text = itemTitle
        if (position == 0) {
            binding.tvDay.text = itemView.context.getString(R.string.today)
        }

        itemView.setOnClickListener {
            listener.invoke(position, arrayListData)
        }
    }
}