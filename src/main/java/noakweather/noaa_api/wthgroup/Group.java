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
package noakweather.noaa_api.wthgroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import noakweather.noaa_api.common.SkyCondition;
import noakweather.noaa_api.common.WeatherCondition;
import noakweather.noaa_api.weather.Pressure;
import noakweather.noaa_api.weather.Temperature;
import noakweather.noaa_api.weather.Visibility;
import noakweather.noaa_api.weather.Wind;
import noakweather.utils.Configs;
import noakweather.utils.IndexedLinkedHashMap;
import noakweather.utils.UtilsDate;
import noakweather.utils.UtilsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;

/**
 * Class representing the base class of the Prob and Tempo classes
 *
 * Author: quark95cos Since: Copyright(c) 2022
 */
public class Group {

    private int skyCondIndex;
    private final boolean isValidFromToDate;
    private String monthString;
    private String yearString;
    private Date validFromDate;
    private Date validToDate;
    private Wind windGroup;
    private Visibility visibilityGroup;
    private Temperature temperatureGroup;
    private Pressure pressureGroup;
    private WeatherCondition weatherConditionGroup;
    private IndexedLinkedHashMap<WeatherCondition, String> weatherConditionsGroup;
    private SkyCondition skyConditionGroup;
    private IndexedLinkedHashMap<SkyCondition, String> skyConditionsGroup;
    private ArrayList<String> parseString = new ArrayList<>();

    private static final Logger LOGGER
            = LogManager.getLogger(Group.class.getName());

    /**
     * Constructor
     */
    public Group() {
        LOGGER.debug("in Group constructor");
        this.skyCondIndex = 0;
        this.isValidFromToDate = false;
        this.monthString = "";
        this.yearString = "";
        this.validFromDate = null;
        this.validToDate = null;
        this.windGroup = null;
        this.visibilityGroup = null;
        this.temperatureGroup = null;
        this.pressureGroup = null;
        this.weatherConditionGroup = null;
        this.weatherConditionsGroup = null;
        this.skyConditionGroup = null;
        this.skyConditionsGroup = null;
        this.parseString = null;
    }

    /**
     * Parse the group handlers information
     *
     * @param token
     * @param handlers
     * @throws noakweather.utils.UtilsException
     */
    protected void parseGroupHandlers(String token, IndexedLinkedHashMap<Pattern, Pair<String, Boolean>> handlers) throws UtilsException {
        boolean isFound = false;
        while (!token.isEmpty()) {
            LOGGER.debug("\n");
            LOGGER.debug(Configs.getInstance().getString("MATCH_DECODED_TOKEN_PROCESSING")
                    + " #" + token + "#");
            handlers.keySet();
            for (Pattern i : handlers.keySet()) {
                LOGGER.debug(Configs.getInstance().getString("MATCH_DECODED_PATTERN_I")
                        + " #" + i + "#  #" + handlers.get(i) + "#");
                Matcher matcher = i.matcher(token);
                while (matcher.find()) {
                    isFound = true;
                    LOGGER.debug(Configs.getInstance().getString("MATCH_DECODED_MATCHER_GROUP_CNT")
                            + " " + matcher.groupCount());
                    LOGGER.debug(Configs.getInstance().getString("MATCH_DECODED_MATCHER_GROUP_0")
                            + " #" + matcher.group(0) + "#");
                    if (matcher.group(0).equals("")) {
                        break;
                    }
                    for (int j = 1; j <= matcher.groupCount(); j++) {
                        LOGGER.debug(Configs.getInstance()
                                .getString("MATCH_DECODED_CAPTURE_GROUP_NUMBER")
                                + " " + Configs.getInstance()
                                        .getString("MATCH_DECODED_CAPTURE_GROUP_NUMBER")
                                + " " + j + "   " + Configs.getInstance()
                                        .getString("MATCH_DECODED_CAPTURED_TEXT")
                                + " #" + matcher.group(j) + "#");
                    }
                    detGroupItems(handlers.get(i).getValue0(), matcher);
                    token = matcher.replaceFirst("").trim();
                    //This is necessary to make sure the last token is properly processed
                    if (token.length() > 0) {
                        token = token + " ";
                    }
                    LOGGER.debug(Configs.getInstance().getString("MATCH_DECODED_TOKEN_AFTER_LAST_MATCH")
                            + " #" + token);
                    LOGGER.debug(Configs.getInstance().getString("MATCH_DECODED_PATTERN")
                            + " " + handlers.get(i).getValue1());
                    if (handlers.get(i).getValue1().equals(false)) {
                        break;
                    }
                    matcher = i.matcher(token);
                }
                if (isFound) {
                    isFound = false;
                    break;
                }
            }
        }
    }

