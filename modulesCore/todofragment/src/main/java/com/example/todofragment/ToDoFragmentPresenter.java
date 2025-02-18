package com.example.todofragment;

public class ToDoFragmentPresenter {
    ToDoFragmentModule toDoFragmentModule;
    ToDoFragment toDoFragment;

    public ToDoFragmentPresenter(ToDoFragment toDoFragment) {
        this.toDoFragment = toDoFragment;
        this.toDoFragmentModule = new ToDoFragmentModule();
    }
}
