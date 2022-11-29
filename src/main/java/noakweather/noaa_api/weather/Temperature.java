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
 * Class representing the temperature section of the report
 *
 * Author: quark95cos Since: Copyright(c) 2022
 */
public class Temperature {

    private static final int MINUS_TEN = -10;
    private static final int MINUS_ONE = -1;
    private static final int PLUS_ONE = 1;
    private static final int PLUS_TWO = 2;
    private static final int PLUS_FIVE = 5;
    private static final int PLUS_NINE = 9;
    private static final int PLUS_TEN = 10;
    private static final int PLUS_THIRTY_TWO = 32;
    private static final Double ZERO_FLOAT = 0.0;
    private Double temperature;
    private Double dewPoint;
    private Double hourlyTemperature;
    private Double hourlyDewPoint;
    private Double SixHourMaximumTemperature;
    private Double SixHourMinimumTemperature;
    private Double TwentyFourHourMaximumTemperature;
    private Double TwentyFourHourMinimumTemperature;
    private Double MaximumTemperature;
    private Double MinimumTemperature;

    private static final Logger LOGGER
            = LogManager.getLogger(Temperature.class.getName());

    /**
     * Constructor
     */
    public Temperature() {
        this.temperature = null;
        this.dewPoint = null;
        this.hourlyTemperature = null;
        this.hourlyDewPoint = null;
        this.SixHourMaximumTemperature = null;
        this.SixHourMinimumTemperature = null;
        this.TwentyFourHourMaximumTemperature = null;
        this.TwentyFourHourMinimumTemperature = null;
        this.MaximumTemperature = null;
        this.MinimumTemperature = null;
    }

