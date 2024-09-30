package com.kodilla.outdoor.equiprent.controller;

import com.kodilla.outdoor.equiprent.dto.WeatherMapDto;
import com.kodilla.outdoor.equiprent.external.open.weather.map.client.OpenWeatherMapClient;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@DisplayName("Tests for WeatherForecastController")
@SpringJUnitWebConfig
@WebMvcTest(WeatherForecastController.class)
public class WeatherForecastControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OpenWeatherMapClient openWeatherMapClient;

    @Test
    @DisplayName("Test for retrieving weather forecast")
    void shouldRetrieveWeatherForecast() throws Exception {
        // Given
        double latitude = 53.16536;
        double longitude = 19.39033;

        // CoordinatesDto
        WeatherMapDto.CoordinatesDto coordinatesDto = new WeatherMapDto.CoordinatesDto();
        coordinatesDto.setLatitude(latitude);
        coordinatesDto.setLongitude(longitude);

        // CityDto
        WeatherMapDto.CityDto cityDto = new WeatherMapDto.CityDto();
        cityDto.setId(1);
        cityDto.setName("ExampleCity");
        cityDto.setCoordinates(coordinatesDto);
        cityDto.setCountry("PL");
        cityDto.setPopulation(100000);
        cityDto.setTimezone(7200);
        cityDto.setSunrise(1633065600L);
        cityDto.setSunset(1633108800L);

        // SysDto
        WeatherMapDto.SysDto sysDto = new WeatherMapDto.SysDto();
        sysDto.setPod("d");

        // SnowDto
        WeatherMapDto.SnowDto snowDto = new WeatherMapDto.SnowDto();
        snowDto.setVolumeFor3h(0.0);

        // RainDto
        WeatherMapDto.RainDto rainDto = new WeatherMapDto.RainDto();
        rainDto.setVolumeFor3h(0.2);

        // WindDto
        WeatherMapDto.WindDto windDto = new WeatherMapDto.WindDto();
        windDto.setSpeed(5.5);
        windDto.setDeg(120);
        windDto.setGust(8.2);

        // CloudsDto
        WeatherMapDto.CloudsDto cloudsDto = new WeatherMapDto.CloudsDto();
        cloudsDto.setAll(75);

        // WeatherDto
        WeatherMapDto.WeatherDto weatherDto = new WeatherMapDto.WeatherDto();
        weatherDto.setId(800);
        weatherDto.setMain("Clear");
        weatherDto.setDescription("clear sky");
        weatherDto.setIcon("01d");

        // MainDto
        WeatherMapDto.MainDto mainDto = new WeatherMapDto.MainDto();
        mainDto.setTemp(15.0);
        mainDto.setFeelsLike(14.5);
        mainDto.setTempMin(13.0);
        mainDto.setTempMax(16.0);
        mainDto.setPressure(1015);
        mainDto.setSeaLevel(1015);
        mainDto.setGroundLevel(1000);
        mainDto.setHumidity(78);

        // WeatherForecastDto
        WeatherMapDto.WeatherForecastDto weatherForecastDto = new WeatherMapDto.WeatherForecastDto();
        weatherForecastDto.setDt(1633076400L);
        weatherForecastDto.setMain(mainDto);
        weatherForecastDto.setWeather(List.of(weatherDto));
        weatherForecastDto.setClouds(cloudsDto);
        weatherForecastDto.setWind(windDto);
        weatherForecastDto.setVisibility(10000);
        weatherForecastDto.setPop(0.0);
        weatherForecastDto.setRain(rainDto);
        weatherForecastDto.setSnow(snowDto);
        weatherForecastDto.setSys(sysDto);
        weatherForecastDto.setDtTxt("2024-09-24 12:00:00");

        // WeatherMapDto
        WeatherMapDto weatherMapDto = new WeatherMapDto();
        weatherMapDto.setWeatherForecasts(List.of(weatherForecastDto));
        weatherMapDto.setCity(cityDto);

        Mockito.when(openWeatherMapClient.getWeatherForecast(latitude, longitude)).thenReturn(weatherMapDto);
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/weather")
                        .param("latitude", "53.16536")
                        .param("longitude", "19.39033")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                // WeatherForecastDto
                .andExpect(MockMvcResultMatchers.jsonPath("$.list.[0].dt", Matchers.is(1633076400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.list[0].visibility", Matchers.is(10000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.list[0].pop", Matchers.is(0.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.list[0].dt_txt", Matchers.is("2024-09-24 12:00:00")))
                // MainDto
                .andExpect(MockMvcResultMatchers.jsonPath("$.list[0].main.temp", Matchers.is(15.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.list[0].main.feels_like", Matchers.is(14.5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.list[0].main.temp_min", Matchers.is(13.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.list[0].main.temp_max", Matchers.is(16.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.list[0].main.pressure", Matchers.is(1015)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.list[0].main.sea_level", Matchers.is(1015)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.list[0].main.grnd_level", Matchers.is(1000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.list[0].main.humidity", Matchers.is(78)))
                // WeatherDto
                .andExpect(MockMvcResultMatchers.jsonPath("$.list[0].weather[0].id", Matchers.is(800)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.list[0].weather[0].main", Matchers.is("Clear")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.list[0].weather[0].description", Matchers.is("clear sky")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.list[0].weather[0].icon", Matchers.is("01d")))
                // CloudsDto
                .andExpect(MockMvcResultMatchers.jsonPath("$.list[0].clouds.all", Matchers.is(75)))
                // WindDto
                .andExpect(MockMvcResultMatchers.jsonPath("$.list[0].wind.speed", Matchers.is(5.5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.list[0].wind.deg", Matchers.is(120)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.list[0].wind.gust", Matchers.is(8.2)))
                // RainDto
                .andExpect(MockMvcResultMatchers.jsonPath("$.list[0].rain.3h", Matchers.is(0.2)))
                // SnowDto
                .andExpect(MockMvcResultMatchers.jsonPath("$.list[0].snow.3h", Matchers.is(0.0)))
                // SysDto
                .andExpect(MockMvcResultMatchers.jsonPath("$.list[0].sys.pod", Matchers.is("d")))
                // CityDto
                .andExpect(MockMvcResultMatchers.jsonPath("$.city.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city.name", Matchers.is("ExampleCity")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city.coord.lat", Matchers.is(53.16536)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city.coord.lon", Matchers.is(19.39033)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city.country", Matchers.is("PL")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city.population", Matchers.is(100000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city.timezone", Matchers.is(7200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city.sunrise", Matchers.is(1633065600)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city.sunset", Matchers.is(1633108800)));
    }
}