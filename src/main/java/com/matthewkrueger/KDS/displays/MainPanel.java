package com.matthewkrueger.KDS.displays;

import com.matthewkrueger.KDS.Settings;

import javax.swing.*;

public class MainPanel extends JLayeredPane{
    //private JLayeredPane layeredPane;

    public MainPanel() {
        super();

        //this.layeredPane = new JLayeredPane();
        this.setPreferredSize(Settings.WINDOW_SIZE);
        //this.setBackground(Settings.BACKGROUND_COLOR);


        this.setVisible(true);

    }

}
