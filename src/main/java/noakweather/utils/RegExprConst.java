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

/**
 * Interface representing the regular expressions used for decoding Metar and
 * TAF data
 *
 * Author: quark95cos Since: Copyright(c) 2022
 */
public interface RegExprConst {
    // Weather string value for Remarks ('RMK')

    public static final String EXTENDED_REMARKS = "RMK";

    /**
     * Month, Day and Year
     */
    public static final Pattern TAF_STR_PATTERN = Pattern.compile(
            "^(?<type>TAF)\\s+"
    );

    /**
     * Month, Day and Year
     */
    public static final Pattern MONTH_DAY_YEAR_PATTERN = Pattern.compile(
            "^(?<year>\\d\\d\\d\\d)/(?<month>\\d\\d)/(?<day>\\d\\d) (?<time>\\d\\d:\\d\\d)?\\s+"
    );

    /**
     * Station, Day, Time and Valid Time Period
     */
    public static final Pattern STATION_DAY_TIME_VALTMPER_PATTERN = Pattern.compile(
            "^(?<station>[A-Z][A-Z0-9]{3}) (?<zday>\\d\\d)(?<zhour>\\d\\d)(?<zmin>\\d\\d)Z\\s?((?<bvaltime>\\d\\d\\d\\d)/(?<evaltime>\\d\\d\\d\\d))?\\s+"
    );

    /**
     * Report Modifier
     */
    public static final Pattern REPORT_MODIFIER_PATTERN = Pattern.compile(
            "^(?<mod>AMD|AUTO|FINO|NIL|TEST|CORR?|RTD|CC[A-G])\\s+"
    );

    /**
     * Wind, Wind Variability
     */
    public static final Pattern WIND_PATTERN = Pattern.compile(
            "^(?:(?<dir>\\d{3}|/{1,5}|MMM|VRB)(?<speed>\\d{2,3})?(?<inden>G(?<gust>\\d{2,3}))?(?<units>KTS?|LT|K|T|KMH|MPS))\\s?((?<varfrom>\\d\\d\\d)V(?<varto>\\d\\d\\d))?\\s+"
    );

    /**
     * Visibility
     */
    public static final Pattern VISIBILITY_PATTERN = Pattern.compile(
            "^(?<vis>(?<dist>(M|P)?\\d\\d\\d\\d|////)(?<dir>[NSEW][EW]?|NDV)?|(?<distu>(M|P)?(\\d+|\\d\\d?/\\d\\d|\\d+\\s+\\d/\\d))(?<units>SM|KM|M|U)|NDV|CAVOK)\\s+"
    );

    /**
     * RVR Runway visual range
     */
    public static final Pattern RUNWAY_PATTERN = Pattern.compile(
            "^(RVRNO|R(?<name>\\d\\d(?<inden>R|L|C)?))/(?<low>(M|P)?(?<lvalue>CLRD|\\d{1,4}))(V(?<high>(M|P)?\\d\\d\\d\\d))?/?/?/?/?(?<unit>\\d{2,4}|FT|N|D|U)\\s+"
    );

    /**
     * Present weather
     */
    public static final Pattern PRESENT_WEATHER_PATTERN = Pattern.compile(
            "^(?<int>(VC|-|\\+)*)(?<desc>(MI|PR|BC|DR|BL|SH|TS|FZ)+)?(?<prec>(DZ|RA|SN|SG|IC|PL|GR|GS|UP|/)*)(?<obsc>BR|FG|FU|VA|DU|SA|HZ|PY)?(?<other>PO|SQ|FC|SS|DS|NSW|/+)?(?<int2>[-+])?\\s+"
    );

    /**
     * Sky condition
     */
    public static final Pattern SKY_CONDITION_PATTERN = Pattern.compile(
            "^(?<cover>VV|CLR|SKC|SCK|NSC|NCD|BKN|SCT|FEW|[O0]VC|///)(?<height>[\\dO]{2,4}|///)?(?<cloud>([A-Z][A-Z]+|///))?\\s+"
    );

    /**
     * Temperature/Dewpoint
     */
    public static final Pattern TEMP_DEWPOINT_PATTERN = Pattern.compile(
            "^((?<signt>(M|-))?(?<temp>\\d+)|//|XX|MM)/((?<signd>(M|-))?(?<dewpt>\\d+)|//|XX|MM)?\\s+"
    );

    /**
     * Altimeter
     */
    public static final Pattern ALTIMETER_PATTERN = Pattern.compile(
            "^(?<unit>A{1,2}|Q|QNH)?(?<press>[\\dO]{3,4}|////)(?<unit2>INS)?\\s+"
    );

