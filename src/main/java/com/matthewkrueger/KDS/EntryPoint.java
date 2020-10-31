package com.matthewkrueger.KDS;

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

import com.matthewkrueger.KDS.displays.DisplayManager;

import java.io.File;
import java.util.Properties;

public class EntryPoint {

    public static void main(String[] args) {

        /* Init the filesystem access nodes, and load properties */
        System.out.println("Starting APP");
        initFileSystem();
        Settings.initProperties();

        /* Create the display manager and set if it should be accelerated */
        DisplayManager dm = new DisplayManager();
        dm.setGraphicsAcceleration(Settings.APP_PROPERTIES.getProperty("use-hardware-acceleration"));

        dm.createMainDisplay();

        //AudioHandler.playAudioFile("/sound/bell.wav");


    }

    public static void initFileSystem(){
        System.out.println("Initializing Filesystem access nodes");
        File applicationDirectory = new File(Settings.APP_DIRECTORY);
        if (! applicationDirectory.exists()){
            //noinspection ResultOfMethodCallIgnored
            applicationDirectory.mkdirs();
        }
        Settings.APP_DIRECTORY_FILE = applicationDirectory;

        if(Settings.APP_PROPERTIES == null) {
            Settings.APP_PROPERTIES = new Properties();
            System.out.println("Properties Created");
        }

    }

}
