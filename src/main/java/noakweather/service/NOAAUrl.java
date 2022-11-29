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

import java.net.URL;
import noakweather.utils.Configs;
import noakweather.utils.UtilsException;
import noakweather.utils.UtilsWeb;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class representing the NOAA URL to fetch the METAR or TAF data from NOAA
 *
 * Author: quark95cos Since: Copyright(c) 2022
 */
public class NOAAUrl {

    private final static String WEATHMETARURL = Configs.getInstance().getString("MISC_METAR_URL");
    private final static String WEATHTAFURL = Configs.getInstance().getString("MISC_TAF_URL");
    private final static String WEATHMETAREXT = Configs.getInstance().getString("MISC_METAR_EXT");
    private final static String WEATHTAFEXT = Configs.getInstance().getString("MISC_TAF_EXT");

    private static final Logger LOGGER
            = LogManager.getLogger(NOAAUrl.class.getName());

    /**
     * Get a new URL instance for Metar information
     *
     * @param station
     * @return a new URL instance
     */
    public URL generateMetarDataUrl(String station) {

        URL urlString = null;

        try {
            urlString = UtilsWeb.getUrl(WEATHMETARURL + station + WEATHMETAREXT);
            LOGGER.debug(Configs.getInstance().getString("NOAA_URL_DECODED_GEN_METAR_URL")
                    + " " + urlString);
        } catch (UtilsException err) {
            LOGGER.error(Configs.getInstance().getString("NOAA_URL_DECODED_GEN_MT_ERR_URL")
                    + " " + err);
        }

        return urlString;
    }

    /**
     * Get a new URL instance for Taf information
     *
     * @param station
     * @return a new URL instance
     */
    public URL generateTafDataUrl(String station) {

        URL urlString = null;

        try {
            urlString = UtilsWeb.getUrl(WEATHTAFURL + station + WEATHTAFEXT);
            LOGGER.debug(Configs.getInstance().getString("NOAA_URL_DECODED_GEN_TAF_URL")
                    + " " + urlString);
        } catch (UtilsException err) {
            LOGGER.error(Configs.getInstance().getString("NOAA_URL_DECODED_GEN_MT_ERR_URL")
                    + " " + err);
        }

        return urlString;
    }
}
