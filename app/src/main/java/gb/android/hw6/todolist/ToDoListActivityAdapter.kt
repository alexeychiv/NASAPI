package gb.android.hw6.todolist

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.RecyclerView
import gb.android.hw6.recycler.ItemTouchHelperAdapter
import gb.android.hw6.recycler.PlanetData
import gb.android.nasapi.R

class ToDoListActivityAdapter(
    private var data: MutableList<ToDoData>,
    private val dragListener: OnStartDragListener
) : RecyclerView.Adapter<ToDoListActivityAdapter.ToDoItemViewHolder>(), ItemTouchHelperAdapter {


    //===============================================================================================
    // LIFECYCLE EVENTS

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoItemViewHolder {
        return ToDoItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_to_do_list_item, parent, false)
        )
    }

    //===============================================================================================
    // VIEW HOLDER MANAGEMENT

    override fun onBindViewHolder(holder: ToDoItemViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun appendItem() {
        data.add(ToDoData("New ToDo", LOW_PRIORITY))
        notifyItemInserted(itemCount - 1)
    }

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
    // VIEW HOLDER

    inner class ToDoItemViewHolder(view: View) : RecyclerView.ViewHolder(view),
        ItemTouchHelperViewHolder {

        fun bind() {

            itemView.findViewById<EditText>(R.id.et_to_do_text).setText(data[adapterPosition].text)
            itemView.findViewById<EditText>(R.id.et_to_do_text)
                .addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun afterTextChanged(s: Editable) {
                        setNewText(itemView.findViewById<EditText>(R.id.et_to_do_text).text.toString())
                    }
                })

            setPriority(data[adapterPosition].priority)
            itemView.findViewById<ImageView>(R.id.low_priority)
                .setOnClickListener { setPriority(1) }
            itemView.findViewById<ImageView>(R.id.medium_priority)
                .setOnClickListener { setPriority(2) }
            itemView.findViewById<ImageView>(R.id.high_priority)
                .setOnClickListener { setPriority(3) }

            itemView.findViewById<ImageView>(R.id.addItemImageView).setOnClickListener { addItem() }
            itemView.findViewById<ImageView>(R.id.removeItemImageView)
                .setOnClickListener { removeItem() }
            itemView.findViewById<ImageView>(R.id.moveItemUp).setOnClickListener { moveUp() }
            itemView.findViewById<ImageView>(R.id.moveItemDown).setOnClickListener { moveDown() }

            itemView.findViewById<ImageView>(R.id.dragHandleImageView)
                .setOnTouchListener { _, event ->
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        dragListener.onStartDrag(this)
                    }
                    false
                }
        }

        private fun setNewText(text: String) {
            data[adapterPosition].text = text
        }

        private fun setPriority(priority: Int) {
            data[adapterPosition].priority = priority
            when (priority) {
                1 -> setPriorityColors(Color.GREEN, Color.DKGRAY, Color.DKGRAY)
                2 -> setPriorityColors(Color.YELLOW, Color.YELLOW, Color.DKGRAY)
                3 -> setPriorityColors(Color.RED, Color.RED, Color.RED)
                else -> setPriorityColors(Color.GREEN, Color.DKGRAY, Color.DKGRAY)
            }
        }

        private fun addItem() {
            data.add(layoutPosition, ToDoData("New ToDo", LOW_PRIORITY))
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

        //===============================================================================================
        // IMPLEMENTATION ItemTouchHelperViewHolder

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(Color.WHITE)
        }


        //===============================================================================================
        // UTILS

        private fun setPriorityColors(colorLow: Int, colorMid: Int, colorHigh: Int) {
            itemView.findViewById<ImageView>(R.id.low_priority).setColorFilter(colorLow)
            itemView.findViewById<ImageView>(R.id.medium_priority).setColorFilter(colorMid)
            itemView.findViewById<ImageView>(R.id.high_priority).setColorFilter(colorHigh)
        }
    }


}