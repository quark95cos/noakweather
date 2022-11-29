/*
 * noakweather(TM) is a Java library for parsing weather data
 * Copyright (C) 2022 quark95cos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package noakweather.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class representing the miscellaneous utilities
 *
 * Author: quark95cos Since: Copyright(c) 2022
 */
public class UtilsMisc {

    /**
     * Split string
     *
     * @param text
     * @param delimiter
     * @return string splits of original string
     */
    public static String[] stringSplit(String text, String delimiter) {
        List<String> parts = new ArrayList<>();

        // If delimiter is null or empty split on blank
        if (delimiter == null || delimiter.length() == 0) {
            char[] varArr = text.trim().toCharArray();
            for (char c : varArr) {
                parts.add(String.valueOf(c));
            }
            return parts.toArray(new String[0]);
        }

        //Delimiter is not null or empty
        text += delimiter;

        for (int i = text.indexOf(delimiter), j = 0; i != -1;) {
            String temp = text.substring(j, i);
            if (temp.trim().length() != 0) {
                parts.add(temp.trim());
            }
            j = i + delimiter.length();
            i = text.indexOf(delimiter, j);
        }

        return parts.toArray(new String[0]);
    }

    /**
     * Split string including start and end position
     *
     * @param text
     * @param delimiter
     * @param startPos
     * @param endPos
     * @return string splits of original string
     */
    public static String[] stringSplit(String text, String delimiter,
            int startPos, int endPos) {
        int counter = 0;
        List<String> parts = new ArrayList<>();

        // If delimiter is null or empty split on blank
        if (delimiter == null || delimiter.length() == 0) {
            char[] varArr = text.trim().toCharArray();
            for (char c : varArr) {
                parts.add(String.valueOf(c));
            }
            return parts.toArray(new String[0]);
        }

        //Delimiter is not null or empty
        text += delimiter;

        for (int i = text.indexOf(delimiter), j = 0; i != -1;) {
            counter += 1;
            String temp = text.substring(j, i);
            if (temp.trim().length() != 0 && counter >= startPos
                    && counter <= endPos) {
                parts.add(temp.trim());
            }
            j = i + delimiter.length();
            i = text.indexOf(delimiter, j);
        }

        return parts.toArray(new String[0]);
    }

