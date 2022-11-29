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
 * Class representing the visibility section of the report
 *
 * Author: quark95cos Since: Copyright(c) 2022
 */
public class Visibility {

    private Integer visVarPrevailRunway;
    private boolean isCavok;
    private boolean isNDV;
    private boolean isVisibilityNotKnown;
    private boolean isVisVarPrevailVariable;
    private boolean isVisVarPrevailRunway;
    private boolean visibilityLessThan;
    private boolean visibilityGreaterThan;
    private boolean visibilitySplitFraction;
    private Double visVarPrevailOne;
    private Double visVarPrevailTwo;
    private Double visibilityMiles;
    private Double visibilityKilometers;
    private Double visibilityMeters;
    private Double visibilityTowSurMiles;
    private Double visibilityTowSurKilometers;
    private String visibilityNDV;
    private String visibilityTowSur;
    private String visVarPrevailDir;

    private static final Logger LOGGER
            = LogManager.getLogger(Visibility.class.getName());

    public Visibility() {
        this.visVarPrevailRunway = 0;
        this.isCavok = false;
        this.isNDV = false;
        this.isVisibilityNotKnown = false;
        this.isVisVarPrevailVariable = false;
        this.isVisVarPrevailRunway = false;
        this.visibilityLessThan = false;
        this.visibilityGreaterThan = false;
        this.visibilitySplitFraction = false;
        this.visVarPrevailOne = 0.0;
        this.visVarPrevailTwo = 0.0;
        this.visibilityMiles = 0.0;
        this.visibilityKilometers = 0.0;
        this.visibilityMeters = 0.0;
        this.visibilityTowSurMiles = 0.0;
        this.visibilityTowSurKilometers = 0.0;
        this.visibilityNDV = null;
        this.visibilityTowSur = null;
        this.visVarPrevailDir = null;
    }

