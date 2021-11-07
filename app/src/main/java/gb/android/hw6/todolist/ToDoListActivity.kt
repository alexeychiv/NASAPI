package gb.android.hw6.todolist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gb.android.nasapi.databinding.ActivityToDoListBinding

class ToDoListActivity : AppCompatActivity() {

    private var _binding: ActivityToDoListBinding? = null
    private val binding: ActivityToDoListBinding
        get() = _binding!!


    lateinit var itemTouchHelper: ItemTouchHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityToDoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = arrayListOf(
            ToDoData("Low Priority ToDo", LOW_PRIORITY),
            ToDoData("Medium Priority ToDo", MEDIUM_PRIORITY),
            ToDoData("Hight Priority ToDo", HIGH_PRIORITY)
        )

        binding.rvToDoList.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

        val adapter = ToDoListActivityAdapter(
            data,
            object : ToDoListActivityAdapter.OnStartDragListener {
                override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                    itemTouchHelper.startDrag(viewHolder)
                }
            }

        )

        binding.rvToDoList.adapter = adapter
        binding.fabNewTodo.setOnClickListener { adapter.appendItem() }

        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.rvToDoList)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}