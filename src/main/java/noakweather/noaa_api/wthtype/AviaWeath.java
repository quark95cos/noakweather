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
package noakweather.noaa_api.wthtype;

import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import noakweather.noaa_api.common.Remarks;
import noakweather.noaa_api.common.RunwayVisualRange;
import noakweather.noaa_api.common.SkyCondition;
import noakweather.noaa_api.common.WeatherCondition;
import noakweather.noaa_api.weather.Pressure;
import noakweather.noaa_api.weather.Temperature;
import noakweather.noaa_api.weather.Visibility;
import noakweather.noaa_api.weather.Wind;
import noakweather.noaa_api.wthgroup.Becoming;
import noakweather.noaa_api.wthgroup.FromGroup;
import noakweather.noaa_api.wthgroup.Prob;
import noakweather.noaa_api.wthgroup.Tempo;
import noakweather.utils.Configs;
import noakweather.utils.IndexedLinkedHashMap;
import noakweather.utils.UtilsDate;
import noakweather.utils.UtilsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;

/**
 * Class representing the base class of the METAR and TAF classes
 *
 * Author: quark95cos Since: Copyright(c) 2022
 */
public class AviaWeath {

    private int skyCondIndex;
    private int fmIndex = 0;
    private int bcmgIndex = 0;
    private int tempoIndex = 0;
    private int probIndex = 0;
    private Date date;
    private Date validFromDate;
    private Date validToDate;
    private Date nxtFcstByDate;
    private boolean isBecmg;
    private boolean isNoSignificantChange;
    private Integer becmgcntr;
    private String dayTemp;
    private String reportString;
    private String stationID;
    private String dateString;
    private String reportModifier;
    private String monthString;
    private String yearString;
    private ArrayList<String> parseString = new ArrayList<>();
    private Wind wind;
    private Visibility visibility;
    private Temperature temperature;
    private Pressure pressure;
    private RunwayVisualRange runwayVisualRange;
    private IndexedLinkedHashMap<RunwayVisualRange, String> runwayVisualRanges;
    private IndexedLinkedHashMap<WeatherCondition, String> weatherConditions;
    private IndexedLinkedHashMap<SkyCondition, String> skyConditions;
    private WeatherCondition weatherConditionRemark;
    private FromGroup fromGroup;
    private IndexedLinkedHashMap<FromGroup, String> fromGroups;
    private Becoming becoming;
    private IndexedLinkedHashMap<Becoming, String> becomings;
    private Tempo tempo;
    private IndexedLinkedHashMap<Tempo, String> tempos;
    private Prob prob;
    private IndexedLinkedHashMap<Prob, String> probs;

    protected Remarks remarks;
    protected WeatherCondition weatherCondition;
    protected SkyCondition skyCondition;
    protected IndexedLinkedHashMap<Pattern, Pair<String, Boolean>> aviaMainWthHandlers;
    protected IndexedLinkedHashMap<Pattern, Pair<String, Boolean>> aviaRemarkWthHandlers;
    protected IndexedLinkedHashMap<Pattern, Pair<String, Boolean>> aviaGroupWthHandlers;

    private static final Logger LOGGER
            = LogManager.getLogger(AviaWeath.class.getName());

    /**
     * Constructor
     */
    public AviaWeath() {
        LOGGER.debug("in AviaWeath constructor");
        this.skyCondIndex = 0;
        this.fmIndex = 0;
        this.bcmgIndex = 0;
        this.tempoIndex = 0;
        this.probIndex = 0;
        this.reportString = null;
        this.stationID = "";
        this.dateString = "";
        this.date = null;
        this.validFromDate = null;
        this.validToDate = null;
        this.nxtFcstByDate = null;
        this.isBecmg = false;
        this.isNoSignificantChange = false;
        this.becmgcntr = 0;
        this.dayTemp = "";
        this.reportModifier = "";
        this.monthString = "";
        this.yearString = "";
        this.parseString = null;
        this.wind = null;
        this.visibility = null;
        this.temperature = null;
        this.pressure = null;
        this.remarks = null;
        this.runwayVisualRange = null;
        this.runwayVisualRanges = null;
        this.weatherCondition = null;
        this.weatherConditions = null;
        this.skyCondition = null;
        this.skyConditions = null;
        this.weatherConditionRemark = null;
        this.aviaMainWthHandlers = null;
        this.aviaRemarkWthHandlers = null;
        this.aviaGroupWthHandlers = null;
        this.fromGroup = null;
        this.fromGroups = null;
        this.becoming = null;
        this.becomings = null;
        this.tempo = null;
        this.tempos = null;
        this.prob = null;
        this.probs = null;
    }