    /**
     * Determine the group (BECMG, TEMPO , PROB and FM)
     *
     * @param type
     * @param value
     */
    private void detGroupItems(String type, Matcher value) throws UtilsException {
        switch (type) {
            case "valtmper":
                // We have a valid to and from time period
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_VALID_TO_FROM_TP"));
                setValidToFromDateInfo(value);
                break;
            case "wind":
                // We have a wind
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_WIND"));
                if (windGroup == null) {
                    windGroup = new Wind();
                }
                windGroup.setMainWindItems(value, 'M');
                break;
            case "visibility":
                // We have visibility
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_VISIBILITY"));
                if (visibilityGroup == null) {
                    visibilityGroup = new Visibility();
                }
                visibilityGroup.setVisibilityItems(value);
                break;
            case "runway":
                // We have Runaway group
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_RVR"));
                //if (runwayVisualRanges == null) {
                //    runwayVisualRanges = new ConcurrentHashMap<>();
                //}
                //parseRunVisualRange(value);
                break;
            case "presentweather":
                // We have Present Weather
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_WEATHER_GROUPS"));
                if (weatherConditionsGroup == null) {
                    weatherConditionsGroup = new IndexedLinkedHashMap<>();
                }
                weatherConditionGroup = new WeatherCondition();
                weatherConditionGroup.setWeatherConditionItems(value);
                weatherConditionsGroup.put(weatherConditionGroup,
                        weatherConditionGroup.getNaturalLanguageString());
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_WEATHER_CONDITIONS")
                        + " " + weatherConditionGroup.getNaturalLanguageString());
                break;
            case "skycondition":
                // We have a sky condition
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_SKY_CONDITIONS"));
                if (skyConditionsGroup == null) {
                    skyConditionsGroup = new IndexedLinkedHashMap<>();
                }
                skyConditionGroup = new SkyCondition();
                skyConditionGroup.setSkyConditionItems(value);
                skyCondIndex++;
                skyConditionsGroup.put(skyConditionGroup,
                        String.valueOf(skyCondIndex) + " "
                        + skyConditionGroup.getNaturalLanguageString());
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_SKY_CONDITIONS")
                        + " " + skyConditionGroup.getNaturalLanguageString());
                break;
            case "tempdewpoint":
                // We have Temperature / Dew Point
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_TEMPERATURE_DEWPOINT"));
                if (temperatureGroup == null) {
                    temperatureGroup = new Temperature();
                }
                temperatureGroup.setTemperatureItems(value);
                break;
            case "altimeter":
                // We have pressure
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_PRESSURE"));
                if (pressureGroup == null) {
                    pressureGroup = new Pressure();
                }
                pressureGroup.setPressureItems(value);
                break;
            case "nosigchng":
                // We have no significant change
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_NO_SIGNIFICANT_CHANGE"));
                //setIsNoSignificantChange(true);
                break;
            case "unparsed":
                // We have Unparsed Data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_UNPARSED_DATA"));
                if (parseString == null) {
                    parseString = new ArrayList<>();
                }
                setParseString(value);
                break;
            default:
                break;
        }
    }

    /**
     * Set the valid from date information
     *
     * @param token
     */
    public void setValidFromDate(String token) {
        if (token != null) {
            try {
                Group.this.setValidFromDate(UtilsDate.setDate(token.substring(0, 2),
                        token.substring(2, 4), token.substring(4, 6),
                        monthString, yearString));
                LOGGER.debug(Configs.getInstance().getString("LOC_TIME_DECODED_VALID_FROM_DATE")
                        + " " + getValidFromDate() + "\n");
            } catch (NumberFormatException | UtilsException e) {
                String errMsg = Configs.getInstance()
                        .getString("LOC_TIME_DECODED_UNABLE_PARSE_DATE_VALUE") + " " + e;
                LOGGER.error(errMsg);
            }
        }
    }

    /**
     * Set the valid to and from date information
     *
     * @param token
     */
    protected void setValidToFromDateInfo(Matcher token) {
        if (token.group("bvaltime") != null && token.group("evaltime") != null) {
            // 1918/2018 means it is valid from the 19th 1800Z to the 20th 1800Z
            try {
                setValidFromDate(UtilsDate.setDate(token.group("bvaltime").substring(0, 2),
                        token.group("bvaltime").substring(2, 4), "00",
                        monthString, yearString));
                setValidToDate(UtilsDate.setDate(token.group("evaltime").substring(0, 2),
                        token.group("evaltime").substring(2, 4), "00",
                        monthString, yearString));
                LOGGER.debug(Configs.getInstance().getString("LOC_TIME_DECODED_VALID_FROM_DATE")
                        + " " + getValidFromDate());
                LOGGER.debug(Configs.getInstance().getString("LOC_TIME_DECODED_VALID_TO_DATE")
                        + " " + getValidToDate() + "\n");
            } catch (NumberFormatException | UtilsException e) {
                String errMsg = Configs.getInstance()
                        .getString("LOC_TIME_DECODED_UNABLE_PARSE_DATE_VALUE") + " " + e;
                LOGGER.error(errMsg);
            }
        }
    }

    /**
     * Get the natural language in human readable form This method will return a
     * string that represents this weather condition using natural language (as
     * opposed to METAR)
     *
     * @param groupString
     * @return a string that represents the sky condition in natural language
     */
    protected String getNaturalLanguageString(String groupString) {
        String temp = groupString;

        if (groupString.equals(Configs.getInstance()
                .getString("EXTENDED_DECODED_FM"))) {
            temp += " " + validFromDate;
        } else {
            temp += " from " + validFromDate + " to " + validToDate;
        }

        if (windGroup != null) {
            if (windGroup.getWindNotDetermined() == null) {
                if (windGroup.isWindDirectionIsVariable()) {
                    temp += "\n     Wind Direction is variable";
                } else if (windGroup.isWindDirectionIsVarGtrSix()) {
                    temp += "\n     Wind Direction is variable (greater than 6 knots)";
                    temp += "  Variable between "
                            + windGroup.getwindDirectionVarOneCompass() + " and "
                            + windGroup.getwindDirectionVarTwoCompass() + " ("
                            + windGroup.getWindDirectionVarOne() + " degrees and "
                            + windGroup.getWindDirectionVarTwo() + " degrees)";
                } else {
                    temp += "\n     Wind dir : "
                            + windGroup.getWindDirectionCompass() + " ("
                            + windGroup.getWindDirection() + " degrees)";
                }
                temp += "\n     Wind speed : "
                        + windGroup.getWindSpeedInMPH() + " mph, "
                        + windGroup.getWindSpeedInKnots() + " knots";
                temp += "\n     Wind gusts : "
                        + windGroup.getWindGustsInMPH() + " mph, "
                        + windGroup.getWindGustsInKnots() + " knots";
            } else {
                temp
                        += "  Wind : The wind direction and speed cannot be determined";
            }
        }

        if (visibilityGroup != null) {
            if (visibilityGroup.isCavok()) {
                temp += "\n     Visibility : "
                        + Configs.getInstance().getString("WEATHER_DECODED_CAVOK");
            } else if (visibilityGroup.isVisibilityGreaterThan()) {
                temp += "\n     Visibility : Greater than "
                        + visibilityGroup.getVisibility() + " mile(s), "
                        + visibilityGroup.getVisibilityKilometers() + " km(s)";
            } else if (!visibilityGroup.isVisibilityLessThan()) {
                temp += "\n     Visibility : "
                        + visibilityGroup.getVisibility() + " mile(s), "
                        + visibilityGroup.getVisibilityKilometers() + " km(s)";
            } else {
                temp += "\n     Visibility : Less than "
                        + visibilityGroup.getVisibility() + " mile(s), "
                        + visibilityGroup.getVisibilityKilometers() + " km(s)";
            }
        }

        if (pressureGroup != null) {
            if (pressureGroup.getPressure() == null) {
                temp += "\n     Pressure   : The pressure cannot be determined";
            } else {
                temp += "\n     Pressure   : "
                        + pressureGroup.getPressure() + " in Hg, "
                        + pressureGroup.getPressureInHectoPascals() + " in hPa";
            }
        }

        if (skyConditionGroup != null) {
            temp += "\n\n     Total sky conditions: "
                    + skyConditionsGroup.size();

            List<String> skyCondList
                    = new ArrayList<>(skyConditionsGroup.values());
            Collections.sort(skyCondList);
            temp = skyCondList.stream().map((value)
                    -> "\n     Value: " + value.substring(2)).reduce(temp, String::concat);
        }

        if (weatherConditionsGroup != null) {
            temp += "\n\n     Total weather conditions: "
                    + weatherConditionsGroup.size();

            List<String> skyCondList
                    = new ArrayList<>(weatherConditionsGroup.values());
            Collections.sort(skyCondList);
            temp = skyCondList.stream().map((value)
                    -> "\n     Value: " + value).reduce(temp, String::concat);
        }

        if (parseString != null) {
            temp += "\n\n     Unparsed Data is as follows";
            temp += "\n     " + parseString;
        } else {
            temp += "\n\n     There is no unparsed data for this";
        }

        temp += "\n";

        return temp;
    }

    /**
     * Set validFromDate
     *
     * @param validFromDate
     */
    public void setValidFromDate(Date validFromDate) {
        this.validFromDate = validFromDate;
    }

    /**
     * Get validFromDate
     *
     * @return validFromDate
     */
    public Date getValidFromDate() {
        return validFromDate;
    }

    /**
     * Set validToDate
     *
     * @param validToDate
     */
    public void setValidToDate(Date validToDate) {
        this.validToDate = validToDate;
    }

    /**
     * Get validToDate
     *
     * @return validToDate
     */
    public Date getValidToDate() {
        return validToDate;
    }

    /**
     * Get isValidFromToDate
     *
     * @return isValidFromToDate
     */
    public boolean isValidFromToDate() {
        return isValidFromToDate;
    }

    /**
     * Get monthString
     *
     * @return monthString
     */
    public String getMonthString() {
        return monthString;
    }

    /**
     * Set monthString
     *
     * @param monthString
     */
    public void setMonthString(String monthString) {
        this.monthString = monthString;
    }

    /**
     * Get yearString
     *
     * @return yearString
     */
    public String getYearString() {
        return yearString;
    }

    /**
     * Set yearString
     *
     * @param yearString
     */
    public void setYearString(String yearString) {
        this.yearString = yearString;
    }

    /**
     * Get windGroup
     *
     * @return windGroup
     */
    public Wind getWindBecoming() {
        return windGroup;
    }

    /**
     * Get skyCondIndex
     *
     * @return skyCondIndex
     */
    public int getSkyCondIndex() {
        return skyCondIndex;
    }

    /**
     * Get visibilityGroup
     *
     * @return visibilityGroup
     */
    public Visibility getVisibilityBecoming() {
        return visibilityGroup;
    }

    /**
     * Get skyConditionGroup
     *
     * @return skyConditionGroup
     */
    public SkyCondition getSkyConditionBecoming() {
        return skyConditionGroup;
    }

    /**
     * Get weatherConditionGroup
     *
     * @return weatherConditionGroup
     */
    public WeatherCondition getWeatherConditionBecoming() {
        return weatherConditionGroup;
    }

    /**
     * Set unparsed data string
     *
     * @param token
     */
    public void setParseString(Matcher token) {
        LOGGER.debug("Unparsed: #" + token.group("unparsed") + "#");
        this.parseString.add(token.group("unparsed"));
    }
}
