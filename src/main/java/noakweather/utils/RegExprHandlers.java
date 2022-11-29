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

import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;

/**
 * Class representing the regular expression handlers
 *
 * Author: quark95cos Since: Copyright(c) 2022
 */
public final class RegExprHandlers {

    private static final Logger LOGGER
            = LogManager.getLogger(RegExprHandlers.class.getName());

    /**
     * Parse the main information
     *
     * @return mainHandlers
     */
    public static final IndexedLinkedHashMap<Pattern, Pair<String, Boolean>> setMainHandlers() {
        IndexedLinkedHashMap<Pattern, Pair<String, Boolean>> mainHandlers;
        mainHandlers = new IndexedLinkedHashMap<>();
        mainHandlers.put(RegExprConst.MONTH_DAY_YEAR_PATTERN,
                Pair.with("mnthdayyr", false));
        mainHandlers.put(RegExprConst.STATION_DAY_TIME_VALTMPER_PATTERN,
                Pair.with("station", false));
        mainHandlers.put(RegExprConst.REPORT_MODIFIER_PATTERN,
                Pair.with("reportmodifier", false));
        mainHandlers.put(RegExprConst.WIND_PATTERN,
                Pair.with("wind", false));
        mainHandlers.put(RegExprConst.VISIBILITY_PATTERN,
                Pair.with("visibility", false));
        mainHandlers.put(RegExprConst.RUNWAY_PATTERN,
                Pair.with("runway", true));
        mainHandlers.put(RegExprConst.PRESENT_WEATHER_PATTERN,
                Pair.with("presentweather", true));
        mainHandlers.put(RegExprConst.SKY_CONDITION_PATTERN,
                Pair.with("skycondition", true));
        mainHandlers.put(RegExprConst.TEMP_DEWPOINT_PATTERN,
                Pair.with("tempdewpoint", false));
        mainHandlers.put(RegExprConst.ALTIMETER_PATTERN,
                Pair.with("altimeter", false));
        mainHandlers.put(RegExprConst.NO_SIG_CHANGE_PATTERN,
                Pair.with("nosigchng", false));

        LOGGER.debug("\n");
        LOGGER.debug(Configs.getInstance().getString("REG_EXPR_DECODED_MAIN_HANDLERS"));
        mainHandlers.forEach((k, v) -> LOGGER.debug((k + ":" + v)));
        LOGGER.debug(Configs.getInstance().getString("REG_EXPR_DECODED_MAIN_HANDLERS") + "\n");

        return mainHandlers;
    }

