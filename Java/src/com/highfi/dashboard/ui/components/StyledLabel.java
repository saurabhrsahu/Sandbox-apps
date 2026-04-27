package com.highfi.dashboard.ui.components;

import javax.swing.JLabel;

import com.highfi.dashboard.ui.theme.AppTheme;

public final class StyledLabel extends JLabel {
    public StyledLabel(String text, int size, boolean bold) {
        super(text);
        setFont(AppTheme.font(size, bold));
        setForeground(AppTheme.TEXT_PRIMARY);
    }
}
