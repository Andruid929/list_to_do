package net.druidlabs.todo.listeners;

import net.druidlabs.todo.res.ui.ToDo;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TMouseListener extends TDListeners implements MouseListener {

    private final ToDo toDo;

    public TMouseListener(ToDo toDo) {
        super(toDo);
        this.toDo = toDo;
    }

    @Override
    protected String registerListener() {
        return "To do MouseListener";
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        toDo.showTaskName(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            toDo.editTask(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
