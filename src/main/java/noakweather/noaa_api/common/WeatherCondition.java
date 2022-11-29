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

import java.util.MissingResourceException;
import java.util.regex.Matcher;
import noakweather.utils.Configs;
import noakweather.utils.IndexedLinkedHashMap;
import noakweather.utils.UtilsException;
import noakweather.utils.WthItemHandlers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class representing the weather conditions
 *
 * Author: quark95cos Since: Copyright(c) 2022
 */
public class WeatherCondition {

    private int precpBeginTime;
    private int precpEndTime;
    private boolean isPrecpBeginTime;
    private boolean isPrecpEndTime;
    private String decodedIntensity;
    private String decodedInVicinity;
    private String decodedDescriptor;
    private String decodedPrecipitation;
    private String decodedObstruction;
    private String decodedNoSignificantWeather;

    private final IndexedLinkedHashMap<String, String> aviaWeathCondWthItemsHandlers;

    private static final Logger LOGGER
            = LogManager.getLogger(WeatherCondition.class.getName());

    /**
     * Constructor
     */
    public WeatherCondition() {
        this.precpBeginTime = 0;
        this.precpEndTime = 0;
        this.isPrecpBeginTime = false;
        this.isPrecpEndTime = false;
        this.decodedIntensity = null;
        this.decodedInVicinity = null;
        this.decodedDescriptor = null;
        this.decodedPrecipitation = null;
        this.decodedObstruction = null;
        this.decodedNoSignificantWeather = null;
        this.aviaWeathCondWthItemsHandlers
                = WthItemHandlers.setWeathCondWthItemsHandlers();
    }

