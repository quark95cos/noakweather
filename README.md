# noakweather
noakweather project

This Java library provides a Metar and TAF decoder.

## What is METAR?
METAR (Meteorological Terminal Air Report) is current weather report format used in aviation. Typical METAR report contains information such as location,
report issue time, wind, visibility, clouds, weather phenomena, temperature, dewpoint and atmospheric pressure.

METAR in raw form is human-readable though it might look cryptic for an untrained person.

Examples of a METAR report is as follows:

2021/12/28 01:52 KCLT 280152Z 22006KT 10SM BKN240 17/13 A2989 RMK AO2 SLP116 T01720133

2021/12/28 01:53 KSEG 280153Z AUTO VRB03KT 7SM OVC014 01/00 A2983 RMK AO2 RAB35E50UPB50E53 SLP104 P0002 T00110000


## What is TAF?
TAF (Terminal Aerodrome Forecast) is a weather forecast report format used in aviation. A TAF report is quite similar to METAR and reports
trends and changes in visibility, wind, clouds, weather, etc over periods of time.

TAF in raw form is also human-readable but requires training to decode.

Examples of a TAF report is as follows:

2021/12/28 02:52 TAF AMD KCLT 280150Z 2802/2906 21006KT P6SM SCT040 BKN150 FM281100 22005KT P6SM SCT008 BKN015 FM281500 22007KT P6SM BKN020
FM281700 21012G18KT P6SM BKN040 FM282300 21010G17KT P6SM SCT050 BKN200

2021/12/28 00:00 TAF TAF KDOV 280000Z 2800/2906 08006KT 9999 OVC030 QNH2979INS TEMPO 2800/2804 8000 -SHRA TEMPO 2806/2810 VRB06KT BECMG 2809/2810
30009KT 9999 BKN020 OVC030 QNH2980INS BECMG 2815/2816 31006KT 9999 BKN120 QNH2989INS BECMG 2819/2820 27006KT 9999 BKN100 QNH2987INS BECMG 2823/2824
09006KT 8000 -RA OVC080 QNH2985INS BECMG 2903/2904 12006KT 8000 -RA OVC050 QNH2983INS


## Build project
To build it, you will need to download and unpack the latest (or recent) version of Maven. Now you can run mvn clean install and Maven will compile
your project, and put the results in the target directory. Also, there is a shell script provided named wethb.sh that will also compile the project.


## Run project
The decoder requires 4 parameters

Type of data: m - metar or t - taf
Station 4-letter ICAO code: For example KCLT or kclt
Print results: N or Y
Logging of run: I - Info, W - Warnings (includes info), D - Debug (includes info and warnings)

A shell script is provided named weth.sh. To run normally run logging as I for info. If there is any error or there is unparsed data found run logging
as D for debug to see why the error or unparsed data is occurring.


## Unparsed data
If for any reason any METAR or TAF data that cannot be parsed is marked as unparsed. Below are examples of such an output. You can review the code and
add the unparsed data accordingly and/or please open an issue.

Unparsed Data is as follows
[510202, TX11/2820Z, TN05/2811Z]

Unparsed Data is as follows
[I1000]


## Issues
If you notice any problems with running this, please open an issue.


## Which groups Metar is able to recognize?

-   Report type METAR, SPECI, and TAF
-   Various amended or correctional report indicators
-   Indicators for missing report, cancelled TAF report, and various indicator for missing data given in remarks section
-   Automated report indicator, automated station type remark, and maintenance indicator
-   ICAO location
-   Report issue time
-   Wind direction, speed and gust speed
-   Wind shear, peak wind and wind shift information
-   Prevailing or directional visibility (including variable visibility groups specified in remarks) in meters or statute miles
-   Surface visibility and visibility from air traffic control tower.
-   Cloud layer information, clear sky conditions, 'no significant cloud' / 'no cloud detected information', and detailed cloud layers information
    specified in remarks
-   Cloud cover of variable density and variable ceiling height
-   Indicator of no significant cloud and good visibility CAVOK
-   Indicatiors for certain secondary locations (e.g. wind shear in the lower levels at path of runway approach, ceiling, etc), and indicators for
    missing visibility or ceiling data.
-   Current and recent weather information, and indicator or weather phenomena end NSW
-   Beginning and ending time of recent weather phenomena
-   Temperature and dew point, including more precise values given in remarks
-   Temperature forecast from TAF reports
-   6-hourly and 24-hourly minimum and maximum temperature
-   Current atmospheric pressure, including QNH, QFE, SLP remarks, atmospheric pressure tendency remark, and groups indicating rapid pressure rise or fall
-   Forecast lowest atmospheric pressure
-   Runway visual range with trend
-   Temperature and state of sea surface or wave height
-   Trend groups NOSIG, BECMG, TEMPO, INTER, FMxxxxxx and various time span groups
-   Groups indicating non-operational sensors
-   Icing and turbulence forecast used by NATO militaries
-   Recent precipitation beginning and ending time
-   Lightning data such as strike type and frequency
-   Groups reporting various phenomena in vicinity of the station (thunderstorm, towering cumulus, altocumulus castellanus, cumulonimbus, mammatus,
    lenticular and rotor clouds, virga, etc.)
-   Density altitude
