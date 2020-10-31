package com.matthewkrueger.KDS.displays;

import com.matthewkrueger.KDS.Settings;

import javax.swing.*;
import java.awt.*;

public class DisplayManager {

    private JFrame mainDisplay;
    private MainPanel mainPanel;

    public void createMainDisplay(){

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        Settings.WINDOW_SIZE = new Dimension(width, height);

        mainDisplay = new JFrame();
        mainPanel = new MainPanel();

        mainDisplay.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        mainDisplay.setContentPane(mainPanel);
        mainDisplay.setBackground(Settings.BACKGROUND_COLOR);
        mainDisplay.pack();
        mainDisplay.setVisible(true);

    }

    /**
     * Sets whether sun.java2d.opengl is used for acceleration
     * @param isAccelerated A string of the boolean of whether or not the graphics context should be accelerated.
     */
    public void setGraphicsAcceleration(String isAccelerated){
        System.setProperty("sun.java2d.opengl", isAccelerated);
    }

}
