package com.matthewkrueger.KDS.displays;

/*
    A KDS for Square
    Copyright (C) 2020  Matthew Krueger

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
    public static void setGraphicsAcceleration(String isAccelerated){
        System.setProperty("sun.java2d.opengl", isAccelerated);
    }

}
