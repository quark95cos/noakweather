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

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Class representing the date utilities
 *
 * Author: quark95cos Since: Copyright(c) 2022
 */
public class UtilsDate {

    /**
     * Set the date information
     *
     * @param day
     * @param hour
     * @param minute
     * @param month
     * @param year
     * @return date
     * @throws UtilsException
     */
    public static Date setDate(String day, String hour, String minute,
            String month, String year
    ) throws UtilsException {
        // Simple DateFormat Format Codes
        // To specify the time format, use a time pattern string.
        // In this pattern, all ASCII letters are reserved as pattern letters,
        // which are defined as the following −
        // Character 	Description                     Example
        // G            Era designator                  AD
        // y            Year in four digits             2001
        // M            Month in year                   July or 07
        // d            Day in month                    10
        // h            Hour in A.M./P.M. (1~12) 	12
        // H            Hour in day (0~23)              22
        // m            Minute in hour                  30
        // s            Second in minute                55
        // S            Millisecond                     234
        // E            Day in week                     Tuesday
        // D            Day in year                     360
        // F            Day of week in month            2 (second Wed. in July)
        // w            Week in year                    40
        // W            Week in month                   1
        // a            A.M./P.M. marker                PM
        // k            Hour in day (1~24)              24
        // K            Hour in A.M./P.M. (0~11) 	10
        // z            Time zone                       Eastern Standard Time
        // '            Escape for text                 Delimiter
        // "            Single quote                    `

        TimeZone gmtTmZone = TimeZone.getTimeZone("GMT");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(gmtTmZone);

        // Initialize day, hour and minute
        int yearInt = 0;
        int monthInt = 0;
        int dayInt = 0;
        int hourInt = 0;
        int minuteInt = 0;

        try {
            //String day = ((String) text).substring(0, 2);
            //String hour = ((String) text).substring(2, 4);
            //String minute = ((String) text).substring(4, 6);

            yearInt = Integer.parseInt(year);
            monthInt = Integer.parseInt(month);
            dayInt = Integer.parseInt(day);
            hourInt = Integer.parseInt(hour);
            minuteInt = Integer.parseInt(minute);
        } catch (NullPointerException nullPointerExc) {
            // text == null
            throw new UtilsException("Date cannot be null. It needs to be 6 bytes in a DDHRMI format", nullPointerExc);
        } catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsExc) {
            // text length is not equal to 6
            throw new UtilsException("Date needs to be 6 bytes in a DDHRMI format", stringIndexOutOfBoundsExc);
        } catch (NumberFormatException numberFormatExc) {
            // text not a number
            throw new UtilsException("Date is not a number", numberFormatExc);
        }

        // Case where the month may have rolled. In this case, the
        // calendar should be rolled back one day
        if (dayInt > calendar.get(Calendar.DAY_OF_MONTH)) {
            calendar.roll(Calendar.DAY_OF_MONTH, false);
        }

        calendar.set(Calendar.YEAR, yearInt);
        calendar.set(Calendar.MONTH, monthInt - 1);
        calendar.set(Calendar.DAY_OF_MONTH, dayInt);
        calendar.set(Calendar.HOUR_OF_DAY, hourInt);
        calendar.set(Calendar.MINUTE, minuteInt);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * Set validFromToDate
     *
     * @param text
     * @return validFromToDate
     * @throws UtilsException
     */
    public static String[] getValidToFromDate(String text)
            throws UtilsException {
        // Valid date and time.
        // 091212 - Forecast valid from the ninth at 1200Z til the tenth
        // at 1200Z.
        // 110024 - Forecast valid from the eleventh at 0000Z till the
        // twelfth at 0000Z.
        // 010524 - Amended forecast valid from the first at 0500Z till
        // the second at 0000Z.
        // -----
        // '091818' means a 24 hour forecast valid from 1800Z on the 9th
        // until 1800Z the following day.
        // 2900Z-2912Z means it is valid from the 29th 0000Z to 1200Z
        // 1918/2018 means it is valid from the 19th 1800Z to the 20th 1800Z
        // TODO: Need to test the format 3100Z-0112Z

        String validFromToDate[] = new String[2];

        // If text is null then throw NullPointerException
        if (text == null) {
            throw new UtilsException("Missing Valid to and from Date/Time");
        }

        if (text.contains("/")) {
            String[] tempVal = UtilsMisc.stringSplit(text, "/");
            if (tempVal.length == 2) {
                validFromToDate[0] = tempVal[0] + "00Z";
                validFromToDate[1] = tempVal[1] + "00Z";
            } else {
                throw new UtilsException("Missing Valid to and/or from Date/Time");
            }
        } else if (text.contains("Z")) {
            String[] tempVal = UtilsMisc.stringSplit(text, "-");
            if (tempVal.length == 2) {
                validFromToDate[0] = UtilsMisc.removeString(tempVal[0], "Z") + "00Z";
                validFromToDate[1] = UtilsMisc.removeString(tempVal[1], "Z") + "00Z";
            } else {
                throw new UtilsException("Missing Valid to and/or from Date/Time");
            }
        } else if (text.length() == 6) {
            validFromToDate[0] = text.substring(0, 4) + "00Z";
            Integer tempVal = Integer.parseInt(text
                    .substring(0, 2)) + 1;
            if (tempVal.toString().length() == 1) {
                validFromToDate[1] = text
                        .substring(0, 1) + tempVal
                        + text.substring(4, 6) + "00Z";
            } else {
                validFromToDate[1] = tempVal
                        + text.substring(4, 6) + "00Z";
            }
        } else {
            //validFromToDate[0] = "Invalid dates";
            //validFromToDate[1] = "Invalid dates";
            // Should not reach here
            // If text is blank then throw NullPointerException
            throw new UtilsException("Missing Valid to and from Date/Time");
        }

        return validFromToDate;
    }
}
