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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;

/**
 * Class representing the handling of certain handlers
 *
 * Author: quark95cos Since: Copyright(c) 2022
 */
public final class WthItemHandlers {

    private static final Logger LOGGER
            = LogManager.getLogger(WthItemHandlers.class.getName());

    /**
     * Parse the runway visual range weather information
     *
     * @return IndexedLinkedHashMap
     */
    public static final IndexedLinkedHashMap<String, String> setRVRWthItemsHandlers() {
        IndexedLinkedHashMap<String, String> rvrWthItemsHandlers;
        rvrWthItemsHandlers = new IndexedLinkedHashMap<>();

        rvrWthItemsHandlers.put(Configs.getInstance().getString("RVR_REPORTABLE_ABOVE"),
                Configs.getInstance().getString("LOC_TIME_DECODED_GREATER_THAN"));
        rvrWthItemsHandlers.put(Configs.getInstance().getString("RVR_REPORTABLE_BELOW"),
                Configs.getInstance().getString("LOC_TIME_DECODED_LESS_THAN"));
        rvrWthItemsHandlers.put(Configs.getInstance().getString("MISC_VALUE_UL"),
                Configs.getInstance().getString("MISC_DECODED_VALUE_L"));
        rvrWthItemsHandlers.put(Configs.getInstance().getString("MISC_VALUE_UR"),
                Configs.getInstance().getString("MISC_DECODED_VALUE_R"));
        rvrWthItemsHandlers.put(Configs.getInstance().getString("MISC_VALUE_UC"),
                Configs.getInstance().getString("MISC_DECODED_VALUE_C"));
        rvrWthItemsHandlers.put(Configs.getInstance().getString("MISC_VALUE_UU"),
                Configs.getInstance().getString("MISC_DECODED_VALUE_U"));
        rvrWthItemsHandlers.put(Configs.getInstance().getString("MISC_VALUE_UD"),
                Configs.getInstance().getString("MISC_DECODED_VALUE_D"));
        rvrWthItemsHandlers.put(Configs.getInstance().getString("MISC_VALUE_UN"),
                Configs.getInstance().getString("MISC_DECODED_VALUE_N"));

        LOGGER.debug("\n");
        LOGGER.debug("RVR Weather Items Handler");
        rvrWthItemsHandlers.forEach((k, v) -> LOGGER.debug((k + ":" + v)));
        LOGGER.debug("RVR Weather Items Handler" + "\n");

        return rvrWthItemsHandlers;
    }

    /**
     * Parse the sky condition weather information
     *
     * @return IndexedLinkedHashMap
     */
    public static final IndexedLinkedHashMap<String, String> setSkyCondWthItemsHandlers() {
        IndexedLinkedHashMap<String, String> skyCondWthItemsHandlers;
        skyCondWthItemsHandlers = new IndexedLinkedHashMap<>();

        skyCondWthItemsHandlers.put(Configs.getInstance().getString("SKY_COND_VERTICAL_VISIBILITY"),
                Configs.getInstance().getString("SKY_COND_DECODED_VERTICAL_VISIBILITY"));
        skyCondWthItemsHandlers.put(Configs.getInstance().getString("SKY_COND_SKY_CLEAR"),
                Configs.getInstance().getString("SKY_COND_DECODED_SKY_CLEAR"));
        skyCondWthItemsHandlers.put(Configs.getInstance().getString("SKY_COND_CLEAR"),
                Configs.getInstance().getString("SKY_COND_DECODED_CLEAR"));
        skyCondWthItemsHandlers.put(Configs.getInstance().getString("SKY_COND_FEW"),
                Configs.getInstance().getString("SKY_COND_DECODED_FEW"));
        skyCondWthItemsHandlers.put(Configs.getInstance().getString("SKY_COND_SCATTERED"),
                Configs.getInstance().getString("SKY_COND_DECODED_SCATTERED"));
        skyCondWthItemsHandlers.put(Configs.getInstance().getString("SKY_COND_BROKEN"),
                Configs.getInstance().getString("SKY_COND_DECODED_BROKEN"));
        skyCondWthItemsHandlers.put(Configs.getInstance().getString("SKY_COND_OVERCAST"),
                Configs.getInstance().getString("SKY_COND_DECODED_OVERCAST"));
        skyCondWthItemsHandlers.put(Configs.getInstance().getString("SKY_COND_NO_CLOUDS_DETECTED"),
                Configs.getInstance().getString("SKY_COND_DECODED_NO_CLOUDS_DETECTED"));
        skyCondWthItemsHandlers.put(Configs.getInstance().getString("SKY_COND_NO_SIGNIFICANT_CLOUDS"),
                Configs.getInstance().getString("SKY_COND_DECODED_NO_SIGNIFICANT_CLOUDS"));
        skyCondWthItemsHandlers.put(Configs.getInstance().getString("CLOUD_CUMULONIMBUS"),
                Configs.getInstance().getString("CLOUD_DECODED_CUMULONIMBUS"));
        skyCondWthItemsHandlers.put(Configs.getInstance().getString("CLOUD_TOWERING_CUMULUS"),
                Configs.getInstance().getString("CLOUD_DECODED_TOWERING_CUMULUS"));

        LOGGER.debug("\n");
        LOGGER.debug("Sky Condition Weather Items Handler");
        skyCondWthItemsHandlers.forEach((k, v) -> LOGGER.debug((k + ":" + v)));
        LOGGER.debug("Sky Condition Weather Items Handler" + "\n");

        return skyCondWthItemsHandlers;
    }

