package gb.android.hw6.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(data: Pair<PlanetData, Boolean>)

}