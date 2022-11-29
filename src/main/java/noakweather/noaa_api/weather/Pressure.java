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
package noakweather.noaa_api.weather;

import java.util.MissingResourceException;
import java.util.regex.Matcher;
import noakweather.utils.Configs;
import noakweather.utils.UtilsException;
import noakweather.utils.UtilsMisc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class representing the pressure section of the report
 *
 * Author: quark95cos Since: Copyright(c) 2022
 */
public class Pressure {

    private static final int ONE_HUND = 100;
    private static final Double POINT_TWO_NINE = .02953;
    private static final Double MINUS_ONE = -1.0;
    private static final Double THIRTY_THREE_EIGHT = 33.8639;
    private static final String SLASH_VALUE = "/";
    private int tendencyCode;
    private int qPressureMM;
    private int qPressureMB;
    private Double pressure;
    private Double seaLevelPressure;
    private Double tendencyPressure;
    private String qPressureType;

    private static final Logger LOGGER
            = LogManager.getLogger(Pressure.class.getName());

    /**
     * Constructor
     */
    public Pressure() {
        this.tendencyCode = -1;
        this.qPressureMM = -1;
        this.qPressureMB = -1;
        this.pressure = null;
        this.seaLevelPressure = null;
        this.tendencyPressure = -999.0;
        this.qPressureType = null;
    }