    /**
     * Set the weather condition information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setWeatherConditionItems(Matcher token) throws UtilsException {
        try {
            LOGGER.debug("int: #" + token.group("int") + "#");
            LOGGER.debug("desc: #" + token.group("desc") + "#");
            LOGGER.debug("prec: #" + token.group("prec") + "#");
            LOGGER.debug("obsc: #" + token.group("obsc") + "#");
            LOGGER.debug("other: #" + token.group("other") + "#");
            LOGGER.debug("int2: #" + token.group("int2") + "#");

            // Default decodedIntensity to Moderate
            decodedIntensity = Configs.getInstance().getString("WEATHER_DECODED_MODERATE");

            if (token.group("other") != null && token.group("other")
                    .equals(Configs.getInstance().getString("WEATHER_NO_SIGNIFICANT_WEATHER"))) {
                setNoSignificantWeather();
                LOGGER.debug(Configs.getInstance().getString("WEATHER_DECODED_GROUP")
                        + " " + Configs.getInstance().getString("WEATHER_DECODED_NO_SIGNIFICANT_WEATHER")
                        + ": " + token.group("other"));
                return;
            }

            if (token.group("int") != null && !token.group("int").equals("")) {
                if (token.group("int").equals(Configs.getInstance().getString("WEATHER_HEAVY"))
                        || token.group("int").equals(Configs.getInstance().getString("WEATHER_LIGHT"))) {
                    setIntensity(token.group("int"));
                    LOGGER.debug(Configs.getInstance().getString("WEATHER_DECODED_GROUP")
                            + " " + Configs.getInstance().getString("WEATHER_DECODED_INTENSITY")
                            + ": " + token.group("int"));
                } else if (token.group("int").equals(Configs.getInstance().getString("LOC_TIME_VC"))) {
                    setInVicinity();
                    LOGGER.debug(Configs.getInstance().getString("WEATHER_DECODED_GROUP")
                            + " " + Configs.getInstance().getString("LOC_TIME_DECODED_VC")
                            + ": " + token.group("int"));
                } else {
                    LOGGER.debug(Configs.getInstance().getString("WEATHER_DECODED_GROUP")
                            + " " + Configs.getInstance().getString("WEATHER_DECODED_INTENSITY")
                            + ": " + Configs.getInstance().getString("WEATHER_DECODED_MODERATE"));
                }
            }

            // If we have a descriptor
            if (token.group("desc") != null) {
                setDescriptor(token.group("desc"));
                LOGGER.debug(Configs.getInstance().getString("WEATHER_DECODED_GROUP")
                        + " " + Configs.getInstance().getString("WEATHER_DECODED_DESCRIPTOR")
                        + ": " + token.group("desc"));
            } else {
                LOGGER.debug(Configs.getInstance().getString("WEATHER_DECODED_GROUP")
                        + " " + Configs.getInstance().getString("WEATHER_DECODED_DESCRIPTOR")
                        + ": " + Configs.getInstance().getString("WEATHER_DECODED_NO_DESCRIPTOR"));
            }

            // If we have precipitation (we should always except for when
            // precipitation is in vicinity!)
            if (token.group("prec") != null && !token.group("prec").equals("")) {
                int index = 0;
                String precStr;
                while (index != token.group("prec").length()) {
                    precStr = token.group("prec").substring(index, index + 2);

                    setPrecipitation(precStr);
                    LOGGER.debug(Configs.getInstance().getString("WEATHER_DECODED_GROUP")
                            + " " + Configs.getInstance().getString("WEATHER_DECODED_PRECIPITATION")
                            + ": " + precStr);
                    index = index + 2;
                }
            } else {
                LOGGER.debug(Configs.getInstance().getString("WEATHER_DECODED_GROUP")
                        + " " + Configs.getInstance().getString("WEATHER_DECODED_NO_PRECIPITATION"));
            }

            // If we have obstruction (we should always except for when
            // obstruction is in vicinity!)
            if (token.group("obsc") != null && !token.group("obsc").equals("")) {
                setObstruction(token.group("obsc"));
                LOGGER.debug(Configs.getInstance().getString("WEATHER_DECODED_GROUP")
                        + " " + Configs.getInstance().getString("WEATHER_DECODED_OBSTRUCTION")
                        + ": " + token.group("obsc"));
            } else {
                LOGGER.debug(Configs.getInstance().getString("WEATHER_DECODED_GROUP")
                        + " " + Configs.getInstance().getString("WEATHER_DECODED_NO_OBSTRUCTION"));
            }
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setWeatherConditionItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            LOGGER.error(errMsg);
            throw new UtilsException("setWeatherConditionItems: ", err);
        }
    }

    /**
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setWeatherConditionBegEnd(Matcher token) throws UtilsException {
        try {
            LOGGER.debug("begin: #" + token.group("begin") + "#");
            LOGGER.debug("begint: #" + token.group("begint") + "#");
            LOGGER.debug("end: #" + token.group("end") + "#");
            LOGGER.debug("endt: #" + token.group("endt") + "#");

            if (token.group("begint") != null && !token.group("begint").equals("")) {
                isPrecpBeginTime = true;
                precpBeginTime = Integer.parseInt(token.group("begint"));
            }

            if (token.group("endt") != null && !token.group("endt").equals("")) {
                isPrecpEndTime = true;
                precpEndTime = Integer.parseInt(token.group("endt"));
            }
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setWeatherConditionBegEnd: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            LOGGER.error(errMsg);
            throw new UtilsException("setWeatherConditionBegEnd: ", err);
        }
    }

    /**
     * Set the intensity for a given weather condition
     *
     * @param intensity the part of a METAR weather condition token which
     * represents the intensity of the weather condition (e.g. '-' - light, '+'
     * - heavy)
     */
    private void setIntensity(String intensity) throws UtilsException {
        try {
            if (intensity != null && !intensity.equals("")) {
                decodedIntensity = aviaWeathCondWthItemsHandlers
                        .getValueAtIndex(aviaWeathCondWthItemsHandlers
                                .getIndexOf(intensity));
            }
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setIntensity: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedIntensity = errMsg;
            LOGGER.error(errMsg);
            throw new UtilsException("setIntensity: ", err);
        }
    }

    /**
     * Set the inVicinity for this Weather Condition
     *
     *
     */
    private void setInVicinity() throws UtilsException {
        try {
            decodedInVicinity = Configs.getInstance().getString("WEATHER_DECODED_VC");
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setInVicinity: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedInVicinity = errMsg;
            LOGGER.error(errMsg);
            throw new UtilsException("setInVicinity: ", err);
        }
    }

    /**
     * Set the isNoSignificantWeather for this Weather Condition
     *
     *
     * @throws noakweather.utils.UtilsException
     */
    public void setNoSignificantWeather() throws UtilsException {
        try {
            decodedNoSignificantWeather = Configs.getInstance()
                    .getString("WEATHER_DECODED_NO_SIGNIFICANT_WEATHER");
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setNoSignificantWeather: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedNoSignificantWeather = errMsg;
            LOGGER.error(errMsg);
            throw new UtilsException("setNoSignificantWeather: ", err);
        }
    }

    /**
     * Set the descriptor for this Weather Condition
     *
     * @param descriptor the part of a METAR weather condition token which
     * represents a description of the quality of the precipitation (e.g. 'BC' -
     * patches, 'SH' - showers)
     * @throws noakweather.utils.UtilsException
     */
    public void setDescriptor(String descriptor) throws UtilsException {
        try {
            decodedDescriptor = aviaWeathCondWthItemsHandlers
                    .getValueAtIndex(aviaWeathCondWthItemsHandlers
                            .getIndexOf(descriptor));
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setDescriptor: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedDescriptor = errMsg;
            LOGGER.error(errMsg);
            throw new UtilsException("setDescriptor: ", err);
        }
    }

