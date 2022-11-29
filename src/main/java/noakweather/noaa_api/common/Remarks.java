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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.MissingResourceException;
import java.util.regex.Matcher;
import noakweather.noaa_api.weather.Pressure;
import noakweather.noaa_api.weather.Temperature;
import noakweather.noaa_api.weather.Visibility;
import noakweather.noaa_api.weather.Wind;
import noakweather.utils.Configs;
import noakweather.utils.IndexedLinkedHashMap;
import noakweather.utils.UtilsDate;
import noakweather.utils.UtilsException;
import noakweather.utils.UtilsMisc;
import noakweather.utils.WthItemHandlers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;

/**
 * Class representing the main class for the remarks section of the METAR and
 * TAF data
 *
 * Author: quark95cos Since: Copyright(c) 2022
 */
public class Remarks {

    private final StringBuffer decodedRemarksString;
    private final IndexedLinkedHashMap<String, String> aviaRemarkWthItemsHandlers;
    private final IndexedLinkedHashMap<String, Pair<String, String>> aviaRemarkWthAltItemsHandlers;

    private static final Logger LOGGER
            = LogManager.getLogger(Remarks.class.getName());

    /**
     * Constructor
     */
    public Remarks() {
        this.decodedRemarksString = new StringBuffer("");
        this.aviaRemarkWthItemsHandlers
                = WthItemHandlers.setRemarksWthItemsHandlers();
        this.aviaRemarkWthAltItemsHandlers
                = WthItemHandlers.setRemarksWthAltItemsHandlers();
    }

