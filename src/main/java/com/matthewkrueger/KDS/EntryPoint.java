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
import com.matthewkrueger.KDS.fontUtils.FontManager;
import com.matthewkrueger.KDS.utils.InternetTest;
import com.matthewkrueger.KDS.utils.OAuth.OAuthSubroutines;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EntryPoint {

    public static void main(String[] args) {

        /* Init the filesystem access nodes, and load properties */
        System.out.println("Starting APP");
        initFileSystem();
        Settings.initProperties();
        initFonts();
        InternetTest.performInternetTest();
        OAuthSubroutines.initSquareClient();
        OAuthSubroutines.startOAuth();

        /* Set
        OAuthMiniServer.startMiniServerForTesting();

        /* Set if graphics acceleration is to be used */
        DisplayManager.setGraphicsAcceleration(Settings.APP_PROPERTIES.getProperty("use-hardware-acceleration"));


        /* Create the display manager and set if it should be accelerated */
        DisplayManager dm = new DisplayManager();

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

    public static void initFonts(){

        FontManager.initFonts();

        InputStream is1 = Settings.class.getResourceAsStream("/fonts/courier-prime/Courier Prime.ttf");
        InputStream is2 = Settings.class.getResourceAsStream("/fonts/courier-prime/Courier Prime Bold.ttf");
        InputStream is3 = Settings.class.getResourceAsStream("/fonts/courier-prime/Courier Prime Bold Italic.ttf");
        InputStream is4 = Settings.class.getResourceAsStream("/fonts/courier-prime/Courier Prime Italic.ttf");

        try {
            FontManager.registerFamily(is1, Font.TRUETYPE_FONT);
            FontManager.registerFamily(is2, Font.TRUETYPE_FONT);
            FontManager.registerFamily(is3, Font.TRUETYPE_FONT);
            FontManager.registerFamily(is4, Font.TRUETYPE_FONT);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

    }

}
