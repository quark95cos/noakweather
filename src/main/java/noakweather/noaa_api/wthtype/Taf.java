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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
 * Class representing the TAF parsing. It inherits from the AviaWeath class
 *
 * Author: quark95cos Since: Copyright(c) 2022
 */
public class Taf extends AviaWeath {

    private static final Logger LOGGER
            = LogManager.getLogger(Taf.class.getName());

    /**
     * Constructor
     */
    public Taf() {
        LOGGER.debug("in Taf constructor");
    }

    /**
     * Parses the taf information
     *
     * @param tafString
     * @throws noakweather.utils.UtilsException
     */
    public void parse(String tafString) throws UtilsException {
        String tafMain = null;
        String tafRemarks = null;

        LOGGER.debug(Configs.getInstance().getString("TAF_DECODED_TAF_STRING")
                + " #" + tafString + "#");

        // Check to see if we have a tafString to parse
        if (tafString == null || tafString.length() == 0) {
            LOGGER.debug(Configs.getInstance().getString("TAF_DECODED_TAF_EMPTY"));
            return;
        }

        setReportString(tafString);

        //String[] tafParts = tafString.split(Configs.getInstance()
        //                .getString("EXTENDED_REMARKS"));
        // First split the metar string to break out the Remarks section so
        // it can be parsed first
        if (tafString.contains(Configs.getInstance().getString("EXTENDED_REMARKS"))) {
            String[] tafParts = UtilsMisc
                    .stringSplit(tafString, Configs.getInstance()
                            .getString("EXTENDED_REMARKS"));
            tafMain = tafParts[0];
            tafRemarks = tafParts[1];
        } else {
            tafMain = tafString;
        }

        prepaviaWeathHandlers();
        tafMain = prepTafString(tafMain);
        String[] tafSplitStrings = UtilsMisc.stringSplit(tafMain,
                Configs.getInstance().getString("MISC_DEL_0x1E"));
        for (int i = 0; i < tafSplitStrings.length; i++) {
            LOGGER.info("[" + i + "] #" + tafSplitStrings[i].trim() + "#");
            parseTafData(tafSplitStrings[i].trim() + " ");
        }
        LOGGER.debug("\n");

        //Parse the Remarks section if it exists
        if (tafRemarks != null) {
            if (tafRemarks.length() > 0) {
                tafRemarks = tafRemarks + " ";
            }
            LOGGER.debug(Configs.getInstance().getString("METAR_DECODED_METAR_REMARKS")
                    + " #" + tafRemarks + "#");
            parseTafRemarks(tafRemarks);
        }
    }

    /**
     * Parse the taf information
     *
     * @param tafData
     * @throws noakweather.utils.UtilsException
     */
    private void parseTafData(String tafData) throws UtilsException {
        parseAviaHandlers(tafData, aviaMainWthHandlers, Configs.getInstance()
                .getString("AVIA_MAIN_HANDLERS"));
    }

    /**
     * Parse the remarks information
     *
     * @param tafRemarks
     * @throws noakweather.utils.UtilsException
     */
    private void parseTafRemarks(String tafRemarks) throws UtilsException {
        remarks = new Remarks();
        aviaRemarkWthHandlers = RegExprHandlers.setRemarksHandlers();
        parseAviaHandlers(tafRemarks, aviaRemarkWthHandlers, Configs.getInstance()
                .getString("AVIA_REMARK_HANDLERS"));
    }