    /**
     * No Significant Change
     */
    public static final Pattern NO_SIG_CHANGE_PATTERN = Pattern.compile(
            "^(?<nosigchng>NOSIG)\\s+"
    );

    /**
     * Funnel Cloud (Tornadic activity_B/E(hh)mm_LOC/DIR_(MOV)). At manual
     * stationS, tornadoes, funnel clouds, or waterspouts shall be coded in the
     * format, TORNADIC ACTIVITY_B/E(hh)mm_LOC/DIR_(MOV)
     */
    public static final Pattern TRN_FC_WSP_PATTERN = Pattern.compile(
            "^(?<type>TORNADO|FUNNEL CLOUD|WATERSPOUT) (?<betime>B|E)(?<time>\\d+) (?<dirfrom>\\w) (?<verb>\\w+) (?<dirto>\\w)?\\s+"
    );

    /**
     * Type of Automated Station (AO1 or AO2). AO1 or AO2 shall be coded in all
     * METAR/SPECI from automated stations.
     */
    public static final Pattern AUTO_PATTERN = Pattern.compile(
            "^A(O|0)(?<type>\\d)\\s+"
    );

    /**
     * Beginning and End of Precipitation. Example RAB20E51
     */
    public static final Pattern BEGIN_END_WEATHER_PATTERN = Pattern.compile(
            "^(?<int>(VC|-|\\+)*)(?<desc>(MI|PR|BC|DR|BL|SH|TS|FZ)+)?(?<prec>(DZ|RA|SN|SG|IC|PL|GR|GS|UP|/)*)(?<obsc>BR|FG|FU|VA|DU|SA|HZ|PY)?(?<other>PO|SQ|FC|SS|DS|NSW|/+)?(?<int2>[-+])?((?<begin>B)(?<begint>\\d\\d)*)?((?<end>E)(?<endt>\\d\\d)*)"
    );

    /**
     * Sea-Level Pressure (SLPppp). At designated stations, the sea-level
     * pressure shall be coded in the format SLPppp
     */
    public static final Pattern SEALVL_PRESS_PATTERN = Pattern.compile(
            "^(?<type>SLP)(?<press>\\d\\d\\d|NO)?\\s+"
    );

    /**
     * Peak Wind (PK_WND_dddff(f)/(hh)mm). The peak wind shall be coded in the
     * format, PK_WND_dddff(f)/(hh)mm of the next METAR
     */
    public static final Pattern PEAK_WIND_PATTERN = Pattern.compile(
            "^PK WND (?<dir>\\d\\d\\d)?(?<speed>P?\\d\\d\\d?)/(?<hour>[0-1][0-9]|[2][0-3])?(?<min>\\d\\d)?\\s+"
    );

    /**
     * Wind Shift (WSHFT_(hh)mm). A wind shift shall be coded in the format
     * WSHFT_(hh)mm
     */
    public static final Pattern WIND_SHIFT_PATTERN = Pattern.compile(
            "^WSHFT (?<hour>\\d\\d)?(?<min>\\d\\d)(\\s+(?<front>FROPA))?\\s+"
    );

    /**
     * Tower or Surface Visibility (TWR_VIS_vvvvv or SFC_VIS_vvvvv). Tower
     * visibility or surface visibility shall be coded in the formats,
     * TWR_VIS_vvvvv or SFC_VIS_vvvvv, respectively
     */
    public static final Pattern TWR_SFC_VIS_PATTERN = Pattern.compile(
            "^(?<type>TWR VIS|SFC VIS) (?<dist>\\d+\\s\\d/\\d|\\d\\d?/\\d\\d?|\\d{1,2})?\\s+"
    );

    /**
     * Variable Prevailing Visibility (VIS_vnvn vnvnVvxvxvx vxvx). Variable
     * prevailing visibility shall be coded in the format VIS_vn vnvnvnVvxvx
     * vxvxvx Sector Visibility (VIS_[DIR]_vvvvv){Plain Language]. The sector
     * visibility shall be coded in the format VIS_[DIR]_vvvvv Visibility At
     * Second Location (VIS_vvvvv_[LOC]). At designated automated stations, the
     * visibility at a second location shall be coded in the format
     * VIS_vvvvv_[LOC] visibility shall be coded in the formats, TWR_VIS_vvvvv
     * or SFC_VIS_vvvvv, respectively
     */
    public static final Pattern VPV_SV_VSL_PATTERN = Pattern.compile(
            "^(?<vis>VIS) (?<dir>([NSEW][EW]))?\\s*(?<dist1>\\d\\d?/\\d\\d?|\\d+\\s+\\d\\d?/\\d\\d?|\\d+)?(\\s*(?<add>V|RWY)\\s*(?<dist2>\\d\\d?/\\d\\d?|\\d+\\s+\\d\\d?/\\d\\d?|\\d+))?\\s+"
    );

