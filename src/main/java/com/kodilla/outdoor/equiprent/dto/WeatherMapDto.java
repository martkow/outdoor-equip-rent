package com.kodilla.outdoor.equiprent.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherMapDto {
    @Schema(description = "List of weather forecasts with data every 3 hours")
    @JsonProperty("list")
    private List<WeatherForecastDto> weatherForecasts;
    @Schema(description = "City information")
    private CityDto city;

    @Data
    public static class WeatherForecastDto {
        @Schema(description = "Time of data forecasted, unix, UTC")
        private long dt;
        @Schema(description = "Main weather data")
        private MainDto main;
        @Schema(description = "Weather condition information")
        private List<WeatherDto> weather;
        @Schema(description = "Cloudiness information")
        private CloudsDto clouds;
        @Schema(description = "Wind information")
        private WindDto wind;
        @Schema(description = "Visibility in metres, maximum 10km")
        private int visibility;
        @Schema(description = "Probability of precipitation (0 to 1)")
        private double pop;
        @Schema(description = "Rain information")
        private RainDto rain;
        @Schema(description = "Snow information")
        private SnowDto snow;
        @Schema(description = "Part of the day (d - day, n - night)")
        private SysDto sys;
        @Schema(description = "Time of data forecasted in ISO format, UTC")
        @JsonProperty("dt_txt")
        private String dtTxt;
    }

    @Data
    public static class MainDto {
        @Schema(description = "Temperature in Celsius")
        private double temp;
        @Schema(description = "Feels like temperature in Celsius")
        @JsonProperty("feels_like")
        private double feelsLike;
        @Schema(description = "Minimum temperature in Celsius")
        @JsonProperty("temp_min")
        private double tempMin;
        @Schema(description = "Maximum temperature in Celsius")
        @JsonProperty("temp_max")
        private double tempMax;
        @Schema(description = "Atmospheric pressure on sea level, hPa")
        private int pressure;
        @Schema(description = "Atmospheric pressure on sea level, hPa")
        @JsonProperty("sea_level")
        private int seaLevel;
        @Schema(description = "Atmospheric pressure on ground level, hPa")
        @JsonProperty("grnd_level")
        private int groundLevel;
        @Schema(description = "Humidity in %")
        private int humidity;
    }

    @Data
    public static class WeatherDto {
        @Schema(description = "Weather condition ID")
        private int id;
        @Schema(description = "Group of weather parameters (Rain, Snow, Clouds, etc.)")
        private String main;
        @Schema(description = "Detailed weather description")
        private String description;
        @Schema(description = "Weather icon ID")
        private String icon;
    }

    @Data
    public static class CloudsDto {
        @Schema(description = "Cloudiness in %")
        private int all;
    }

    @Data
    public static class WindDto {
        @Schema(description = "Wind speed in meter/sec")
        private double speed;
        @Schema(description = "Wind direction in degrees")
        private int deg;
        @Schema(description = "Wind gust speed in meter/sec")
        private double gust;
    }

    @Data
    public static class RainDto {
        @Schema(description = "Rain volume for last 3 hours, mm")
        @JsonProperty("3h")
        private double volumeFor3h;
    }

    @Data
    public static class SnowDto {
        @Schema(description = "Snow volume for last 3 hours, mm")
        @JsonProperty("3h")
        private double volumeFor3h;
    }

    @Data
    public static class SysDto {
        @Schema(description = "Part of the day (n - night, d - day)")
        private String pod;
    }

    @Data
    public static class CityDto {
        @Schema(description = "City ID")
        private int id;
        @Schema(description = "City name")
        private String name;
        @Schema(description = "Geo location")
        @JsonProperty("coord")
        private CoordinatesDto coordinates;
        @Schema(description = "Country code (e.g., GB, JP)")
        private String country;
        @Schema(description = "City population")
        private int population;
        @Schema(description = "Shift in seconds from UTC")
        private int timezone;
        @Schema(description = "Sunrise time in Unix format, UTC")
        private long sunrise;
        @Schema(description = "Sunset time in Unix format, UTC")
        private long sunset;
    }

    @Data
    public static class CoordinatesDto {
        @Schema(description = "Geo location latitude")
        @JsonProperty("lat")
        private double latitude;
        @Schema(description = "Geo location longitude")
        @JsonProperty("lon")
        private double longitude;
    }
}