    /**
     * Set the temperature information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setTemperatureItems(Matcher token) throws UtilsException {
        try {
            LOGGER.debug("signt: #" + token.group("signt") + "#");
            LOGGER.debug("temp: #" + token.group("temp") + "#");
            LOGGER.debug("signd: #" + token.group("signd") + "#");
            LOGGER.debug("dewpt: #" + token.group("dewpt") + "#");

            // Temperature is missing from report
            if (token.group("temp") == null) {
                this.temperature = null;
            } else {
                this.temperature = Double.parseDouble(token.group("temp"));
            }

            //this.temperature != 0.0) {
            if (token.group("signt") != null && token.group("signt").equals("M")
                    && Double.compare(this.temperature, ZERO_FLOAT) != 0) {
                this.temperature *= MINUS_ONE; // negate
            }

            LOGGER.debug(Configs.getInstance().getString("TEMP_DECODED_TEMPERATURE")
                    + " " + this.temperature);

            // DewPoint is missing from report
            if (token.group("dewpt") == null) {
                this.dewPoint = null;
            } else {
                this.dewPoint = Double.parseDouble(token.group("dewpt"));
            }

            //this.dewPoint != 0.0)
            if (token.group("signd") != null && token.group("signd").equals("M")
                    && Double.compare(this.dewPoint, ZERO_FLOAT) != 0) {
                this.dewPoint *= MINUS_ONE; // negate
            }

            LOGGER.debug(Configs.getInstance().getString("TEMP_DECODED_DEWPT")
                    + " " + this.dewPoint);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setTemperatureItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            LOGGER.error(errMsg);
            throw new UtilsException("setTemperatureItems: ", err);
        }
    }

    /**
     * Set the hourly temperate information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setHourlyTemperatureItems(Matcher token) throws UtilsException {
        try {
            LOGGER.debug("type: #" + token.group("type") + "#");
            LOGGER.debug("tsign: #" + token.group("tsign") + "#");
            LOGGER.debug("temp: #" + token.group("temp") + "#");
            LOGGER.debug("dsign: #" + token.group("dsign") + "#");
            LOGGER.debug("dewpt: #" + token.group("dewpt") + "#");

            if (Integer.parseInt(token.group("tsign")) == PLUS_ONE) {
                hourlyTemperature = Double.parseDouble(token.group("temp")) / MINUS_TEN;
            } else {
                hourlyTemperature = Double.parseDouble(token.group("temp")) / PLUS_TEN;
            }
            if (token.group("dsign") != null && token.group("dewpt") != null) {
                if (Integer.parseInt(token.group("dsign")) == PLUS_ONE) {
                    hourlyDewPoint = Double.parseDouble(token.group("dewpt")) / MINUS_TEN;
                } else {
                    hourlyDewPoint = Double.parseDouble(token.group("dewpt")) / PLUS_TEN;
                }
            }

            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_HOURLY_TEMP")
                    + " " + hourlyTemperature);
            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_HOURLY_DEWPT")
                    + " " + hourlyDewPoint);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setHourlyTemperatureItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            LOGGER.error(errMsg);
            throw new UtilsException("setHourlyTemperatureItems: ", err);
        }
    }

    /**
     * Set the six hour maximum and minimum temperature information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setSixHourMaxMinTemperature(Matcher token) throws UtilsException {
        try {
            LOGGER.debug("type: #" + token.group("type") + "#");
            LOGGER.debug("sign: #" + token.group("sign") + "#");
            LOGGER.debug("temp: #" + token.group("temp") + "#");

            switch (Integer.parseInt(token.group("type"))) {
                case PLUS_ONE:
                    if (Integer.parseInt(token.group("sign")) == PLUS_ONE) {
                        SixHourMaximumTemperature
                                = Double.parseDouble(token.group("temp")) / MINUS_TEN;
                    } else {
                        SixHourMaximumTemperature
                                = Double.parseDouble(token.group("temp")) / PLUS_TEN;
                    }
                    LOGGER.debug(Configs.getInstance()
                            .getString("EXTENDED_DECODED_6_HOUR_MAX_TEMP")
                            + " " + SixHourMaximumTemperature);
                    break;
                case PLUS_TWO:
                    if (Integer.parseInt(token.group("sign")) == PLUS_ONE) {
                        SixHourMinimumTemperature
                                = Double.parseDouble(token.group("temp")) / MINUS_TEN;
                    } else {
                        SixHourMinimumTemperature
                                = Double.parseDouble(token.group("temp")) / PLUS_TEN;
                    }
                    LOGGER.debug(Configs.getInstance()
                            .getString("EXTENDED_DECODED_6_HOUR_MIN_TEMP")
                            + " " + SixHourMinimumTemperature);
                    break;
                //Should not happen
                default:
                    break;
            }
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setSixHourMaxMinTemperature: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            LOGGER.error(errMsg);
            throw new UtilsException("setSixHourMaxMinTemperature: ", err);
        }
    }

    /**
     * Set the twenty four hour maximum and minimum temperature information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setTwentyFourHourMaxMinTemperature(Matcher token) throws UtilsException {
        try {
            LOGGER.debug("type: #" + token.group("type") + "#");
            LOGGER.debug("maxsign: #" + token.group("maxsign") + "#");
            LOGGER.debug("maxtemp: #" + token.group("maxtemp") + "#");
            LOGGER.debug("minsign: #" + token.group("minsign") + "#");
            LOGGER.debug("mintemp: #" + token.group("mintemp") + "#");

            if (Integer.parseInt(token.group("maxsign")) == PLUS_ONE) {
                TwentyFourHourMaximumTemperature
                        = Double.parseDouble(token.group("maxtemp")) / MINUS_TEN;
            } else {
                TwentyFourHourMaximumTemperature
                        = Double.parseDouble(token.group("maxtemp")) / PLUS_TEN;
            }
            if (Integer.parseInt(token.group("minsign")) == PLUS_ONE) {
                TwentyFourHourMinimumTemperature
                        = Double.parseDouble(token.group("mintemp")) / MINUS_TEN;
            } else {
                TwentyFourHourMinimumTemperature
                        = Double.parseDouble(token.group("mintemp")) / PLUS_TEN;
            }

            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_24_HOUR_MAX_TEMP")
                    + " " + TwentyFourHourMaximumTemperature);
            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODED_24_HOUR_MIN_TEMP")
                    + " " + TwentyFourHourMinimumTemperature);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setTwentyFourHourMaxMinTemperature: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            LOGGER.error(errMsg);
            throw new UtilsException("setTwentyFourHourMaxMinTemperature: ", err);
        }
    }

    /**
     * Get temperature
     *
     * @return temperature
     */
    public Double getTemperature() {
        return temperature;
    }

    /**
     * Set temperature
     *
     * @param temperature
     */
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    /**
     * Get dew point
     *
     * @return dewPoint
     */
    public Double getDewpoint() {
        return dewPoint;
    }

