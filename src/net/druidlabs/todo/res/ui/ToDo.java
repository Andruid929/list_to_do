package net.druidlabs.todo.res.ui;

import net.druidlabs.todo.listeners.TKeyListener;
import net.druidlabs.todo.listeners.TMouseListener;
import net.druidlabs.todo.task.TaskManager;
import net.druidlabs.todo.util.RefuseToBeEmptyException;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;
import java.util.*;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static net.druidlabs.todo.res.values.Colours.BG_COLOUR;
import static net.druidlabs.todo.res.values.Colours.TEXT_COLOUR;
import static net.druidlabs.todo.res.values.Dims.*;
import static net.druidlabs.todo.res.values.Fonts.DEFAULT_FONT;
import static net.druidlabs.todo.res.values.Strings.TITLE;

/**
 * Creates a To-do list window. Get an instance of the app by calling the static {@link #launch()}.
 * */

public class ToDo extends JFrame {

    private final JPanel panel = new JPanel(null);

    private final Map<JCheckBox, JLabel> ITEM_MAP = new LinkedHashMap<>();

    private final List<JCheckBox> PREV_ITEMS = new ArrayList<>();

    private final MouseListener mouseListener = new TMouseListener(this);

    private final ActionListener actionListener = this::setCompleted;

    private ToDo() {
        this.setTitle(TITLE);
        this.setSize(SIZE);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);

        KeyListener keyListener = new TKeyListener(this);
        this.addKeyListener(keyListener);

        ImageIcon ICON = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/net/druidlabs/todo/res/icon/to_do_icon.png")));
        this.setIconImage(ICON.getImage());

        try {
            if (!TaskManager.read(PREV_ITEMS, actionListener, mouseListener, ITEM_MAP, panel, this)) {
                JCheckBox initialItem = new JCheckBox();
                initialItem.setBounds(20, 17, 15, 15);
                initialItem.setFocusable(false);
                initialItem.setBackground(BG_COLOUR);
                initialItem.addActionListener(actionListener);
                panel.add(initialItem);

                PREV_ITEMS.add(initialItem);

                JLabel initialItemLabel = new JLabel();
                initialItemLabel.setBounds(40, 10, this.getWidth() - 100, 30);
                initialItemLabel.setText("Modify the source code because the existing code sucks a pop");
                initialItemLabel.setFont(DEFAULT_FONT);
                initialItemLabel.setForeground(TEXT_COLOUR);
                initialItemLabel.setFocusable(false);
                initialItemLabel.addMouseListener(mouseListener);
                panel.add(initialItemLabel);

                ITEM_MAP.put(initialItem, initialItemLabel);

            }
        } catch (IOException | RefuseToBeEmptyException _) {
            System.err.println("No save files");
        }

        panel.setBackground(BG_COLOUR);
        panel.setPreferredSize(PANEL_SIZE);
        panel.addKeyListener(keyListener);
        panel.setFocusable(true);

        this.add(panel);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setFocusable(false);
        scrollPane.setBackground(BG_COLOUR);
        scrollPane.setForeground(BG_COLOUR);

        JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
        scrollBar.setUI(scrollBarUI());

        this.add(scrollPane);

