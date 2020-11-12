package com.matthewkrueger.KDS.fontUtils;

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

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FontManager {

    private static final Map<String, FontFamily> FONTS = new HashMap<>();
    private static boolean init = false;


    /**
     * Load fonts shipped with this and system fonts to the fonts map
     */
    public static void initFonts(){

        GraphicsEnvironment ge  = GraphicsEnvironment.getLocalGraphicsEnvironment();

        for(String fontFamilyName : ge.getAvailableFontFamilyNames()){
            FONTS.put(fontFamilyName, new FontFamily(fontFamilyName));
            //System.out.println("Loading font" + fontFamilyName);
        }
        init = true;

    }

    /**
     *
     * @param fontStream the {@link InputStream} to read the font from.
     * @param fontFormat A font format, such as Font.TRUETYPE_FONT
     * @throws IOException if it cannot read the font
     * @throws FontFormatException if it cannot create the fontformat. Generally you should use Font.TRUETYPE_FONT
     */
    public static void registerFamily(InputStream fontStream, int fontFormat) throws IOException, FontFormatException {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(Font.createFont(fontFormat, fontStream));

        FONTS.clear();
        initFonts();

    }

    /**
     * Get a font family from the FONTS that are maintained by this class
     * @param familyName the name of the font family
     * @return the FontFamily object. Call getFont(int fontSize, int fontStyle) to get the specific font.
     */
    public static FontFamily getFontFamily(String familyName){

        // guard clause for an uninitialized map
        if(!init)
            initFonts();

        if(FONTS.containsKey(familyName)){

            return FONTS.get(familyName);

        }else{

            System.out.println("Font family " + familyName + " not found. Using SansSerif");
            return FONTS.get("SansSerif");

        }

    }

    /**
     * Get a font family from the FONTS that are maintained by this class
     * @param familyName the name of the font family
     * @return the FontFamily object. Call getFont(int fontSize, int fontStyle) to get the specific font.
     */
    public static FontFamily getFontFamily(String familyName, FontLogging log){

        // guard clause for an uninitialized map
        if(!init)
            initFonts();

        if(FONTS.containsKey(familyName)){

            return FONTS.get(familyName);

        }else{

            log.log("Font family " + familyName + " not found. Using SansSerif");
            return FONTS.get("SansSerif");

        }

    }

}