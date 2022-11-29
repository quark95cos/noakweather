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
import noakweather.utils.Configs;
import noakweather.utils.IndexedLinkedHashMap;
import noakweather.utils.UtilsException;
import noakweather.utils.WthItemHandlers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class representing the runway visual range information
 *
 * Author: quark95cos Since: Copyright(c) 2022
 */
public class RunwayVisualRange {

    private int runwayNumber;               // runway number
    private int lowestReportable;           // (ft)
    private int highestReportable;          // (ft)
    private char approachDirection;         // L/R/C
    private char reportableModifier;        // P - below, M - above
    private char reportableTrend;           // U - increasing, D - decreasing, N - no change
    private boolean isContaminationCondClrd;
    private String decodedReportableModifier = null;
    private static final String UNABLE_PARSE_VALUE = "Unable to parse value:";
    private final IndexedLinkedHashMap<String, String> aviaRVRWthItemsHandlers;

    private static final Logger LOGGER
            = LogManager.getLogger(RunwayVisualRange.class.getName());

    public RunwayVisualRange() {
        this.runwayNumber = 0;
        this.lowestReportable = 0;
        this.highestReportable = 0;
        this.approachDirection = ' ';
        this.reportableModifier = ' ';
        this.reportableTrend = ' ';
        this.isContaminationCondClrd = false;
        this.decodedReportableModifier = null;
        this.aviaRVRWthItemsHandlers = WthItemHandlers.setRVRWthItemsHandlers();
    }

    /**
     * Set the runway number information
     *
     * @param runwayNumber the part of a METAR RVR token which represents a
     * runway number
     * @throws noakweather.utils.UtilsException
     */
    public void setRunwayNumber(int runwayNumber) throws UtilsException {
        try {
            LOGGER.debug(Configs.getInstance().getString("RVR_DECODED_RUNWAY_NUMBER")
                    + " " + runwayNumber);
            this.runwayNumber = runwayNumber;
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setRunwayNumber: " + UNABLE_PARSE_VALUE + " " + err;
            LOGGER.error(errMsg);
            throw new UtilsException("setRunwayNumber: ", err);
        }
    }

    /**
     * Set the approach direction information
     *
     * @param direction the part of a METAR RVR token which represents an
     * approach direction (e.g. 'L', 'R' or 'C')
     * @throws noakweather.utils.UtilsException
     */
    public void setApproachDirection(char direction) throws UtilsException {
        try {
            LOGGER.debug(Configs.getInstance().getString("RVR_DECODED_RUNWAY_APPROACH_DIRECTION")
                    + " " + direction);
            this.approachDirection = direction;
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setApproachDirection: " + UNABLE_PARSE_VALUE + " " + err;
            LOGGER.error(errMsg);
            throw new UtilsException("setApproachDirection: ", err);
        }
    }

    /**
     * Set the reportable modifier information
     *
     * @param modifier the part of a METAR RVR token which represents a modifier
     * used to specify if the visual range is above or below the following value
     * @throws noakweather.utils.UtilsException
     */
    public void setReportableModifier(char modifier) throws UtilsException {
        try {
            this.reportableModifier = modifier;
            LOGGER.debug(Configs.getInstance().getString("RVR_DECODED_RUNWAY_MP_RVR_MODIFIER")
                    + " " + this.reportableModifier);

            decodedReportableModifier = aviaRVRWthItemsHandlers
                    .getValueAtIndex(aviaRVRWthItemsHandlers
                            .getIndexOf(String.valueOf(modifier)));
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setReportableModifier: " + UNABLE_PARSE_VALUE + " " + err;
            LOGGER.error(errMsg);
            throw new UtilsException("setReportableModifier: ", err);
        }
    }

    /**
     * Set the contamination condition information
     *
     * @param isContaminationCondClrd
     * @throws noakweather.utils.UtilsException
     */
    public void setContaminationCondClrd(boolean isContaminationCondClrd) throws UtilsException {
        try {
            this.isContaminationCondClrd = isContaminationCondClrd;
            LOGGER.debug(Configs.getInstance()
                    .getString("RVR_DECODED_RUNWAY_CONTMNTN_COND_CLRD_DIRECTION")
                    + " " + this.isContaminationCondClrd);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setContaminationCondClrd: " + UNABLE_PARSE_VALUE + " " + err;
            LOGGER.error(errMsg);
            throw new UtilsException("setContaminationCondClrd: ", err);
        }
    }

