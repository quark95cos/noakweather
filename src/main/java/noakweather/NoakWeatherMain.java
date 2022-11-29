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
package noakweather;

import java.util.Locale;
import noakweather.noaa_api.common.Weather;
import noakweather.noaa_api.wthtype.Metar;
import noakweather.noaa_api.wthtype.Taf;
import noakweather.utils.Configs;
import noakweather.utils.UtilsException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

/**
 * Class representing the main program
 *
 * Author: quark95cos Since: Copyright(c) 2022
 */
public class NoakWeatherMain {

    static String station = "KCLT";
    static Metar metar = null;
    static Taf taf = null;

    private static final Logger LOGGER
            = LogManager.getLogger(NoakWeatherMain.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Configs.getInstance().setLocale(Locale.ENGLISH);
        //Configs configs = Configs.getInstance();

        if (args.length < 4) {
            System.out.println(Configs.getInstance().getString("LOG_DECODED_MSG_NOT_EN"));
            LOGGER.fatal(Configs.getInstance().getString("LOG_DECODED_MSG_NOT_EN"));
            System.out.println(Configs.getInstance().getString("LOG_DECODED_MSG_MET_PARM"));
            LOGGER.fatal(Configs.getInstance().getString("LOG_DECODED_MSG_MET_PARM"));
            System.out.println(Configs.getInstance().getString("LOG_DECODED_MSG_TAF_PARM") + "\n");
            LOGGER.fatal(Configs.getInstance().getString("LOG_DECODED_MSG_TAF_PARM"));
            System.out.println(Configs.getInstance().getString("LOG_DECODED_MSG_EXIT") + "\n");
            LOGGER.fatal(Configs.getInstance().getString("LOG_DECODED_MSG_EXIT"));
            System.exit(0);
        }

        // This works to set a particular Logger's logging level
        // For future use
        //System.out.println(LogManager.getLogger(AviaWeath.class.getName()));
        //Configurator.setLevel(AviaWeath.class.getName(), Level.WARN);
        //System.out.println(LogManager.getLogger(AviaWeath.class.getName()));

        /*System.out.println("args.length: " + args.length);
        System.out.println("args[0]: " + args[0]);
        System.out.println("args[1]: " + args[1]);
        System.out.println("args[2]: " + args[2]);
        System.out.println("args[3]: " + args[3]);*/
        // Parse parameters
        if (args[3].toUpperCase().matches("I")) {
            // Set the root LOGGER to Level.INFO
            Configurator.setRootLevel(Level.INFO);
            System.out.println(Configs.getInstance().getString("LOG_DECODED_INFO") + "\n");
        } else if (args[3].toUpperCase().matches("W")) {
            // Set the root LOGGER to Level.WARN
            Configurator.setRootLevel(Level.WARN);
            System.out.println(Configs.getInstance().getString("LOG_DECODED_WARN") + "\n");
        } else if (args[3].toUpperCase().matches("D")) {
            // Set the root LOGGER to Level.DEBUG
            Configurator.setRootLevel(Level.DEBUG);
            System.out.println(Configs.getInstance().getString("LOG_DECODED_DEBUG") + "\n");
        } else {
            // Set the root LOGGER to Level.INFO
            Configurator.setRootLevel(Level.INFO);
            System.out.println(Configs.getInstance().getString("LOG_DECODED_UNKN") + "\n");
        }

        //String whichVersion = System.getProperty("java.version");
        //System.out.println(whichVersion);
        //String whichJVMVersion = System.getProperty("java.vm.version");
        //System.out.println(whichJVMVersion);
        //metar = Weather.getMetar(station, "Y", MiscConstInterface.MISC_METAR_M);
        try {
            if (args[0].toUpperCase().matches(Configs.getInstance().getString("MISC_METAR_M"))) {
                LOGGER.info("Processing Metar data");
                if (args[1].length() == 4) {
                    station = args[1].toUpperCase();
                    LOGGER.info("station: " + station);
                    metar = Weather.getMetar(station, args[2].toUpperCase(), args[0].toUpperCase());
                } else {
                    LOGGER.info("No station was specified. Will be default");
                    metar = Weather.getMetar(station, "Y", Configs.getInstance().getString("MISC_METAR_M"));
                }
            } else if (args[0].toUpperCase().matches(Configs.getInstance().getString("MISC_TAF_T"))) {
                LOGGER.info("Processing Taf data");
                if (args[1].length() == 4) {
                    station = args[1].toUpperCase();
                    LOGGER.info("station: " + station);
                    taf = Weather.getTaf(station, args[2].toUpperCase(), args[0].toUpperCase());
                } else {
                    LOGGER.info("No station was specified. Will be default");
                    taf = Weather.getTaf(station, "Y", Configs.getInstance().getString("MISC_TAF_T"));
                }
            } else {
                System.out.println(Configs.getInstance().getString("LOG_DECODED_MSG_UNK_WTH_TYP"));
                LOGGER.error(Configs.getInstance().getString("LOG_DECODED_MSG_UNK_WTH_TYP"));
                System.out.println(Configs.getInstance().getString("LOG_DECODED_MSG_MET_PARM"));
                LOGGER.error(Configs.getInstance().getString("LOG_DECODED_MSG_MET_PARM"));
                System.out.println(Configs.getInstance().getString("LOG_DECODED_MSG_TAF_PARM"));
                LOGGER.error(Configs.getInstance().getString("LOG_DECODED_MSG_TAF_PARM"));
            }
        } catch (UtilsException err) {
            System.out.println(err + ": Check log file for details of error");
        }
    }
}
