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
 * Class representing the wind section of the report
 *
 * Author: quark95cos Since: Copyright(c) 2022
 */
public class Wind {

    private int windDirection;
    private int windDirectionMin;
    private int windDirectionMax;
    private int windDirectionVarOne;
    private int windDirectionVarTwo;
    private int peakWindDirection;
    private int peakWindHour;
    private int peakWindMin;
    private boolean windDirectionCalm;
    private boolean windDirectionIsVariable;
    private boolean windDirectionIsVarGtrSix;
    private Double windSpeed;
    private Double windGusts;
    private Double peakWindSpeed;
    private String windNotDetermined;
    private String windDirectionCompass;
    private String windDirectionVarOneCompass;
    private String windDirectionVarTwoCompass;
    private String peakWindDirectionCompass;

    private static final Logger LOGGER
            = LogManager.getLogger(Wind.class.getName());

    public Wind() {
        this.windDirection = 0;
        this.windDirectionMin = 0;
        this.windDirectionMax = 0;
        this.peakWindDirection = 0;
        this.windDirectionVarOne = 0;
        this.windDirectionVarTwo = 0;
        this.peakWindHour = 0;
        this.peakWindMin = 0;
        this.windDirectionCalm = false;
        this.windDirectionIsVariable = false;
        this.windSpeed = 0.0;
        this.windGusts = 0.0;
        this.peakWindSpeed = 0.0;
        this.windNotDetermined = null;
        this.windDirectionCompass = null;
        this.windDirectionVarOneCompass = null;
        this.windDirectionVarTwoCompass = null;
        this.peakWindDirectionCompass = null;
    }