    /**
     * Parse the weather condition information
     *
     * @return IndexedLinkedHashMap
     */
    public static final IndexedLinkedHashMap<String, String> setWeathCondWthItemsHandlers() {
        IndexedLinkedHashMap<String, String> weathCondWthItemsHandlers;
        weathCondWthItemsHandlers = new IndexedLinkedHashMap<>();

        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_LIGHT"),
                Configs.getInstance().getString("WEATHER_DECODED_LIGHT"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_HEAVY"),
                Configs.getInstance().getString("WEATHER_DECODED_HEAVY"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_SHALLOW"),
                Configs.getInstance().getString("WEATHER_DECODED_SHALLOW"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_PARTIAL"),
                Configs.getInstance().getString("WEATHER_DECODED_PARTIAL"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_PATCHES"),
                Configs.getInstance().getString("WEATHER_DECODED_PATCHES"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_LOW_DRIFTING"),
                Configs.getInstance().getString("WEATHER_DECODED_LOW_DRIFTING"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_BLOWING"),
                Configs.getInstance().getString("WEATHER_DECODED_BLOWING"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_SHOWERS"),
                Configs.getInstance().getString("WEATHER_DECODED_SHOWERS"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_THUNDERSTORMS"),
                Configs.getInstance().getString("WEATHER_DECODED_THUNDERSTORMS"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_FREEZING"),
                Configs.getInstance().getString("WEATHER_DECODED_FREEZING"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_DRIZZLE"),
                Configs.getInstance().getString("WEATHER_DECODED_DRIZZLE"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_RAIN"),
                Configs.getInstance().getString("WEATHER_DECODED_RAIN"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_SNOW"),
                Configs.getInstance().getString("WEATHER_DECODED_SNOW"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_SNOW_GRAINS"),
                Configs.getInstance().getString("WEATHER_DECODED_SNOW_GRAINS"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_ICE_CRYSTALS"),
                Configs.getInstance().getString("WEATHER_DECODED_ICE_CRYSTALS"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_ICE_PELLETS"),
                Configs.getInstance().getString("WEATHER_DECODED_ICE_PELLETS"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_HAIL"),
                Configs.getInstance().getString("WEATHER_DECODED_HAIL"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_SMALL_HAIL"),
                Configs.getInstance().getString("WEATHER_DECODED_SMALL_HAIL"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_UNKNOWN_PRECIPITATION"),
                Configs.getInstance().getString("WEATHER_DECODED_UNKNOWN_PRECIP"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_THUNDERSTORMS"),
                Configs.getInstance().getString("WEATHER_DECODED_THUNDERSTORMS"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_DUST_SAND_WHIRLS"),
                Configs.getInstance().getString("WEATHER_DECODED_DUST_SAND_WHIRLS"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_SQUALLS"),
                Configs.getInstance().getString("WEATHER_DECODED_SQUALLS"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_FUNNEL_CLOUD"),
                Configs.getInstance().getString("WEATHER_DECODED_FUNNEL_CLOUD"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_SAND_STORM"),
                Configs.getInstance().getString("WEATHER_DECODED_SAND_STORM"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_DUST_STORM"),
                Configs.getInstance().getString("WEATHER_DECODED_DUST_STORM"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_MIST"),
                Configs.getInstance().getString("WEATHER_DECODED_MIST"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_FOG"),
                Configs.getInstance().getString("WEATHER_DECODED_FOG"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_SMOKE"),
                Configs.getInstance().getString("WEATHER_DECODED_SMOKE"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_VOLCANIC_ASH"),
                Configs.getInstance().getString("WEATHER_DECODED_VOLCANIC_ASH"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_WIDESPREAD_DUST"),
                Configs.getInstance().getString("WEATHER_DECODED_WIDESPREAD_DUST"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_SAND"),
                Configs.getInstance().getString("WEATHER_DECODED_SAND"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_HAZE"),
                Configs.getInstance().getString("WEATHER_DECODED_HAZE"));
        weathCondWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_SPRAY"),
                Configs.getInstance().getString("WEATHER_DECODED_SPRAY"));

        LOGGER.debug("\n");
        LOGGER.debug("Weather Condition Items Handler");
        weathCondWthItemsHandlers.forEach((k, v) -> LOGGER.debug((k + ":" + v)));
        LOGGER.debug("Weather Condition Items Handler" + "\n");

        return weathCondWthItemsHandlers;
    }

    /**
     * Parse the remarks weather information
     *
     * @return IndexedLinkedHashMap
     */
    public static final IndexedLinkedHashMap<String, String> setRemarksWthItemsHandlers() {
        IndexedLinkedHashMap<String, String> remarkWthItemsHandlers;
        remarkWthItemsHandlers = new IndexedLinkedHashMap<>();

        remarkWthItemsHandlers.put(Configs.getInstance().getString("PRESS_FALLING"),
                Configs.getInstance().getString("EXTENDED_DECODED_PRESSURE_FALLING_RAPIDLY"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("PRESS_RISING"),
                Configs.getInstance().getString("EXTENDED_DECODED_PRESSURE_RISING_RAPIDLY"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("EXTENDED_TORNADO"),
                Configs.getInstance().getString("EXTENDED_DECODED_TORNADO"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("EXTENDED_FUNNEL_CLOUD"),
                Configs.getInstance().getString("EXTENDED_DECODED_FUNNEL_CLOUD"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("EXTENDED_WATERSPOUT"),
                Configs.getInstance().getString("EXTENDED_DECODED_WATERSPOUT"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("LOC_TIME_BEGIN"),
                Configs.getInstance().getString("LOC_TIME_DECODED_BEGIN"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("LOC_TIME_END"),
                Configs.getInstance().getString("LOC_TIME_DECODED_ENDING"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("EXTENDED_ICING"),
                Configs.getInstance().getString("EXTENDED_DECODED_ICING"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_IN_CLD"),
                Configs.getInstance().getString("WEATHER_DECODED_IN_CLD"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_IN_PRECIPITATION"),
                Configs.getInstance().getString("WEATHER_DECODED_IN_PRECIPITATION"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("EXTENDED_AUTO_A01"),
                Configs.getInstance().getString("EXTENDED_DECODED_AUTO_AO1"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("EXTENDED_AUTO_AO1"),
                Configs.getInstance().getString("EXTENDED_DECODED_AUTO_AO1"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("EXTENDED_AUTO_A02"),
                Configs.getInstance().getString("EXTENDED_DECODED_AUTO_AO2"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("EXTENDED_AUTO_AO2"),
                Configs.getInstance().getString("EXTENDED_DECODED_AUTO_AO2"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("LOC_TIME_OHD"),
                Configs.getInstance().getString("LOC_TIME_DECODED_OHD"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("LOC_TIME_VC"),
                Configs.getInstance().getString("LOC_TIME_DECODED_VC"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("LOC_TIME_DISTANT"),
                Configs.getInstance().getString("LOC_TIME_DECODED_DISTANT"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_THUNDERSTORMS"),
                Configs.getInstance().getString("WEATHER_DECODED_THUNDERSTORMS"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("CLOUD_CUMULONIMBUS"),
                Configs.getInstance().getString("CLOUD_DECODED_CUMULONIMBUS"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("CLOUD_TOWERING_CUMULUS"),
                Configs.getInstance().getString("CLOUD_DECODED_TOWERING_CUMULUS"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("CLOUD_ALTOCUMULUS_CASTELLANUS"),
                Configs.getInstance().getString("CLOUD_DECODED_ALTOCUMULUS_CASTELLANUS"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("CLOUD_CUMULONIMBUS_MAMMATUS"),
                Configs.getInstance().getString("CLOUD_DECODED_CUMULONIMBUS_MAMMATUS"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_VIRGA"),
                Configs.getInstance().getString("WEATHER_DECODED_VIRGA"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("LOC_TIME_OHD"),
                Configs.getInstance().getString("LOC_TIME_DECODED_OHD"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("LOC_TIME_VC"),
                Configs.getInstance().getString("LOC_TIME_DECODED_VC"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("LOC_TIME_DECODED_DISTANT"),
                Configs.getInstance().getString("LOC_TIME_DECODED_DISTANT"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("LOC_TIME_DISIPATED"),
                Configs.getInstance().getString("LOC_TIME_DECODED_DISIPATED"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("LOC_TIME_TOP"),
                Configs.getInstance().getString("LOC_TIME_DECODED_TOP"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("WEATHER_TRACE"),
                Configs.getInstance().getString("WEATHER_DECODED_TRACE"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("EXTENDED_6_HOUR_PRECIPITATION_AMOUNT"),
                Configs.getInstance().getString("EXTENDED_DECODED_6_HOUR_PRECIPITATION"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("EXTENDED_24_HOUR_PRECIPITATION_AMOUNT"),
                Configs.getInstance().getString("EXTENDED_DECODED_24_HOUR_PRECIPITATION"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("EXTENDED_PRESSURE_TENDENCY_INCR_DEACR"),
                Configs.getInstance().getString("EXTENDED_DECODED_PRESSURE_TENDENCY_INCR_DEACR"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("EXTENDED_PRESSURE_TENDENCY_INCR_STEADY_OR_INCR_INCR_SLOWLY"),
                Configs.getInstance().getString("EXTENDED_DECODED_PRESSURE_TENDENCY_INCR_STEADY_OR_INCR_INCR_SLOWLY"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("EXTENDED_PRESSURE_TENDENCY_INCR_STEADY_UNSTEADY"),
                Configs.getInstance().getString("EXTENDED_DECODED_PRESSURE_TENDENCY_INCR_STEADY_UNSTEADY"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("EXTENDED_PRESSURE_TENDENCY_DEACR_OR_STEADY_INCR"),
                Configs.getInstance().getString("EXTENDED_DECODED_PRESSURE_TENDENCY_DEACR_OR_STEADY_INCR"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("EXTENDED_PRESSURE_TENDENCY_STEADY"),
                Configs.getInstance().getString("EXTENDED_DECODED_PRESSURE_TENDENCY_STEADY"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("EXTENDED_PRESSURE_TENDENCY_DEACR_INCR"),
                Configs.getInstance().getString("EXTENDED_DECODED_PRESSURE_TENDENCY_DEACR_INCR"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("EXTENDED_PRESSURE_TENDENCY_DEACR_STEADY_OR_DEACR_DEACR_SLOWLY"),
                Configs.getInstance().getString("EXTENDED_DECODED_PRESSURE_TENDENCY_DEACR_STEADY_OR_DEACR_DEACR_SLOWLY"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("EXTENDED_PRESSURE_TENDENCY_DEACR_STEADY_UNSTEADY"),
                Configs.getInstance().getString("EXTENDED_DECODED_PRESSURE_TENDENCY_DEACR_STEADY_UNSTEADY"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("EXTENDED_PRESSURE_TENDENCY_STEADY_INCR_DEACR"),
                Configs.getInstance().getString("EXTENDED_DECODED_PRESSURE_TENDENCY_STEADY_INCR_DEACR"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("CLOUD_OKTA_1"),
                Configs.getInstance().getString("SKY_COND_DECODED_FEW"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("CLOUD_OKTA_2"),
                Configs.getInstance().getString("SKY_COND_DECODED_FEW"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("CLOUD_OKTA_3"),
                Configs.getInstance().getString("SKY_COND_DECODED_SCATTERED"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("CLOUD_OKTA_4"),
                Configs.getInstance().getString("SKY_COND_DECODED_SCATTERED"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("CLOUD_OKTA_5"),
                Configs.getInstance().getString("SKY_COND_DECODED_BROKEN"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("CLOUD_OKTA_6"),
                Configs.getInstance().getString("SKY_COND_DECODED_BROKEN"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("CLOUD_OKTA_7"),
                Configs.getInstance().getString("SKY_COND_DECODED_BROKEN"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("CLOUD_OKTA_8"),
                Configs.getInstance().getString("SKY_COND_DECODED_OVERCAST"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("CLOUD_CUMULUS"),
                Configs.getInstance().getString("CLOUD_DECODED_CUMULUS"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("CLOUD_CUMULONIMBUS"),
                Configs.getInstance().getString("CLOUD_DECODED_CUMULONIMBUS"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("CLOUD_CUMULUS_FRACTUS"),
                Configs.getInstance().getString("CLOUD_DECODED_CUMULUS_FRACTUS"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("CLOUD_STRATUS"),
                Configs.getInstance().getString("CLOUD_DECODED_STRATUS"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("CLOUD_STRATUS_FRACTUS"),
                Configs.getInstance().getString("CLOUD_DECODED_STRATUS_FRACTUS"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("CLOUD_STRATOCUMULUS"),
                Configs.getInstance().getString("CLOUD_DECODED_STRATOCUMULUS"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("CLOUD_NIMBOSTRATUS"),
                Configs.getInstance().getString("CLOUD_DECODED_NIMBOSTRATUS"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("CLOUD_ALTOSTRATUS"),
                Configs.getInstance().getString("CLOUD_DECODED_ALTOSTRATUS"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("CLOUD_ALTOCUMULUS"),
                Configs.getInstance().getString("CLOUD_DECODED_ALTOCUMULUS"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("CLOUD_CIRROSTRATUS"),
                Configs.getInstance().getString("CLOUD_DECODED_CIRROSTRATUS"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("CLOUD_CIRROCUMULUS"),
                Configs.getInstance().getString("CLOUD_DECODED_CIRROCUMULUS"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("CLOUD_CIRRUS"),
                Configs.getInstance().getString("CLOUD_DECODED_CIRRUS"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("LOC_TIME_MOVG"),
                Configs.getInstance().getString("LOC_TIME_DECODED_MOVNG"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("EXTENDED_SENSOR_STATUS_INDICATOR_RVRNO"),
                Configs.getInstance().getString("EXTENDED_DECODED_SENSOR_STATUS_INDICATOR_RVRNO"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("EXTENDED_SENSOR_STATUS_INDICATOR_PWINO"),
                Configs.getInstance().getString("EXTENDED_DECODED_SENSOR_STATUS_INDICATOR_PWINO"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("EXTENDED_SENSOR_STATUS_INDICATOR_PNO"),
                Configs.getInstance().getString("EXTENDED_DECODED_SENSOR_STATUS_INDICATOR_PNO"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("EXTENDED_SENSOR_STATUS_INDICATOR_FZRANO"),
                Configs.getInstance().getString("EXTENDED_DECODED_SENSOR_STATUS_INDICATOR_FZRANO"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("EXTENDED_SENSOR_STATUS_INDICATOR_TSNO"),
                Configs.getInstance().getString("EXTENDED_DECODED_SENSOR_STATUS_INDICATOR_TSNO"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("EXTENDED_SENSOR_STATUS_INDICATOR_VISNO"),
                Configs.getInstance().getString("EXTENDED_DECODED_SENSOR_STATUS_INDICATOR_VISNO"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("EXTENDED_SENSOR_STATUS_INDICATOR_CHINO"),
                Configs.getInstance().getString("EXTENDED_DECODED_SENSOR_STATUS_INDICATOR_CHINO"));
        remarkWthItemsHandlers.put(Configs.getInstance().getString("EXTENDED_MAINTENANCE_CHECK_INDICATOR"),
                Configs.getInstance().getString("EXTENDED_DECODED_MAINTENANCE_CHECK_INDICATOR"));

        LOGGER.debug("\n");
        LOGGER.debug("Remarks Weather Items Handler");
        remarkWthItemsHandlers.forEach((k, v) -> LOGGER.debug((k + ":" + v)));
        LOGGER.debug("Remarks Weather Items Handler" + "\n");

        return remarkWthItemsHandlers;
    }

    /**
     * Parse the remarks weather alternate information
     *
     * @return IndexedLinkedHashMap
     */
    public static final IndexedLinkedHashMap<String, Pair<String, String>> setRemarksWthAltItemsHandlers() {
        IndexedLinkedHashMap<String, Pair<String, String>> remarkWthAltItemsHandlers;
        remarkWthAltItemsHandlers = new IndexedLinkedHashMap<>();

        remarkWthAltItemsHandlers.put(Configs.getInstance().getString("LOC_TIME_OCCASIONAL"),
                Pair.with(Configs.getInstance().getString("LOC_TIME_DECODED_OCCASIONAL"),
                        Configs.getInstance().getString("EXTENDED_DECODED_LTNG_FREQ_LESS_1")));
        remarkWthAltItemsHandlers.put(Configs.getInstance().getString("LOC_TIME_FREQUENT"),
                Pair.with(Configs.getInstance().getString("LOC_TIME_DECODED_FREQUENT"),
                        Configs.getInstance().getString("EXTENDED_DECODED_LTNG_FREQ_1_TO_6")));
        remarkWthAltItemsHandlers.put(Configs.getInstance().getString("LOC_TIME_CONTINUOUS"),
                Pair.with(Configs.getInstance().getString("LOC_TIME_DECODED_CONTINUOUS"),
                        Configs.getInstance().getString("EXTENDED_DECODED_LTNG_FREQ_MORE_6")));
        remarkWthAltItemsHandlers.put(Configs.getInstance().getString("WEATHER_IN_CLD"),
                Pair.with(Configs.getInstance().getString("WEATHER_DECODED_IN_CLD"),
                        Configs.getInstance().getString("WEATHER_DECODED_LIGHTNING_OBSERVED")));
        remarkWthAltItemsHandlers.put(Configs.getInstance().getString("WEATHER_CLD_TO_CLD"),
                Pair.with(Configs.getInstance().getString("WEATHER_DECODED_CLD_TO_CLD"),
                        Configs.getInstance().getString("WEATHER_DECODED_LIGHTNING_OBSERVED")));
        remarkWthAltItemsHandlers.put(Configs.getInstance().getString("WEATHER_CLD_GRND"),
                Pair.with(Configs.getInstance().getString("WEATHER_DECODED_CLD_GRND"),
                        Configs.getInstance().getString("WEATHER_DECODED_LIGHTNING_OBSERVED")));
        remarkWthAltItemsHandlers.put(Configs.getInstance().getString("WEATHER_CLD_TO_AIR"),
                Pair.with(Configs.getInstance().getString("WEATHER_DECODED_CLD_TO_AIR"),
                        Configs.getInstance().getString("WEATHER_DECODED_LIGHTNING_OBSERVED")));
        remarkWthAltItemsHandlers.put(Configs.getInstance().getString("WEATHER_CLD_TO_WATER"),
                Pair.with(Configs.getInstance().getString("WEATHER_DECODED_CLD_TO_WATER"),
                        Configs.getInstance().getString("WEATHER_DECODED_LIGHTNING_OBSERVED")));

        LOGGER.debug("\n");
        LOGGER.debug("Remarks Weather Alternate Items Handler");
        remarkWthAltItemsHandlers.forEach((k, v) -> LOGGER.debug((k + ":" + v)));
        LOGGER.debug("Remarks Weather Alternate Items Handler" + "\n");

        return remarkWthAltItemsHandlers;
    }
}