    /**
     * Set dew point
     *
     * @param dewpoint
     */
    public void setDewpoint(Double dewpoint) {
        this.dewPoint = dewpoint;
    }

    /**
     * Get hourly temperature
     *
     * @return hourly temperature
     */
    public Double getHourlyTemperature() {
        return hourlyTemperature;
    }

    /**
     * Set hourly temperature
     *
     * @param hourlyTemperature
     */
    public void setHourlyTemperature(Double hourlyTemperature) {
        this.hourlyTemperature = hourlyTemperature;
    }

    /**
     * Get hourly dew point
     *
     * @return hourly dewPoint
     */
    public Double getHourlyDewPoint() {
        return hourlyDewPoint;
    }

    /**
     * Set hourly dew point
     *
     * @param hourlyDewPoint
     */
    public void setHourlyDewPoint(Double hourlyDewPoint) {
        this.hourlyDewPoint = hourlyDewPoint;
    }

    /**
     * Get six hour maximum temperature
     *
     * @return six hourly maximum temperature
     */
    public Double getSixHourMaximumTemperature() {
        return SixHourMaximumTemperature;
    }

    /**
     * Set six hour maximum temperature
     *
     * @param SixHourMaximumTemperature temperature
     */
    public void setSixHourMaximumTemperature(Double SixHourMaximumTemperature) {
        this.SixHourMaximumTemperature = SixHourMaximumTemperature;
    }

    /**
     * Get twenty four hour maximum temperature
     *
     * @return twenty four hour maximum temperature
     */
    public Double getTwentyFourHourMaximumTemperature() {
        return TwentyFourHourMaximumTemperature;
    }

    /**
     * Get twenty four hour minimum temperature
     *
     * @return twenty four hour minimum temperature
     */
    public Double getTwentyFourHourMinimumTemperature() {
        return TwentyFourHourMinimumTemperature;
    }

    /**
     * Get maximum temperature
     *
     * @return maximum temperature
     */
    public Double getMaximumTemperature() {
        return MaximumTemperature;
    }

    /**
     * Set maximum temperature
     *
     * @param MaximumTemperature temperature
     */
    public void setMaximumTemperature(Double MaximumTemperature) {
        this.MaximumTemperature = MaximumTemperature;
    }

    /**
     * Get minimum temperature
     *
     * @return minimum temperature
     */
    public Double getMinimumTemperature() {
        return MinimumTemperature;
    }

    /**
     * Set minimum temperature
     *
     * @param MinimumTemperature temperature
     */
    public void setMinimumTemperature(Double MinimumTemperature) {
        this.MinimumTemperature = MinimumTemperature;
    }

    /**
     * Get temperature in celsius
     *
     * @return temperature in celsius
     */
    public Double getTemperatureInCelsius() {
        return this.temperature;
    }

    /**
     * Get temperature in fahrenheit
     *
     * @return temperature in fahrenheit
     */
    public Double getTemperatureInFahrenheit() {
        if (this.temperature == null) {
            return null;
        }

        // Round
        double f = UtilsMisc.roundValue(this.temperature * PLUS_NINE / PLUS_FIVE + PLUS_THIRTY_TWO, 2);

        return f;
    }

    /**
     * Get hourly temperature in celsius
     *
     * @return temperature in celsius
     */
    public Double getHourlyTemperatureInCelsius() {
        return this.hourlyTemperature;
    }

    /**
     * Get hourly temperature in fahrenheit
     *
     * @return temperature in fahrenheit
     */
    public Double getHourlyTemperatureInFahrenheit() {
        if (this.hourlyTemperature == null) {
            return null;
        }

        // Round
        double f = UtilsMisc.roundValue(this.hourlyTemperature * PLUS_NINE / PLUS_FIVE + PLUS_THIRTY_TWO, 2);

        return f;
    }

    /**
     * Get dew point in celsius
     *
     * @return dew point in celsius
     */
    public Double getDewPointInCelsius() {
        return this.dewPoint;
    }

    /**
     * Get dew point in fahrenheit
     *
     * @return dew point in fahrenheit
     */
    public Double getDewPointInFahrenheit() {
        if (this.dewPoint == null) {
            return null;
        }

        // Round
        double f = UtilsMisc.roundValue(this.dewPoint * PLUS_NINE / PLUS_FIVE + PLUS_THIRTY_TWO, 2);

        return f;
    }