    /**
     * Set the main wind information
     *
     * @param token
     * @param windType
     * @throws noakweather.utils.UtilsException
     */
    public void setMainWindItems(Matcher token, char windType)
            throws UtilsException {
        boolean windInKnots = false;

        LOGGER.debug("dir: #" + token.group("dir") + "#");
        LOGGER.debug("speed: #" + token.group("speed") + "#");
        LOGGER.debug("inden: #" + token.group("inden") + "#");
        LOGGER.debug("gust: #" + token.group("gust") + "#");
        LOGGER.debug("units: #" + token.group("units") + "#");
        LOGGER.debug("varfrom: #" + token.group("varfrom") + "#");
        LOGGER.debug("varto: #" + token.group("varto") + "#");

        // If token.group("varfrom") != null && token.group("varto") != null
        // then the wind is of form xxxVxxx. All other tokens are null
        if (token.group("varfrom") != null && token.group("varto") != null) {
            LOGGER.debug(Configs.getInstance().getString("WIND_DECODED_VARIABLE_DIRECTION_GTR_6_KNOTS"));
            if (windType == 'M') {
                this.windDirectionIsVarGtrSix = true;
                this.windDirectionVarOne = Integer.parseInt(token.group("varfrom"));
                this.windDirectionVarOneCompass = WindDir.getFormattedWindDir(this.windDirectionVarOne);
                this.windDirectionVarTwo = Integer.parseInt(token.group("varto"));
                this.windDirectionVarTwoCompass = WindDir.getFormattedWindDir(this.windDirectionVarTwo);
                LOGGER.debug(Configs.getInstance().getString("WIND_DECODED_DIRECTION_VAR_GTR_SIX")
                        + ": " + this.windDirectionIsVarGtrSix);
                LOGGER.debug(Configs.getInstance().getString("WIND_DECODED_DIRECTION_VAR_ONE")
                        + ": " + this.windDirectionVarOne);
                LOGGER.debug(Configs.getInstance().getString("WIND_DECODED_DIRECTION_VAR_ONE_COMPASS")
                        + ": " + this.windDirectionVarOneCompass);
                LOGGER.debug(Configs.getInstance().getString("WIND_DECODED_DIRECTION_VAR_TWO")
                        + ": " + this.windDirectionVarTwo);
                LOGGER.debug(Configs.getInstance().getString("WIND_DECODED_DIRECTION_VAR_TWO_COMPASS")
                        + ": " + this.windDirectionVarTwoCompass);
            }
        }

        // Check if the token.group("dir") has alphanumeric values other than
        // WindDir.WIND_VARIABLE (VRB)
        try {
            if (!token.group("dir").equals(Configs.getInstance().getString("WIND_VARIABLE"))) {
                if (!UtilsMisc.containsOnlyNumbers(
                        UtilsMisc.removeNonNumeric(token.group("dir")))) {
                    if (windType == 'M') {
                        this.windNotDetermined = Configs.getInstance().getString("WIND_NOT_DETERMINED");
                        LOGGER.debug(Configs.getInstance().getString("WIND_DECODED_WIND_SPEED")
                                + " " + Configs.getInstance().getString("WIND_DECODED_CONTAINS_ALPHA"));
                    }
                    return;
                }
            }
        } catch (NumberFormatException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setMainWindItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            LOGGER.error(errMsg);
            throw new UtilsException("setMainWindItems: ", err);
        }

        // Note: There have been cases where wind started with VRB
        // and did not end with KT. This seems to only happen in the
        // US, so assuming knots.
        if (token.group("units").equals(Configs.getInstance().getString("WIND_KNOTS_1"))
                || token.group("units").equals(Configs.getInstance().getString("WIND_KNOTS_2"))
                || (token.group("dir").equals(Configs.getInstance().getString("WIND_VARIABLE"))
                && !token.group("units").equals(Configs.getInstance().getString("WIND_MILES_PER_SEC")))) {
            if (windType == 'M') {
                windInKnots = true;
                LOGGER.debug(Configs.getInstance().getString("WIND_DECODED_WIND_SPEED")
                        + " " + Configs.getInstance().getString("WIND_DECODED_KNOTS"));
            }
        } else {
            LOGGER.debug(Configs.getInstance().getString("WIND_DECODED_WIND_SPEED")
                    + " " + Configs.getInstance().getString("WIND_DECODED_METERS_PER_SECOND"));
        }

        try {
            if (windType == 'M') {
                if (!token.group("dir").equals(Configs.getInstance().getString("WIND_VARIABLE"))) {
                    // We have gusts
                    this.windDirection = Integer.parseInt(token.group("dir"));
                    LOGGER.debug(Configs.getInstance().getString("WIND_DECODED_WIND_SPEED")
                            + " " + Configs.getInstance().getString("LOC_TIME_DECODED_DIRECTION")
                            + ": " + this.windDirection);
                    if (this.windDirection == 0) {
                        this.windDirectionCalm = true;
                        this.windDirectionCompass = Configs.getInstance()
                                .getString("WIND_DECODED_DIR_CALM");
                    } else {
                        this.windDirectionCompass = WindDir.getFormattedWindDir(this.windDirection);
                    }
                    LOGGER.debug(Configs.getInstance().getString("WIND_DECODED_WIND_SPEED")
                            + " " + Configs.getInstance().getString("LOC_TIME_DECODED_DIRECTION")
                            + " " + Configs.getInstance().getString("WIND_DECODED_DIR_CALM")
                            + ": " + this.windDirectionCalm);
                    LOGGER.debug(Configs.getInstance().getString("WIND_DECODED_WIND_SPEED")
                            + " " + Configs.getInstance().getString("LOC_TIME_DECODED_DIRECTION")
                            + " " + Configs.getInstance().getString("LOC_TIME_DECODED_COMPASS")
                            + ": " + this.windDirectionCompass);
                } else {
                    LOGGER.debug(Configs.getInstance()
                            .getString("WIND_DECODED_VARIABLE_DIRECTION_LSS_EQL_6_KNOTS"));
                    this.windDirectionIsVariable = true;
                }
            }
        } catch (NumberFormatException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setMainWindItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            LOGGER.error(errMsg);
            throw new UtilsException("setMainWindItems: ", err);
        }

        // Set wind speed
        if (windType == 'M') {
            if (windInKnots) {
                this.windSpeed = Double.parseDouble(token.group("speed"));
            } else {
                setWindSpeedInMPS(Double.parseDouble(token.group("speed")));
            }
            LOGGER.debug(Configs.getInstance().getString("WIND_DECODED_WIND_SPEED")
                    + ": " + this.windSpeed);
        }

        if (windType == 'M') {
            if (token.group("gust") != null) {
                // We have wind gusts
                LOGGER.debug(Configs.getInstance().getString("WIND_DECODED_WIND_GUST"));

                if (windInKnots) {
                    this.windGusts = Double.parseDouble(token.group("gust"));
                } else {
                    setWindGustsInMPS(Double.parseDouble(token.group("gust")));
                }
            } else {
                // We do not have gusts
                LOGGER.debug(Configs.getInstance().getString("WIND_DECODED_WIND_NO_GUST"));
            }
            LOGGER.debug(Configs.getInstance().getString("WIND_DECODED_WIND_GUST")
                    + ": " + this.windGusts);
        }
    }