    /**
     * Parse the avia handlers information
     *
     * @param token
     * @param handlers
     * @param handlersType
     * @throws noakweather.utils.UtilsException
     */
    protected void parseAviaHandlers(String token, IndexedLinkedHashMap<Pattern, Pair<String, Boolean>> handlers, String handlersType) throws UtilsException {
        boolean isFound = false;
        while (!token.isEmpty()) {
            LOGGER.debug("\n");
            LOGGER.debug(Configs.getInstance().getString("MATCH_DECODED_TOKEN_PROCESSING")
                    + " #" + token + "#");
            handlers.keySet();
            for (Pattern i : handlers.keySet()) {
                LOGGER.debug(Configs.getInstance().getString("MATCH_DECODED_PATTERN_I")
                        + " #" + i + "#  #" + handlers.get(i) + "#");
                Matcher matcher = i.matcher(token);
                while (matcher.find()) {
                    isFound = true;
                    LOGGER.debug(Configs.getInstance().getString("MATCH_DECODED_MATCHER_GROUP_CNT")
                            + " " + matcher.groupCount());
                    LOGGER.debug(Configs.getInstance().getString("MATCH_DECODED_MATCHER_GROUP_0")
                            + " #" + matcher.group(0) + "#");
                    if (matcher.group(0).equals("")) {
                        break;
                    }
                    for (int j = 1; j <= matcher.groupCount(); j++) {
                        LOGGER.debug(Configs.getInstance()
                                .getString("MATCH_DECODED_CAPTURE_GROUP_NUMBER")
                                + " " + Configs.getInstance()
                                        .getString("MATCH_DECODED_CAPTURE_GROUP_NUMBER")
                                + " " + j + "   " + Configs.getInstance()
                                        .getString("MATCH_DECODED_CAPTURED_TEXT")
                                + " #" + matcher.group(j) + "#");
                    }
                    if (handlersType.equals(Configs.getInstance()
                            .getString("AVIA_MAIN_HANDLERS"))) {
                        detMainItems(handlers.get(i).getValue0(), matcher);
                    } else if (handlersType.equals(Configs.getInstance()
                            .getString("AVIA_REMARK_HANDLERS"))) {
                        detRemarkItems(handlers.get(i).getValue0(), matcher);
                    }
                    token = matcher.replaceFirst("").trim();
                    //This is necessary to make sure the last token is properly processed
                    if (token.length() > 0) {
                        token = token + " ";
                    }
                    LOGGER.debug(Configs.getInstance().getString("MATCH_DECODED_TOKEN_AFTER_LAST_MATCH")
                            + " #" + token);
                    LOGGER.debug(Configs.getInstance().getString("MATCH_DECODED_PATTERN")
                            + " " + handlers.get(i).getValue1());
                    if (handlers.get(i).getValue1().equals(false)) {
                        break;
                    }
                    matcher = i.matcher(token);
                }
                if (isFound) {
                    isFound = false;
                    break;
                }
            }
        }
    }

