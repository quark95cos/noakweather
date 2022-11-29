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
package noakweather.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import noakweather.utils.Configs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class representing the fetching of the METAR or TAF data from NOAA
 *
 * Author: quark95cos Since: Copyright(c) 2022
 */
public class WeatherCondHttpClient {

    private static String station = "";

    private static final Logger LOGGER
            = LogManager.getLogger(WeatherCondHttpClient.class.getName());

    /**
     * !!!!!!!!!!!!!!!!!!!!! NOT IN USE - NEED TO FINALIZE
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     *
     * @param station
     * @return empty string
     */
    public static String refreshWeather(String station) {
        WeatherCondHttpClient.station = station;
        System.out.println(Configs.getInstance().getString("MISC_STATION")
                + " #" + WeatherCondHttpClient.station + "#");

        // Create an URL object
        // URL url;
        return "";
    }

    /**
     *
     * @param station
     * @param dataType
     * @return metar or taf weather information
     */
    public static String fetchMetarOrTaf(String station, String dataType) {
        StringBuilder weatherData = new StringBuilder();

        try {
            // Create an URL and URLConnection objects
            URL url = null;
            URLConnection connection = null;
            InputStream inputStream = null;
            if (dataType.equals(Configs.getInstance().getString("MISC_METAR_M"))) {
                url = new NOAAUrl().generateMetarDataUrl(station);
            } else if (dataType.equals(Configs.getInstance().getString("MISC_TAF_T"))) {
                url = new NOAAUrl().generateTafDataUrl(station);
            } // Should never happen
            else {

            }

            if (url != null) {
                connection = url.openConnection();
            }

            if (connection != null) {
                inputStream = connection.getInputStream();
            }

            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                weatherData.append(line).append(" ");
            }
        } catch (IOException e) {
            LOGGER.error(Configs.getInstance().getString("EXCEP_FAILED_DOWNLOAD_FILE")
                    + " " + e);
        } catch (NullPointerException e) {
            LOGGER.error(Configs.getInstance().getString("EXCEP_NULL_POINTER_EXCEPTION")
                    + " " + e);
        }

        LOGGER.debug(Configs.getInstance().getString("MISC_WEATHER_DATA")
                + " " + weatherData.toString());

        return weatherData.toString();
    }

    private WeatherCondHttpClient() {
    }
}