    /**
     * Set the peak wind information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setPeakWindItems(Matcher token) throws UtilsException {
        LOGGER.debug("dir: #" + token.group("dir") + "#");
        LOGGER.debug("speed: #" + token.group("speed") + "#");
        LOGGER.debug("hour: #" + token.group("hour") + "#");
        LOGGER.debug("min: #" + token.group("min") + "#");

        try {
            this.peakWindDirection = Integer.parseInt(token.group("dir"));
            this.peakWindDirectionCompass = WindDir.getFormattedWindDir(this.peakWindDirection);
            this.peakWindSpeed = Double.parseDouble(token.group("speed"));

            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_PEAK_WIND_DIR")
                    + " " + this.peakWindDirection);
            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_PEAK_WIND")
                    + " " + Configs.getInstance().getString("LOC_TIME_DECODED_COMPASS")
                    + ": " + this.peakWindDirectionCompass);
            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_PEAK_WIND_SPEED")
                    + " " + this.peakWindSpeed);

            if (token.group("hour") != null) {
                this.peakWindHour = Integer.parseInt(token.group("hour"));
                LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_PEAK_WIND")
                        + " " + Configs.getInstance().getString("MSRMNT_DECODED_HOUR")
                        + ": " + this.peakWindHour);
            }

            this.peakWindMin = Integer.parseInt(token.group("min"));
            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_PEAK_WIND")
                    + " " + Configs.getInstance().getString("MSRMNT_DECODED_MINUTES")
                    + ": " + this.peakWindMin);
        } catch (NumberFormatException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setPeakWindItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            LOGGER.error(errMsg);
            throw new UtilsException("setPeakWindItems: ", err);
        }
    }

    /**
     * Get windDirectionVarOne
     *
     * @return wind direction
     */
    public Integer getWindDirectionVarOne() {
        return windDirectionVarOne;
    }

    /**
     * Set windDirectionVarOne
     *
     * @param windDirectionVarOne
     */
    public void setWindDirectionVarOne(Integer windDirectionVarOne) {
        this.windDirectionVarOne = windDirectionVarOne;
    }

    /**
     * Get windDirectionVarOneCompass
     *
     * @return wind direction compass
     */
    public String getwindDirectionVarOneCompass() {
        return windDirectionVarOneCompass;
    }

    /**
     * Set windDirectionVarOneCompass
     *
     * @param windDirectionVarOneCompass
     */
    public void setwindDirectionVarOneCompass(String windDirectionVarOneCompass) {
        this.windDirectionVarOneCompass = windDirectionVarOneCompass;
    }

    /**
     * Get windDirectionVarTwo
     *
     * @return wind direction
     */
    public Integer getWindDirectionVarTwo() {
        return windDirectionVarTwo;
    }

    /**
     * Set windDirectionVarTwo
     *
     * @param windDirectionVarTwo
     */
    public void setWindDirectionVarTwo(Integer windDirectionVarTwo) {
        this.windDirectionVarTwo = windDirectionVarTwo;
    }

    /**
     * Get windDirectionVarTwoCompass
     *
     * @return wind direction compass
     */
    public String getwindDirectionVarTwoCompass() {
        return windDirectionVarTwoCompass;
    }

    /**
     * Set windDirectionVarTwoCompass
     *
     * @param windDirectionVarTwoCompass
     */
    public void setwindDirectionVarTwoCompass(String windDirectionVarTwoCompass) {
        this.windDirectionVarTwoCompass = windDirectionVarTwoCompass;
    }

    /**
     * Get windDirectionCalm
     *
     * @return wind direction
     */
    public boolean getWindDirectionCalm() {
        return windDirectionCalm;
    }

    /**
     * Set windDirectionCalm
     *
     * @param windDirectionCalm
     */
    public void setWindDirectionCalm(boolean windDirectionCalm) {
        this.windDirectionCalm = windDirectionCalm;
    }

    /**
     * Get windDirection
     *
     * @return wind direction
     */
    public int getWindDirection() {
        return windDirection;
    }

    /**
     * Set windDirection
     *
     * @param windDirection
     */
    public void setWindDirection(int windDirection) {
        this.windDirection = windDirection;
    }

    /**
     * Get windDirectionCompass
     *
     * @return wind direction compass
     */
    public String getWindDirectionCompass() {
        return windDirectionCompass;
    }

    /**
     * Set windDirectionCompass
     *
     * @param windDirectionCompass
     */
    public void setWindDirectionCompass(String windDirectionCompass) {
        this.windDirectionCompass = windDirectionCompass;
    }

    /**
     * Get windDirectionMin
     *
     * @return wind direction min
     */
    public int getWindDirectionMin() {
        return windDirectionMin;
    }

    /**
     * Set windDirectionMin
     *
     * @param windDirectionMin
     */
    public void setWindDirectionMin(int windDirectionMin) {
        this.windDirectionMin = windDirectionMin;
    }

    /**
     * Get windDirectionMax
     *
     * @return wind direction max
     */
    public int getWindDirectionMax() {
        return windDirectionMax;
    }

