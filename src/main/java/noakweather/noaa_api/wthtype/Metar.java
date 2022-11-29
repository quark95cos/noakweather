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
package noakweather.noaa_api.wthtype;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import noakweather.noaa_api.common.Remarks;
import noakweather.utils.Configs;
import noakweather.utils.RegExprConst;
import noakweather.utils.RegExprHandlers;
import noakweather.utils.UtilsException;
import noakweather.utils.UtilsMisc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;

/**
 * Class representing the METAR parsing. It inherits from the AviaWeath class
 *
 * Author: quark95cos Since: Copyright(c) 2022
 */
public class Metar extends AviaWeath {

    private static final Logger LOGGER
            = LogManager.getLogger(Metar.class.getName());

    /**
     * Constructor
     */
    public Metar() {
        LOGGER.debug("in Metar constructor");
    }

    /**
     * Parse the metar information
     *
     * @param metarString
     * @throws noakweather.utils.UtilsException
     */
    public void parse(String metarString) throws UtilsException {
        String metarMain = null;
        String metarRemarks = null;

        LOGGER.debug(Configs.getInstance().getString("METAR_DECODED_METAR_PARSE_STRING")
                + " #" + metarString + "#");

        // Check to see if we have a metarString to parse
        if (metarString == null || metarString.length() == 0) {
            LOGGER.debug(Configs.getInstance().getString("METAR_DECODED_EMPTY_METAR_DATA"));
            return;
        }

        setReportString(metarString);

        // First split the metar string to break out the Remarks section so
        // it can be parsed first
        if (metarString.contains(Configs.getInstance().getString("EXTENDED_REMARKS"))) {
            String[] metarParts = UtilsMisc
                    .stringSplit(metarString, Configs.getInstance()
                            .getString("EXTENDED_REMARKS"));
            metarMain = metarParts[0];
            metarRemarks = metarParts[1];
        } else {
            metarMain = metarString;
        }

        if (metarMain.length() > 0) {
            metarMain = metarMain + " ";
        }
        LOGGER.debug(Configs.getInstance().getString("METAR_DECODED_METAR_MAIN")
                + " #" + metarMain + "#");
        parseMetarMain(metarMain);

        //Parse the Remarks section if it exists
        if (metarRemarks != null) {
            if (metarRemarks.length() > 0) {
                metarRemarks = metarRemarks + " ";
            }
            LOGGER.debug(Configs.getInstance().getString("METAR_DECODED_METAR_REMARKS")
                    + " #" + metarRemarks + "#");
            parseMetarRemarks(metarRemarks);
        }
    }

    /**
     * Parse the main Metar information
     *
     * @param metarMain
     * @throws noakweather.utils.UtilsException
     */
    private void parseMetarMain(String metarMain) throws UtilsException {
        aviaMainWthHandlers = RegExprHandlers.setMainHandlers();
        // Add the UNPARSED_PATTERN after calling aviaMainWthHandlers for any specific handlers
        // that are necessary
        aviaMainWthHandlers.put(RegExprConst.UNPARSED_PATTERN,
                Pair.with("unparsed", false));
        LOGGER.debug("\n");
        LOGGER.debug(Configs.getInstance().getString("METAR_DECODED_METAR_MAIN_HANDLERS"));
        aviaMainWthHandlers.forEach((k, v) -> LOGGER.debug((k + ":" + v)));
        LOGGER.debug(Configs.getInstance().getString("METAR_DECODED_METAR_MAIN_HANDLERS") + "\n");

        parseAviaHandlers(metarMain, aviaMainWthHandlers, Configs.getInstance()
                .getString("AVIA_MAIN_HANDLERS"));
    }

    /**
     * Parse the remarks information
     *
     * @param metarRemarks
     * @throws noakweather.utils.UtilsException
     */
    private void parseMetarRemarks(String metarRemarks) throws UtilsException {
        remarks = new Remarks();
        aviaRemarkWthHandlers = RegExprHandlers.setRemarksHandlers();
        parseAviaHandlers(metarRemarks, aviaRemarkWthHandlers, Configs.getInstance()
                .getString("AVIA_REMARK_HANDLERS"));
    }