    /**
     * Set the pressure information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setPressureItems(Matcher token) throws UtilsException {
        try {
            LOGGER.debug("unit: #" + token.group("unit") + "#");
            LOGGER.debug("press: #" + token.group("press") + "#");
            LOGGER.debug("unit2: #" + token.group("unit2") + "#");

            // There may be cases of alphanumeric values in the pressure. If so,
            // just return
            if (!UtilsMisc.containsOnlyNumbers(token.group("press"))) {
                LOGGER.debug(Configs.getInstance().getString("PRESS_DECODED_CONTNS_ALPHA_VALUES"));
                return;
            }

            // Get the initial pressure
            this.pressure = Double.parseDouble(token.group("press"));

            // Get pressure, which is reported in hundreths
            //
            // format: AP P P P
            //           h h h h
            //     A - altimeter in inches of mercury
            //     P P P P - tens, units, tenths and hundreths inches mercury
            //      h h h h (no decimal point coded)
            if (token.group("unit") != null
                    && (token.group("unit").equals(Configs.getInstance().getString("WEATHER_ALTIMETER_A"))
                    || token.group("unit").equals(Configs.getInstance().getString("WEATHER_ALTIMETER_AA")))) {
                // Correct for no decimal point
                this.pressure = pressure / ONE_HUND;
                LOGGER.debug(Configs.getInstance().getString("PRESS_DECODED_PRESSURE")
                        + " " + this.pressure
                        + " " + Configs.getInstance().getString("MSRMNT_DECODED_HG"));
            } // Alternative pressure (HPa/mB) (HectoPascal/Millbar)
            // QPPPP - QNH
            // Format: "QPPPP" -> Q - indicator for QNH, PPPP - Pressure value.
            // Measured in hecto Pascal (HPa), 1 Hpa = 1 mB(millibar)
            // Also QNH ending in INS - in inches of mercury (QNHPPPPINS)
            else if (token.group("unit") != null && token.group("unit")
                    .equals(Configs.getInstance().getString("WEATHER_ALTIMETER_Q"))) {
                LOGGER.debug(Configs.getInstance().getString("PRESS_DECODED_PRESSURE")
                        + " " + this.pressure
                        + " " + Configs.getInstance().getString("MSRMNT_DECODED_HPA"));
                // Convert to Hg
                this.pressure *= POINT_TWO_NINE;
                LOGGER.debug(Configs.getInstance().getString("PRESS_DECODED_PRESSURE")
                        + " " + this.pressure
                        + " " + Configs.getInstance().getString("MSRMNT_DECODED_HG"));
            } else if (token.group("unit") != null && token.group("unit")
                    .equals(Configs.getInstance().getString("WEATHER_ALTIMETER_QNH"))) {
                // Correct for no decimal point
                this.pressure = pressure / ONE_HUND;
                LOGGER.debug(Configs.getInstance().getString("PRESS_DECODED_PRESSURE")
                        + " " + this.pressure
                        + " " + Configs.getInstance().getString("MSRMNT_DECODED_HG"));
            } else {
                LOGGER.debug(Configs.getInstance().getString("PRESS_DECODED_UNKNOWN"));
            }
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setPressureItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            LOGGER.error(errMsg);
            throw new UtilsException("setPressureItems: ", err);
        }
    }

    /**
     * Set the sea level pressure information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setSLPressureItems(Matcher token) throws UtilsException {
        try {
            LOGGER.debug("press: #" + token.group("press") + "#");

            // Get pressure, which is reported in hundreths
            if (token.group("press").equals(Configs.getInstance()
                    .getString("EXTENDED_NO_SEA_LEVEL_PRESSURE"))) {
                this.seaLevelPressure = MINUS_ONE;
                LOGGER.debug(Configs.getInstance().getString("PRESS_DECODED_PRESSURE")
                        + " " + this.seaLevelPressure
                        + " " + Configs.getInstance().getString("MSRMNT_DECODED_HPA"));
            } else { //if (token.group("press").equals(ExtendedConstInterface.EXTENDED_SEA_LEVEL_PRESSURE)) {
                LOGGER.debug("token: " + token.group("press").substring(0, 1));
                if (token.group("press").substring(0, 1)
                        .equals(Configs.getInstance().getString("MISC_VALUE_9"))) {
                    this.seaLevelPressure = Double.parseDouble(Configs.getInstance()
                            .getString("MISC_VALUE_9")
                            + token.group("press").substring(0, 2) + "."
                            + token.group("press").substring(2, 3));
                } else {
                    this.seaLevelPressure = Double.parseDouble(Configs.getInstance()
                            .getString("MISC_VALUE_10")
                            + token.group("press").substring(0, 2) + "."
                            + token.group("press").substring(2, 3));
                }
                LOGGER.debug(Configs.getInstance().getString("PRESS_DECODED_PRESSURE")
                        + " " + this.seaLevelPressure
                        + " " + Configs.getInstance().getString("MSRMNT_DECODED_HPA"));

                // Convert to Hg
                this.seaLevelPressure *= POINT_TWO_NINE;
                LOGGER.debug(Configs.getInstance().getString("PRESS_DECODED_PRESSURE")
                        + " " + this.seaLevelPressure
                        + " " + Configs.getInstance().getString("MSRMNT_DECODED_HG"));
            }
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setSLPressureItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            LOGGER.error(errMsg);
            throw new UtilsException("setSLPressureItems: ", err);
        }
    }

    /**
     * Set the pressure tendency information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setTendencyPressureItems(Matcher token) throws UtilsException {
        try {
            LOGGER.debug("type: #" + token.group("type") + "#");
            LOGGER.debug("tend: #" + token.group("tend") + "#");
            LOGGER.debug("press: #" + token.group("press") + "#");

            // Get pressure tendency code
            this.tendencyCode = Integer.parseInt(token.group("tend"));
            LOGGER.debug(Configs.getInstance().getString("PRESS_DECODED_TENDENCY_CODE")
                    + " " + this.tendencyCode);

            // Get pressure, which is reported in hundreths
            if (!token.group("press").contains(SLASH_VALUE)) {
                this.tendencyPressure = Double.parseDouble(token.group("press").substring(0, 2)
                        + "." + token.group("press").substring(2, 3));
            }
            LOGGER.debug(Configs.getInstance().getString("PRESS_DECODED_PRESSURE")
                    + " " + this.tendencyPressure
                    + " " + Configs.getInstance().getString("MSRMNT_DECODED_HPA"));

            // Convert to Hg
            if (!token.group("press").contains(SLASH_VALUE)) {
                this.tendencyPressure *= POINT_TWO_NINE;
            }
            LOGGER.debug(Configs.getInstance().getString("PRESS_DECODED_PRESSURE")
                    + " " + this.tendencyPressure
                    + " " + Configs.getInstance().getString("MSRMNT_DECODED_HG"));
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setTendencyPressureItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            LOGGER.error(errMsg);
            throw new UtilsException("setTendencyPressureItems: ", err);
        }
    }

    /**
     * Set the pressure Q code information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setQFEQNHQNEPressureItems(Matcher token) throws UtilsException {
        try {
            LOGGER.debug("pressq: #" + token.group("pressq") + "#");
            LOGGER.debug("pressmm: #" + token.group("pressmm") + "#");
            LOGGER.debug("pressmb: #" + token.group("pressmb") + "#");

            if (token.group("pressq").equals(Configs.getInstance().getString("PRESS_QFE"))) {
                this.qPressureType = Configs.getInstance().getString("PRESS_DECODED_FIELD_ELEVATION");
            } else if (token.group("pressq").equals(Configs.getInstance().getString("PRESS_QNH"))) {
                this.qPressureType = Configs.getInstance().getString("PRESS_DECODED_NORMAL_HEIGHT");
            } else if (token.group("pressq").equals(Configs.getInstance().getString("PRESS_QNE"))) {
                this.qPressureType = Configs.getInstance().getString("PRESS_DECODED_NORMAL_ELEVATION");
            } else {

            }

            LOGGER.debug(Configs.getInstance().getString("PRESS_DECODED_PRESSURE_Q_TYPE")
                    + " " + this.qPressureType);

            this.qPressureMM = Integer.parseInt(token.group("pressmm"));
            this.qPressureMB = Integer.parseInt(token.group("pressmb"));

            LOGGER.debug(Configs.getInstance().getString("PRESS_DECODED_PRESSURE_Q")
                    + " " + Configs.getInstance().getString("LOC_TIME_DECODED_IN")
                    + " " + Configs.getInstance().getString("MSRMNT_DECODED_MM")
                    + ": " + this.qPressureMM);
            LOGGER.debug(Configs.getInstance().getString("PRESS_DECODED_PRESSURE_Q")
                    + " " + Configs.getInstance().getString("LOC_TIME_DECODED_IN")
                    + " " + Configs.getInstance().getString("MSRMNT_DECODED_MB")
                    + ": " + this.qPressureMB);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setQFEQNHQNEPressureItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            LOGGER.error(errMsg);
            throw new UtilsException("setQFEQNHQNEPressureItems: ", err);
        }
    }

    /**
     * Set the tendency code
     *
     * @param tendencyCode
     */
    public void setTendencyCode(int tendencyCode) {
        this.tendencyCode = tendencyCode;
    }

