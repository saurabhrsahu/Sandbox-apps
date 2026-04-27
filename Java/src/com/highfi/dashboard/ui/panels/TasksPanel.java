package com.highfi.dashboard.ui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.highfi.dashboard.model.TaskItem;
import com.highfi.dashboard.service.DashboardState;
import com.highfi.dashboard.ui.components.StyledLabel;
import com.highfi.dashboard.ui.theme.AppTheme;

public final class TasksPanel extends JPanel {
    private final DashboardState state;
    private final JPanel taskListPanel = new JPanel();
    private final JTextField taskInput = new JTextField();

    public TasksPanel(DashboardState state) {
        this.state = state;
        setLayout(new BorderLayout(12, 12));
        setBackground(AppTheme.CARD_BG);
        setBorder(AppTheme.cardBorder());

        JPanel addBar = new JPanel(new BorderLayout(8, 8));
        addBar.setOpaque(false);
        taskInput.setFont(AppTheme.font(14, false));
        JButton addButton = new JButton("Add Task");
        addButton.setFont(AppTheme.font(13, true));
        addButton.addActionListener(evt -> onAddTask());

        addBar.add(taskInput, BorderLayout.CENTER);
        addBar.add(addButton, BorderLayout.EAST);

        taskListPanel.setOpaque(false);
        taskListPanel.setLayout(new BoxLayout(taskListPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(taskListPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 227, 243)));

        add(new StyledLabel("Task Planner", 18, true), BorderLayout.NORTH);
        add(addBar, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
        setPreferredSize(new Dimension(0, 420));

        rebuildTaskList();
        state.addPropertyChangeListener(evt -> rebuildTaskList());
    }

    private void onAddTask() {
        String text = taskInput.getText().trim();
        if (!text.isEmpty()) {
            state.addTask(text);
            taskInput.setText("");
        }
    }

    private void rebuildTaskList() {
        taskListPanel.removeAll();
        for (TaskItem task : state.getTasks()) {
            JCheckBox box = new JCheckBox(task.getTitle(), task.isCompleted());
            box.setOpaque(false);
            box.setFont(AppTheme.font(14, false));
            box.setBorder(BorderFactory.createEmptyBorder(8, 4, 8, 4));
            box.addActionListener(evt -> state.setTaskCompleted(task.getId(), box.isSelected()));
            taskListPanel.add(box);
        }
        taskListPanel.revalidate();
        taskListPanel.repaint();
    }
}