    /**
     * Prepare the avia weather handlers information
     *
     * @throws noakweather.utils.UtilsException
     */
    private void prepaviaWeathHandlers() throws UtilsException {
        aviaMainWthHandlers = RegExprHandlers.setMainHandlers();
        // Add additional handlers that are necessary
        aviaMainWthHandlers.put(RegExprConst.TAF_STR_PATTERN,
                Pair.with("tafstr", false));
        aviaMainWthHandlers.put(RegExprConst.GROUP_BECMG_TEMPO_PROB_PATTERN,
                Pair.with("grpbecmgtempprob", false));
        aviaMainWthHandlers.put(RegExprConst.GROUP_FM_PATTERN,
                Pair.with("grpfm", false));
        // Add the UNPARSED_PATTERN after calling aviaMainWthHandlers for any specific handlers
        // that are necessary
        aviaMainWthHandlers.put(RegExprConst.UNPARSED_PATTERN,
                Pair.with("unparsed", false));
        LOGGER.debug("\n");
        LOGGER.debug(Configs.getInstance().getString("TAF_DECODED_TAF_MAIN_HANDLERS"));
        aviaMainWthHandlers.forEach((k, v) -> LOGGER.debug((k + ":" + v)));
        LOGGER.debug(Configs.getInstance().getString("TAF_DECODED_TAF_MAIN_HANDLERS") + "\n");

        aviaGroupWthHandlers = RegExprHandlers.setGroupHandlers();
        LOGGER.debug("\n");
        LOGGER.debug(Configs.getInstance().getString("TAF_DECODED_TAF_GROUPS_HANDLERS"));
        aviaGroupWthHandlers.forEach((k, v) -> LOGGER.debug((k + ":" + v)));
        LOGGER.debug(Configs.getInstance().getString("TAF_DECODED_TAF_GROUPS_HANDLERS") + "\n");
    }