    /**
     * Set the visibility information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setVisibilityItems(Matcher token) throws UtilsException {
        LOGGER.debug("vis: #" + token.group("vis") + "#");
        LOGGER.debug("dist: #" + token.group("dist") + "#");
        LOGGER.debug("dir: #" + token.group("dir") + "#");
        LOGGER.debug("distu: #" + token.group("distu") + "#");
        LOGGER.debug("units: #" + token.group("units") + "#");

        try {
            LOGGER.debug(Configs.getInstance().getString("VISIBILITY_DECODED_VISIBILITY"));
            if (token.group("vis").
                    equals(Configs.getInstance().getString("WEATHER_CAVOK"))) {
                // CAVOK
                //
                // Visibility greater than 10Km, no cloud below 5000 ft or
                // minimum
                // sector altitude, whichever is the lowest and no CB
                // (Cumulonimbus) or
                // over development and no significant weather.
                this.isCavok = true;
                LOGGER.debug(Configs.getInstance().getString("WEATHER_CAVOK")
                        + " " + Configs.getInstance().getString("LOC_TIME_DECODED_LOCTED"));
            } else if (token.group("vis").equals(Configs.getInstance()
                    .getString("WEATHER_NO_DIRECTIONAL_VARIATION"))) {
                this.visibilityNDV = Configs.getInstance()
                        .getString("WEATHER_DECODED_NO_DIRECTIONAL_VARIATION");
                this.isNDV = true;
                LOGGER.debug(Configs.getInstance().getString("VISIBILITY_DECODED_VISIBILITY")
                        + ": " + this.visibilityNDV);
            } else if (UtilsMisc.removeNonNumeric(
                    token.group("vis")).isEmpty()) {
                // Non numeric value but not CAVOK, NDV nor P6SM
                this.isVisibilityNotKnown = true;
                LOGGER.debug(Configs.getInstance().getString("VISIBILITY_DECODED_NON_NUMERIC"));
            } else if (token.group("units") != null && !token.group("units").equals("")) {
                Double visibility = null;

                if (token.group("distu").startsWith("M")) {
                    LOGGER.debug(Configs.getInstance().getString("VISIBILITY_DECODED_VISIBILITY")
                            + " " + Configs.getInstance().getString("LOC_TIME_DECODED_LESS_THAN"));
                    this.visibilityLessThan = true;
                    visibility = getVisibilityFromSource(token.group("distu")
                            .substring(1, token.group("distu").length()));
                } else if (token.group("distu").startsWith("P")) {
                    LOGGER.debug(Configs.getInstance().getString("VISIBILITY_DECODED_VISIBILITY")
                            + " " + Configs.getInstance().getString("LOC_TIME_DECODED_GREATER_THAN"));
                    this.visibilityGreaterThan = true;
                    visibility = getVisibilityFromSource(token.group("distu")
                            .substring(1, token.group("distu").length()));
                } else {
                    visibility = getVisibilityFromSource(token.group("distu"));
                }

                if (token.group("units").equals(Configs.getInstance()
                        .getString("WEATHER_STATUE_MILE"))) {
                    // Get visibility
                    // format: (M)VVVVVSM
                    //     (M) - used to indicate less than
                    //     VVVVV - miles (00001SM)
                    //     SM - statute miles
                    this.visibilityMiles = visibility;
                    this.visibilityKilometers = visibility * 1.609344;
                    LOGGER.debug(Configs.getInstance().getString("VISIBILITY_DECODED_VISIBILITY")
                            + " " + Configs.getInstance().getString("MSRMNT_DECODED_MILES")
                            + ": " + this.visibilityMiles);
                    LOGGER.debug(Configs.getInstance().getString("VISIBILITY_DECODED_VISIBILITY")
                            + " " + Configs.getInstance().getString("MSRMNT_DECODED_KILOMETERS")
                            + ": " + this.visibilityKilometers);
                } else if (token.group("units").equals(Configs.getInstance()
                        .getString("WEATHER_KILOMETER"))) {
                    this.visibilityKilometers = visibility;
                    this.visibilityMiles = visibilityKilometers * 0.62137;
                    LOGGER.debug(Configs.getInstance().getString("VISIBILITY_DECODED_VISIBILITY")
                            + " " + Configs.getInstance().getString("MSRMNT_DECODED_MILES")
                            + ": " + this.visibilityMiles);
                    LOGGER.debug(Configs.getInstance().getString("VISIBILITY_DECODED_VISIBILITY")
                            + " " + Configs.getInstance().getString("MSRMNT_DECODED_KILOMETERS")
                            + ": " + this.visibilityKilometers);
                } else {
                    LOGGER.debug("Should not get here");
                }
            } else if (token.group("vis").equals("9999")) {
                // Horizontal visibility in meters
                LOGGER.debug(Configs.getInstance().getString("VISIBILITY_DECODED_VISIBILITY")
                        + " 9999");
                this.visibilityKilometers = 10.0;
                this.visibilityMiles = visibilityKilometers * 0.62137;
                LOGGER.debug(Configs.getInstance().getString("VISIBILITY_DECODED_VISIBILITY")
                        + " " + Configs.getInstance().getString("MSRMNT_DECODED_MILES")
                        + ": " + this.visibilityMiles);
                LOGGER.debug(Configs.getInstance().getString("VISIBILITY_DECODED_VISIBILITY")
                        + " " + Configs.getInstance().getString("MSRMNT_DECODED_KILOMETERS")
                        + ": " + this.visibilityKilometers);
            } else if (token.group("vis").equals("0000")) {
                // Horizontal visibility in meters
                LOGGER.debug(Configs.getInstance().getString("VISIBILITY_DECODED_VISIBILITY")
                        + " 0000");
                this.visibilityLessThan = true;
                this.visibilityKilometers = 50.0 / 1000.0;
                this.visibilityMiles = visibilityKilometers * 0.62137;
                LOGGER.debug(Configs.getInstance().getString("VISIBILITY_DECODED_VISIBILITY")
                        + " " + Configs.getInstance().getString("MSRMNT_DECODED_MILES")
                        + ": " + this.visibilityMiles);
                LOGGER.debug(Configs.getInstance().getString("VISIBILITY_DECODED_VISIBILITY")
                        + " " + Configs.getInstance().getString("MSRMNT_DECODED_KILOMETERS")
                        + ": " + this.visibilityKilometers);
            } else if (UtilsMisc.containsOnlyNumbers(token.group("vis"))) {
                // Horizontal visibility in meters not 9999 nor 0000
                LOGGER.debug(Configs.getInstance().getString("VISIBILITY_DECODED_VISIBILITY")
                        + " " + token.group("vis"));
                this.visibilityKilometers = Double.parseDouble(
                        token.group("vis")) / 1000.0;
                this.visibilityMiles = visibilityKilometers * 0.62137;
                LOGGER.debug(Configs.getInstance().getString("VISIBILITY_DECODED_VISIBILITY")
                        + " " + Configs.getInstance().getString("MSRMNT_DECODED_MILES")
                        + ": " + this.visibilityMiles);
                LOGGER.debug(Configs.getInstance().getString("VISIBILITY_DECODED_VISIBILITY")
                        + " " + Configs.getInstance().getString("MSRMNT_DECODED_KILOMETERS")
                        + ": " + this.visibilityKilometers);
            } else {
                LOGGER.debug("Should not get here");
            }
        } catch (NumberFormatException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setVisibilityItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            LOGGER.error(errMsg);
            throw new UtilsException("setVisibilityItems: ", err);
        }
    }

    /**
     * Set the tower or surface visibility information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setTowerSurfVisItems(Matcher token) throws UtilsException {
        LOGGER.debug("type: #" + token.group("type") + "#");
        LOGGER.debug("dist: #" + token.group("dist") + "#");

        try {
            this.visibilityTowSur = token.group("type");
            LOGGER.debug(Configs.getInstance().getString("EXTENDED_DECODE_CTL_TWR_VIS")
                    + ": " + this.visibilityTowSur);

            this.visibilityTowSurMiles = getVisibilityFromSource(token.group("dist"));
            LOGGER.debug(Configs.getInstance().getString("MSRMNT_DECODED_MILES")
                    + ": " + this.visibilityTowSurMiles);
            this.visibilityTowSurKilometers = visibilityTowSurMiles * 1.609344;
            LOGGER.debug(Configs.getInstance().getString("MSRMNT_DECODED_KILOMETERS")
                    + ": " + this.visibilityTowSurKilometers);
        } catch (NumberFormatException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setTowerSurfVisItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
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
    public void setVarPrevVisSecVisVisSecLocItems(Matcher token)
            throws UtilsException {
        LOGGER.debug("vis: #" + token.group("vis") + "#");
        LOGGER.debug("dir: #" + token.group("dir") + "#");
        LOGGER.debug("dist1: #" + token.group("dist1") + "#");
        LOGGER.debug("add: #" + token.group("add") + "#");
        LOGGER.debug("dist2: #" + token.group("dist2") + "#");

        try {
            this.isVisVarPrevailVariable = false;
            this.isVisVarPrevailRunway = false;
            //getVisibilityFromSource(token.group("dist"))
            if (token.group("add") != null) {
                switch (token.group("add")) {
                    case "V":
                        visVarPrevailOne = getVisibilityFromSource(token.group("dist1"));
                        visVarPrevailTwo = getVisibilityFromSource(token.group("dist2"));
                        this.isVisVarPrevailVariable = true;
                        LOGGER.debug(Configs.getInstance().getString("VISIBILITY_DECODED_VAR_PREVAIL_ONE")
                                + ": " + this.visVarPrevailOne);
                        LOGGER.debug(Configs.getInstance().getString("VISIBILITY_DECODED_VAR_PREVAIL_TWO")
                                + ": " + this.visVarPrevailTwo);
                        LOGGER.debug(Configs.getInstance().getString("VISIBILITY_DECODED_IS_VISVAR_PREVAIL_VARIABLE")
                                + ": " + this.isVisVarPrevailVariable);
                        break;
                    case "RWY":
                        visVarPrevailOne = getVisibilityFromSource(token.group("dist1"));
                        visVarPrevailRunway = Integer.parseInt(token.group("dist2"));
                        this.isVisVarPrevailRunway = true;
                        LOGGER.debug(Configs.getInstance().getString("VISIBILITY_DECODED_VAR_PREVAIL_ONE")
                                + ": " + this.visVarPrevailOne);
                        LOGGER.debug(Configs.getInstance().getString("VISIBILITY_DECODED_VISVAR_PREVAIL_RUNWAY")
                                + ": " + this.visVarPrevailRunway);
                        LOGGER.debug(Configs.getInstance().getString("VISIBILITY_DECODED_IS_VISVAR_PREVAIL_RUNWAY")
                                + ": " + this.isVisVarPrevailRunway);
                        break;
                    default:
                        LOGGER.debug(Configs.getInstance().getString("VISIBILITY_DECODED_PREVAIL_UNKNOWN"));
                        break;
                }
            } else {
                visVarPrevailDir = token.group("dir");
                LOGGER.debug(Configs.getInstance().getString("VISIBILITY_DECODED_VISVAR_PREVAIL_DIR")
                        + ": " + this.visVarPrevailDir);
                visVarPrevailOne = getVisibilityFromSource(token.group("dist1"));
                LOGGER.debug(Configs.getInstance().getString("VISIBILITY_DECODED_VAR_PREVAIL_ONE")
                        + ": " + this.visVarPrevailOne);
            }
        } catch (NumberFormatException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setVarPrevVisSecVisVisSecLocItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            LOGGER.error(errMsg);
            throw new UtilsException("setVarPrevVisSecVisVisSecLocItems: ", err);
        }
    }

    /**
     * Get the visibility from source information
     *
     * @param token
     * @return visibility
     */
    private Double getVisibilityFromSource(String token) throws UtilsException {
        String whole = "";
        String fraction = "";
        Double visibility = 0.0;

        try {
            if (token.indexOf('/') == -1) {
                // No fractions to deal with
                whole = token.substring(0, token.length());
                LOGGER.debug("No fraction");
            } else {
                if (token.indexOf(' ') != -1) {
                    whole = token.substring(0, token.indexOf(' '));
                    fraction = token.substring(token.indexOf(' ') + 1,
                            token.length());
                    this.visibilitySplitFraction = true;
                } else {
                    whole = "0";
                    fraction = token.substring(0, token.length());
                }
                LOGGER.debug("whole: " + whole);
                LOGGER.debug("fraction: " + fraction);
                LOGGER.debug("visibilitySplit: " + visibilitySplitFraction);
            }

            visibility = Double.parseDouble(whole);

            if (!fraction.equals("")) {
                String[] visParts = UtilsMisc.stringSplit(fraction, "/");
                visibility = visibility
                        + Double.parseDouble(visParts[0])
                        / Double.parseDouble(visParts[1]);
            }

            LOGGER.debug(Configs.getInstance().getString("VISIBILITY_DECODED_VISIBILITY")
                    + ": " + visibility);
        } catch (NumberFormatException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "getVisibilityFromSource: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            LOGGER.error(errMsg);
            throw new UtilsException("getVisibilityFromSource: ", err);
        }

        return visibility;
    }