    /**
     * Parse the remarks information
     *
     * @return remarkHandlers
     */
    public static final IndexedLinkedHashMap<Pattern, Pair<String, Boolean>> setRemarksHandlers() {
        IndexedLinkedHashMap<Pattern, Pair<String, Boolean>> remarkHandlers;
        remarkHandlers = new IndexedLinkedHashMap<>();
        remarkHandlers.put(RegExprConst.PRES_RF_RAPDLY_PATTERN,
                Pair.with("presrisfal", false));
        remarkHandlers.put(RegExprConst.TRN_FC_WSP_PATTERN,
                Pair.with("trnfcwsp", false));
        remarkHandlers.put(RegExprConst.AUTO_PATTERN,
                Pair.with("auto", false));
        remarkHandlers.put(RegExprConst.BEGIN_END_WEATHER_PATTERN,
                Pair.with("beginendwthr", true));
        remarkHandlers.put(RegExprConst.ICING_PATTERN,
                Pair.with("icing", false));
        remarkHandlers.put(RegExprConst.PEAK_WIND_PATTERN,
                Pair.with("peakwind", false));
        remarkHandlers.put(RegExprConst.WIND_SHIFT_PATTERN,
                Pair.with("windshift", false));
        remarkHandlers.put(RegExprConst.LIGHTNING_PATTERN,
                Pair.with("lightning", true));
        remarkHandlers.put(RegExprConst.SEALVL_PRESS_PATTERN,
                Pair.with("sealvlpress", false));
        remarkHandlers.put(RegExprConst.TEMP_6HR_MAX_MIN_PATTERN,
                Pair.with("sixhrmaxmintemp", true));
        remarkHandlers.put(RegExprConst.PRECIP_1HR_PATTERN,
                Pair.with("precip1hr", false));
        remarkHandlers.put(RegExprConst.TEMP_1HR_PATTERN,
                Pair.with("temp1hr", false));
        remarkHandlers.put(RegExprConst.TEMP_24HR_PATTERN,
                Pair.with("temp24hr", false));
        remarkHandlers.put(RegExprConst.PRESS_3HR_PATTERN,
                Pair.with("press3hr", false));
        remarkHandlers.put(RegExprConst.PRECIP_3HR_24HR_PATTERN,
                Pair.with("precip3hr24hr", true));
        remarkHandlers.put(RegExprConst.DENSITY_ALTITUDE_PATTERN,
                Pair.with("denalt", false));
        remarkHandlers.put(RegExprConst.CLOUD_OKTA_PATTERN,
                Pair.with("cloudokta", true));
        remarkHandlers.put(RegExprConst.LAST_OBS_PATTERN,
                Pair.with("lastobs", false));
        remarkHandlers.put(RegExprConst.PRESS_Q_PATTERN,
                Pair.with("pressqfn", false));
        remarkHandlers.put(RegExprConst.AUTOMATED_MAINTENANCE_PATTERN,
                Pair.with("automaint", true));
        remarkHandlers.put(RegExprConst.TWR_SFC_VIS_PATTERN,
                Pair.with("twrsfcvis", false));
        remarkHandlers.put(RegExprConst.VPV_SV_VSL_PATTERN,
                Pair.with("vpvsvvsl", true));
        remarkHandlers.put(RegExprConst.TS_CLD_LOC_PATTERN,
                Pair.with("tsloc", true));
        remarkHandlers.put(RegExprConst.SNOW_ON_GRND_PATTERN,
                Pair.with("snwongrnd", true));
        remarkHandlers.put(RegExprConst.NXT_FCST_BY_PATTERN,
                Pair.with("nxtfcstby", false));
        // Wind situations can occur in remarks in certain cases
        remarkHandlers.put(RegExprConst.WIND_PATTERN,
                Pair.with("windre", false));
        remarkHandlers.put(RegExprConst.UNPARSED_PATTERN,
                Pair.with("unparsed", false));

        LOGGER.debug("\n");
        LOGGER.debug(Configs.getInstance().getString("REG_EXPR_DECODED_REMARKS_HANDLERS"));
        remarkHandlers.forEach((k, v) -> LOGGER.debug((k + ":" + v)));
        LOGGER.debug(Configs.getInstance().getString("REG_EXPR_DECODED_REMARKS_HANDLERS") + "\n");

        return remarkHandlers;
    }

    /**
     * Parse the group information
     *
     * @return groupHandlers
     */
    public static final IndexedLinkedHashMap<Pattern, Pair<String, Boolean>> setGroupHandlers() {
        IndexedLinkedHashMap<Pattern, Pair<String, Boolean>> groupHandlers;
        groupHandlers = new IndexedLinkedHashMap<>();
        groupHandlers.put(RegExprConst.VALTMPER_PATTERN,
                Pair.with("valtmper", false));
        groupHandlers.put(RegExprConst.WIND_PATTERN,
                Pair.with("wind", false));
        groupHandlers.put(RegExprConst.VISIBILITY_PATTERN,
                Pair.with("visibility", false));
        groupHandlers.put(RegExprConst.RUNWAY_PATTERN,
                Pair.with("runway", true));
        groupHandlers.put(RegExprConst.PRESENT_WEATHER_PATTERN,
                Pair.with("presentweather", true));
        groupHandlers.put(RegExprConst.SKY_CONDITION_PATTERN,
                Pair.with("skycondition", true));
        groupHandlers.put(RegExprConst.TEMP_DEWPOINT_PATTERN,
                Pair.with("tempdewpoint", false));
        groupHandlers.put(RegExprConst.ALTIMETER_PATTERN,
                Pair.with("altimeter", false));
        groupHandlers.put(RegExprConst.NO_SIG_CHANGE_PATTERN,
                Pair.with("nosigchng", false));
        groupHandlers.put(RegExprConst.UNPARSED_PATTERN,
                Pair.with("unparsed", false));

        LOGGER.debug("\n");
        LOGGER.debug(Configs.getInstance().getString("REG_EXPR_DECODED_GROUPS_HANDLERS"));
        groupHandlers.forEach((k, v) -> LOGGER.debug((k + ":" + v)));
        LOGGER.debug(Configs.getInstance().getString("REG_EXPR_DECODED_GROUPS_HANDLERS") + "\n");

        return groupHandlers;
    }
}
