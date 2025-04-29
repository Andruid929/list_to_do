package net.druidlabs.todo.listeners;

import net.druidlabs.todo.res.ui.ToDo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TActionListener extends TDListeners implements ActionListener {

    private final ToDo toDo;

    public TActionListener(ToDo toDo) {
        super(toDo);
        this.toDo = toDo;
    }

    @Override
    protected String registerListener() {
        return "";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        toDo.setCompleted(e);
    }
}
