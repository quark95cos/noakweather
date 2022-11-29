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
package noakweather.utils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class representing the web utilities
 *
 * Author: quark95cos Since: Copyright(c) 2022
 */
public class UtilsWeb {

    /**
     * Parses the provided string to a valid web address, that can be used to
     * fetch information
     *
     * @param urlString specification for the URL
     * @return a new URL instance
     * @throws UtilsException
     */
    public static URL getUrl(String urlString) throws UtilsException {
        URL url = null;

        try {
            url = new URL(urlString);
        } catch (MalformedURLException uRLExc) {
            // If urlString is null or length is 0 then catch MalformedURLException
            throw new UtilsException("URL not provided", uRLExc);
        }

        return url;
    }
}
