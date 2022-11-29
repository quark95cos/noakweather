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

import java.util.regex.Pattern;
import noakweather.utils.Configs;
import noakweather.utils.IndexedLinkedHashMap;
import noakweather.utils.UtilsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;

/**
 * Class representing the from group section of the report
 *
 * Author: quark95cos Since: Copyright(c) 2022
 */
public class FromGroup extends Group {

    private static final Logger LOGGER
            = LogManager.getLogger(FromGroup.class.getName());

    public FromGroup() {
        LOGGER.debug("in FromGroup constructor");
    }

    /**
     * Set the from group information and return the natural language in human
     * readable form
     *
     * @param daytime
     * @param token
     * @param monthString
     * @param yearString
     * @param groupWeathHandlers
     * @return index
     * @throws noakweather.utils.UtilsException
     */
    public String setFromGroupItems(String daytime, String token, String monthString, String yearString,
            IndexedLinkedHashMap<Pattern, Pair<String, Boolean>> groupWeathHandlers) throws UtilsException {

        LOGGER.debug("Inside setFromGroupItems daytime processing: #" + daytime + "#");
        LOGGER.debug("Inside setFromGroupItems token processing: #" + token + "#");
        LOGGER.debug("Inside setFromGroupItems monthString: #" + monthString + "#");
        LOGGER.debug("Inside setFromGroupItems yearString: #" + yearString + "#\n");

        setMonthString(monthString);
        setYearString(yearString);
        setValidFromDate(daytime);

        parseGroupHandlers(token, groupWeathHandlers);

        return getNaturalLanguageString(Configs.getInstance()
                .getString("EXTENDED_DECODED_FM"));
    }
}