    /**
     * Determine the main data information
     *
     * @param type
     * @param value
     */
    private void detMainItems(String type, Matcher value) throws UtilsException {
        switch (type) {
            case "mnthdayyr":
                // We have year and month data
                LOGGER.debug("year: #" + value.group("year") + "#");
                LOGGER.debug("month: #" + value.group("month") + "#");
                LOGGER.debug("day: #" + value.group("day") + "#");
                LOGGER.debug("time: #" + value.group("time") + "#");
                setYearString(value.group("year"));
                setMonthString(value.group("month"));
                break;
            case "station":
                // We have station data
                LOGGER.debug("stationID: #" + value.group("station") + "#");
                LOGGER.debug("zday: #" + value.group("zday") + "#");
                LOGGER.debug("zhour: #" + value.group("zhour") + "#");
                LOGGER.debug("zmin: #" + value.group("zmin") + "#");
                LOGGER.debug("bvaltime: #" + value.group("bvaltime") + "#");
                LOGGER.debug("evaltime: #" + value.group("evaltime") + "#");
                setStationID(value.group("station"));
                setDateInfo(value);
                setValidToFromDateInfo(value);
                break;
            case "reportmodifier":
                // We have reportmodifier data
                LOGGER.debug(Configs.getInstance().getString("MATCH_DECODED_PATTERN")
                        + " #" + getReportModifier() + "#");
                setReportModifier(value.group("mod"));
                break;
            case "wind":
                // We have wind data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_WIND"));
                if (wind == null) {
                    wind = new Wind();
                }
                wind.setMainWindItems(value, 'M');
                break;
            case "visibility":
                // We have visibility data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_VISIBILITY"));
                if (visibility == null) {
                    visibility = new Visibility();
                }
                visibility.setVisibilityItems(value);
                break;
            case "runway":
                // We have Runaway group data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_RVR"));
                if (runwayVisualRanges == null) {
                    runwayVisualRanges = new IndexedLinkedHashMap<>();
                }
                parseRunVisualRange(value);
                break;
            case "presentweather":
                // We have Present Weather data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_WEATHER_GROUPS"));
                if (weatherConditions == null) {
                    weatherConditions = new IndexedLinkedHashMap<>();
                }
                weatherCondition = new WeatherCondition();
                weatherCondition.setWeatherConditionItems(value);
                weatherConditions.put(weatherCondition,
                        weatherCondition.getNaturalLanguageString());
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_WEATHER_CONDITIONS")
                        + " " + weatherCondition.getNaturalLanguageString());
                break;
            case "skycondition":
                // We have a sky condition data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_SKY_CONDITIONS"));
                if (skyConditions == null) {
                    skyConditions = new IndexedLinkedHashMap<>();
                }
                skyCondition = new SkyCondition();
                skyCondition.setSkyConditionItems(value);
                skyCondIndex++;
                skyConditions.put(skyCondition,
                        String.valueOf(skyCondIndex) + " "
                        + skyCondition.getNaturalLanguageString());
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_SKY_CONDITIONS")
                        + " " + skyCondition.getNaturalLanguageString());
                break;
            case "tempdewpoint":
                // We have Temperature / Dew Point data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_TEMPERATURE_DEWPOINT"));
                if (temperature == null) {
                    temperature = new Temperature();
                }
                temperature.setTemperatureItems(value);
                break;
            case "altimeter":
                // We have pressure data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_PRESSURE"));
                pressure = new Pressure();
                pressure.setPressureItems(value);
                break;
            case "nosigchng":
                // We have no significant change data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_NO_SIGNIFICANT_CHANGE"));
                setIsNoSignificantChange(true);
                break;
            case "tafstr":
                // We have a TAF string which is captured but ignored data
                LOGGER.debug(Configs.getInstance().getString("MISC_TAF"));
                break;
            case "grpbecmgtempprob":
            case "grpfm":
                // We have group BECMG|TEMPO|PROB|FM data data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_BECMGTEMPOPROBFM_DATA"));
                parseGroups(value);
                break;
            case "unparsed":
                // We have Unparsed data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_UNPARSED_DATA"));
                if (parseString == null) {
                    parseString = new ArrayList<>();
                }
                setParseString(value);
                break;
            default:
                break;
        }
    }

    /**
     * Determine the remarks data information
     *
     * @param type
     * @param value
     */
    private void detRemarkItems(String type, Matcher value) throws UtilsException {
        switch (type) {
            case "presrisfal":
                // We have Pressure Rising or Falling Rapidly data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_PRESSURE_RISING_FALLING_RAPIDLY"));
                remarks.setPressureRFRapidlyItems(value);
                break;
            case "trnfcwsp":
                // We have Tornadic Activity data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_TORNADIC_ACTIVITY"));
                remarks.setTornadicActivity(value);
                break;
            case "auto":
                // We have Automated Station data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_AUTOMATED_STATION"));
                remarks.setAutomatedStation(value);
                break;
            case "beginendwthr":
                // We have Beginning and End of Precipitation data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_BEG_END_PRECIPITATION"));
                weatherConditionRemark = new WeatherCondition();
                weatherConditionRemark.setWeatherConditionItems(value);
                weatherConditionRemark.setWeatherConditionBegEnd(value);
                remarks.setWeatherConditionBegEnd(weatherConditionRemark);
                break;
            case "sealvlpress":
                // We have a Sea Level Pressure data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_SEA_LEVEL_PRESSURE"));
                if (pressure == null) {
                    pressure = new Pressure();
                }
                pressure.setSLPressureItems(value);
                remarks.setSeaLevelPressure(pressure);
                break;
            case "peakwind":
                // We have a Peak Wind data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_PEAK_WIND"));
                if (wind == null) {
                    wind = new Wind();
                }
                wind.setPeakWindItems(value);
                remarks.setPeakWindSpeed(wind);
                break;
            case "windshift":
                // We have a Wind Shift data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_WIND_SHIFT"));
                remarks.setWindShiftItems(value);
                break;
            case "twrsfcvis":
                // We have Tower or Surface Visibility data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_TWRSRFVIS"));
                if (visibility == null) {
                    visibility = new Visibility();
                }
                visibility.setTowerSurfVisItems(value);
                remarks.setTowerSurfVisItems(visibility);
                break;
            case "vpvsvvsl":
                // We have Variable Prevailing Visibility, Sector Visibility
                // or Visibility At Second Location data
                LOGGER.debug(Configs.getInstance()
                        .getString("LOG_DECODED_FOUND_VARPREVVIS_SECTORVIS_VISSECONDLOC"));
                // A new instance of Visibility should be created as this is
                // shared with 3 different remarks
                if (visibility == null) {
                    visibility = new Visibility();
                }
                visibility.setVarPrevVisSecVisVisSecLocItems(value);
                remarks.setVarPrevVisSecVisVisSecLocItems(visibility);
                break;
            case "lightning":
                // We have Lighting data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_LIGHTNING"));
                remarks.setLightningItems(value);
                break;
            case "tsloc":
                // We have Thunderstorm Location data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_THUNDERSTORM_LOC"));
                remarks.setThunderCloudLocationItems(value);
                break;
            case "icing":
                // We have Icing data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_ICING"));
                remarks.setIcingItems(value);
                break;
            case "sixhrmaxmintemp":
                // We have 6-hourly maximum or minimum temperature data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_6_HOURLY_MAX_MIN_TEMPERATURE"));
                if (temperature == null) {
                    temperature = new Temperature();
                }
                temperature.setSixHourMaxMinTemperature(value);
                switch (Integer.parseInt(value.group("type"))) {
                    case 1:
                        remarks.setSixHourMaximumTemperature(temperature);
                        break;
                    case 2:
                        remarks.setSixHourMinimumTemperature(temperature);
                        break;
                    // Should not happen
                    default:
                        break;
                }
                break;
            case "precip1hr":
                // We have precipitation that has fallen in the past hour data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_PRECIPITATION_FALLEN_PAST_HOUR"));
                remarks.setHourlyPrecipitationItems(value);
                break;
            case "precip3hr24hr":
                // We have 3hr/6hr/24hr precipitation data
                switch (Integer.parseInt(value.group("type"))) {
                    case 6:
                        // We have 3 and 6 hour precipitation that has fallen
                        LOGGER.debug(Configs.getInstance()
                                .getString("LOG_DECODED_FOUND_3_6_HOUR_PRECIPITATION_FALLEN"));
                        break;
                    case 7:
                        // We have 24 hour precipitation that has fallen
                        LOGGER.debug(Configs.getInstance()
                                .getString("LOG_DECODED_FOUND_24_HOUR_PRECIPITATION_FALLEN"));
                        break;
                    // Should not happen
                    default:
                        break;
                }
                remarks.setSixTwentyFourHourPrecipitationItems(value);
                break;
            case "temp1hr":
                // We have hourly temperature and dew point data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_HOURLY_TEMPERATURE_DEWPOINT"));
                if (temperature == null) {
                    temperature = new Temperature();
                }
                temperature.setHourlyTemperatureItems(value);
                remarks.setHourlyTemperatureDewPoint(temperature);
                break;
            case "temp24hr":
                // We have 24-hour maximum  and minimum temperature data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_24_HOUR_MAX_MIN_TEMPERATURE"));
                if (temperature == null) {
                    temperature = new Temperature();
                }
                temperature.setTwentyFourHourMaxMinTemperature(value);
                remarks.setTwentyFourHourMaxMinTemperature(temperature);
                break;
            case "press3hr":
                // We have 3-hour pressure tendency data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_3_HOUR_PRESSURE_TENDENCY"));
                if (pressure == null) {
                    pressure = new Pressure();
                }
                pressure.setTendencyPressureItems(value);
                remarks.setThreeHourPressureTendencyItems(pressure);
                break;
            case "denalt":
                // We have Density Altitude data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_DENSITY_ALTITUDE"));
                remarks.setDensityAltitudeItems(value);
                break;
            case "cloudokta":
                // We have Clouds data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_CLOUDS"));
                remarks.setCloudOktaItems(value);
                break;
            case "lastobs":
                // We have Last Observation data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_LAST_OBS"));
                remarks.setLastObsItems(value);
                break;
            case "pressqfn":
                // We have QFE/QNH/QNE pressure data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_QFE_QNH_QNE_PRESSURE"));
                if (pressure == null) {
                    pressure = new Pressure();
                }
                pressure.setQFEQNHQNEPressureItems(value);
                remarks.setQFEQNHQNEPressure(pressure);
                break;
            case "automaint":
                // We have automated maintenance data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_AUTOMATED_MAINTENANCE_DATA"));
                remarks.setAutomatedMaintenanceItems(value);
                break;
            case "snwongrnd":
                // We have snow on ground data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_SNOW_ON_GROUND"));
                remarks.setSnowOnGround(value);
                break;
            case "nxtfcstby":
                // We have next forecast by data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_NEXT_FORECAST_BY"));
                LOGGER.debug("type: #" + value.group("type") + "#");
                LOGGER.debug("zday: #" + value.group("zday") + "#");
                LOGGER.debug("zhour: #" + value.group("zhour") + "#");
                LOGGER.debug("zmin: #" + value.group("zmin") + "#");
                setNxtFcstByDateInfo(value);
                remarks.setNextForecastItems(getNxtFcstByDate());
                break;
            case "windre":
                // TODO Still needs to be coded
                // We have a wind in the remarks section
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_REMARK_WIND"));
                if (wind == null) {
                    wind = new Wind();
                }
                //wind.setMainWindItems(value);
                break;
            case "unparsed":
                // We have Unparsed data
                LOGGER.debug(Configs.getInstance().getString("LOG_DECODED_FOUND_UNPARSED_DATA"));
                if (parseString == null) {
                    parseString = new ArrayList<>();
                }
                setParseString(value);
                break;
            default:
                break;
        }
    }

    /**
     * Parse the runway visual range information
     *
     * @param token
     */
    private void parseRunVisualRange(Matcher token) throws UtilsException {
        // We have a RVR
        LOGGER.debug("group(0): #" + token.group(0) + "#");
        LOGGER.debug("name: #" + token.group("name") + "#");
        LOGGER.debug("inden: #" + token.group("inden") + "#");
        LOGGER.debug("low: #" + token.group("low") + "#");
        LOGGER.debug("lvalue: #" + token.group("lvalue") + "#");
        LOGGER.debug("high: #" + token.group("high") + "#");
        LOGGER.debug("unit: #" + token.group("unit") + "#");

        runwayVisualRange = new RunwayVisualRange();

        if (token.group(0).contains("/")) {
            // Get the runway number
            runwayVisualRange.setRunwayNumber(Integer.parseInt(token
                    .group("name").substring(0, 2)));

            // Get the approach direction if it exists.
            if (token.group("inden") != null) {
                runwayVisualRange.setApproachDirection(token.group("inden")
                        .charAt(0));
            }

            // Determine if we have a modifier for maximum or minimum
            if (token.group("low").startsWith("P")
                    || token.group("low").startsWith("M")) {
                runwayVisualRange.setReportableModifier(
                        token.group("low").charAt(0));
            }

            // We may have the contamination conditions cease to exist
            if (token.group("low").equals("CLRD")) {
                runwayVisualRange.setContaminationCondClrd(true);
            } // We may have a trend
            else if (token.group("unit").equals("U")
                    || token.group("unit").equals("D")
                    || token.group("unit").equals("N")) {
                runwayVisualRange.setLowestReportable(Integer
                        .parseInt(token.group("lvalue")));
                //.parseInt(UtilsMisc.removeNonNumeric(token.group("low"))));
                runwayVisualRange.setReportableTrend(token.group("unit").charAt(0));
            } else {
                LOGGER.debug(Configs.getInstance().getString("RVR_DECODED_RVR_LOWEST_REPORTABLE"));
                runwayVisualRange.setLowestReportable(Integer
                        .parseInt(token.group("lvalue")));
                //.parseInt(UtilsMisc.removeNonNumeric(token.group("low"))));
            }
        } // May not need this part. Need to find examples
        else {
            LOGGER.debug("RVR: In else part: " + token.group(0));
        }

        runwayVisualRanges.put(runwayVisualRange,
                runwayVisualRange.getNaturalLanguageString());
        LOGGER.debug(Configs.getInstance().getString("RVR_RUNWAY_VISUAL_RANGE")
                + " " + runwayVisualRange.getNaturalLanguageString());
    }

    /**
     * Parse the groups information
     *
     * @param token
     */
    private void parseGroups(Matcher token) throws UtilsException {
        LOGGER.debug("group: #" + token.group("group") + "#");
        LOGGER.debug("obs: #" + token.group("obs") + "#");

        if (token.group("group")
                .equals(Configs.getInstance().getString("EXTENDED_FM"))) {
            LOGGER.debug("daytime: #" + token.group("daytime") + "#");
            fmIndex++;
            if (fromGroups == null) {
                fromGroups = new IndexedLinkedHashMap<>();
            }
            fromGroup = new FromGroup();
            fromGroups.put(fromGroup, String.valueOf(fmIndex) + " "
                    + fromGroup.setFromGroupItems(token.group("daytime"), token.group("obs"), monthString, yearString, aviaGroupWthHandlers));
        } else if (token.group("group")
                .equals(Configs.getInstance().getString("EXTENDED_BECMG"))) {
            bcmgIndex++;
            if (becomings == null) {
                becomings = new IndexedLinkedHashMap<>();
            }
            becoming = new Becoming();
            becomings.put(becoming, String.valueOf(bcmgIndex) + " "
                    + becoming.setBecomingItems(token.group("obs"), monthString, yearString, aviaGroupWthHandlers));
        } else if (token.group("group")
                .equals(Configs.getInstance().getString("EXTENDED_TEMPO"))
                || token.group("group")
                        .equals(Configs.getInstance().getString("EXTENDED_TEMP0"))) {
            tempoIndex++;
            if (tempos == null) {
                tempos = new IndexedLinkedHashMap<>();
            }
            tempo = new Tempo();
            tempos.put(tempo, String.valueOf(tempoIndex) + " "
                    + tempo.setTempoItems(token.group("obs"), monthString, yearString, aviaGroupWthHandlers));
        } else if (token.group("group")
                .startsWith(Configs.getInstance().getString("EXTENDED_PROB"))
                || token.group("group")
                        .startsWith(Configs.getInstance().getString("EXTENDED_PR0B"))) {
            probIndex++;
            if (probs == null) {
                probs = new IndexedLinkedHashMap<>();
            }
            prob = new Prob();
            probs.put(prob, String.valueOf(probIndex) + " "
                    + prob.setProbItems(token.group("group"), token.group("obs"), monthString, yearString, aviaGroupWthHandlers));
        }
    }

    /**
     * Set the date information
     *
     * @param token The date information.
     */
    private void setDateInfo(Matcher token) {
        // Date and time of the report
        // format: YYGGggZ
        //     YY - date
        //     GG - hours
        //     gg - minutes
        //     Z - Zulu (UTC)
        if (token.group("zday") != null && token.group("zhour") != null
                && token.group("zmin") != null) {
            try {
                dayTemp = token.group("zday");
                String fullDate = token.group("zday") + token.group("zhour")
                        + token.group("zmin");
                LOGGER.debug(Configs.getInstance().getString("LOC_TIME_DECODED_FULL_DATE")
                        + " " + fullDate);
                setDate(UtilsDate.setDate(token.group("zday"),
                        token.group("zhour"), token.group("zmin"),
                        monthString, yearString));
            } catch (NumberFormatException | UtilsException e) {
                String errMsg = Configs.getInstance().getString("LOC_TIME_DECODED_UNABLE_PARSE_DATE_VALUE")
                        + " " + e;
                LOGGER.error(errMsg);
            }
        }
    }

    /**
     * Set the next forecast by date information
     *
     * @param token The date information.
     */
    private void setNxtFcstByDateInfo(Matcher token) {
        // Date and time of the report
        // format: YYGGggZ
        //     YY - date
        //     GG - hours
        //     gg - minutes
        //     Z - Zulu (UTC)
        if (token.group("zday") != null && token.group("zhour") != null
                && token.group("zmin") != null) {
            try {
                dayTemp = token.group("zday");
                String fullDate = token.group("zday") + token.group("zhour")
                        + token.group("zmin");
                LOGGER.debug(Configs.getInstance().getString("LOC_TIME_DECODED_FULL_DATE")
                        + " " + fullDate);
                setNxtFcstByDate(UtilsDate.setDate(token.group("zday"),
                        token.group("zhour"), token.group("zmin"),
                        monthString, yearString));
                LOGGER.debug(Configs.getInstance().getString("LOC_TIME_DECODED_NXT_FCST_BY_DATE")
                        + " " + getNxtFcstByDate() + "\n");
            } catch (NumberFormatException | UtilsException e) {
                String errMsg = Configs.getInstance().getString("LOC_TIME_DECODED_UNABLE_PARSE_DATE_VALUE")
                        + " " + e;
                LOGGER.error(errMsg);
            }
        }
    }

    /**
     * Set the valid to and from date information
     *
     * @param token The valid to and from date information.
     */
    private void setValidToFromDateInfo(Matcher token) {
        if (token.group("bvaltime") != null && token.group("evaltime") != null) {
            // 1918/2018 means it is valid from the 19th 1800Z to the 20th 1800Z
            try {
                setValidFromDate(UtilsDate.setDate(token.group("bvaltime").substring(0, 2),
                        token.group("bvaltime").substring(2, 4), "00",
                        monthString, yearString));
                setValidToDate(UtilsDate.setDate(token.group("evaltime").substring(0, 2),
                        token.group("evaltime").substring(2, 4), "00",
                        monthString, yearString));
                LOGGER.debug(Configs.getInstance().getString("LOC_TIME_DECODED_VALID_FROM_DATE")
                        + " " + getValidFromDate());
                LOGGER.debug(Configs.getInstance().getString("LOC_TIME_DECODED_VALID_TO_DATE")
                        + " " + getValidToDate() + "\n");
            } catch (NumberFormatException | UtilsException e) {
                String errMsg = Configs.getInstance()
                        .getString("LOC_TIME_DECODED_UNABLE_PARSE_DATE_VALUE") + " " + e;
                LOGGER.error(errMsg);
            }
        }
    }

    /**
     * Get the full report string value. This is the report in its original form
     *
     * @return The original report string.
     */
    public String getReportString() {
        return reportString;
    }

    /**
     * Set the full report string value. This is the report in its original form
     *
     * @param reportString The original report string.
     */
    public void setReportString(String reportString) {
        this.reportString = reportString;
    }

    /**
     * Get stationID
     *
     * @return stationID
     */
    public String getStationID() {
        return stationID;
    }

    /**
     * Set stationID
     *
     * @param stationID
     */
    public void setStationID(String stationID) {
        this.stationID = stationID;
    }

    /**
     * Get dateString
     *
     * @return dateString
     */
    public String getDateString() {
        return dateString;
    }

    /**
     * Set dateString
     *
     * @param dateString
     */
    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    /**
     * Get date
     *
     * @return date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Set date
     *
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Get nxtFcstByDate
     *
     * @return date
     */
    public Date getNxtFcstByDate() {
        return nxtFcstByDate;
    }

    /**
     * Set nxtFcstByDate
     *
     * @param nxtFcstByDate
     */
    public void setNxtFcstByDate(Date nxtFcstByDate) {
        this.nxtFcstByDate = nxtFcstByDate;
    }

    /**
     * Get validFromDate
     *
     * @return date
     */
    public Date getValidFromDate() {
        return validFromDate;
    }

    /**
     * Set validFromDate
     *
     * @param validFromDate
     */
    public void setValidFromDate(Date validFromDate) {
        this.validFromDate = validFromDate;
    }

    /**
     * Get validToDate
     *
     * @return date
     */
    public Date getValidToDate() {
        return validToDate;
    }

    /**
     * Set validToDate
     *
     * @param validToDate
     */
    public void setValidToDate(Date validToDate) {
        this.validToDate = validToDate;
    }

    /**
     * Get isBecmg
     *
     * @return isBecmg
     */
    public boolean isIsBecmg() {
        return isBecmg;
    }

    /**
     * Set isBecmg
     *
     * @param isBecmg
     */
    public void setIsBecmg(boolean isBecmg) {
        this.isBecmg = isBecmg;
    }

    /**
     * Get becmgcntr
     *
     * @return becmgcntr
     */
    public Integer getBecmgcntr() {
        return becmgcntr;
    }

    /**
     * Set becmgcntr
     *
     * @param becmgcntr
     */
    public void setBecmgcntr(Integer becmgcntr) {
        this.becmgcntr = becmgcntr;
    }

    /**
     * Get dayTemp
     *
     * @return dayTemp
     */
    public String getDayTemp() {
        return dayTemp;
    }

    /**
     * Set dayTemp
     *
     * @param dayTemp
     */
    public void setDayTemp(String dayTemp) {
        this.dayTemp = dayTemp;
    }

    /**
     * Get reportModifier
     *
     * @return reportModifier
     */
    public String getReportModifier() {
        return reportModifier;
    }

    /**
     * Set reportModifier
     *
     * @param reportModifier
     */
    public void setReportModifier(String reportModifier) {
        this.reportModifier = reportModifier;
    }

    /**
     * Get yearString
     *
     * @return yearString
     */
    public String getYearString() {
        return yearString;
    }

    /**
     * Set yearString
     *
     * @param yearString
     */
    public void setYearString(String yearString) {
        this.yearString = yearString;
    }

    /**
     * Get monthString
     *
     * @return monthString
     */
    public String getMonthString() {
        return monthString;
    }

    /**
     * Set monthString
     *
     * @param monthString
     */
    public void setMonthString(String monthString) {
        this.monthString = monthString;
    }

    /**
     * Get isNoSignificantChange
     *
     * @return isNoSignificantChange NSC
     */
    public boolean isNoSignificantChange() {
        return isNoSignificantChange;
    }

    /**
     * Set isNoSignificantChange
     *
     * @param isNoSignificantChange
     */
    public void setIsNoSignificantChange(boolean isNoSignificantChange) {
        this.isNoSignificantChange = isNoSignificantChange;
    }

    /**
     * get runwayVisualRange
     *
     * @return runwayVisualRange less than
     */
    public RunwayVisualRange getRunwayVisualRange() {
        return runwayVisualRange;
    }

    /**
     * Get weatherCondition
     *
     * @return weatherCondition less than
     */
    public WeatherCondition getWeatherCondition() {
        return weatherCondition;
    }

    /**
     * Get skyCondition
     *
     * @return skyCondition less than
     */
    public SkyCondition getSkyCondition() {
        return skyCondition;
    }

    /**
     * Get weatherConditions
     *
     * @return weatherConditions
     */
    public IndexedLinkedHashMap<WeatherCondition, String> getWeatherConditions() {
        return weatherConditions;
    }

    /**
     * Get skyConditions
     *
     * @return skyConditions
     */
    public IndexedLinkedHashMap<SkyCondition, String> getSkyConditions() {
        return skyConditions;
    }

    /**
     * Get parseString
     *
     * @return parseString
     */
    public ArrayList<String> getParseString() {
        return parseString;
    }

    /**
     * Set parseString
     *
     * @param token
     */
    public void setParseString(Matcher token) {
        LOGGER.debug("Unparsed: #" + token.group("unparsed") + "#");
        this.parseString.add(token.group("unparsed"));
    }

    /**
     * Get visibility
     *
     * @return visibility object
     */
    public Visibility getVisibility() {
        return visibility;
    }

    /**
     * Get wind
     *
     * @return wind object
     */
    public Wind getWind() {
        return wind;
    }

    /**
     * Get temperature
     *
     * @return temperature object
     */
    public Temperature getTemperature() {
        return temperature;
    }

    /**
     * Get pressure
     *
     * @return pressure object
     */
    public Pressure getPressure() {
        return pressure;
    }

    /**
     * Set remarks
     *
     * @param remarks
     */
    public void setRemarks(Remarks remarks) {
        this.remarks = remarks;
    }

    /**
     * Get remarks
     *
     * @return remarks object
     */
    public Remarks getRemarks() {
        return remarks;
    }

    /**
     * Get runwayVisualRanges
     *
     * @return runwayVisualRanges
     */
    public IndexedLinkedHashMap<RunwayVisualRange, String> getRunwayVisualRanges() {
        return runwayVisualRanges;
    }

    /**
     * Get aviaMainWthHandlers
     *
     * @return aviaMainWthHandlers
     */
    public IndexedLinkedHashMap<Pattern, Pair<String, Boolean>> getAviaMainWeathHandlers() {
        return aviaMainWthHandlers;
    }

    /**
     * Get aviaRemarkWthHandlers
     *
     * @return aviaRemarkWthHandlers
     */
    public IndexedLinkedHashMap<Pattern, Pair<String, Boolean>> getAviaRemarkWeathHandlers() {
        return aviaRemarkWthHandlers;
    }

    /**
     * Get aviaGroupWthHandlers
     *
     * @return aviaGroupWthHandlers
     */
    public IndexedLinkedHashMap<Pattern, Pair<String, Boolean>> getAviaGroupWeathHandlers() {
        return aviaGroupWthHandlers;
    }

    /**
     * Get fromGroups
     *
     * @return fromGroups
     */
    public IndexedLinkedHashMap<FromGroup, String> getFromGroups() {
        return fromGroups;
    }

    /**
     * Get becomings
     *
     * @return becomings
     */
    public IndexedLinkedHashMap<Becoming, String> getBecomings() {
        return becomings;
    }

    /**
     * Get tempos
     *
     * @return tempos
     */
    public IndexedLinkedHashMap<Tempo, String> getTempos() {
        return tempos;
    }

    /**
     * Get probs
     *
     * @return probs
     */
    public IndexedLinkedHashMap<Prob, String> getProbs() {
        return probs;
    }
}