    /**
     * Display the metar data in a human-readable format
     */
    public void print() {
        System.out.println("\n");
        System.out.println("In Metar Print");
        System.out.println("Station id : " + getStationID());
        System.out.println("Date       : " + getDate());

        if (getDate() != null) {
            SimpleDateFormat ft
                    = new SimpleDateFormat("E MMM dd hh:mm:ss a zzz yyyy");
            System.out.println("Date ft    : " + ft.format(getDate()));
        } else {
            System.out.println("Date ft    : No valid date found");
        }

        if (getReportModifier().length() != 0) {
            System.out.println("Report modifier: " + getReportModifier());
        }

        if (getWind() != null) {
            if (getWind().getWindNotDetermined() == null) {
                if (getWind().getWindDirectionCalm()) {
                    System.out.println("Wind       : "
                            + getWind().getWindDirectionCompass());
                } else {
                    if (getWind().isWindDirectionIsVariable()) {
                        System.out.println("Wind Direction is variable");
                    } else if (getWind().isWindDirectionIsVarGtrSix()) {
                        System.out.println(
                                "Wind Direction is variable (greater than 6 knots)");
                        System.out.println("Variable between "
                                + getWind().getwindDirectionVarOneCompass() + " and "
                                + getWind().getwindDirectionVarTwoCompass() + " ("
                                + getWind().getWindDirectionVarOne() + " degrees and "
                                + getWind().getWindDirectionVarTwo() + " degrees)");
                    } else {
                        System.out.println("Wind dir   : "
                                + getWind().getWindDirectionCompass() + " ("
                                + getWind().getWindDirection() + " degrees)");
                    }
                    System.out.println("Wind speed : "
                            + getWind().getWindSpeedInMPH() + " mph, "
                            + getWind().getWindSpeedInKnots() + " knots");
                    System.out.println("Wind gusts : "
                            + getWind().getWindGustsInMPH() + " mph, "
                            + getWind().getWindGustsInKnots() + " knots");
                }
            } else {
                System.out.println(
                        "\nWind       : The wind direction and speed cannot be determined");
            }
        }

        if (getVisibility() != null) {
            if (getVisibility().isCavok()) {
                System.out.println("Visibility : "
                        + Configs.getInstance().getString("WEATHER_DECODED_CAVOK"));
            } else if (getVisibility().isNDV()) {
                System.out.println("Visibility : "
                        + getVisibility().getVisibilityNDV());
            } else if (getVisibility().isVisibilityNotKnown()) {
                System.out.println("Visibility : Visibility not known");
            } else if (!getVisibility().isVisibilityLessThan()) {
                System.out.println("Visibility : "
                        + getVisibility().getVisibility() + " mile(s), "
                        + getVisibility().getVisibilityKilometers() + " km(s)");
            } else {
                System.out.println("Visibility : < "
                        + getVisibility().getVisibility() + " mile(s), "
                        + getVisibility().getVisibilityKilometers() + " km(s)");
            }
        }

        if (getPressure() != null) {
            if (getPressure().getPressure() == null) {
                System.out.println(
                        "\nPressure   : The pressure cannot be determined");
            } else {
                System.out.println("Pressure   : "
                        + getPressure().getPressure() + " in Hg, "
                        + getPressure().getPressureInHectoPascals() + " in hPa");
            }
        }

        if (getTemperature() != null) {
            System.out.println("Temperature: "
                    + getTemperature().getTemperatureInCelsius() + " C, "
                    + getTemperature().getTemperatureInFahrenheit() + " F");
            System.out.println("DewPoint   : "
                    + getTemperature().getDewPointInCelsius() + " C, "
                    + getTemperature().getDewPointInFahrenheit() + " F");
        } else {
            System.out.println(
                    "\nTemperature   : The temperatures cannot be determined");
        }

        if (getRunwayVisualRanges() != null) {
            System.out.println("\nTotal Runway Visual Ranges: "
                    + getRunwayVisualRanges().size());

            List<String> runwayVisualRangeList
                    = new ArrayList<>(getRunwayVisualRanges().values());
            Collections.sort(runwayVisualRangeList);
            runwayVisualRangeList.forEach((value) -> {
                System.out.println("Value: " + value);
            });
        } else {
            System.out.println(
                    "\nRunway Visual Range   : The RVR cannot be determined");
        }

        if (getWeatherConditions() != null) {
            System.out.println("\nTotal weather conditions: "
                    + getWeatherConditions().size());

            List<String> weathCondList
                    = new ArrayList<>(getWeatherConditions().values());
            Collections.sort(weathCondList);
            weathCondList.forEach((value) -> {
                System.out.println("Value: " + value);
            });
        } else {
            System.out.println(
                    "\nWeather Conditions   : The weather conditions cannot be determined");
        }

        if (getSkyConditions() != null) {
            System.out.println("\nTotal sky conditions: "
                    + getSkyConditions().size());

            List<String> skyCondList = new ArrayList<>(getSkyConditions().values());
            Collections.sort(skyCondList);
            skyCondList.forEach((value) -> {
                System.out.println("Value: " + value.substring(2));
            });
        } else {
            System.out.println(
                    "\nSky Conditions   : The sky conditions cannot be determined");
        }

        System.out.println();

        if (isNoSignificantChange()) {
            System.out.println(Configs.getInstance()
                    .getString("WEATHER_DECODED_NO_SIGNIFICANT_CHANGE"));
        }
        System.out.println();

        if (getRemarks() != null) {
            System.out.println("Remarks are as follows");
            System.out.println(getRemarks().getDecodedRemarksString());
        } else {
            System.out.println("There are no remarks for this");
        }

        //if (!getParseString().isEmpty()) {
        if (getParseString() != null) {
            System.out.println("Unparsed Data is as follows");
            System.out.println(getParseString());
        } else {
            System.out.println("There is no unparsed data for this");
        }
    }
}
