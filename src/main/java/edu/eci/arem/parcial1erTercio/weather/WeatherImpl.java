package edu.eci.arem.parcial1erTercio.weather;

import edu.eci.arem.parcial1erTercio.nanospark.components.NanoSparkException;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WeatherImpl implements WeatherService{
    private String urlApi = "api.openweathermap.org/data/2.5/weather?q=";
    private String apiKey = "&appid=1ed571629ef45ccfcd520ecc8fd1043a";

    public String getWeatherByName(String city) throws NanoSparkException {
        //api.openweathermap.org/data/2.5/weather?q=London&appid=1ed571629ef45ccfcd520ecc8fd1043a
        String weather = "";
        try {
            URL url = new URL(urlApi+city+apiKey);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();

            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {

                StringBuilder response = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();

                System.out.println(response);

                weather = response.toString();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weather;
    }
}
