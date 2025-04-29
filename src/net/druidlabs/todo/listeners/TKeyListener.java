package net.druidlabs.todo.listeners;

import net.druidlabs.todo.res.ui.ToDo;
import net.druidlabs.todo.util.RefuseToBeEmptyException;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TKeyListener extends TDListeners implements KeyListener {

    private final ToDo toDo;

    public TKeyListener(ToDo toDo) {
        super(toDo);
        this.toDo = toDo;
    }

    @Override
    protected String registerListener() {
        return "To do KeyListener";
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_INSERT) {
            toDo.addTask();
        }

        if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            try {
                toDo.deleteLastTask();
            } catch (RefuseToBeEmptyException ex) {
                JOptionPane.showMessageDialog(toDo, "I refuse to be empty",
                        "Warning", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace(System.err);
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
