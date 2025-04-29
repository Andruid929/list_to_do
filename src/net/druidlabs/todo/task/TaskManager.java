package net.druidlabs.todo.task;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.List;
import java.util.Map;

import static net.druidlabs.todo.res.values.Colours.BG_COLOUR;
import static net.druidlabs.todo.res.values.Colours.TEXT_COLOUR;
import static net.druidlabs.todo.res.values.Fonts.DEFAULT_FONT;

public abstract class TaskManager {

    private static final String FILE_PATH = "C:\\Users\\" + System.getenv("USERNAME") + "\\Desktop\\To do tasks\\";
    private static final String FILENAME = "Tasks.tsk";

    public static boolean save(Map<JCheckBox, JLabel> itemMap) throws IOException {
        createSave();

        var saveFile = new File(FILE_PATH + FILENAME);

        if (!saveFile.exists()) return false;

        try (FileWriter fileWriter = new FileWriter(saveFile);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {

            for (JLabel task : itemMap.values()) {
                writer.write(task.getText() + "\r\n");
            }
        }

        return true;
    }

    private static void createSave() throws IOException {
        boolean _ = new File(FILE_PATH).mkdir();

        boolean _ = new File(FILE_PATH + FILENAME).createNewFile();
    }

    public static boolean read(List<JCheckBox> prevItems, ActionListener actionListener, MouseListener mouseListener,
                               Map<JCheckBox, JLabel> itemMap, JPanel panel, JFrame frame) throws IOException {

        var saveFile = new File(FILE_PATH + FILENAME);

        if (!saveFile.exists()) return false;


        try (FileReader fileReader = new FileReader(saveFile);
             BufferedReader reader = new BufferedReader(fileReader)) {

            String firstLine = reader.readLine();

            if ((firstLine == null) || firstLine.isBlank()) {
                return false;
            } else {
                readTaskFile(firstLine, prevItems, actionListener, mouseListener, itemMap, panel, frame);
            }

            String task;
            while ((task = reader.readLine()) != null) {
                readTaskFile(task, prevItems, actionListener, mouseListener, itemMap, panel, frame);
            }
        }

        return true;
    }

    private static void readTaskFile(String taskName, List<JCheckBox> prevItems, ActionListener actionListener, MouseListener mouseListener,
                                     Map<JCheckBox, JLabel> itemMap, JPanel panel, JFrame frame) {
        JCheckBox lastItem = new JCheckBox();

        if (prevItems.isEmpty()) {
            lastItem.setBounds(20, -13, 15, 15);
        } else {
            lastItem = prevItems.getLast();
        }

        int x = lastItem.getX();
        int y = lastItem.getY();

        JCheckBox newItem = new JCheckBox();
        newItem.setBounds(x, y + 30, 15, 15);
        newItem.setFocusable(false);
        newItem.setBackground(BG_COLOUR);
        newItem.addActionListener(actionListener);
        panel.add(newItem);

        prevItems.add(newItem);

        JLabel newItemLabel = new JLabel(taskName);
        newItemLabel.setBounds(newItem.getX() + 20, newItem.getY() - 7, frame.getWidth() - 100, 30);
        newItemLabel.setFocusable(false);
        newItemLabel.setFont(DEFAULT_FONT);
        newItemLabel.setForeground(TEXT_COLOUR);
        newItemLabel.addMouseListener(mouseListener);
        panel.add(newItemLabel);

        itemMap.put(newItem, newItemLabel);

        panel.revalidate();
        panel.repaint();
    }

}