        this.setVisible(true);

    }

    /**
     * Launches a new instance of the To-do list application.
     * */

    public static void launch() {
        new ToDo();
    }

    private BasicScrollBarUI scrollBarUI() {
        return new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = BG_COLOUR;
                this.trackColor = BG_COLOUR;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return blankButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return blankButton();
            }

            private JButton blankButton() {
                JButton button = new JButton();
                button.setSize(INVISIBLE_BUTTON_SIZE);
                button.setMaximumSize(INVISIBLE_BUTTON_SIZE);
                button.setMinimumSize(INVISIBLE_BUTTON_SIZE);
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setBackground(BG_COLOUR);
                button.setBorderPainted(false);
                return button;
            }
        };
    }

    public void setCompleted(ActionEvent e) { //Called when a JCheckBox is checked
        JCheckBox item = (JCheckBox) e.getSource();
        JLabel itemLabel = ITEM_MAP.get(item);

        if (item.isSelected()) {
            itemLabel.setForeground(Color.DARK_GRAY);
        } else {
            itemLabel.setForeground(TEXT_COLOUR);
        }
    }

    public void addTask() { //Called when it's time to create a new task
        String taskName = JOptionPane.showInputDialog(this, "What's the task?", "Add a task", JOptionPane.PLAIN_MESSAGE);

        if (!taskName.isBlank()) {
            JCheckBox lastItem = PREV_ITEMS.getLast();

            int x = lastItem.getX();
            int y = lastItem.getY();

            JCheckBox newItem = new JCheckBox();
            newItem.setBounds(x, y + 30, 15, 15);
            newItem.setFocusable(false);
            newItem.setBackground(BG_COLOUR);
            newItem.addActionListener(actionListener);
            panel.add(newItem);

            PREV_ITEMS.add(newItem);

            JLabel newItemLabel = new JLabel(taskName);
            newItemLabel.setBounds(newItem.getX() + 20, newItem.getY() - 7, this.getWidth() - 100, 30);
            newItemLabel.setFocusable(false);
            newItemLabel.setFont(DEFAULT_FONT);
            newItemLabel.setForeground(TEXT_COLOUR);
            newItemLabel.addMouseListener(mouseListener);
            panel.add(newItemLabel);

            ITEM_MAP.put(newItem, newItemLabel);

            panel.revalidate();
            panel.repaint();

            try {
                if (TaskManager.save(ITEM_MAP)) {
                    System.out.println("Save successful");
                }
            } catch (IOException e) {
                System.err.println("Encountered errors while saving");
                e.printStackTrace(System.err);
            }
        }
    }

    public void showTaskName(MouseEvent e) {
        JLabel task = (JLabel) e.getSource();

        if (task.getForeground() == Color.DARK_GRAY) {
            return;
        }

        JOptionPane.showMessageDialog(this, task.getText(), "Task name", JOptionPane.PLAIN_MESSAGE);
    }

    public void deleteLastTask() throws RefuseToBeEmptyException {
        if (ITEM_MAP.size() == 1) {
            throw new RefuseToBeEmptyException("I assume there's one task remaining");
        }

        JCheckBox lastItem = PREV_ITEMS.getLast();
        JLabel lastLabel = ITEM_MAP.get(lastItem);

        if ((lastItem == null) || (lastLabel == null)) {
            System.err.println("It be null");
            lastItem = PREV_ITEMS.get(PREV_ITEMS.size() - 2);
            lastLabel = ITEM_MAP.get(lastItem);
        }

        PREV_ITEMS.removeLast();
        ITEM_MAP.remove(lastItem, lastLabel);

        panel.remove(lastItem);
        panel.remove(lastLabel);

        panel.revalidate();
        panel.repaint();

        try {
            if (TaskManager.save(ITEM_MAP)) {
                System.out.println("Save successful");
            }
        } catch (IOException _) {
            System.err.println("Encountered errors while saving");
        }
    }

    private String previewTaskName(String task) {
        if (task.length() > 30) {
            return task.substring(0, 30).concat("...");
        }

        return task;
    }

    public void editTask(MouseEvent e) { //Edit an existing task
        JLabel task = (JLabel) e.getSource();

        String text = JOptionPane.showInputDialog(this, previewTaskName(task.getText()),
                "Edit task", JOptionPane.PLAIN_MESSAGE);

        if (text == null) {
            return;
        }

        if (text.isBlank()) {
            System.err.println("Blank rename, cancelling");
            return;
        }
        task.setText(text);

        try {
            if (TaskManager.save(ITEM_MAP)) {
                System.out.println("Save successful");
            }
        } catch (IOException _) {
            System.err.println("Encountered errors while saving");
        }
    }
}