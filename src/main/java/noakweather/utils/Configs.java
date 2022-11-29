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

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Class representing the configuration information
 *
 * Author: quark95cos Since: Copyright(c) 2022
 */
public class Configs {

    /**
     * The singleton instance.
     *
     */
    private static final Configs INSTANCE = new Configs();

    /**
     * Name of the bundle.
     *
     */
    private static final String BUNDLE_NAME = "configs";

    /**
     * Bundle variable.
     *
     */
    private ResourceBundle fResourceBundle;

    /**
     * Private constructor.
     */
    private Configs() {
    }

    /**
     * Get INSTANCE
     *
     * @return the Configs instance.
     */
    public static Configs getInstance() {
        return INSTANCE;
    }

    /**
     * !!!!!!!!!!!!!!!!!!!!! NOT IN USE - NEED TO FINALIZE
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */
    private static class ConfigsHolder {

        private static final Configs INSTANCE = new Configs();
    }

    /**
     * Sets the locale of the bundle.
     *
     * @param locale the locale to set.
     */
    public void setLocale(final Locale locale) {
        Locale.setDefault(locale);
        ResourceBundle.clearCache();
        fResourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
    }

    /**
     * Get the translation of the configuration
     *
     * @param config the string to get
     * @return the translation of config
     */
    public String getString(final String config) {
        return fResourceBundle.getString(config);
    }

    /**
     * Get the translation of the configuration with the arguments
     *
     * @param config the translation to get
     * @param arguments the arguments to fill
     * @return the translation of config with the arguments
     */
    public String getString(final String config, final Object... arguments) {
        return MessageFormat.format(getString(config), arguments);
    }
}