    /**
     * Get isCAVOK
     *
     * @return isCAVOK
     */
    public boolean isCavok() {
        return isCavok;
    }

    /**
     * Get isNDV
     *
     * @return isNDV
     */
    public boolean isNDV() {
        return isNDV;
    }

    /**
     * Get visibilityNDV
     *
     * @return visibilityNDV
     */
    public String getVisibilityNDV() {
        return visibilityNDV;
    }

    /**
     * Set isCAVOK
     *
     * @param isCavok
     */
    public void setIsCavok(boolean isCavok) {
        this.isCavok = isCavok;
    }

    /**
     * Get isVisibilityNotKnown
     *
     * @return is CAVOK
     */
    public boolean isVisibilityNotKnown() {
        return isVisibilityNotKnown;
    }

    /**
     * Set isVisibilityNotKnown
     *
     * @param isVisibilityNotKnown
     */
    public void setIsVisibilityNotKnown(boolean isVisibilityNotKnown) {
        this.isVisibilityNotKnown = isVisibilityNotKnown;
    }

    /**
     * Get visibility
     *
     * @return visibility in miles
     */
    public Double getVisibility() {
        if (visibilityMiles != null) {
            return UtilsMisc.roundValue(visibilityMiles, 2);
        } else if (visibilityKilometers != null) {
            return UtilsMisc.roundValue(visibilityKilometers / 1.609344, 2);
        } else if (visibilityMeters != null) {
            return UtilsMisc.roundValue(visibilityMeters / 1609.344, 2);
        }
        return null;
    }

