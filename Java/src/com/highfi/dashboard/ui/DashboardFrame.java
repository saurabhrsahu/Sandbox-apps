package com.highfi.dashboard.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.highfi.dashboard.service.DashboardState;
import com.highfi.dashboard.ui.components.GradientPanel;
import com.highfi.dashboard.ui.panels.InsightsPanel;
import com.highfi.dashboard.ui.panels.OverviewPanel;
import com.highfi.dashboard.ui.panels.TasksPanel;
import com.highfi.dashboard.ui.theme.AppTheme;

public final class DashboardFrame {
    private static final String APP_TITLE = "PulseBoard - HighFi Productivity Dashboard";
    private static final String CARD_OVERVIEW = "overview";
    private static final String CARD_TASKS = "tasks";
    private static final String CARD_INSIGHTS = "insights";

    private final DashboardState state = new DashboardState();
    private final CardLayout cards = new CardLayout();
    private final JPanel content = new JPanel(cards);

    public void showFrame() {
        JFrame frame = new JFrame(APP_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        frame.add(buildHeader(), BorderLayout.NORTH);
        frame.add(buildSidebar(), BorderLayout.WEST);
        frame.add(buildContent(), BorderLayout.CENTER);

        frame.setMinimumSize(new Dimension(980, 620));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel buildHeader() {
        JPanel header = new GradientPanel(AppTheme.HEADER_START, AppTheme.HEADER_END);
        header.setLayout(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        JLabel title = new JLabel("PulseBoard");
        title.setFont(AppTheme.font(26, true));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Focus smarter. Deliver faster.");
        subtitle.setFont(AppTheme.font(14, false));
        subtitle.setForeground(new Color(232, 238, 255));

        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.add(title);
        titleBlock.add(Box.createVerticalStrut(4));
        titleBlock.add(subtitle);

        JPanel statusPill = new JPanel();
        statusPill.setBackground(new Color(255, 255, 255, 40));
        statusPill.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 80), 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        JLabel statusLabel = new JLabel("Today: 4 priorities, 2 done");
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(AppTheme.font(12, true));
        statusPill.add(statusLabel);

        header.add(titleBlock, BorderLayout.WEST);
        header.add(statusPill, BorderLayout.EAST);
        return header;
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(AppTheme.BG_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 14, 20, 14));

        sidebar.add(createNavButton("Overview", CARD_OVERVIEW));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(createNavButton("Tasks", CARD_TASKS));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(createNavButton("Insights", CARD_INSIGHTS));
        sidebar.add(Box.createVerticalGlue());

        JLabel footer = new JLabel("v2.0 modular edition");
        footer.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        footer.setFont(AppTheme.font(11, false));
        footer.setForeground(new Color(122, 130, 160));
        sidebar.add(footer);
        return sidebar;
    }

    private JButton createNavButton(String text, String card) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        button.setFont(AppTheme.font(14, true));
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 231, 245)),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        button.addActionListener(evt -> cards.show(content, card));
        return button;
    }

    private JPanel buildContent() {
        content.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        content.setBackground(AppTheme.BG_SURFACE);

        content.add(new OverviewPanel(state), CARD_OVERVIEW);
        content.add(new TasksPanel(state), CARD_TASKS);
        content.add(new InsightsPanel(state), CARD_INSIGHTS);

        cards.show(content, CARD_OVERVIEW);
        return content;
    }
}
