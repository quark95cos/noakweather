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

import noakweather.utils.Configs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class representing wind direction information
 *
 * Author: quark95cos Since: Copyright(c) 2022
 */
public class WindDir {

    public static final Logger LOGGER
            = LogManager.getLogger(WindDir.class.getName());

    public WindDir() {
    }

    /**
     * Get formatted wind direction From wind direction in degrees, determine
     * compass direction as a string (e.g NW)
     *
     * @param degrees
     * @return wind direction compass
     */
    public static String getFormattedWindDir(Integer degrees) {
        String direction = Configs.getInstance().getString("WIND_DECODED_DIR_UNKNOWN");

        LOGGER.debug("Input degrees: " + degrees);

        if (degrees >= 347.5 || degrees < 12.5) { //0
            direction = Configs.getInstance().getString("WIND_DIR_NORTH");
        } else if (degrees >= 12.5 && degrees < 32.5) { //45
            direction = Configs.getInstance().getString("WIND_DIR_NORTH_NORTH_EAST");
        } else if (degrees >= 32.5 && degrees < 55) { //45
            direction = Configs.getInstance().getString("WIND_DIR_NORTH_EAST");
        } else if (degrees >= 55 && degrees < 77.5) { //45
            direction = Configs.getInstance().getString("WIND_DIR_EAST_NORTH_EAST");
        } else if (degrees >= 77.5 && degrees < 100) { //90
            direction = Configs.getInstance().getString("WIND_DIR_EAST");
        } else if (degrees >= 100 && degrees < 122.5) { //90
            direction = Configs.getInstance().getString("WIND_DIR_EAST_SOUTH_EAST");
        } else if (degrees >= 122.5 && degrees < 145) { //135
            direction = Configs.getInstance().getString("WIND_DIR_SOUTH_EAST");
        } else if (degrees >= 145 && degrees < 167.5) { //135
            direction = Configs.getInstance().getString("WIND_DIR_SOUTH_SOUTH_EAST");
        } else if (degrees >= 167.5 && degrees < 190) { //180
            direction = Configs.getInstance().getString("WIND_DIR_SOUTH");
        } else if (degrees >= 190 && degrees < 212.5) { //180
            direction = Configs.getInstance().getString("WIND_DIR_SOUTH_SOUTH_WEST");
        } else if (degrees >= 212.5 && degrees < 235) { //225
            direction = Configs.getInstance().getString("WIND_DIR_SOUTH_WEST");
        } else if (degrees >= 235 && degrees < 257.5) { //225
            direction = Configs.getInstance().getString("WIND_DIR_WEST_SOUTH_WEST");
        } else if (degrees >= 257.5 && degrees < 280) { //270
            direction = Configs.getInstance().getString("WIND_DIR_WEST");
        } else if (degrees >= 280 && degrees < 302.5) { //315
            direction = Configs.getInstance().getString("WIND_DIR_WEST_NORTH_WEST");
        } else if (degrees >= 302.5 && degrees < 325) { //315
            direction = Configs.getInstance().getString("WIND_DIR_NORTH_WEST");
        } else if (degrees >= 325 && degrees < 347.5) { //315
            direction = Configs.getInstance().getString("WIND_DIR_NORTH_NORTH_WEST");
        } else {
            // Should never happen
            LOGGER.debug(Configs.getInstance().getString("WIND_DECODED_NOT_DETERMINED"));
        }

        return direction;
    }

}
