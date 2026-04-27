package com.highfi.dashboard.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.highfi.dashboard.ui.theme.AppTheme;

public final class MetricTile extends JPanel {
    private final JLabel valueLabel = new JLabel();

    public MetricTile(String label, int value, Color accent) {
        setLayout(new BorderLayout());
        setBackground(AppTheme.CARD_BG);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 234, 247)),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)));

        JPanel accentLine = new JPanel();
        accentLine.setBackground(accent);
        accentLine.setPreferredSize(new Dimension(0, 5));

        valueLabel.setFont(AppTheme.font(30, true));
        valueLabel.setForeground(new Color(36, 42, 62));

        JLabel textLabel = new JLabel(label);
        textLabel.setFont(AppTheme.font(13, false));
        textLabel.setForeground(AppTheme.TEXT_MUTED);

        add(accentLine, BorderLayout.NORTH);
        add(valueLabel, BorderLayout.CENTER);
        add(textLabel, BorderLayout.SOUTH);

        setValue(value);
    }

    public void setValue(int value) {
        valueLabel.setText(String.valueOf(value));
    }
}
