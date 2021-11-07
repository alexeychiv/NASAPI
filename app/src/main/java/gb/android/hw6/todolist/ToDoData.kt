package gb.android.hw6.todolist

val HIGH_PRIORITY = 3
val MEDIUM_PRIORITY = 2
val LOW_PRIORITY = 1

class ToDoData(
    var text: String = "New ToDo",
    var priority: Int = LOW_PRIORITY
)