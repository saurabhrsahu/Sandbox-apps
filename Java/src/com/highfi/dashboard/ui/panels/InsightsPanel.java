package com.highfi.dashboard.ui.panels;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.highfi.dashboard.service.DashboardState;
import com.highfi.dashboard.ui.components.InsightRow;
import com.highfi.dashboard.ui.components.StyledLabel;
import com.highfi.dashboard.ui.theme.AppTheme;

public final class InsightsPanel extends JPanel {
    private final DashboardState state;
    private final InsightRow completionRate = new InsightRow("Average completion rate", "0% this week");
    private final InsightRow riskAlert = new InsightRow("Risk alert", "No risks detected");

    public InsightsPanel(DashboardState state) {
        this.state = state;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(AppTheme.CARD_BG);
        setBorder(AppTheme.cardBorder());

        add(new StyledLabel("Performance Insights", 18, true));
        add(Box.createVerticalStrut(14));
        add(new InsightRow("Deep-work window", "09:30 - 11:00 (best)"));
        add(completionRate);
        add(new InsightRow("Most productive category", "Architecture & planning"));
        add(riskAlert);
        add(Box.createVerticalGlue());

        refresh();
        state.addPropertyChangeListener(evt -> refresh());
    }

    private void refresh() {
        int total = state.getTasks().size();
        int completed = state.getCompletedCount();
        int percentage = total == 0 ? 0 : (int) Math.round((completed * 100.0) / total);

        completionRate.setValue(percentage + "% this week");
        if (state.getPendingCount() > 2) {
            riskAlert.setValue(state.getPendingCount() + " tasks approaching deadline");
        } else {
            riskAlert.setValue("No risks detected");
        }
    }
}
