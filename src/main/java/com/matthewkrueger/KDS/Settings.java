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

import com.matthewkrueger.KDS.fontUtils.Fonts;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {

    public static Fonts FONTS = new Fonts();

    public static Dimension WINDOW_SIZE = new Dimension(1280,720),
            RESET_WINDOW_SIZE = new Dimension(850,480),
            ERROR_WINDOW_SIZE = new Dimension(480,200);

    public static Color BACKGROUND_COLOR = new Color(10, 10, 10);

    /* App home file settings */
    public static final String HOME_DIRECTORY = System.getProperty("user.home"),
            APP_DIRECTORY = HOME_DIRECTORY + "/Documents/MattKDS/",
            APP_PROPERTIES_FILE_NAME = "settings.properties";


    /* App Properties Settings */
    public static File APP_DIRECTORY_FILE = null;
    public static File APP_PROPERTIES_FILE = null;
    public static Properties APP_PROPERTIES = new Properties();


    public static void saveProperties() {
        try {
            FileOutputStream fr = new FileOutputStream(APP_PROPERTIES_FILE);
            APP_PROPERTIES.store(fr, "Properties");
            fr.close();
            //System.out.println("After saving properties: " + APP_PROPERTIES);
        } catch (Exception e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void initProperties(){
        try{
            /* Create new file if not exists */
            if(Settings.APP_PROPERTIES_FILE == null) {
                Settings.APP_PROPERTIES_FILE = new File(Settings.APP_DIRECTORY_FILE, Settings.APP_PROPERTIES_FILE_NAME);
                if(!Settings.APP_PROPERTIES_FILE.exists()){
                    //noinspection ResultOfMethodCallIgnored
                    Settings.APP_PROPERTIES_FILE.createNewFile();
                    System.out.println("Created new Properties file, saving to disk.");
                    saveProperties();
                }
            }

            /* Load current properties on the disk */
            loadProperties();

            /* Fill Properties if any are blank */
            fillPropertyIfNotExists("use-hardware-acceleration", "false");

            /* Save any changes that may have been made */
            saveProperties();

        } catch(IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void loadProperties() {
        try {
            FileInputStream fi = new FileInputStream(APP_PROPERTIES_FILE);
            APP_PROPERTIES.load(fi);
            fi.close();
            //System.out.println("After Loading properties: " + APP_PROPERTIES);
        } catch (Exception e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void fillPropertyIfNotExists(String key, String value){

        if(!Settings.APP_PROPERTIES.containsKey(key)){
            System.out.println("Setting " + key + " does not exist. Filling with default value " + value);
            Settings.APP_PROPERTIES.setProperty(key, value);
        }

    }


}
