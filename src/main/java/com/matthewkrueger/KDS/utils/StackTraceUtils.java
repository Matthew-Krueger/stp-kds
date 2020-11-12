package com.matthewkrueger.KDS.utils;

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

public class StackTraceUtils {

    public static StackTraceElement[] buildBetterStackTraceElementArray(Throwable throwable) {

        StackTraceElement[] exceptionStackTrace = throwable.getStackTrace();
        StackTraceElement[] causeStackTrace = null;

        int exceptionLength = exceptionStackTrace.length;
        int causeLength = 0;

        if(throwable.getCause()!=null) {

            causeStackTrace = buildBetterStackTraceElementArray(throwable.getCause());
            causeLength = causeStackTrace.length;

        }

        StackTraceElement[] result = new StackTraceElement[causeLength + exceptionLength];

        System.arraycopy(exceptionStackTrace, 0, result, 0, exceptionLength);

        if(causeLength!=0 & causeStackTrace!=null)
            System.arraycopy(causeStackTrace, 0, result, exceptionLength, causeLength);

        return result;

    }

    public static String asString(StackTraceElement[] stes) {

        String result = "";

        for(StackTraceElement ste:stes) {
            result += ste.toString() + "\n";
        }

        return result;

    }

    public static String asHTML(StackTraceElement[] stes) {
        String result = "<html><h3>Stacktrace:</h3>";

        for(StackTraceElement ste:stes) {
            result += "<p>" + ste.toString() + "</p>";
        }

        result += "</html>";

        return result;
    }

}