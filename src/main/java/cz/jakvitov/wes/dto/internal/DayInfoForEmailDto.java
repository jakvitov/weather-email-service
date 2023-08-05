package cz.jakvitov.wes.dto.internal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Object containing specific info about a day to be filled in the templating engine to an email.
 */

public class DayInfoForEmailDto {

    private Map<String, ArrayList<LocalDateTime>> importantWeatherCodes;

    private Double averageMoningTemperature = 0D;

    private Double averageNoonTemperature = 0D;

    private Double averageAfternoonTemperature = 0D;

    //Defaultly the max/min value so during creation with EmailFormatter - we can safely compare every temperature with > <
    private Double minTemperature = Double.MAX_VALUE;

    private Double maxTemperature = Double.MIN_VALUE;

    private LocalDate day;

    public DayInfoForEmailDto() {
    }

    public Map<String, ArrayList<LocalDateTime>> getImportantWeatherCodes() {
        return importantWeatherCodes;
    }

    public void setImportantWeatherCodes(Map<String, ArrayList<LocalDateTime>> importantWeatherCodes) {
        this.importantWeatherCodes = importantWeatherCodes;
    }

    public Double getAverageMoningTemperature() {
        return averageMoningTemperature;
    }

    public void setAverageMoningTemperature(Double averageMoningTemperature) {
        this.averageMoningTemperature = averageMoningTemperature;
    }

    public Double getAverageNoonTemperature() {
        return averageNoonTemperature;
    }

    public void setAverageNoonTemperature(Double averageNoonTemperature) {
        this.averageNoonTemperature = averageNoonTemperature;
    }

    public Double getAverageAfternoonTemperature() {
        return averageAfternoonTemperature;
    }

    public void setAverageAfternoonTemperature(Double averageAfternoonTemperature) {
        this.averageAfternoonTemperature = averageAfternoonTemperature;
    }

    public Double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(Double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public Double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(Double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }
}