    /**
     * Set the precipitation for this Weather Condition
     *
     * @param precipitation the part of a METAR weather condition token which
     * represents a specific type of precipitation (e.g. 'SN', 'RA')
     * @throws noakweather.utils.UtilsException
     */
    public void setPrecipitation(String precipitation) throws UtilsException {
        try {
            decodedPrecipitation = aviaWeathCondWthItemsHandlers
                    .getValueAtIndex(aviaWeathCondWthItemsHandlers
                            .getIndexOf(precipitation));
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setPrecipitation: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedPrecipitation = errMsg;
            LOGGER.error(errMsg);
            throw new UtilsException("setPrecipitation: ", err);
        }
    }

    /**
     * Set the obstruction for this Weather Condition
     *
     * @param obstruction the part of a METAR weather condition token which
     * represents a specific type of obstruction (e.g. 'FG', 'HZ')
     * @throws noakweather.utils.UtilsException
     */
    public void setObstruction(String obstruction) throws UtilsException {
        try {
            decodedObstruction = aviaWeathCondWthItemsHandlers
                    .getValueAtIndex(aviaWeathCondWthItemsHandlers
                            .getIndexOf(obstruction));
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setObstruction: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedObstruction = errMsg;
            LOGGER.error(errMsg);
            throw new UtilsException("setObstruction: ", err);
        }
    }

    /**
     * Get the decoded descriptor
     *
     * @return decodedDescriptor
     */
    public String getDecodedDescriptor() {
        return decodedDescriptor;
    }

    /**
     * Get decoded intensity
     *
     * @return decodedIntensity
     */
    public String getDecodedIntensity() {
        return decodedIntensity;
    }

    /**
     * Get decoded precipitation
     *
     * @return decodedPrecipitation
     */
    public String getDecodedPrecipitation() {
        return decodedPrecipitation;
    }

    /**
     * Get decoded obstruction
     *
     * @return decodedObstruction
     */
    public String getDecodedObstruction() {
        return decodedObstruction;
    }

    /**
     * Set decoded descriptor
     *
     * @param string
     */
    public void setDecodedDescriptor(String string) {
        decodedDescriptor = string;
    }

    /**
     * Set decoded intensity
     *
     * @param string
     */
    public void setDecodedIntensity(String string) {
        decodedIntensity = string;
    }

    /**
     * Set decoded precipitation
     *
     * @param string
     */
    public void setDecodedPrecipitation(String string) {
        decodedPrecipitation = string;
    }

    /**
     * Set decoded obstruction
     *
     * @param string
     */
    public void setDecodedObstruction(String string) {
        decodedObstruction = string;
    }

    /**
     * Get the natural language in human readable form This method will return a
     * string that represents this weather condition using natural language (as
     * opposed to METAR)
     *
     * @return a string that represents the weather condition in natural
     * language
     * @throws noakweather.utils.UtilsException
     */
    public String getNaturalLanguageString() throws UtilsException {
        try {
            String temp = "";

            if (decodedNoSignificantWeather != null
                    && !decodedNoSignificantWeather.isEmpty()) {
                temp += decodedNoSignificantWeather;
                return temp;
            }

            temp += decodedIntensity;
            if (decodedDescriptor != null && !decodedDescriptor.isEmpty()) {
                temp += " " + decodedDescriptor;
            }

            if (decodedPrecipitation != null && !decodedPrecipitation.isEmpty()) {
                temp += " " + decodedPrecipitation;
            }

            if (decodedObstruction != null && !decodedObstruction.isEmpty()) {
                temp += " " + decodedObstruction;
            }

            if (decodedInVicinity != null && !decodedInVicinity.isEmpty()) {
                temp += " " + decodedInVicinity;
            }

            return temp;
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "getNaturalLanguageString: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            LOGGER.error(errMsg);
            throw new UtilsException("getNaturalLanguageString: ", err);
        }
    }

    /**
     * Get the natural language in human readable form This method will return a
     * string that represents this weather conditions begin and end times using
     * natural language (as opposed to METAR)
     *
     * @return a string that represents the weather condition in natural
     * language
     * @throws noakweather.utils.UtilsException
     */
    public String getNaturalLanguageBegEndString() throws UtilsException {
        try {
            String temp = "";

            if (isPrecpBeginTime) {
                temp += " begins at " + precpBeginTime + " minutes past the hour";
            }

            if (isPrecpEndTime) {
                temp += " ends at " + precpEndTime + " minutes past the hour";
            }

            return temp;
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "getNaturalLanguageBegEndString: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            LOGGER.error(errMsg);
            throw new UtilsException("getNaturalLanguageBegEndString: ", err);
        }
    }
}
