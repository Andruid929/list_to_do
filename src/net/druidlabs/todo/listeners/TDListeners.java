package net.druidlabs.todo.listeners;

import net.druidlabs.todo.res.ui.ToDo;

public abstract class TDListeners {

    public TDListeners(ToDo toDo) {
        System.out.println(registerListener());
    }

    protected abstract String registerListener();

}
