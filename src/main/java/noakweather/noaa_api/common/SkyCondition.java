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
 * Class representing the sky conditions
 *
 * Author: quark95cos Since: Copyright(c) 2022
 */
public class SkyCondition {

    private int height;
    private String decodedContraction;
    private String decodedModifier;
    private final IndexedLinkedHashMap<String, String> aviaSkyCondWthItemsHandlers;

    private static final Logger LOGGER
            = LogManager.getLogger(SkyCondition.class.getName());

    /**
     * Constructor
     */
    public SkyCondition() {
        this.height = 0;
        this.decodedContraction = null;
        this.decodedModifier = null;
        this.aviaSkyCondWthItemsHandlers
                = WthItemHandlers.setSkyCondWthItemsHandlers();
    }

    /**
     * Set the sky condition information
     *
     * @param token
     * @throws noakweather.utils.UtilsException
     */
    public void setSkyConditionItems(Matcher token) throws UtilsException {
        try {
            LOGGER.debug("cover: #" + token.group("cover") + "#");
            LOGGER.debug("height: #" + token.group("height") + "#");
            LOGGER.debug("cloud: #" + token.group("cloud") + "#");

            setContraction(token.group("cover"));
            LOGGER.debug(Configs.getInstance().getString("SKY_COND_DECODED_CONTRACTION")
                    + " " + token.group("cover"));
            LOGGER.debug(aviaSkyCondWthItemsHandlers
                    .getValueAtIndex(aviaSkyCondWthItemsHandlers
                            .getIndexOf(token.group("cover"))) + " " + token.group("cover"));
            if (token.group("height") != null && !token.group("height").equals("")) {
                setHeight(Integer.parseInt(token.group("height")));
                LOGGER.debug(Configs.getInstance().getString("SKY_COND_DECODED_HEIGHT")
                        + " " + token.group("height"));
            }
            if (token.group("cloud") != null && !token.group("cloud").equals("")) {
                // we have a modifier
                setModifier(token.group("cloud"));
                LOGGER.debug(Configs.getInstance().getString("SKY_COND_DECODED_MODIFIER")
                        + " " + token.group("cloud"));
            }
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setSkyConditionItems: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedContraction = errMsg;
            LOGGER.error(errMsg);
            throw new UtilsException("setSkyConditionItems: ", err);
        }
    }

    /**
     * Set the sky contraction (FEW, SCT, etc.) information
     *
     * @param contraction the part of a METAR sky condition token which
     * represents a contraction for the sky condition (e.g. 'FEW', 'SCT')
     * @throws noakweather.utils.UtilsException
     */
    private void setContraction(String contraction) throws UtilsException {
        try {
            decodedContraction = aviaSkyCondWthItemsHandlers
                    .getValueAtIndex(aviaSkyCondWthItemsHandlers
                            .getIndexOf(contraction));
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setContraction: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedContraction = errMsg;
            LOGGER.error(errMsg);
            throw new UtilsException("setContraction: ", err);
        }
    }

    /**
     * Set the sky modifier information
     *
     * @param modifier the part of a METAR sky condition token which represents
     * a modifier used to specify if the sky condition is of a certain type
     * @throws noakweather.utils.UtilsException
     */
    private void setModifier(String modifier) throws UtilsException {
        try {
            decodedModifier = aviaSkyCondWthItemsHandlers
                    .getValueAtIndex(aviaSkyCondWthItemsHandlers
                            .getIndexOf(modifier));
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "setModifier: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedContraction = errMsg;
            LOGGER.error(errMsg);
            throw new UtilsException("setModifier: ", err);
        }
    }

    /**
     * Set the height of the sky condition
     *
     * @param height the part of a METAR sky condition token which represents
     * the height of the sky condition (in hundreds of feet)
     */
    public void setHeight(int height) {
        this.height = height * 100; // for hundreds of feet
    }

    /**
     * Get the part of a METAR sky condition token which represents the height
     * of the sky condition (in hundreds of feet)
     *
     * @return height
     */
    public int getHeight() {
        return height; // in hundreds of feet
    }

    /**
     * Get the decoded sky contraction (FEW, SCT, etc.) information
     *
     * @return decodedContraction
     */
    public String getDecodedContraction() {
        return decodedContraction;
    }

    /**
     * Get the decoded sky modifier information
     *
     * @return decodedModifier
     */
    public String getDecodedModifier() {
        return decodedModifier;
    }

    /**
     * Get the decoded sky contraction (FEW, SCT, etc.) information
     *
     * @param string
     */
    public void setDecodedContraction(String string) {
        decodedContraction = string;
    }

    /**
     * Get the decoded sky modifier information
     *
     * @param string
     */
    public void setDecodedModifier(String string) {
        decodedModifier = string;
    }

    /**
     * Get the natural language in human readable form This method will return a
     * string that represents this sky condition using natural language (as
     * opposed to METAR)
     *
     * @return a string that represents the sky condition in natural language
     * @throws noakweather.utils.UtilsException
     */
    public String getNaturalLanguageString() throws UtilsException {
        try {
            String temp = "";

            if (decodedContraction.equals(Configs.getInstance().getString("SKY_COND_DECODED_CLEAR"))
                    || decodedContraction.equals(Configs
                            .getInstance().getString("SKY_COND_DECODED_NO_CLOUDS_DETECTED"))
                    || decodedContraction.equals(Configs
                            .getInstance().getString("SKY_COND_DECODED_NO_SIGNIFICANT_CLOUDS"))) {
                return decodedContraction;
            } else {
                temp += decodedContraction;
            }

            if (decodedContraction
                    .equals(Configs.getInstance().getString("SKY_COND_DECODED_VERTICAL_VISIBILITY"))) {
                temp += " " + Configs.getInstance().getString("LOC_TIME_DECODED_OF");
            } else {
                temp += " " + Configs.getInstance().getString("LOC_TIME_DECODED_AT");
            }

            temp += " " + getHeight() + " "
                    + Configs.getInstance().getString("MSRMNT_DECODED_FEET");

            if (decodedModifier != null && !decodedModifier.isEmpty()) {
                temp += " (" + decodedModifier + ")";
            }

            return temp;
        } catch (IllegalArgumentException | IndexOutOfBoundsException | MissingResourceException err) {
            String errMsg = "getNaturalLanguageString: "
                    + Configs.getInstance().getString("MISC_UNABLE_PARSE_VALUE") + " " + err;
            decodedContraction = errMsg;
            LOGGER.error(errMsg);
            throw new UtilsException("getNaturalLanguageString: ", err);
        }
    }
}
