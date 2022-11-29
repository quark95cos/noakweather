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
package noakweather.noaa_api.common;

//import noakweather.noakutils.UtilsException;
//import noakweather.noakutils.UtilsMisc;
import noakweather.noaa_api.wthtype.Metar;
import noakweather.noaa_api.wthtype.Taf;
import noakweather.service.WeatherCondHttpClient;
import noakweather.utils.Configs;
import noakweather.utils.UtilsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class representing the weather section the retrieves either the METAR or TAF
 * report
 *
 * Author: quark95cos Since: Copyright(c) 2022
 */
public class Weather {

    private static final Logger LOGGER
            = LogManager.getLogger(Weather.class.getName());

    /**
     * Get the TAF information
     *
     * @param station
     * @param parsePrint
     * @param dataType
     * @return
     * @throws noakweather.utils.UtilsException
     */
    public static Taf getTaf(String station, String parsePrint, String dataType) throws UtilsException {
        String tafData = null;

        Taf taf = new Taf();

        LOGGER.info(Configs.getInstance().getString("MISC_STATION")
                + " " + station);
        tafData = WeatherCondHttpClient.fetchMetarOrTaf(station, dataType);
        System.out.println(Configs.getInstance().getString("MISC_RAW_TAFDATA")
                + " #" + tafData + "#");
        LOGGER.info(Configs.getInstance().getString("MISC_RAW_TAFDATA")
                + " #" + tafData + "#");
        if (tafData != null && tafData.length() > 0) {
            taf.parse(tafData);
            if (parsePrint.equals("Y")) {
                System.out.println("\n\n\n"
                        + Configs.getInstance().getString("MISC_RAW_TAFDATA")
                        + " #" + tafData + "#");
                taf.print();
            }
        }
        else {
            System.out.println(Configs.getInstance().getString("MISC_TAF_NONE")
            + " " + station);
        }

        return taf;
    }

    //public static Metar getMetar(String station, int timeout) {
    /**
     * Get the METAR information
     *
     * @param station
     * @param parsePrint
     * @param dataType
     * @return
     * @throws noakweather.utils.UtilsException
     */
    public static Metar getMetar(String station, String parsePrint, String dataType) throws UtilsException {
        String metarData = null;

        Metar metar = new Metar();

        LOGGER.info(Configs.getInstance().getString("MISC_STATION")
                + " " + station);
        metarData = WeatherCondHttpClient.fetchMetarOrTaf(station, dataType);
        System.out.println(Configs.getInstance().getString("MISC_RAW_METARDATA")
                + " #" + metarData + "#");
        LOGGER.info(Configs.getInstance().getString("MISC_RAW_METARDATA")
                + " #" + metarData + "#");
        if (metarData != null && metarData.length() > 0) {
            metar.parse(metarData);
            if (parsePrint.equals("Y")) {
                System.out.println("\n\n\n"
                        + Configs.getInstance().getString("MISC_RAW_METARDATA")
                        + " #" + metarData + "#");
                metar.print();
            }
        }
        else {
            System.out.println(Configs.getInstance().getString("MISC_METAR_NONE")
            + " " + station);
        }

        return metar;
    }
}
