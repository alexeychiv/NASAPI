package gb.android.hw6.recycler

import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.RecyclerView
import gb.android.nasapi.R

class RecyclerActivityAdapter(
    private var onListItemClickListener: OnListItemClickListener,
    private var data: MutableList<Pair<PlanetData, Boolean>>,
    private val dragListener: OnStartDragListener
) : RecyclerView.Adapter<BaseViewHolder>(), ItemTouchHelperAdapter {

    //===============================================================================================
    // COMPANION

    companion object {
        private const val TYPE_EARTH = 0
        private const val TYPE_MARS = 1
        private const val TYPE_HEADER = 2
    }

    //===============================================================================================
    // LIFECYCLE EVENTS

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_EARTH -> EarthViewHolder(
                inflater.inflate(R.layout.activity_recycler_item_earth, parent, false) as View
            )
            TYPE_MARS ->
                MarsViewHolder(
                    inflater.inflate(R.layout.activity_recycler_item_mars, parent, false) as View
                )
            else -> HeaderViewHolder(
                inflater.inflate(R.layout.activity_recycler_header_item, parent, false) as View
            )
        }

    }

    //===============================================================================================
    // VIEW HOLDER MANAGEMENT

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> TYPE_HEADER
            data[position].first.someDescription.isNullOrBlank() -> TYPE_MARS
            else -> TYPE_EARTH
        }
    }

    fun appendItem() {
        data.add(generateItem())
        notifyItemInserted(itemCount - 1)
    }

    private fun generateItem() = Pair(PlanetData("Mars", ""), false)


    //===============================================================================================
    // VIEW HOLDER EVENT INTERFACES

    interface OnListItemClickListener {
        fun onItemClick(data: PlanetData)
    }

    interface ItemTouchHelperViewHolder {
        fun onItemSelected()
        fun onItemClear()
    }

    interface OnStartDragListener {
        fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
    }

    //===============================================================================================
    // IMPLEMENTATION ItemTouchHelperAdapter

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        data.removeAt(fromPosition).apply {
            data.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, this)
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    //===============================================================================================
    // VIEW HOLDERS

    inner class HeaderViewHolder(view: View) : BaseViewHolder(view) {

        override fun bind(data: Pair<PlanetData, Boolean>) {
            itemView.setOnClickListener { onListItemClickListener.onItemClick(data.first) }
        }
    }

    inner class EarthViewHolder(view: View) : BaseViewHolder(view) {

        override fun bind(data: Pair<PlanetData, Boolean>) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                itemView.findViewById<TextView>(R.id.descriptionTextView).text =
                    data.first.someDescription
                itemView.findViewById<ImageView>(R.id.wikiImageView)
                    .setOnClickListener { onListItemClickListener.onItemClick(data.first) }
            }
        }
    }

    inner class MarsViewHolder(view: View) : BaseViewHolder(view), ItemTouchHelperViewHolder {

        override fun bind(data: Pair<PlanetData, Boolean>) {
            itemView.findViewById<ImageView>(R.id.marsImageView)
                .setOnClickListener { onListItemClickListener.onItemClick(data.first) }
            itemView.findViewById<ImageView>(R.id.addItemImageView).setOnClickListener { addItem() }
            itemView.findViewById<ImageView>(R.id.removeItemImageView)
                .setOnClickListener { removeItem() }
            itemView.findViewById<ImageView>(R.id.moveItemUp).setOnClickListener { moveUp() }
            itemView.findViewById<ImageView>(R.id.moveItemDown).setOnClickListener { moveDown() }

            itemView.findViewById<TextView>(R.id.marsDescriptionTextView).visibility =
                if (data.second) View.VISIBLE else View.GONE

            itemView.findViewById<TextView>(R.id.marsTextView).setOnClickListener { toggleText() }

            itemView.findViewById<ImageView>(R.id.dragHandleImageView)
                .setOnTouchListener { _, event ->
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        dragListener.onStartDrag(this)
                    }
                    false
                }
        }

        private fun addItem() {
            data.add(layoutPosition, generateItem())
            notifyItemInserted(layoutPosition)
        }

        private fun removeItem() {
            data.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition)
        }

        private fun moveUp() {
            layoutPosition.takeIf { it > 1 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition - 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition - 1)
            }
        }

        private fun moveDown() {
            layoutPosition.takeIf { it < data.size - 1 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition + 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition + 1)
            }
        }

        private fun toggleText() {
            data[layoutPosition] = data[layoutPosition].let {
                it.first to !it.second
            }
            notifyItemChanged(layoutPosition)
        }

        //===============================================================================================
        // IMPLEMENTATION ItemTouchHelperViewHolder

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(Color.WHITE)
        }
    }
}