    /**
     * Test if string is Null or white space
     *
     * @param text
     * @return if text is null or contains whitespace
     */
    public static boolean isStringNullOrWhiteSpace(String text) {
        if (text == null) {
            return true;
        }

        for (int i = 0; i < text.length(); i++) {
            if (!Character.isWhitespace(text.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Test if string contains white spaces
     *
     * @param text
     * @return if text contains whitespace
     */
    public static boolean containsWhiteSpaces(String text) {
        // It cannot contain whitespaces if it is null or empty
        if (text == null || text.length() == 0) {
            return false;
        }

        for (int i = 0; i < text.length(); i++) {
            if (Character.isWhitespace(text.charAt(i))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Test if string contains only numbers
     *
     * @param text
     * @return if text contains only numbers
     */
    public static boolean containsOnlyNumbers(String text) {
        // It cannot contain only numbers if it is null or empty
        if (text == null || text.length() == 0) {
            return false;
        }

        return text.matches("^[0-9]+$");
    }

    /**
     * Test if string contains only numbers, decimal, negative sign
     *
     * @param text
     * @return if text contains only numbers, decimal, negative sign
     */
    public static boolean containsOnlyNumbersSpecial(String text) {
        // It cannot contain only numbers, etc.if it is null or empty
        if (text == null || text.length() == 0) {
            return false;
        }

        return text.matches("^([0-9]|.|-)+$");
    }

    /**
     * Test if string contains only alphanumeric
     *
     * @param text
     * @return if text contains only alphanumeric
     */
    public static boolean containsOnlyAlphaNumeric(String text) {
        // It cannot contain only numbers if it is null or empty
        if (text == null || text.length() == 0) {
            return false;
        }

        return text.matches("^([A-Za-z]|[0-9])+$");
    }

    /**
     * Remove white spaces from string
     *
     * @param text
     * @return replace all white space
     * @throws UtilsException
     */
    public static String removeWhiteSpaces(String text)
            throws UtilsException {

        // If text is null then return UtilsException
        if (text == null) {
            throw new UtilsException("No search text has been provided");
        }

        return text.replaceAll("\\s+", "");
    }

    /**
     * Replace all numeric characters from string
     *
     * @param text
     * @return replace all numeric characters
     * @throws UtilsException
     */
    public static String removeNumeric(String text)
            throws UtilsException {

        // If text is null then return UtilsException
        if (text == null) {
            throw new UtilsException("No search text has been provided");
        }

        return text.replaceAll("[0-9.]", "");
    }

    /**
     * Replace all non-numeric characters from string
     *
     * @param text
     * @return replace all non numeric characters
     * @throws UtilsException
     */
    public static String removeNonNumeric(String text)
            throws UtilsException {

        // If text is null then return UtilsException
        if (text == null) {
            throw new UtilsException("No search text has been provided");
        }

        return text.replaceAll("[^0-9.]", "");
    }

    /**
     * Replace all strings (removeText) from full string
     *
     * @param text
     * @param removeText
     * @return replace all strings removeText
     * @throws UtilsException
     */
    public static String removeString(String text, String removeText)
            throws UtilsException {

        // If text is null then return UtilsException
        if (text == null) {
            throw new UtilsException("No text has been provided");
        }

        // If removeText is null then return UtilsException
        if (removeText == null) {
            throw new UtilsException("No remove text has been provided");
        }

        return text.replaceAll(removeText, "");
    }

    /**
     * Find index of character in string
     *
     * @param text
     * @param c
     * @param n
     * @return index of character in string
     */
    public static int findIndexOf(String text, char c, int n) {
        if (text == null || n < 1) {
            return -1;
        }

        int pos = text.indexOf(c);

        while (--n > 0 && pos != -1) {
            pos = text.indexOf(c, pos + 1);
        }

        return pos;
    }

    /**
     * Find index of string in string
     *
     * @param text
     * @param str
     * @param n
     * @return index of string in string
     */
    public static int findIndexOf(String text, String str, int n) {
        if (text == null || n < 1) {
            return -1;
        }

        int pos = text.indexOf(str);

        while (--n > 0 && pos != -1) {
            pos = text.indexOf(str, pos + 1);
        }

        return pos;
    }

    /**
     * Find last index of character in string
     *
     * @param text
     * @param c
     * @param n
     * @return last index of character in string
     */
    public static int findLastIndexOf(String text, char c, int n) {
        if (text == null || n < 1) {
            return -1;
        }

        int pos = text.length();

        while (n-- > 0 && pos != -1) {
            pos = text.lastIndexOf(c, pos - 1);
        }

        return pos;
    }

    /**
     * The count of string (searchText) in string
     *
     * @param text
     * @param searchText
     * @return count of string in string
     * @throws UtilsException
     */
    public static int countMatches(String text, String searchText)
            throws UtilsException {

        // If searchText is null then return UtilsException
        if (searchText == null || searchText.length() == 0) {
            throw new UtilsException("No search text has been provided");
        }

        Pattern pattern = Pattern.compile(searchText, Pattern.LITERAL);

        Matcher matcher = pattern.matcher(text);

        int count = 0;
        int pos = 0;

        while (matcher.find(pos)) {
            count++;
            pos = matcher.start() + 1;
        }

        return count;
    }

    /**
     * Get all positions of string (searchText) in string
     *
     * @param text
     * @param searchText
     * @return all positions of string in string
     * @throws UtilsException
     */
    public static ArrayList<Integer> getMatchPositions(String text, String searchText)
            throws UtilsException {

        ArrayList<Integer> textArray = new ArrayList<>();

        // If searchText is null then return UtilsException
        if (searchText == null || searchText.length() == 0) {
            throw new UtilsException("No search text has been provided");
        }

        Pattern pattern = Pattern.compile(searchText, Pattern.LITERAL);

        Matcher matcher = pattern.matcher(text);

        int pos = 0;

        while (matcher.find(pos)) {
            pos = matcher.start() + 1;
            textArray.add(pos);
        }

        return textArray;
    }

    /**
     * Strip zeroes from string
     *
     * @param text
     * @return string with zeroes stripped
     * @throws UtilsException
     */
    public static String stripZeros(String text)
            throws UtilsException {

        // If text is null then return UtilsException
        if (text == null) {
            throw new UtilsException("No text has been provided");
        }

        if (text.contains(".")) {
            while (text.length() > 1 && (text.endsWith("0")
                    || text.endsWith("."))) {
                text = text.substring(0, text.length() - 1);
            }
        }

        return text;
    }

    /**
     * Rounding of value to a certain precision
     *
     * @param value
     * @param precision
     * @return rounding of value to a certain precision
     */
    public static double roundValue(double value, int precision) {

        int scale = (int) Math.pow(10, precision);

        return (double) Math.round(value * scale) / scale;
    }

    /**
     *
     * Beep!
     *
     */
    public static void beep() {
        java.awt.Toolkit.getDefaultToolkit().beep();
    }
}
