package cz.jakvitov.wes.exception;

/**
 * Exception to be thrown when city is not found.
 */
public class CityNotFoundException extends RuntimeException{

    String cityName;
    String countryISO;

    public CityNotFoundException(String cityName, String countryISO) {
        this.cityName = cityName;
        this.countryISO = countryISO;
    }

    public CityNotFoundException(String message, String cityName, String countryISO) {
        super(message);
        this.cityName = cityName;
        this.countryISO = countryISO;
    }

    public CityNotFoundException(String message, Throwable cause, String cityName, String countryISO) {
        super(message, cause);
        this.cityName = cityName;
        this.countryISO = countryISO;
    }

    public CityNotFoundException(Throwable cause, String cityName, String countryISO) {
        super(cause);
        this.cityName = cityName;
        this.countryISO = countryISO;
    }

    public CityNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String cityName, String countryISO) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.cityName = cityName;
        this.countryISO = countryISO;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryISO() {
        return countryISO;
    }

    public void setCountryISO(String countryISO) {
        this.countryISO = countryISO;
    }
}
