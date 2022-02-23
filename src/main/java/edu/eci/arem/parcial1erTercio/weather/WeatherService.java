package edu.eci.arem.parcial1erTercio.weather;

import edu.eci.arem.parcial1erTercio.nanospark.components.NanoSparkException;

public interface WeatherService {
    String getWeatherByName(String city) throws NanoSparkException;

}
