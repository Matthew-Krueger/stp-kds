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

import java.util.HashMap;
import java.util.Map;

public class Fonts {

    private static Map<String, FontFamily> fontFamilies = new HashMap<>();

    public Fonts(HashMap<String, FontFamily> fontFamilies){
        Fonts.fontFamilies = fontFamilies;
    }

    public Fonts(){
        Fonts.fontFamilies = new HashMap<>();
    }

    public void put(String familyName, FontFamily fontFamily){
        fontFamilies.put(familyName, fontFamily);
    }

    public FontFamily getFontFamily(String familyName){

        if(fontFamilies.containsKey(familyName)){
            return fontFamilies.get(familyName);
        }

        //Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).warning("Font family " + familyName + " not found. Using Courier Prime.");
        return (fontFamilies.containsKey("Courier Prime")) ? fontFamilies.get("Courier Prime") : fontFamilies.get("Monospaced");

    }



}