    /**
     * Prepare the taf string information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    private String prepTafString(String token) throws UtilsException {
        // Add the "0x1E" character so it can be as the delimiter
        Pattern pattern = Pattern.compile(Configs.getInstance()
                .getString("EXTENDED_SP_FM"), Pattern.LITERAL);
        Matcher matcher = pattern.matcher(token);

        int pos = 0;
        matcher.reset();
        boolean result = matcher.find();
        if (result) {
            StringBuffer sb = new StringBuffer();
            do {
                pos = matcher.start() + 1;
                //System.out.println("pos = " + pos);
                //System.out.println("tafString pos = " + tafString.charAt(pos) + "  tafString pos+2 = " + tafString.charAt(pos+2));
                if (UtilsMisc.containsOnlyNumbers(Character.toString(token.charAt(pos + 2)))) {
                    matcher.appendReplacement(sb, Configs.getInstance().getString("MISC_DEL_0x1E")
                            + Configs.getInstance().getString("EXTENDED_FM"));
                }
                result = matcher.find();
                //System.out.println("result = " + result);
            } while (result);
            matcher.appendTail(sb);
            token = sb.toString();
        }

        token = token.replace(Configs.getInstance().getString("EXTENDED_BECMG"),
                Configs.getInstance().getString("MISC_DEL_0x1E")
                + Configs.getInstance().getString("EXTENDED_BECMG"));
        token = token.replace(Configs.getInstance().getString("EXTENDED_REMARKS"),
                Configs.getInstance().getString("MISC_DEL_0x1E")
                + Configs.getInstance().getString("EXTENDED_REMARKS"));
        token = token.replace(Configs.getInstance().getString("EXTENDED_TEMPO"),
                Configs.getInstance().getString("MISC_DEL_0x1E")
                + Configs.getInstance().getString("EXTENDED_TEMPO"));
        token = token.replace(Configs.getInstance().getString("EXTENDED_TEMP0"),
                Configs.getInstance().getString("MISC_DEL_0x1E")
                + Configs.getInstance().getString("EXTENDED_TEMPO"));
        token = token.replace(Configs.getInstance().getString("EXTENDED_PROB"),
                Configs.getInstance().getString("MISC_DEL_0x1E")
                + Configs.getInstance().getString("EXTENDED_PROB"));
        token = token.replace(Configs.getInstance().getString("EXTENDED_PR0B"),
                Configs.getInstance().getString("MISC_DEL_0x1E")
                + Configs.getInstance().getString("EXTENDED_PROB"));
        token = token.replace(Configs.getInstance().getString("EXTENDED_TX"),
                Configs.getInstance().getString("MISC_DEL_0x1E")
                + Configs.getInstance().getString("EXTENDED_TX"));
        // TODO Extended AMD and AMDS
        // This is for ExtendedConstants.EXTENDED_AMD and
        // ExtendedConstants.EXTENDED_AMDS This is causing issue.
        //tafString = tafString.replace(ExtendedConstants.EXTENDED_AMD,
        //    MiscConstants.MISC_DEL_0x1E + ExtendedConstants.EXTENDED_AMD);
        token = token.replace(Configs.getInstance().getString("EXTENDED_LAST"),
                Configs.getInstance().getString("MISC_DEL_0x1E")
                + Configs.getInstance().getString("EXTENDED_LAST"));
        LOGGER.debug(Configs.getInstance().getString("TAF_DECODED_TAF_0x1E_STRING")
                + " #" + token + "#\n");

        return token;
    }

    /**
     * Display the taf data in a human-readable format
     */
    public void print() {
        System.out.println("In Taf Print");
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

        if (getValidFromDate() != null && getValidToDate() != null) {
            System.out.println("Report valid from " + getValidFromDate()
                    + " to " + getValidToDate());
        } else {
            System.out.println("Valid from and to is unknown");
        }

        if (getTemperature() != null) {
            System.out.println("Maximum Temperature : "
                    + getTemperature().getMaximumTemperature() + " C at "
                    + // + maxTempDate +
                    "\nMinimum Temperature : "
                    + getTemperature().getMinimumTemperature() + " C at "); // + minTempDate);
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
                        "Wind       : The wind direction and speed cannot be determined");
            }
        }

        if (getVisibility() != null) {
            if (getVisibility().isCavok()) {
                System.out.println("Visibility : "
                        + Configs.getInstance().getString("WEATHER_DECODED_CAVOK"));
            } else if (getVisibility().isVisibilityGreaterThan()) {
                System.out.println("Visibility : Greater than "
                        + getVisibility().getVisibility() + " mile(s), "
                        + getVisibility().getVisibilityKilometers() + " km(s)");
                //System.out.println("Visibility : " +
                //    WeatherConstants.WEATHER_DECODED_P6SM);
            } else if (!getVisibility().isVisibilityLessThan()) {
                System.out.println("Visibility : "
                        + getVisibility().getVisibility() + " mile(s), "
                        + getVisibility().getVisibilityKilometers() + " km(s)");
            } else {
                System.out.println("Visibility : Less than "
                        + getVisibility().getVisibility() + " mile(s), "
                        + getVisibility().getVisibilityKilometers() + " km(s)");
            }
        }

        if (getPressure() != null) {
            if (getPressure().getPressure() == null) {
                System.out.println(
                        "Pressure   : The pressure cannot be determined");
            } else {
                System.out.println("Pressure   : "
                        + getPressure().getPressure() + " in Hg, "
                        + getPressure().getPressureInHectoPascals() + " in hPa");
            }
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
        }

        if (getSkyConditions() != null) {
            System.out.println("\nTotal sky conditions: "
                    + getSkyConditions().size());

            List<String> skyCondList
                    = new ArrayList<>(getSkyConditions().values());
            Collections.sort(skyCondList);
            skyCondList.forEach((value) -> {
                System.out.println("Value: " + value.substring(2));
            });
        }

        System.out.println();

        //if (!getParseString().isEmpty()) {
        if (getParseString() != null) {
            System.out.println("Unparsed Data is as follows");
            System.out.println(getParseString());
        } else {
            System.out.println("There is no unparsed data for this");
        }

        System.out.println();

        if (getFromGroups() != null) {
            List<String> fromGroupsList
                    = new ArrayList<>(getFromGroups().values());
            Collections.sort(fromGroupsList);
            fromGroupsList.forEach((value) -> {
                System.out.println("Value: " + value.substring(2));
            });
        }

        System.out.println();

        if (getBecomings() != null) {
            List<String> becomingsList
                    = new ArrayList<>(getBecomings().values());
            Collections.sort(becomingsList);
            becomingsList.forEach((value) -> {
                System.out.println("Value: " + value.substring(2));
            });
        }

        System.out.println();

        if (getTempos() != null) {
            List<String> temposList
                    = new ArrayList<>(getTempos().values());
            Collections.sort(temposList);
            temposList.forEach((value) -> {
                System.out.println("Value: " + value.substring(2));
            });
        }

        System.out.println();

        if (getProbs() != null) {
            List<String> temposList
                    = new ArrayList<>(getProbs().values());
            Collections.sort(temposList);
            temposList.forEach((value) -> {
                System.out.println("Value: " + value.substring(2));
            });
        }

        System.out.println();

        if (getRemarks() != null) {
            System.out.println("Remarks are as follows");
            System.out.println(getRemarks().getDecodedRemarksString());
        }
    }
}
