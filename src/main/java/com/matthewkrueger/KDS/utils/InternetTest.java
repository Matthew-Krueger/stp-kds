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

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class InternetTest {


    public static boolean performInternetTest() {

        boolean isConnected = isConnectedToInternet();

        if(!isConnected) {
            try {
                GenericErrorDisplay.getGenericErrorDisplay("No Internet Connection", "<html><body><h1>There is no internet connection available.</h1><br />"
                                + "<p>While internet is required for this application, clicking ignore will attempt to continue running<br />"
                                + "the program; however, the program will still not function correctly without internet.</p></body></html>",
                        GenericErrorDisplay.GenericErrorSettings.FATAL_RECOVER, new Exception()).LATCH.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            };
        }

        return isConnected;

    }


    public static boolean isConnectedToInternet() {
        try {
            URL url = new URL("http://www.google.com");
            URLConnection connection = url.openConnection();
            connection.connect();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}