    /**
     * Set the reportable trend information
     *
     * @param trend the part of a METAR RVR token which represents a modifier
     * used to specify if the visual range is above or below the following value
     * @throws noakweather.utils.UtilsException
     */
    public void setReportableTrend(char trend) throws UtilsException {
        try {
            this.reportableTrend = trend;
            LOGGER.debug(Configs.getInstance().getString("RVR_DECODED_RVR_MODIFIER")
                    + " " + this.reportableTrend);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setReportableTrend: " + UNABLE_PARSE_VALUE + " " + err;
            LOGGER.error(errMsg);
            throw new UtilsException("setReportableTrend: ", err);
        }
    }

    /**
     * Set the lowest reportable value information
     *
     * @param lowestReportable the part of a METAR RVR token which represents
     * the lowest reportable value for visual range
     * @throws noakweather.utils.UtilsException
     */
    public void setLowestReportable(int lowestReportable) throws UtilsException {
        try {
            this.lowestReportable = lowestReportable;
            LOGGER.debug(Configs.getInstance().getString("RVR_DECODED_RVR_LOWEST_REPORTABLE")
                    + " " + lowestReportable);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setLowestReportable: " + UNABLE_PARSE_VALUE + " " + err;
            LOGGER.error(errMsg);
            throw new UtilsException("setLowestReportable: ", err);
        }
    }

    /**
     * Set the highest reportable value information
     *
     * @param highestReportable the part of a METAR RVR token which represents
     * the highest reportable value for visual range
     * @throws noakweather.utils.UtilsException
     */
    public void setHighestReportable(int highestReportable) throws UtilsException {
        try {
            this.highestReportable = highestReportable;
            LOGGER.debug(Configs.getInstance().getString("RVR_DECODED_RVR_HIGHEST_REPORTABLE")
                    + " " + highestReportable);
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setHighestReportable: " + UNABLE_PARSE_VALUE + " " + err;
            LOGGER.error(errMsg);
            throw new UtilsException("setHighestReportable: ", err);
        }
    }

    /**
     * Get the approach direction
     *
     * @return approachDirection
     */
    public char getApproachDirection() {
        return approachDirection;
    }

    /**
     * Get highest reportable value
     *
     * @return highestReportable
     */
    public int getHighestReportable() {
        return highestReportable;
    }

    /**
     * Get lowest reportable value
     *
     * @return lowestReportable
     */
    public int getLowestReportable() {
        return lowestReportable;
    }

    /**
     * Get reportable modifier
     *
     * @return reportableModifier
     */
    public char getReportableModifier() {
        return reportableModifier;
    }

    /**
     * Get reportable trend
     *
     * @return reportableTrend
     */
    public char getReportableTrend() {
        return reportableTrend;
    }

    /**
     * Get runway number
     *
     * @return runwayNumber
     */
    public int getRunwayNumber() {
        return runwayNumber;
    }

    /**
     * Get decoded reportable modifier
     *
     * @return decodedReportableModifier
     */
    public String getDecodedReportableModifier() {
        return decodedReportableModifier;
    }

    /**
     * Get the natural language in human readable form This method will return a
     * string that represents this runway visual range using natural language
     * (as opposed to METAR)
     *
     * @return a string that represents the runway visual range in natural
     * language
     * @throws noakweather.utils.UtilsException
     */
    public String getNaturalLanguageString() throws UtilsException {
        String temp = Integer.toString(runwayNumber);

        try {
            if (!String.valueOf(approachDirection).trim().isEmpty()) {
                temp += " " + aviaRVRWthItemsHandlers
                        .getValueAtIndex(aviaRVRWthItemsHandlers
                                .getIndexOf(String.valueOf(approachDirection)));
            }

            if (decodedReportableModifier != null
                    && !decodedReportableModifier.isEmpty()) {
                temp += " " + decodedReportableModifier;
            }

            if (highestReportable > 0) {
                temp += " " + lowestReportable;
                temp += Configs.getInstance().getString("LOC_TIME_DECODED_TO")
                        + " " + highestReportable
                        + " " + Configs.getInstance().getString("MSRMNT_DECODED_FEET") + ".";
            } else {
                temp += " " + lowestReportable + " "
                        + Configs.getInstance().getString("MSRMNT_DECODED_FEET") + ".";
            }

            if (isContaminationCondClrd) {
                temp += " " + Configs.getInstance()
                        .getString("RVR_DECODED_RUNWAY_CTMNRN_COND_CSD_TO_EXIST");
            }

            if (!String.valueOf(reportableTrend).trim().isEmpty()) {
                temp += " " + aviaRVRWthItemsHandlers
                        .getValueAtIndex(aviaRVRWthItemsHandlers
                                .getIndexOf(String.valueOf(reportableTrend)));
            }
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "getNaturalLanguageString: " + UNABLE_PARSE_VALUE + " " + err;
            LOGGER.error(errMsg);
            throw new UtilsException("getNaturalLanguageString: ", err);
        }
        return temp;
    }
}
