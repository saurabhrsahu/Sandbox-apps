package com.highfi.dashboard.service;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.highfi.dashboard.model.TaskItem;

public final class DashboardState {
    private final PropertyChangeSupport propertyChanges = new PropertyChangeSupport(this);
    private final List<TaskItem> tasks = new ArrayList<>();
    private int nextTaskId = 1;

    public DashboardState() {
        addTask("Prepare sprint demo notes");
        addTask("Review PR #128 with QA checklist");
        addTask("Plan API latency optimization");
    }

    public List<TaskItem> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    public void addTask(String title) {
        TaskItem newTask = new TaskItem(nextTaskId++, title, false);
        tasks.add(newTask);
        propertyChanges.firePropertyChange("tasks", null, newTask);
        propertyChanges.firePropertyChange("metrics", null, null);
    }

    public void setTaskCompleted(int taskId, boolean completed) {
        for (TaskItem task : tasks) {
            if (task.getId() == taskId && task.isCompleted() != completed) {
                task.setCompleted(completed);
                propertyChanges.firePropertyChange("tasks", null, task);
                propertyChanges.firePropertyChange("metrics", null, null);
                return;
            }
        }
    }

    public int getCompletedCount() {
        int completed = 0;
        for (TaskItem task : tasks) {
            if (task.isCompleted()) {
                completed++;
            }
        }
        return completed;
    }

    public int getPendingCount() {
        return Math.max(0, tasks.size() - getCompletedCount());
    }

    public int getInFocusCount() {
        return getPendingCount() > 0 ? 1 : 0;
    }

    public int getBlockedCount() {
        return 0;
    }

    public int getFocusScore() {
        return Math.min(100, 45 + (getCompletedCount() * 10));
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChanges.addPropertyChangeListener(listener);
    }
}
