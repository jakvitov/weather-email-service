package cz.jakvitov.wes.dto.types;

/**
 * Options, that can be added to Open meteo api get request as path parameters to specify the request
 */

public enum WeatherOptions {

    DAYS("forecast_days"),
    LATITUDE("latitude"),
    LONGITUDE("longitude"),
    HOURLY_OPTIONS("hourly"),
    TEMPERATURE("temperature_2m"),
    WIND_SPEED("windspeed_10m"),
    RAIN("rain"),
    WEATHER_CODE("weathercode");


    private final String value;

    WeatherOptions(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
