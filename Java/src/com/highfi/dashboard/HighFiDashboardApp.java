package com.highfi.dashboard;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.highfi.dashboard.ui.DashboardFrame;

public final class HighFiDashboardApp {
    private HighFiDashboardApp() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            applySystemLookAndFeel();
            new DashboardFrame().showFrame();
        });
    }

    private static void applySystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            // Keep default look and feel if system look and feel is unavailable.
        }
    }
}