    /**
     * Lightning (Frequency_LTG(type)_[LOC]).
     */
    public static final Pattern LIGHTNING_PATTERN = Pattern.compile(
            "^((?<freq>OCNL|FRQ|CONS)\\s+)?LTG((?<typeic>IC)?(?<typecc>CC)?(?<typecg>CG)?(?<typeca>CA)?(?<typecw>CW)?)*? (?<loc>(OHD|VC|DSNT))\\s+((?<dir>[NSEW][EW]?)-?(?<dir2>[NSEW][EW]?)*)?\\s+"
    );

    /**
     * Thunderstorm Location (TS_LOC_(MOV_DIR)) [Plain Language].
     * Thunderstorm(s) shall be coded in the format, TS_LOC_(MOV_DIR)
     * Significant Cloud Types - Cumulonimbus (CB), Towering Cumulus (TCU),
     * Altocumulus Castellanus (ACC) Cumulonimbus Mammatus (CBMAM), Virga
     * (VIRGA)
     */
    public static final Pattern TS_CLD_LOC_PATTERN = Pattern.compile(
            "^(?<type>TS|CB|TCU|ACC|CBMAM|VIRGA)\\s*(?<loc>(OHD|VC|DSNT|DSIPTD|TOP|TR)?)\\s*((?<dir>[NSEW][EW]?)-?(?<dir2>[NSEW][EW]?)*)?(\\s*MOV\\s*(?<dirm>[NSEW][EW]?))?\\s+"
    );

    /**
     * Pressure Rising or Falling Rapidly (PRESRR or PRESFR)
     */
    public static final Pattern PRES_RF_RAPDLY_PATTERN = Pattern.compile(
            "^PRES(?<presrisfal>\\w)\\w\\s+"
    );

    /**
     * Icing
     */
    public static final Pattern ICING_PATTERN = Pattern.compile(
            "^(?<type>ICG)((?<typeic>IC)?(?<typeip>IP)?)\\s(?<extra>\\w\\w\\w\\w\\s\\w\\w)\\s+"
    );

    /**
     * 6-hour maximum and minimum temperature in tenths degrees C format; 1sTTT
     * and 2sTTT
     */
    public static final Pattern TEMP_6HR_MAX_MIN_PATTERN = Pattern.compile(
            "^(?<type>1|2)(?<sign>0|1)(?<temp>\\d{2,3})\\s+"
    );

    /**
     * Hourly Precipitation Amount (Prrrr). At designated automated stations,
     * the hourly precipitation amount shall be coded in the format, Prrrr
     */
    public static final Pattern PRECIP_1HR_PATTERN = Pattern.compile(
            "^(?<type>P)(?<precip>\\d\\d\\d\\d)\\s+"
    );

    /**
     * 3-hour pressure tendency
     */
    public static final Pattern PRESS_3HR_PATTERN = Pattern.compile(
            "^(?<type>5)(?<tend>[0-8])(?<press>\\d\\d\\d)\\s+"
    );

    /**
     * 3- and 6-hour Precipitation (6RRRR). At designated stations, the 3- and
     * 6-hourly precipitation group shall be coded in the format 6RRRR 24-Hour
     * Precipitation Amount (7R24R24 R24R24). At designated stations, the
     * 24-hour precipitation amount shall be coded in the format, 7R24R24R24R24
     */
    public static final Pattern PRECIP_3HR_24HR_PATTERN = Pattern.compile(
            "^(?<type>6|7)(?<precip>\\d{1,5}|/{1,5})\\s+"
    );

    /**
     * Hourly Temperature and Dew Point (TsnT'T'T'snT'dT'dT'd). At designated
     * stations, the hourly temperature and dew point group shall be coded to
     * the tenth of a degree Celsius in the format, TsnT'T'T'snT'dT'dT'd
     */
    public static final Pattern TEMP_1HR_PATTERN = Pattern.compile(
            "^(?<type>T)(?<tsign>0|1)(?<temp>\\d\\d\\d)((?<dsign>0|1)(?<dewpt>\\d\\d\\d))?\\s+"
    );

