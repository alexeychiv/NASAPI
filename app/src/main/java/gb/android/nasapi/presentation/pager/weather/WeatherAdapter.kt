package gb.android.nasapi.presentation.pager.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import gb.android.nasapi.R
import gb.android.nasapi.domain.weather.DonkiDomainDataModel

class WeatherAdapter : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    private var weatherData: List<DonkiDomainDataModel> = listOf()

    fun setWeather(data: List<DonkiDomainDataModel>) {
        weatherData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val holder = WeatherViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_weather, parent, false)
        )
        return holder
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.render(weatherData[position])
    }

    override fun getItemCount() = weatherData.size

    //==============================================================================================
    //VIEW HOLDER

    inner class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun render(donkiDomainDataModel: DonkiDomainDataModel) {
            itemView.findViewById<TextView>(R.id.tv_weather_message_type).text =
                donkiDomainDataModel.messageType
            itemView.findViewById<TextView>(R.id.tv_weather_message_time).text =
                donkiDomainDataModel.messageIssueTime
            itemView.findViewById<TextView>(R.id.tv_weather_message_body).text =
                donkiDomainDataModel.messageBody
        }
    }
}