    /**
     * Get visibilityMiles
     *
     * @return visibility in miles
     */
    public Double getVisibilityMiles() {
        return UtilsMisc.roundValue(visibilityMiles, 2);
    }

    /**
     * Set visibilityMiles
     *
     * @param visibilityMiles
     */
    public void setVisibilityMiles(Double visibilityMiles) {
        this.visibilityMiles = visibilityMiles;
    }

    /**
     * Get visibility in kilometers
     *
     * @return visibility in kilometers
     */
    public Double getVisibilityKilometers() {
        return UtilsMisc.roundValue(visibilityKilometers, 2);
    }

    /**
     * Set visibilityKilometers
     *
     * @param visibilityKilometers
     */
    public void setVisibilityKilometers(Double visibilityKilometers) {
        this.visibilityKilometers = visibilityKilometers;
    }

    /**
     * Get visibility in meters
     *
     * @return visibility in meters
     */
    public Double getVisibilityMeters() {
        return UtilsMisc.roundValue(visibilityMeters, 2);
    }

    /**
     * Set visibilityMeters
     *
     * @param visibilityMeters
     */
    public void setVisibilityMeters(Double visibilityMeters) {
        this.visibilityMeters = visibilityMeters;
    }

    /**
     * Get visibilityLessThan
     *
     * @return is visibility less than
     */
    public boolean isVisibilityLessThan() {
        return visibilityLessThan;
    }

