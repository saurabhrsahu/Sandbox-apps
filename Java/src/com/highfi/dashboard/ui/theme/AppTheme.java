package com.highfi.dashboard.ui.theme;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

public final class AppTheme {
    public static final Color BG_SURFACE = new Color(252, 253, 255);
    public static final Color BG_SIDEBAR = new Color(246, 248, 255);
    public static final Color CARD_BG = Color.WHITE;
    public static final Color TEXT_PRIMARY = new Color(46, 52, 76);
    public static final Color TEXT_MUTED = new Color(92, 100, 130);
    public static final Color BORDER = new Color(231, 236, 248);
    public static final Color HEADER_START = new Color(57, 85, 221);
    public static final Color HEADER_END = new Color(115, 81, 222);

    private AppTheme() {
    }

    public static Font font(int size, boolean bold) {
        return new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, size);
    }

    public static Border cardBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                BorderFactory.createEmptyBorder(16, 16, 16, 16));
    }

    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_BG);
        panel.setBorder(cardBorder());
        return panel;
    }
}
