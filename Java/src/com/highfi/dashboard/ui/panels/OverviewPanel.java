package com.highfi.dashboard.ui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.highfi.dashboard.service.DashboardState;
import com.highfi.dashboard.ui.components.MetricTile;
import com.highfi.dashboard.ui.components.StyledLabel;
import com.highfi.dashboard.ui.theme.AppTheme;

public final class OverviewPanel extends JPanel {
    private final DashboardState state;
    private final MetricTile pendingTile = new MetricTile("Pending", 0, new Color(255, 166, 48));
    private final MetricTile inFocusTile = new MetricTile("In Focus", 0, new Color(92, 184, 92));
    private final MetricTile blockedTile = new MetricTile("Blocked", 0, new Color(237, 85, 101));
    private final JProgressBar focusMeter = new JProgressBar(0, 100);

    public OverviewPanel(DashboardState state) {
        this.state = state;
        setLayout(new BorderLayout(14, 14));
        setOpaque(false);

        JPanel metrics = new JPanel(new GridLayout(1, 3, 12, 12));
        metrics.setOpaque(false);
        metrics.add(pendingTile);
        metrics.add(inFocusTile);
        metrics.add(blockedTile);

        JPanel focusPanel = AppTheme.createCardPanel();
        focusPanel.setLayout(new BoxLayout(focusPanel, BoxLayout.Y_AXIS));
        focusPanel.add(new StyledLabel("Daily Focus Meter", 16, true));
        focusPanel.add(Box.createVerticalStrut(10));

        focusMeter.setStringPainted(true);
        focusMeter.setPreferredSize(new Dimension(0, 28));
        focusPanel.add(focusMeter);
        focusPanel.add(Box.createVerticalStrut(12));
        focusPanel.add(new StyledLabel("Tip: complete one medium task before lunch.", 13, false));

        add(metrics, BorderLayout.NORTH);
        add(focusPanel, BorderLayout.CENTER);

        refresh();
        state.addPropertyChangeListener(evt -> refresh());
    }

    private void refresh() {
        pendingTile.setValue(state.getPendingCount());
        inFocusTile.setValue(state.getInFocusCount());
        blockedTile.setValue(state.getBlockedCount());

        int focusValue = state.getFocusScore();
        focusMeter.setValue(focusValue);
        focusMeter.setString(focusValue + "% - " + (focusValue >= 70 ? "Great flow" : "Keep momentum"));
    }
}