    /**
     * Get hourly dew point in celsius
     *
     * @return dew point in celsius
     */
    public Double getHourlyDewPointInCelsius() {
        return this.hourlyDewPoint;
    }

    /**
     * Get hourly dew point in fahrenheit
     *
     * @return dew point in fahrenheit
     */
    public Double getHourlyDewPointInFahrenheit() {
        if (this.hourlyDewPoint == null) {
            return null;
        }

        // Round
        double f = UtilsMisc.roundValue(this.hourlyDewPoint * PLUS_NINE / PLUS_FIVE + PLUS_THIRTY_TWO, 2);

        return f;
    }

    /**
     * Get six hourly maximum temperature in celsius
     *
     * @return dew point in celsius
     */
    public Double getSixHourlyMaximumTemperatureInCelsius() {
        return this.SixHourMaximumTemperature;
    }

    /**
     * Get six hourly maximum temperature in fahrenheit
     *
     * @return dew point in fahrenheit
     */
    public Double getSixHourlyMaximumTemperatureInFahrenheit() {
        if (this.SixHourMaximumTemperature == null) {
            return null;
        }

        // Round
        double f = UtilsMisc.roundValue(this.SixHourMaximumTemperature * PLUS_NINE / PLUS_FIVE + PLUS_THIRTY_TWO, 2);

        return f;
    }

    /**
     * Get six hourly minimum temperature in celsius
     *
     * @return dew point in celsius
     */
    public Double getSixHourlyMinimumTemperatureInCelsius() {
        return this.SixHourMinimumTemperature;
    }

    /**
     * Get six hourly minimum temperature in fahrenheit
     *
     * @return dew point in fahrenheit
     */
    public Double getSixHourlyMinimumTemperatureInFahrenheit() {
        if (this.SixHourMinimumTemperature == null) {
            return null;
        }

        // Round
        double f = UtilsMisc.roundValue(this.SixHourMinimumTemperature * PLUS_NINE / PLUS_FIVE + PLUS_THIRTY_TWO, 2);

        return f;
    }

    /**
     * Get twenty four hour maximum temperature in celsius
     *
     * @return twenty four hour maximum temperature in celcius
     */
    public Double getTwentyFourHourMaximumTemperatureInCelsius() {
        return TwentyFourHourMaximumTemperature;
    }

    /**
     * Get twenty four hour maximum temperature in fahrenheit
     *
     * @return twenty four hour maximum temperature in fahrenheit
     */
    public Double getTwentyFourHourMaximumTemperatureInFahrenheit() {
        if (this.TwentyFourHourMaximumTemperature == null) {
            return null;
        }

        // Round
        double f = UtilsMisc.roundValue(this.TwentyFourHourMaximumTemperature * PLUS_NINE / PLUS_FIVE + PLUS_THIRTY_TWO, 2);

        return f;
    }

    /**
     * Get twenty four hour minimum temperature in celsius
     *
     * @return twenty four hour minimum temperature in celcius
     */
    public Double getTwentyFourHourMinimumTemperatureInCelsius() {
        return TwentyFourHourMinimumTemperature;
    }

    /**
     * Get twenty four hour minimum temperature in fahrenheit
     *
     * @return twenty four hour minimum temperature in fahrenheit
     */
    public Double getTwentyFourHourMinimumTemperatureInFahrenheit() {
        if (this.TwentyFourHourMinimumTemperature == null) {
            return null;
        }

        // Round
        double f = UtilsMisc.roundValue(this.TwentyFourHourMinimumTemperature * PLUS_NINE / PLUS_FIVE + PLUS_THIRTY_TWO, 2);

        return f;
    }

    /**
     * Get six hour minimum temperature
     *
     * @return Six Hour Minimum Temperature in fahrenheit
     */
    public Double getSixHourMinimumTemperature() {
        return SixHourMinimumTemperature;
    }

    /**
     * Set six hour minimum temperature
     *
     * @param SixHourMinimumTemperature
     */
    public void setSixHourMinimumTemperature(Double SixHourMinimumTemperature) {
        this.SixHourMinimumTemperature = SixHourMinimumTemperature;
    }
}