    /**
     * Get the tendency code
     *
     * @return tendencyCode
     */
    public int getTendencyCode() {
        return tendencyCode;
    }

    /**
     * Set the pressure
     *
     * @param value pressure in inches Hg
     */
    public void setPressure(Double value) {
        // Round so we're consistent
        if (value != null) {
            double val = value;
            val = UtilsMisc.roundValue(val, 2);
            this.pressure = val;
        } else {
            this.pressure = value;
        }
    }

    /**
     * Get the pressure
     *
     * @return pressure in inches Hg
     */
    public Double getPressure() {
        return UtilsMisc.roundValue(pressure, 2);
    }

    /**
     * Get the pressure in pascals
     *
     * @return pressure in hPa
     */
    public Double getPressureInHectoPascals() {
        if (pressure == null) {
            return null;
        }

        // Convert to hPa
        double val = pressure * THIRTY_THREE_EIGHT;

        // Round
        return UtilsMisc.roundValue(val, 2);
    }

    /**
     * Get the sea level pressure
     *
     * @return pressure in inches Hg
     */
    public Double getSLPressure() {
        return UtilsMisc.roundValue(seaLevelPressure, 5);
    }

    /**
     * Get the sea level pressure in pascals
     *
     * @return pressure in hPa
     */
    public Double getSLPressureInHectoPascals() {
        if (seaLevelPressure == null) {
            return null;
        }

        // Convert to hPa
        double val = seaLevelPressure * THIRTY_THREE_EIGHT;

        // Round
        return UtilsMisc.roundValue(val, 2);
    }

    /**
     * Get the pressure tendency
     *
     * @return pressure in inches Hg
     */
    public Double getTendencyPressure() {
        return UtilsMisc.roundValue(tendencyPressure, 5);
    }

    /**
     * Get the pressure tendency in pascals
     *
     * @return pressure in hPa
     */
    public Double getTendencyPressureInHectoPascals() {
        if (tendencyPressure == null) {
            return null;
        }

        // Convert to hPa
        double val = tendencyPressure * THIRTY_THREE_EIGHT;

        // Round
        return UtilsMisc.roundValue(val, 2);
    }

    /**
     * Get the sea level pressure
     *
     * @return seaLevelPressure in hPa
     */
    public Double getSeaLevelPressure() {
        return UtilsMisc.roundValue(seaLevelPressure, 5);
    }

    /**
     * Get the pressure in mm
     *
     * @return qPressureMM in mm
     */
    public int getqPressureMM() {
        return qPressureMM;
    }

    /**
     * Get the pressure in millibars
     *
     * @return qPressureMB in millibars
     */
    public int getqPressureMB() {
        return qPressureMB;
    }

    /**
     * Get the pressure type
     *
     * @return qPressure Type
     */
    public String getqPressureType() {
        return qPressureType;
    }
}
