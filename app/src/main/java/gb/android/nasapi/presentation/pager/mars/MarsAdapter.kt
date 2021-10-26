package gb.android.nasapi.presentation.pager.mars

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import gb.android.nasapi.R
import gb.android.nasapi.domain.mars.MarsDomainDataModel

class MarsAdapter : RecyclerView.Adapter<MarsAdapter.MarsViewHolder>() {

    private var marsData: List<MarsDomainDataModel> = listOf()

    fun setMarsPics(data: List<MarsDomainDataModel>) {
        marsData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarsViewHolder {
        val holder = MarsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_mars, parent, false)
        )
        return holder
    }

    override fun onBindViewHolder(holder: MarsViewHolder, position: Int) {
        holder.render(marsData[position])
    }

    override fun getItemCount() = marsData.size

    //==============================================================================================
    //VIEW HOLDER

    inner class MarsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun render(marsDomainDataModel: MarsDomainDataModel) {
            itemView.findViewById<TextView>(R.id.tv_mars_pic_date).text =
                marsDomainDataModel.earth_date

            itemView.findViewById<ImageView>(R.id.iv_mars_pic).load(marsDomainDataModel.img_src)
        }
    }
}