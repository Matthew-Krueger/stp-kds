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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class FontFamily {

    /**
     * Contains a map of onts, mapped by
     */
    private final Map<Integer, Map<Integer, Font>> fontCache;
    private final String fontFamilyName;

    /**
     * Construct a font family from a name (do not create fonts)
     */
    public FontFamily(String name){
        this.fontFamilyName = name;
        this.fontCache = new HashMap<>();
    }

    /**
     * Gets a font and caches it. This function will return a newly created font that will be accessible in the future
     * @param fontSize the size of the font
     * @param fontStyle the style of the font, given by the Font.STYLE
     * @return the new font
     */
    public Font getFont(int fontSize, int fontStyle){

        if(fontCache.containsKey(fontSize)){

            Map<Integer,Font> fontSizeSubmap = fontCache.get(fontSize);

            // since the font size exists, test for the individual style
            if(fontSizeSubmap.containsKey(fontStyle)){
                return fontCache.get(fontSize).get(fontStyle);
            }else{
                fontSizeSubmap.put(fontStyle, new Font(fontFamilyName, fontStyle, fontSize));
                return fontSizeSubmap.get(fontStyle);
            }

        }

        fontCache.put(fontSize, new HashMap<>());
        Map<Integer, Font> fontSizeSubmap = fontCache.get(fontSize);
        fontSizeSubmap.put(fontStyle, new Font(fontFamilyName, fontStyle, fontSize));
        return fontSizeSubmap.get(fontStyle);

    }

    /**
     * Gets a font and caches it. This function will return a newly created font that will be accessible in the future
     * @param fontSize the size of the font
     * @param fontStyle the style of the font, given by the Font.STYLE
     * @param log the Logging function, use a lambda. (s){ System.out.println(s) }
     * @return the new font
     */
    public Font getFont(int fontSize, int fontStyle, FontLogging log){

        if(fontCache.containsKey(fontSize)){

            Map<Integer,Font> fontSizeSubmap = fontCache.get(fontSize);

            // since the font size exists, test for the individual style
            if(fontSizeSubmap.containsKey(fontStyle)){
                return fontCache.get(fontSize).get(fontStyle);
            }else{

                // cache miss of the font size
                log.log(
                        "Font Family " + fontFamilyName + " at size " + fontSize + " and type "+ fontStyle + " cache miss."
                );
                fontSizeSubmap.put(fontStyle, new Font(fontFamilyName, fontStyle, fontSize));
                return fontSizeSubmap.get(fontStyle);
            }

        }

        log.log("Font Family " + fontFamilyName + " at size " + fontSize + " cache miss.");
        fontCache.put(fontSize, new HashMap<>());
        Map<Integer, Font> fontSizeSubmap = fontCache.get(fontSize);
        fontSizeSubmap.put(fontStyle, new Font(fontFamilyName, fontStyle, fontSize));
        return fontSizeSubmap.get(fontStyle);

    }

}