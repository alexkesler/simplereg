package org.kesler.simplereg.gui.util;

import com.alee.extended.panel.CenterPanel;
import com.alee.extended.panel.WebOverlay;
import org.kesler.simplereg.util.ResourcesUtil;

import javax.swing.*;
import java.awt.*;

public class ProgressOverlay extends WebOverlay {
    private CenterPanel progressOverlayPanel;

    public ProgressOverlay(Component component) {
        super(component);
        JLabel processLabel = new JLabel();
        processLabel.setIcon(ResourcesUtil.getIcon("loading.gif"));

        progressOverlayPanel = new CenterPanel(processLabel);
        progressOverlayPanel.setVisible(false);

        addOverlay(progressOverlayPanel);
    }

    public void showProgress() {
        progressOverlayPanel.setVisible(true);
    }

    public void hideProgress() {
        progressOverlayPanel.setVisible(false);
    }
}