    /**
     * 24-Hour Maximum and Minimum Temperature 4snTxTxTxsnTnTnTn; tenth of
     * degree Celsius; reported at midnight local standard time; 1 if
     * temperature below 0°C and 0 if temperature 0°C or higher, e.g.,
     * 400461006.
     */
    public static final Pattern TEMP_24HR_PATTERN = Pattern.compile(
            "^(?<type>4)(?<maxsign>0|1)(?<maxtemp>\\d\\d\\d)((?<minsign>0|1)(?<mintemp>\\d\\d\\d))\\s+"
    );

    /**
     * Density Altitude Example DENSITY ALT 800FT
     */
    public static final Pattern DENSITY_ALTITUDE_PATTERN = Pattern.compile(
            "^(?<type>DENSITY ALT) (?<denalt>\\d{1,5})(?<units>FT)\\s+"
    );

    /**
     * Sky Conditions FEW = 1 to 2 oktas; SCT (Scattered) = 3 to 4 oktas; BKN
     * (Broken) = 5 to 7 oktas; OVC (Overcast) = 8 oktas; Examples AC8SC1, CI TR
     * "^(?<cloud>(CU|CB|CF|ST|SC|SF|NS|AS|AC|CS|CC|CI))(?<okta>\\d|\\s\\w{1,3})?(\\s*(?<verb>MOVG)\\s*(?<dirm>[NSEW][EW]?))?"
     */
    public static final Pattern CLOUD_OKTA_PATTERN = Pattern.compile(
            "^(?<cloud>(CU|CF|ST|SC|SF|NS|AS|AC|CS|CC|CI))(?<okta>\\d|\\s\\w{1,3})?(\\s*(?<verb>MOVG)\\s*(?<dirm>[NSEW][EW]?))?"
    );

    /**
     * Last Observation Example LAST STFD OBS
     */
    public static final Pattern LAST_OBS_PATTERN = Pattern.compile(
            "^(?<last>LAST)\\s+"
    );

    /**
     * Pressure (Q Codes) - QFE=Q-Field Elevation QNH=Q-Normal Height
     * QNE=Q-Normal Elevation QFE747/996, It is 744 mm of mercury = 996
     * millibars
     */
    public static final Pattern PRESS_Q_PATTERN = Pattern.compile(
            "^(?<pressq>QFE|QNH|QNE)((?<pressmm>\\d{3,4})?(/(?<pressmb>\\d{3,4}))?)?\\s+"
    );

    /**
     * Automated Maintenance Data RVRNO: RVR missing; PWINO: precipitation
     * identifier information not available; PNO: precipitation amount not
     * available; FZRANO: freezing rain information not available; TSNO:
     * thunderstorm information not available (may indicate augmenting weather
     * observer not logged on); VISNO [LOC]: visibility at second location not
     * available, e.g. VISNO RWY06; CHINO [LOC]: (cloud-height- indicator) sky
     * condition at secondary location not available, e.g., CHINO RWY06. $:
     * Maintenance check indicator ASOS requires maintenance
     */
    public static final Pattern AUTOMATED_MAINTENANCE_PATTERN = Pattern.compile(
            "^(?<typeam>|RVRNO|PWINO|PNO|FZRANO|TSNO|VISNO|CHINO)\\s(?<loc>\\w+\\d+)?|(?<typemc>\\$)\\s+"
    );

    /**
     * Groups - BECMG - Becoming, TEMPO - Temporary, PROB - Probability
     * forecasts
     */
    public static final Pattern GROUP_BECMG_TEMPO_PROB_PATTERN = Pattern.compile(
            "^(?<group>BECMG|TEMPO|PROB\\d\\d) (?<obs>(\\S+\\s){1,})"
    );

    /**
     * Group - FM - From
     */
    public static final Pattern GROUP_FM_PATTERN = Pattern.compile(
            "^(?<group>FM)(?<daytime>\\d\\d\\d\\d\\d\\d) (?<obs>(\\S+\\s){1,})"
    );

    /**
     *
     */
    public static final Pattern VALTMPER_PATTERN = Pattern.compile(
            "^(?<bvaltime>\\d\\d\\d\\d)/(?<evaltime>\\d\\d\\d\\d)\\s+"
    );

    /**
     *
     */
    public static final Pattern NXT_FCST_BY_PATTERN = Pattern.compile(
            "^(?<type>NXT FCST BY) (?<zday>\\d\\d)(?<zhour>\\d\\d)(?<zmin>\\d\\d)Z\\s+"
    );

    /**
     *
     */
    public static final Pattern SNOW_ON_GRND_PATTERN = Pattern.compile(
            "^(?<type>SOG) (?<amt>\\d{1,3})\\s+"
    );

    /**
     * Unparsed
     */
    public static final Pattern UNPARSED_PATTERN = Pattern.compile(
            "^(?<unparsed>\\S+)\\s+"
    );
}