    /**
     * Set visibilityLessThan
     *
     * @param visibilityLessThan
     */
    public void setVisibilityLessThan(boolean visibilityLessThan) {
        this.visibilityLessThan = visibilityLessThan;
    }

    /**
     * Get visibilityGreaterThan
     *
     * @return is visibility greater than
     */
    public boolean isVisibilityGreaterThan() {
        return visibilityGreaterThan;
    }

    /**
     * Set visibilityGreaterThan
     *
     * @param visibilityGreaterThan
     */
    public void setVisibilityGreaterThan(boolean visibilityGreaterThan) {
        this.visibilityGreaterThan = visibilityGreaterThan;
    }

    /**
     * Get visibilitySplitFraction
     *
     * @return visibility less than
     */
    public boolean isVisibilitySplitFraction() {
        return visibilitySplitFraction;
    }

    /**
     * Set VisibilitySplitFraction
     *
     * @param visibilitySplitFraction
     */
    public void setVisibilitySplitFraction(boolean visibilitySplitFraction) {
        this.visibilitySplitFraction = visibilitySplitFraction;
    }

    /**
     * Get visibilityTowSurMiles
     *
     * @return visibilityTowSurMiles
     */
    public Double getVisibilityTowSurMiles() {
        return visibilityTowSurMiles;
    }

    /**
     * Get visibilityTowSurKilometers
     *
     * @return visibilityTowSurKilometers
     */
    public Double getVisibilityTowSurKilometers() {
        return visibilityTowSurKilometers;
    }

    /**
     * Get visibilityTowSur
     *
     * @return visibilityTowSur
     */
    public String getVisibilityTowSur() {
        return visibilityTowSur;
    }

    /**
     * Get visVarPrevailRunway
     *
     * @return visVarPrevailRunway
     */
    public Integer getVisVarPrevailRunway() {
        return visVarPrevailRunway;
    }

    /**
     * Get isVisVarPrevailVariable
     *
     * @return isVisVarPrevailVariable
     */
    public boolean isVisVarPrevailVariable() {
        return isVisVarPrevailVariable;
    }

    /**
     * Get isVisVarPrevailRunway
     *
     * @return isVisVarPrevailRunway
     */
    public boolean isVisVarPrevailRunway() {
        return isVisVarPrevailRunway;
    }

    /**
     * Get visVarPrevailOne
     *
     * @return visibilityTowSur
     */
    public Double getVisVarPrevailOne() {
        return visVarPrevailOne;
    }

    /**
     * Get visVarPrevailTwo
     *
     * @return visibilityTowSur
     */
    public Double getVisVarPrevailTwo() {
        return visVarPrevailTwo;
    }

    /**
     * Get visVarPrevailDir
     *
     * @return visibilityTowSur
     */
    public String getVisVarPrevailDir() {
        return visVarPrevailDir;
    }
}
