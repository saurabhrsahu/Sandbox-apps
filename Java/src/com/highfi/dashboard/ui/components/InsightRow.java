package com.highfi.dashboard.ui.components;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.highfi.dashboard.ui.theme.AppTheme;

public final class InsightRow extends JPanel {
    private final JLabel valueLabel = new JLabel();

    public InsightRow(String label, String value) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        JLabel left = new JLabel(label);
        left.setFont(AppTheme.font(14, true));
        left.setForeground(new java.awt.Color(56, 64, 91));

        valueLabel.setFont(AppTheme.font(14, false));
        valueLabel.setForeground(new java.awt.Color(90, 102, 130));
        valueLabel.setText(value);

        add(left, BorderLayout.WEST);
        add(valueLabel, BorderLayout.EAST);
    }

    public void setValue(String value) {
        valueLabel.setText(value);
    }
}