    /**
     * Set the rising/falling rapidly pressure
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setPressureRFRapidlyItems(Matcher token) throws UtilsException {
        try {
            LOGGER.debug("presrisfal: #" + token.group("presrisfal") + "#");

            decodedRemarksString.append(aviaRemarkWthItemsHandlers
                    .getValueAtIndex(aviaRemarkWthItemsHandlers
                            .getIndexOf(token.group("presrisfal")))).append("\n");

            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_REMARKS_STRING")
                    + " " + decodedRemarksString);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setPressureRFRapidlyItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedRemarksString.append(errMsg).append("\n");
            LOGGER.error(errMsg);
            throw new UtilsException("setPressureRFRapidlyItems: ", err);
        }
    }

    /**
     * Set the icing information such as type
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setIcingItems(Matcher token) throws UtilsException {
        try {
            LOGGER.debug("type: #" + token.group("type") + "#");
            LOGGER.debug("typeic: #" + token.group("typeic") + "#");
            LOGGER.debug("typeip: #" + token.group("typeip") + "#");
            LOGGER.debug("extra: #" + token.group("extra") + "#");

            if (token.group("type") != null) {
                decodedRemarksString.append(aviaRemarkWthItemsHandlers
                        .getValueAtIndex(aviaRemarkWthItemsHandlers
                                .getIndexOf(token.group("type"))));
            }
            if (token.group("typeic") != null) {
                decodedRemarksString.append(" ").append(aviaRemarkWthItemsHandlers
                        .getValueAtIndex(aviaRemarkWthItemsHandlers
                                .getIndexOf(token.group("typeic"))));
            }
            if (token.group("typeip") != null) {
                decodedRemarksString.append(" ").append(aviaRemarkWthItemsHandlers
                        .getValueAtIndex(aviaRemarkWthItemsHandlers
                                .getIndexOf(token.group("typeip"))));
            }
            if (token.group("extra") != null) {
                decodedRemarksString.append(" ").append(token.group("extra")
                        .toLowerCase());
            }
            decodedRemarksString.append("\n");

            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_REMARKS_STRING")
                    + " " + decodedRemarksString);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setIcingItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedRemarksString.append(errMsg).append("\n");
            LOGGER.error(errMsg);
            throw new UtilsException("setIcingItems: ", err);
        }
    }

    /**
     * Set the tornadic activity such as type, time and direction
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setTornadicActivity(Matcher token) throws UtilsException {
        try {
            LOGGER.debug("type: #" + token.group("type") + "#");
            LOGGER.debug("betime: #" + token.group("betime") + "#");
            LOGGER.debug("time: #" + token.group("time") + "#");
            LOGGER.debug("dirfrom: #" + token.group("dirfrom") + "#");
            LOGGER.debug("dirto: #" + token.group("dirto") + "#");

            decodedRemarksString.append(aviaRemarkWthItemsHandlers
                    .getValueAtIndex(aviaRemarkWthItemsHandlers
                            .getIndexOf(token.group("type")))).append(" ");

            if (!token.group("betime").isEmpty()) {
                decodedRemarksString.append(aviaRemarkWthItemsHandlers
                        .getValueAtIndex(aviaRemarkWthItemsHandlers
                                .getIndexOf(token.group("betime")))).append(" ")
                        .append(token.group("time")).append(" ")
                        .append(Configs.getInstance().getString("MSRMNT_DECODED_MIN_AFT_HR"))
                        .append(" ");
            }

            decodedRemarksString.append(Configs.getInstance()
                    .getString("LOC_TIME_DECODED_LOCTED")).append(" ");

            if (!token.group("dirfrom").isEmpty()) {
                decodedRemarksString.append(token.group("dirfrom")).append(" ");
            }

            if (!token.group("dirto").isEmpty()) {
                decodedRemarksString.append(Configs.getInstance().getString("LOC_TIME_DECODED_MOVNG"))
                        .append(" ").append(token.group("dirto")).append(" ");
            }

            decodedRemarksString.append("\n");

            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_REMARKS_STRING")
                    + " " + decodedRemarksString);
        } catch (NumberFormatException err) {
            String errMsg = "setTornadicActivity: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedRemarksString.append(errMsg).append("\n");
            LOGGER.error(errMsg);
            throw new UtilsException("setTornadicActivity: ", err);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setTornadicActivity: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedRemarksString.append(errMsg).append("\n");
            LOGGER.error(errMsg);
            throw new UtilsException("setTornadicActivity: ", err);
        }
    }

    /**
     * Set the automated station information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setAutomatedStation(Matcher token) throws UtilsException {
        try {
            LOGGER.debug("type: #" + token.group("type") + "#");

            decodedRemarksString.append(aviaRemarkWthItemsHandlers
                    .getValueAtIndex(aviaRemarkWthItemsHandlers
                            .getIndexOf(token.group("type")
                                    + Configs.getInstance().getString("MISC_VALUE_AS")))).append("\n");

            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_REMARKS_STRING")
                    + " " + decodedRemarksString);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setAutomatedStation: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedRemarksString.append(errMsg).append("\n");
            LOGGER.error(errMsg);
            throw new UtilsException("setAutomatedStation: ", err);
        }
    }

    /**
     * Set the beginning and ending of the weather conditions
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setWeatherConditionBegEnd(WeatherCondition token) throws UtilsException {
        try {
            decodedRemarksString.append(token.getNaturalLanguageString());
            decodedRemarksString.append(token.getNaturalLanguageBegEndString())
                    .append("\n");

            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_REMARKS_STRING")
                    + " " + decodedRemarksString);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setWeatherConditionBegEnd: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedRemarksString.append(errMsg).append("\n");
            LOGGER.error(errMsg);
            throw new UtilsException("setWeatherConditionBegEnd: ", err);
        }
    }

    /**
     * Set the sea level pressure
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setSeaLevelPressure(Pressure token) throws UtilsException {
        try {
            decodedRemarksString.append(Configs.getInstance()
                    .getString("EXTENDED_DECODED_SEA_LEVEL_PRESSURE")).append(" ");
            if (token.getSLPressure() == -1.0f) {
                decodedRemarksString.append(Configs.getInstance()
                        .getString("EXTENDED_DECODED_NO_SEA_LEVEL_PRESSURE"));
            } else {
                decodedRemarksString.append(token.getSLPressure())
                        .append(" ").append(Configs.getInstance().getString("MSRMNT_DECODED_HG"))
                        .append(", ").append(token.getSLPressureInHectoPascals())
                        .append(" ").append(Configs.getInstance().getString("MSRMNT_DECODED_HPA"));
            }
            decodedRemarksString.append("\n");

            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_REMARKS_STRING")
                    + " " + decodedRemarksString);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setSeaLevelPressure: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedRemarksString.append(errMsg).append("\n");
            LOGGER.error(errMsg);
            throw new UtilsException("setSeaLevelPressure: ", err);
        }
    }

    /**
     * Set the next forecast information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setNextForecastItems(Date token) throws UtilsException {
        try {
            decodedRemarksString.append(Configs.getInstance()
                    .getString("EXTENDED_DECODED_NEXT_FORECAST_BY")).append(" ").append(token);

            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_REMARKS_STRING")
                    + " " + decodedRemarksString);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setNextForecastItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedRemarksString.append(errMsg).append("\n");
            LOGGER.error(errMsg);
            throw new UtilsException("setNextForecastItems: ", err);
        }
    }

    /**
     * Set the peak wind speed and direction information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setPeakWindSpeed(Wind token) throws UtilsException {
        try {
            decodedRemarksString.append(Configs.getInstance().getString("EXTENDED_DECODED_PEAK_WIND_DIR"))
                    .append(" ").append(token.getPeakWindDirectionCompass()).append(" (")
                    .append(token.getPeakWindDirection())
                    .append(" ").append(Configs.getInstance().getString("TEMP_DECODED_DEGREES")).append(")\n")
                    .append(Configs.getInstance().getString("EXTENDED_DECODED_PEAK_WIND_SPEED"))
                    .append(" ").append(token.getPeakWindSpeedInMPH())
                    .append(" ").append(Configs.getInstance().getString("WIND_DECODED_MILES_PER_HOUR"))
                    .append(", ").append(token.getPeakWindSpeed())
                    .append(" ").append(Configs.getInstance().getString("WIND_DECODED_KNOTS")).append("\n")
                    .append(Configs.getInstance().getString("EXTENDED_DECODED_PEAK_WIND_SPEED"));
            if (token.getPeakWindHour() != 0) {
                SimpleDateFormat ftWS = new SimpleDateFormat(Configs.getInstance().getString("LOC_TIME_DECODED_HH_AMPM"));
                decodedRemarksString.append(" ").append(token.getPeakWindMin())
                        .append(" ").append(Configs.getInstance().getString("MSRMNT_DECODED_MIN_AFT"))
                        .append(" ").append(ftWS.format(token.getPeakWindHour()));
            } else {
                decodedRemarksString.append(" ").append(token.getPeakWindMin())
                        .append(" ").append(Configs.getInstance().getString("MSRMNT_DECODED_MIN_AFT_HR"));
            }
            decodedRemarksString.append("\n");

            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_REMARKS_STRING")
                    + " " + decodedRemarksString);
        } catch (NumberFormatException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setPeakWindSpeed: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedRemarksString.append(errMsg).append("\n");
            LOGGER.error(errMsg);
            throw new UtilsException("setPeakWindSpeed: ", err);
        }
    }

    /**
     * Set the wind shift information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setWindShiftItems(Matcher token) throws UtilsException {
        try {
            LOGGER.debug("hour: #" + token.group("hour") + "#");
            LOGGER.debug("min: #" + token.group("min") + "#");
            LOGGER.debug("front: #" + token.group("front") + "#");

            SimpleDateFormat ftWS
                    = new SimpleDateFormat(Configs.getInstance().getString("LOC_TIME_DECODED_HH_MM_AMPM"));
            decodedRemarksString.append(Configs.getInstance().getString("EXTENDED_DECODED_WIND_SHIFT_AT"))
                    .append(" ").append(ftWS.format(UtilsDate.setDate("0",
                    token.group("hour"), token.group("min"), "0", "0")))
                    .append("\n");

            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_REMARKS_STRING")
                    + " " + decodedRemarksString);
        } catch (NumberFormatException | UtilsException err) {
            String errMsg = "setWindShiftItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedRemarksString.append(errMsg).append("\n");
            LOGGER.error(errMsg);
            throw new UtilsException("setWindShiftItems: ", err);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setWindShiftItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedRemarksString.append(errMsg).append("\n");
            LOGGER.error(errMsg);
            throw new UtilsException("setWindShiftItems: ", err);
        }
    }

    /**
     * !!!!!!!!!!!!!!!!!!!!! NOT IN USE - NEED TO FINALIZE
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setTowerSurfVisItems(Visibility token) throws UtilsException {
        try {
            if (token.getVisibilityTowSur()
                    .equals(Configs.getInstance().getString("EXTENDED_TOWER_VISIBILITY"))) {
                decodedRemarksString.append(Configs.getInstance().getString("EXTENDED_DECODED_CTL_TWR_VIS"))
                        .append(" ").append(token.getVisibilityTowSurMiles());
            } else {
                decodedRemarksString.append(Configs.getInstance().getString("EXTENDED_DECODED_SFC_VIS"))
                        .append(" ").append(token.getVisibilityTowSurMiles());
            }
            decodedRemarksString.append(" ")
                    .append(Configs.getInstance().getString("MSRMNT_DECODED_MILES")).append("\n");

            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_REMARKS_STRING")
                    + " " + decodedRemarksString);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setTowerSurfVisItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedRemarksString.append(errMsg).append("\n");
            LOGGER.error(errMsg);
            throw new UtilsException("setTowerSurfVisItems: ", err);
        }
    }

    /**
     * Set the variable prevailing visibility information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setVarPrevVisSecVisVisSecLocItems(Visibility token) throws UtilsException {
        try {
            if (token.isVisVarPrevailVariable()) {
                decodedRemarksString.append(Configs.getInstance().getString("EXTENDED_DECODED_VIS_VRES_BETWN"))
                        .append(" ").append(token.getVisVarPrevailOne())
                        .append(" ").append(Configs.getInstance().getString("MSRMNT_DECODED_MILES"))
                        .append(" ").append(Configs.getInstance().getString("LOC_TIME_DECODED_AND"))
                        .append(" ").append(token.getVisVarPrevailTwo())
                        .append(Configs.getInstance().getString("MSRMNT_DECODED_MILES"));
            } else if (token.isVisVarPrevailRunway()) {
                decodedRemarksString.append(Configs.getInstance().getString("EXTENDED_DECODED_VIS_OF"))
                        .append(" ").append(token.getVisVarPrevailOne())
                        .append(" ").append(Configs.getInstance().getString("EXTENDED_DECODE_MILES_AT_RNWY"))
                        .append(" ").append(token.getVisVarPrevailRunway());
            } else if (token.getVisVarPrevailDir() != null) {
                decodedRemarksString.append(Configs.getInstance().getString("EXTENDED_DECODE_VIS_OF"))
                        .append(" ").append(token.getVisVarPrevailOne())
                        .append(" ").append(Configs.getInstance().getString("MSRMNT_DECODED_MILES"))
                        .append(" ").append(Configs.getInstance().getString("LOC_TIME_DECODED_IN"))
                        .append(" ").append(Configs.getInstance().getString("LOC_TIME_DECODED_THE"))
                        .append(" ").append(token.getVisVarPrevailDir())
                        .append(" ").append(Configs.getInstance().getString("LOC_TIME_DECODED_OCTANT"));
            }
            decodedRemarksString.append("\n");

            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_REMARKS_STRING")
                    + " " + decodedRemarksString);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setVarPrevVisSecVisVisSecLocItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedRemarksString.append(errMsg).append("\n");
            LOGGER.error(errMsg);
            throw new UtilsException("setVarPrevVisSecVisVisSecLocItems: ", err);
        }
    }

    /**
     * Set the lightning information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setLightningItems(Matcher token) throws UtilsException {
        try {
            LOGGER.debug("freq: #" + token.group("freq") + "#");
            LOGGER.debug("typeic: #" + token.group("typeic") + "#");
            LOGGER.debug("typecc: #" + token.group("typecc") + "#");
            LOGGER.debug("typecg: #" + token.group("typecg") + "#");
            LOGGER.debug("typeca: #" + token.group("typeca") + "#");
            LOGGER.debug("typecw: #" + token.group("typecw") + "#");
            LOGGER.debug("loc: #" + token.group("loc") + "#");
            LOGGER.debug("dir: #" + token.group("dir") + "#");
            LOGGER.debug("dir2: #" + token.group("dir2") + "#");

            decodedRemarksString.append(Configs.getInstance().getString("WEATHER_DECODED_LIGHTNING"))
                    .append(" ");

            if (token.group("freq") != null) {
                decodedRemarksString.append(" ").append(aviaRemarkWthAltItemsHandlers
                        .getValueAtIndex(aviaRemarkWthAltItemsHandlers
                                .getIndexOf(token.group("freq"))).getValue0()).append(" ")
                        .append(aviaRemarkWthAltItemsHandlers
                                .getValueAtIndex(aviaRemarkWthAltItemsHandlers
                                        .getIndexOf(token.group("freq"))).getValue1());
            }

            if (token.group("typeic") != null) {
                decodedRemarksString.append(" ").append(aviaRemarkWthAltItemsHandlers
                        .getValueAtIndex(aviaRemarkWthAltItemsHandlers
                                .getIndexOf(token.group("typeic"))).getValue0()).append(" ")
                        .append(aviaRemarkWthAltItemsHandlers
                                .getValueAtIndex(aviaRemarkWthAltItemsHandlers
                                        .getIndexOf(token.group("typeic"))).getValue1());
            }
            if (token.group("typecc") != null) {
                decodedRemarksString.append(" ").append(aviaRemarkWthAltItemsHandlers
                        .getValueAtIndex(aviaRemarkWthAltItemsHandlers
                                .getIndexOf(token.group("typecc"))).getValue0()).append(" ")
                        .append(aviaRemarkWthAltItemsHandlers
                                .getValueAtIndex(aviaRemarkWthAltItemsHandlers
                                        .getIndexOf(token.group("typecc"))).getValue1());
            }
            if (token.group("typecg") != null) {
                decodedRemarksString.append(" ").append(aviaRemarkWthAltItemsHandlers
                        .getValueAtIndex(aviaRemarkWthAltItemsHandlers
                                .getIndexOf(token.group("typecg"))).getValue0()).append(" ")
                        .append(aviaRemarkWthAltItemsHandlers
                                .getValueAtIndex(aviaRemarkWthAltItemsHandlers
                                        .getIndexOf(token.group("typecg"))).getValue1());
            }
            if (token.group("typeca") != null) {
                decodedRemarksString.append(" ").append(aviaRemarkWthAltItemsHandlers
                        .getValueAtIndex(aviaRemarkWthAltItemsHandlers
                                .getIndexOf(token.group("typeca"))).getValue0()).append(" ")
                        .append(aviaRemarkWthAltItemsHandlers
                                .getValueAtIndex(aviaRemarkWthAltItemsHandlers
                                        .getIndexOf(token.group("typeca"))).getValue1());
            }
            if (token.group("typecw") != null) {
                decodedRemarksString.append(" ").append(aviaRemarkWthAltItemsHandlers
                        .getValueAtIndex(aviaRemarkWthAltItemsHandlers
                                .getIndexOf(token.group("typecw"))).getValue0()).append(" ")
                        .append(aviaRemarkWthAltItemsHandlers
                                .getValueAtIndex(aviaRemarkWthAltItemsHandlers
                                        .getIndexOf(token.group("typecw"))).getValue1());
            }

            if (token.group("loc") != null) {
                decodedRemarksString.append(" ").append(aviaRemarkWthItemsHandlers
                        .getValueAtIndex(aviaRemarkWthItemsHandlers
                                .getIndexOf(token.group("loc"))));
            }

            if (token.group("dir") != null) {
                decodedRemarksString.append(" ")
                        .append(Configs.getInstance().getString("LOC_TIME_DECODED_LOCTED"))
                        .append(" ").append(token.group("dir"));
            }
            if (token.group("dir2") != null) {
                decodedRemarksString.append(" ")
                        .append(Configs.getInstance().getString("LOC_TIME_DECODED_TO"))
                        .append(" ").append(token.group("dir2"));
            }
            decodedRemarksString.append("\n");

            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_REMARKS_STRING")
                    + " " + decodedRemarksString);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setLightningItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedRemarksString.append(errMsg).append("\n");
            LOGGER.error(errMsg);
            throw new UtilsException("setLightningItems: ", err);
        }
    }

    /**
     * Set the thunder cloud location information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setThunderCloudLocationItems(Matcher token) throws UtilsException {
        try {
            LOGGER.debug("type: #" + token.group("type") + "#");
            LOGGER.debug("loc: #" + token.group("loc") + "#");
            LOGGER.debug("dir: #" + token.group("dir") + "#");
            LOGGER.debug("dir2: #" + token.group("dir2") + "#");
            LOGGER.debug("dirm: #" + token.group("dirm") + "#");

            decodedRemarksString.append(Configs.getInstance()
                    .getString("WEATHER_DECODED_THUNDERSTORM_CLOUD_LOC")).append(" ");

            if (token.group("type") != null) {
                decodedRemarksString.append(" ").append(aviaRemarkWthItemsHandlers
                        .getValueAtIndex(aviaRemarkWthItemsHandlers
                                .getIndexOf(token.group("type"))));
            }

            if (token.group("loc") != null && !token.group("loc").equals("")) {
                decodedRemarksString.append(" ").append(aviaRemarkWthItemsHandlers
                        .getValueAtIndex(aviaRemarkWthItemsHandlers
                                .getIndexOf(token.group("loc"))));
            }

            if (token.group("dir") != null) {
                decodedRemarksString
                        .append(" ").append(Configs.getInstance().getString("LOC_TIME_DECODED_LOCTED"))
                        .append(" ").append(token.group("dir"));
            }
            if (token.group("dir2") != null) {
                decodedRemarksString
                        .append(" ").append(Configs.getInstance().getString("LOC_TIME_DECODED_TO"))
                        .append(" ").append(token.group("dir2"));
            }
            if (token.group("dirm") != null) {
                decodedRemarksString
                        .append(" ").append(Configs.getInstance().getString("LOC_TIME_DECODED_MOVNG"))
                        .append(" ").append(Configs.getInstance().getString("LOC_TIME_DECODED_TO"))
                        .append(" ").append(Configs.getInstance().getString("LOC_TIME_DECODED_THE"))
                        .append(" ").append(token.group("dirm"));
            }
            decodedRemarksString.append("\n");

            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_REMARKS_STRING")
                    + " " + decodedRemarksString);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setThunderCloudLocationItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedRemarksString.append(errMsg).append("\n");
            LOGGER.error(errMsg);
            throw new UtilsException("setThunderCloudLocationItems: ", err);
        }
    }

    /**
     * Set the six hour maximum temperature information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setSixHourMaximumTemperature(Temperature token) throws UtilsException {
        try {
            if (token.getSixHourlyMaximumTemperatureInCelsius() != null) {
                decodedRemarksString
                        .append(Configs.getInstance().getString("EXTENDED_DECODED_6_HOUR_MAX_TEMP"))
                        .append(" ").append(token.getSixHourlyMaximumTemperatureInCelsius())
                        .append(" ").append(Configs.getInstance().getString("TEMP_DECODED_CELSIUS"))
                        .append(", ")
                        .append(token.getSixHourlyMaximumTemperatureInFahrenheit())
                        .append(" ").append(Configs.getInstance().getString("TEMP_DECODED_FAHRENHEIT"))
                        .append("\n");
            }

            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_REMARKS_STRING")
                    + " " + decodedRemarksString);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setSixHourMaximumTemperature: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedRemarksString.append(errMsg).append("\n");
            LOGGER.error(errMsg);
            throw new UtilsException("setSixHourMaximumTemperature: ", err);
        }
    }

    /**
     * Set the six hour minimum temperature information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setSixHourMinimumTemperature(Temperature token) throws UtilsException {
        try {
            if (token.getSixHourlyMinimumTemperatureInCelsius() != null) {
                decodedRemarksString
                        .append(Configs.getInstance().getString("EXTENDED_DECODED_6_HOUR_MIN_TEMP"))
                        .append(" ").append(token.getSixHourlyMinimumTemperatureInCelsius())
                        .append(" ").append(Configs.getInstance().getString("TEMP_DECODED_CELSIUS"))
                        .append(", ")
                        .append(token.getSixHourlyMinimumTemperatureInFahrenheit())
                        .append(" ").append(Configs.getInstance().getString("TEMP_DECODED_FAHRENHEIT"))
                        .append("\n");
            }

            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_REMARKS_STRING")
                    + " " + decodedRemarksString);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setSixHourMinimumTemperature: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedRemarksString.append(errMsg).append("\n");
            LOGGER.error(errMsg);
            throw new UtilsException("setSixHourMinimumTemperature: ", err);
        }
    }

    /**
     * Set the hourly temperature and dew point information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setHourlyTemperatureDewPoint(Temperature token) throws UtilsException {
        try {
            decodedRemarksString
                    .append(Configs.getInstance().getString("EXTENDED_DECODED_HOURLY_TEMP"))
                    .append(" ").append(token.getHourlyTemperatureInCelsius())
                    .append(" ").append(Configs.getInstance().getString("TEMP_DECODED_CELSIUS"))
                    .append(", ")
                    .append(token.getHourlyTemperatureInFahrenheit())
                    .append(" ").append(Configs.getInstance().getString("TEMP_DECODED_FAHRENHEIT"))
                    .append("\n");

            decodedRemarksString
                    .append(Configs.getInstance().getString("EXTENDED_DECODED_HOURLY_DEWPT"))
                    .append(" ").append(token.getHourlyDewPointInCelsius())
                    .append(" ").append(Configs.getInstance().getString("TEMP_DECODED_CELSIUS"))
                    .append(", ")
                    .append(token.getHourlyDewPointInFahrenheit())
                    .append(" ").append(Configs.getInstance().getString("TEMP_DECODED_FAHRENHEIT"))
                    .append("\n");

            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_REMARKS_STRING")
                    + " " + decodedRemarksString);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setHourlyTemperatureDewPoint: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedRemarksString.append(errMsg).append("\n");
            LOGGER.error(errMsg);
            throw new UtilsException("setHourlyTemperatureDewPoint: ", err);
        }
    }

    /**
     * Set the twenty four hour maximum and minimum temperature information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setTwentyFourHourMaxMinTemperature(Temperature token) throws UtilsException {
        try {
            decodedRemarksString.append(Configs.getInstance().getString("EXTENDED_DECODED_24_HOUR_MAX_TEMP"))
                    .append(" ").append(token.getTwentyFourHourMaximumTemperatureInCelsius())
                    .append(" ").append(Configs.getInstance().getString("TEMP_DECODED_CELSIUS"))
                    .append(", ")
                    .append(token.getTwentyFourHourMaximumTemperatureInFahrenheit())
                    .append(" ").append(Configs.getInstance().getString("TEMP_DECODED_FAHRENHEIT"))
                    .append("\n");

            decodedRemarksString.append(Configs.getInstance().getString("EXTENDED_DECODED_24_HOUR_MIN_TEMP"))
                    .append(" ").append(token.getTwentyFourHourMinimumTemperatureInCelsius())
                    .append(" ").append(Configs.getInstance().getString("TEMP_DECODED_CELSIUS"))
                    .append(", ")
                    .append(token.getTwentyFourHourMinimumTemperatureInFahrenheit())
                    .append(" ").append(Configs.getInstance().getString("TEMP_DECODED_FAHRENHEIT"))
                    .append("\n");

            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_REMARKS_STRING")
                    + " " + decodedRemarksString);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setTwentyFourHourMaxMinTemperature: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedRemarksString.append(errMsg).append("\n");
            LOGGER.error(errMsg);
            throw new UtilsException("setTwentyFourHourMaxMinTemperature: ", err);
        }
    }

    /**
     * Set the hourly precipitation information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setHourlyPrecipitationItems(Matcher token) throws UtilsException {
        try {
            LOGGER.debug("type: #" + token.group("type") + "#");
            LOGGER.debug("precip: #" + token.group("precip") + "#");

            decodedRemarksString
                    .append(Configs.getInstance().getString("EXTENDED_DECODED_HOURLY_PRECIPITATION"))
                    .append(" ");
            if (String.valueOf(Integer.parseInt(token.group("precip"))).equals(Configs.getInstance().getString("MISC_VALUE_0"))) {
                decodedRemarksString.append(Configs.getInstance()
                        .getString("EXTENDED_DECODED_LESS_HOURLY_PRECIPITATION_AMOUNT"));
            } else {
                decodedRemarksString.append(Integer.parseInt(token.group("precip")));
            }
            decodedRemarksString.append(Configs.getInstance()
                    .getString("EXTENDED_DECODED_HOURLY_PRECIPITATION_AMOUNT")).append("\n");

            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_REMARKS_STRING")
                    + " " + decodedRemarksString);
        } catch (NumberFormatException err) {
            String errMsg = "setHourlyPrecipitationItems: "
                    + Configs.getInstance().getString("LOC_TIME_DECODED_UNABLE_PARSE_VALUE")
                    + " " + err;
            decodedRemarksString.append(errMsg).append("\n");
            LOGGER.error(errMsg);
            throw new UtilsException("setHourlyPrecipitationItems: ", err);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setHourlyPrecipitationItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedRemarksString.append(errMsg).append("\n");
            LOGGER.error(errMsg);
            throw new UtilsException("setHourlyPrecipitationItems: ", err);
        }
    }

    /**
     * Set the six and twenty four hour precipitation information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setSixTwentyFourHourPrecipitationItems(Matcher token) throws UtilsException {
        try {
            LOGGER.debug("type: #" + token.group("type") + "#");
            LOGGER.debug("precip: #" + token.group("precip") + "#");
            decodedRemarksString.append(aviaRemarkWthItemsHandlers
                    .getValueAtIndex(aviaRemarkWthItemsHandlers
                            .getIndexOf(token.group("type")
                                    + Configs.getInstance().getString("MISC_VALUE_HP")))).append(" ");

            if (UtilsMisc.containsOnlyNumbers(token.group("precip"))) {
                decodedRemarksString.append(Double.parseDouble(token.group("precip")) / 100.0)
                        .append(" ").append(Configs.getInstance()
                        .getString("EXTENDED_DECODED_6_24_HOUR_PRECIPITATION_AMOUNT"));
            } else {
                decodedRemarksString.append(Configs.getInstance()
                        .getString("EXTENDED_DECODED_IND_6_24_HOUR_PRECIPITATION_AMOUNT"));
            }
            decodedRemarksString.append("\n");
            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_REMARKS_STRING")
                    + " " + decodedRemarksString);
        } catch (NumberFormatException err) {
            String errMsg = "setSixTwentyFourHourPrecipitationItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedRemarksString.append(errMsg).append("\n");
            LOGGER.error(errMsg);
            throw new UtilsException("setSixTwentyFourHourPrecipitationItems: ", err);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setSixTwentyFourHourPrecipitationItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedRemarksString.append(errMsg).append("\n");
            LOGGER.error(errMsg);
            throw new UtilsException("setSixTwentyFourHourPrecipitationItems: ", err);
        }
    }

    /**
     * Set the three hour pressure tendency information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setThreeHourPressureTendencyItems(Pressure token) throws UtilsException {
        try {
            decodedRemarksString.append(Configs.getInstance()
                    .getString("EXTENDED_DECODED_PRESSURE_TENDENCY"))
                    .append(" ");

            decodedRemarksString.append(aviaRemarkWthItemsHandlers
                    .getValueAtIndex(aviaRemarkWthItemsHandlers
                            .getIndexOf(String.valueOf(token.getTendencyCode())
                                    + Configs.getInstance().getString("MISC_VALUE_PT"))))
                    .append(" ");

            decodedRemarksString.append("\n")
                    .append(Configs.getInstance().getString("EXTENDED_DECODED_PRESSURE_TENDENCY_CHANGE"))
                    .append(" ").append(token.getTendencyPressure())
                    .append(" ").append(Configs.getInstance().getString("MSRMNT_DECODED_HG"))
                    .append(", ")
                    .append(token.getTendencyPressureInHectoPascals())
                    .append(" ").append(Configs.getInstance().getString("MSRMNT_DECODED_HPA"))
                    .append("\n");

            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_REMARKS_STRING")
                    + " " + decodedRemarksString);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setThreeHourPressureTendencyItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedRemarksString.append(errMsg).append("\n");
            LOGGER.error(errMsg);
            throw new UtilsException("setThreeHourPressureTendencyItems: ", err);
        }
    }

    /**
     * Set the density altitude information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setDensityAltitudeItems(Matcher token) throws UtilsException {
        try {
            LOGGER.debug("type: #" + token.group("type") + "#");
            LOGGER.debug("denalt: #" + token.group("denalt") + "#");
            LOGGER.debug("units: #" + token.group("units") + "#");

            decodedRemarksString.append(Configs.getInstance().getString("EXTENDED_DECODED_DENSITY_ALTITUDE"))
                    .append(" ").append(Integer.parseInt(token.group("denalt")))
                    .append(" ").append(token.group("units")).append("\n");

            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_REMARKS_STRING")
                    + " " + decodedRemarksString);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setDensityAltitudeItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedRemarksString.append(errMsg).append("\n");
            LOGGER.error(errMsg);
            throw new UtilsException("setDensityAltitudeItems: ", err);
        }
    }

    /**
     * Set the cloud okta information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setCloudOktaItems(Matcher token) throws UtilsException {
        String windDirection = null;
        try {
            LOGGER.debug("cloud: #" + token.group("cloud") + "#");
            LOGGER.debug("okta: #" + token.group("okta") + "#");
            LOGGER.debug("verb: #" + token.group("verb") + "#");
            LOGGER.debug("dirm: #" + token.group("dirm") + "#");

            decodedRemarksString
                    .append(Configs.getInstance().getString("CLOUD_DECODED_CLOUD_AND_COVER"))
                    .append(" ");

            if (token.group("okta").trim().equals(Configs.getInstance().getString("WEATHER_TRACE"))) {
                decodedRemarksString
                        .append(Configs.getInstance().getString("WEATHER_DECODED_TRACE"))
                        .append(" ");
            } else if (token.group("okta").trim().equals(Configs.getInstance().getString("LOC_TIME_DISTANT_I"))) {
                decodedRemarksString
                        .append(Configs.getInstance().getString("LOC_TIME_DECODED_DISTANT"))
                        .append(" ");
            } else {
                decodedRemarksString.append(aviaRemarkWthItemsHandlers
                        .getValueAtIndex(aviaRemarkWthItemsHandlers
                                .getIndexOf(token.group("okta")
                                        + Configs.getInstance().getString("MISC_VALUE_CO"))))
                        .append(" ");
            }
            //else {
            //    windDirection = token.group("okta");
            //}

            decodedRemarksString.append(aviaRemarkWthItemsHandlers
                    .getValueAtIndex(aviaRemarkWthItemsHandlers
                            .getIndexOf(token.group("cloud")))).append(" ");

            if (windDirection != null) {
                decodedRemarksString.append(" ").append(windDirection);
            }

            if (token.group("verb") != null) {
                decodedRemarksString.append(" ").append(aviaRemarkWthItemsHandlers
                        .getValueAtIndex(aviaRemarkWthItemsHandlers
                                .getIndexOf(token.group("verb"))));
            }

            if (token.group("dirm") != null) {
                decodedRemarksString.append(" ").append(token.group("dirm"));
            }

            decodedRemarksString.append("\n");

            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_REMARKS_STRING")
                    + " " + decodedRemarksString);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setCloudOktaItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedRemarksString.append(errMsg).append("\n");
            LOGGER.error(errMsg);
            throw new UtilsException("setCloudOktaItems: ", err);
        }
    }

    /**
     * Set the last observation information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setLastObsItems(Matcher token) throws UtilsException {
        try {
            LOGGER.debug("last: #" + token.group("last") + "#");

            decodedRemarksString.append(Configs.getInstance()
                    .getString("EXTENDED_DECODED_LAST_OBSERVATION")).append("\n");

            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_REMARKS_STRING")
                    + " " + decodedRemarksString);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setLastObsItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedRemarksString.append(errMsg).append("\n");
            LOGGER.error(errMsg);
            throw new UtilsException("setLastObsItems: ", err);
        }
    }

    /**
     * Set the pressure Q code information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setQFEQNHQNEPressure(Pressure token) throws UtilsException {
        try {
            decodedRemarksString
                    .append(Configs.getInstance().getString("EXTENDED_DECODED_PRESSURE_Q"))
                    .append(" ").append(token.getqPressureType())
                    .append("  ").append(token.getqPressureMM())
                    .append(" ").append(Configs.getInstance().getString("MSRMNT_DECODED_MM"))
                    .append(" ").append(token.getqPressureMB())
                    .append(" ").append(Configs.getInstance().getString("MSRMNT_DECODED_MB"))
                    .append("\n");

            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_REMARKS_STRING")
                    + " " + decodedRemarksString);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setQFEQNHQNEPressure: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedRemarksString.append(errMsg).append("\n");
            LOGGER.error(errMsg);
            throw new UtilsException("setQFEQNHQNEPressure: ", err);
        }
    }

    /**
     * Set the automated maintenance information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setAutomatedMaintenanceItems(Matcher token) throws UtilsException {
        try {
            LOGGER.debug("typeam: #" + token.group("typeam") + "#");
            LOGGER.debug("loc: #" + token.group("loc") + "#");
            LOGGER.debug("typemc: #" + token.group("typemc") + "#");

            decodedRemarksString
                    .append(Configs.getInstance()
                            .getString("EXTENDED_DECODED_AUTOMATED_MAINTENANCE"))
                    .append(" ");
            //PNO FZRANO RVRNO PWINO TSNO VISNO CHINO $
            if (token.group("typeam") != null) {
                decodedRemarksString.append(aviaRemarkWthItemsHandlers
                        .getValueAtIndex(aviaRemarkWthItemsHandlers
                                .getIndexOf(token.group("typeam"))));
            }

            if (token.group("typemc") != null) { // && token.group("typemc").equals(Configs.getInstance()
                decodedRemarksString.append(aviaRemarkWthItemsHandlers
                        .getValueAtIndex(aviaRemarkWthItemsHandlers
                                .getIndexOf(token.group("typemc"))));
            }

            if (token.group("loc") != null) {
                decodedRemarksString.append(" ").append(token.group("loc"));
            }
            decodedRemarksString.append("\n");

            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_REMARKS_STRING")
                    + " " + decodedRemarksString);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setAutomatedMaintenanceItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedRemarksString.append(errMsg).append("\n");
            LOGGER.error(errMsg);
            throw new UtilsException("setAutomatedMaintenanceItems: ", err);
        }
    }

    /**
     * Set the snow on ground information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setSnowOnGround(Matcher token) throws UtilsException {
        try {
            LOGGER.debug("type: #" + token.group("type") + "#");
            LOGGER.debug("amt: #" + token.group("amt") + "#");

            decodedRemarksString
                    .append(Configs.getInstance()
                            .getString("EXTENDED_DECODED_SNOW_ON_GROUND"))
                    .append(" ").append(token.group("amt"))
                    .append(" ").append(Configs.getInstance()
                    .getString("MSRMNT_DECODED_CM"))
                    .append(", ").append(UtilsMisc.roundValue(Double.parseDouble(token.group("amt")) / 2.54, 2))
                    .append(" ").append(Configs.getInstance()
                    .getString("MSRMNT_DECODED_INCHES"))
                    .append("\n");
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setSnowOnGround: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedRemarksString.append(errMsg).append("\n");
            LOGGER.error(errMsg);
            throw new UtilsException("setSnowOnGround: ", err);
        }
    }

    /**
     * Get the decoded remarks information
     *
     * @return decodedRemarksString
     */
    public StringBuffer getDecodedRemarksString() {
        return decodedRemarksString;
    }
}