    /**
     * Set windDirectionMax
     *
     * @param windDirectionMax
     */
    public void setWindDirectionMax(int windDirectionMax) {
        this.windDirectionMax = windDirectionMax;
    }

    /**
     * Get windDirectionIsVariable
     *
     * @return is wind direction variable less than or equal 6 knots
     */
    public boolean isWindDirectionIsVariable() {
        return windDirectionIsVariable;
    }

    /**
     * Get windDirectionIsVarGtrSix
     *
     * @return is wind direction variable greater than 6 knots
     */
    public boolean isWindDirectionIsVarGtrSix() {
        return windDirectionIsVarGtrSix;
    }

    /**
     * Set windDirectionIsVariable
     *
     * @param windDirectionIsVariable
     */
    public void setWindDirectionIsVariable(boolean windDirectionIsVariable) {
        this.windDirectionIsVariable = windDirectionIsVariable;
    }

    /**
     * Get wind speed
     *
     * @return wind speed in knots
     */
    public Double getWindSpeedInKnots() {
        return windSpeed;
    }

    /**
     * Set wind speed
     *
     * @param windSpeed
     */
    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    /**
     * Get wind speed in MPS
     *
     * @return wind speed in meters per second
     */
    public Double getWindSpeedInMPS() {
        return this.windSpeed * 0.5148;
    }

    /**
     * Set wind speed in MPS
     *
     * @param value wind speed in meters per second
     */
    protected void setWindSpeedInMPS(Double value) {
        this.windSpeed = value / 0.5148;
    }

    /**
     * Get wind speed in MPH
     *
     * @return wind speed in MPH
     */
    public Double getWindSpeedInMPH() {
        if (this.windSpeed == null) {
            return null;
        }

        double f = this.windSpeed * 1.1508;
        // Round to the nearest MPH
        f = Math.round(f);

        return f;
    }

    /**
     * Get wind gusts in knots
     *
     * @return wind gusts in knots
     */
    public Double getWindGustsInKnots() {
        return windGusts;
    }

    /**
     * Set wind gusts
     *
     * @param windGusts
     */
    public void setWindGusts(Double windGusts) {
        this.windGusts = windGusts;
    }

    /**
     * Get wind gusts in MPS
     *
     * @return wind gust speed in meters per second
     */
    public Double getWindGustsInMPS() {
        return this.windGusts * 0.5148;
    }

    /**
     * Set wind gusts in MPS
     *
     * @param value wind gust speed in meters per second
     */
    protected void setWindGustsInMPS(Double value) {
        this.windGusts = value / 0.5148;
    }

    /**
     * Get wind gusts in MPH
     *
     * @return wind gust speed in MPH
     */
    public Double getWindGustsInMPH() {
        if (this.windGusts == null) {
            return null;
        }

        double f = this.windGusts * 1.1508;
        // roundValue to the nearest MPH
        f = Math.round(f);

        return f;
    }

    /**
     * Get peakWindDirection
     *
     * @return peak wind direction
     */
    public int getPeakWindDirection() {
        return peakWindDirection;
    }

    /**
     * Set peakWindDirection
     *
     * @param peakWindDirection
     */
    public void setPeakWindDirection(Integer peakWindDirection) {
        this.peakWindDirection = peakWindDirection;
    }

    /**
     * Get peakWindDirectionCompass
     *
     * @return peak wind direction compass
     */
    public String getPeakWindDirectionCompass() {
        return peakWindDirectionCompass;
    }

    /**
     * Set peakWindDirectionCompass
     *
     * @param peakWindDirectionCompass
     */
    public void setPeakWindDirectionCompass(String peakWindDirectionCompass) {
        this.peakWindDirectionCompass = peakWindDirectionCompass;
    }

    /**
     * Get peakWindHour
     *
     * @return hour
     */
    public int getPeakWindHour() {
        return peakWindHour;
    }

    /**
     * Get peakWindMin
     *
     * @return minutes after hour
     */
    public int getPeakWindMin() {
        return peakWindMin;
    }

    /**
     * Get peakWindSpeed
     *
     * @return peak wind speed in knots
     */
    public Double getPeakWindSpeed() {
        return peakWindSpeed;
    }

    /**
     * Get peak wind speed in MPH
     *
     * @return peak wind speed in MPH
     */
    public Double getPeakWindSpeedInMPH() {
        if (this.peakWindSpeed == null) {
            return null;
        }

        double f = this.peakWindSpeed * 1.1508;
        // Round to the nearest MPH
        f = Math.round(f);

        return f;
    }

    /**
     * Get WindNotDetermined. Used for unknown wind
     *
     * @return windNotDetermined
     */
    public String getWindNotDetermined() {
        return windNotDetermined;
    }
}
