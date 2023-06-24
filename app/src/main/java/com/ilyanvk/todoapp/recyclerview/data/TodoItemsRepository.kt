package com.ilyanvk.todoapp.recyclerview.data

import java.util.Date

object TodoItemsRepository {

    private val todoItems = mutableListOf<TodoItem>()
    private var currentId = 0
    var toEdit: TodoItem? = null
    var onRepositoryUpdate: () -> Unit = {}
    var showCompleted = true
        set(value) {
            field = value
            onRepositoryUpdate()
        }

    init {
        addTodoItem(
            "Вымыть посуду",
            Priority.LOW,
            null,
            false,
            Date(),
            null
        )
        addTodoItem(
            "Сходить в магазин",
            Priority.MEDIUM,
            null,
            false,
            Date(),
            null
        )
        addTodoItem(
            "Посетить зубного врача",
            Priority.HIGH,
            Date(),
            true,
            Date(),
            null
        )
        addTodoItem(
            "Подготовиться к экзамену",
            Priority.HIGH,
            Date(),
            false,
            Date(),
            null
        )
        addTodoItem(
            "Заказать билеты в кино",
            Priority.MEDIUM,
            Date(),
            false,
            Date(),
            null
        )
        addTodoItem(
            "Экзамен по английскому",
            Priority.MEDIUM,
            Date(),
            true,
            Date(),
            null
        )
        addTodoItem(
            "Сходить в спортзал",
            Priority.LOW,
            null,
            false,
            Date(),
            Date()
        )
        addTodoItem(
            "Посадить цветы на балконе",
            Priority.LOW,
            null,
            false,
            Date(),
            null
        )
        addTodoItem(
            "Подготовить подарок для мамы",
            Priority.MEDIUM,
            Date(),
            false,
            Date(),
            null
        )
        addTodoItem(
            "Отправить отчет в финансовый отдел",
            Priority.HIGH,
            null,
            false,
            Date(),
            null
        )
        addTodoItem(
            "Это очень длинный текст" + ", это очень длинный текст".repeat(10),
            Priority.HIGH,
            null,
            false,
            Date(),
            null
        )
        addTodoItem(
            "Блин я ничего не понимаю",
            Priority.HIGH,
            Date(),
            true,
            Date(),
            null
        )
        addTodoItem(
            "Как к Date() добвить n дней?",
            Priority.LOW,
            Date(),
            false,
            Date(),
            null
        )
        addTodoItem(
            "Как сделать так, чтобы завтрашняя дата была подписана 'завтра', а не ддммгггг?",
            Priority.LOW,
            Date(),
            false,
            Date(),
            null
        )
    }

    fun getTodoItems(): List<TodoItem> {
        if (showCompleted) {
            return todoItems.toList()
        }
        return todoItems.filter { !it.isCompleted }
    }

    fun countCompletedTodoItems(): Int {
        return todoItems.count { it.isCompleted }
    }

    fun addTodoItem(todoItem: TodoItem) {
        todoItems.add(todoItem)
        onRepositoryUpdate()
    }

    fun addTodoItem(
        text: String,
        priority: Priority,
        deadline: Date?,
        isCompleted: Boolean,
        creationDate: Date,
        modificationDate: Date?
    ) {
        addTodoItem(
            TodoItem(
                (currentId++).toString(),
                text,
                priority,
                deadline,
                isCompleted,
                creationDate,
                modificationDate
            )
        )
    }

    fun updateTodoItem(todoItem: TodoItem) {
        val newTodoItem = todoItem.copy(modificationDate = Date())
        val index = todoItems.indexOfFirst { it.id == todoItem.id }
        todoItems[index] = newTodoItem
        onRepositoryUpdate()
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        todoItems.remove(todoItem)
        onRepositoryUpdate()
    }
}
