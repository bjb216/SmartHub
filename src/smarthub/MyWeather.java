package smarthub;

import java.io.IOException;
import net.aksingh.owmjapis.AbstractForecast.City;
import net.aksingh.owmjapis.AbstractWeather.Weather;
import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.DailyForecast;
import net.aksingh.owmjapis.DailyForecast.Forecast;
import net.aksingh.owmjapis.DailyForecast.Forecast.Temperature;
import net.aksingh.owmjapis.OpenWeatherMap;
import org.json.JSONException;
import java.util.Date;
import java.util.ArrayList;

public class MyWeather {

    public OpenWeatherMap owm;
    String city;

    
    
    //Constructer
    public MyWeather(String city) {
        owm = new OpenWeatherMap("457fbd57a1d3f89a7c4db388f84338bf");
        this.city = city;
    }

    //Create a Day object
    public void getHourly() {

    }

    /*Create a day object that will have high temp, low temp, boolean precipitation, wind
      buld a day object, build a weekly array
     */
    public void multiDayForecast(ArrayList forecastArray, int dayNumber) throws IOException, JSONException {
        System.out.println("weekly");
        byte size = 0;
        DailyForecast daily = owm.dailyForecastByCityName(city, size);
        City c = daily.getCityInstance();
        int count = dayNumber;

        //Weather input variables
        String weather;
        float humidity;
        float pressure;
        Date date;
        float wind;
        int highTemp;
        int lowTemp;

        System.out.println(c.getCityName() + " " + c.getCountryCode());
        //System.out.println(daily.getCityInstance());

        //Changed the i from beign 0 to 1
        for (int i = 0; i < dayNumber; i++) {
            Forecast ft = daily.getForecastInstance(i);//Having the problem here
            Weather w = ft.getWeatherInstance(0);
            Temperature t = ft.getTemperatureInstance();

            weather = w.getWeatherDescription();
            humidity = ft.getHumidity();
            pressure = ft.getPressure();
            date = ft.getDateTime();
            wind = ft.getWindSpeed();
            highTemp = (int) t.getMaximumTemperature();
            lowTemp = (int) t.getMinimumTemperature();

            DayWeather tempDay = new DayWeather(date, weather, humidity, pressure, highTemp, lowTemp, wind);

            forecastArray.add(tempDay);
        }
    }

    public void printMultiDayForecast(ArrayList<DayWeather> forecast) {
        //Changed i to be a 1 instead of a 0
        for (int i = 0; i < forecast.size(); i++) {
            System.out.println("");
            System.out.println("Day of the Weather Number: " + (i + 1));
            forecast.get(i).printForecast();
            System.out.println("");
        }
        //System.out.println("The Size of the Arraylist is: " + forecast.size());
    }

    public void getDaily() throws IOException, JSONException {
        System.out.println("Daily");
        byte size = 0;
        DailyForecast daily = owm.dailyForecastByCityName(city, size);
        City c = daily.getCityInstance();
        System.out.println(c.getCityName() + " " + c.getCountryCode());

        //Changed this as it used to be 1*************
        int count = 1;
        for (int i = 0; i < count; i++) {
            Forecast ft = daily.getForecastInstance(i);
            Weather w = ft.getWeatherInstance(0);
            Temperature t = ft.getTemperatureInstance();

            String weather = w.getWeatherDescription();
            float humidity = ft.getHumidity();
            float pressure = ft.getPressure();
            Date date = ft.getDateTime();
            float wind = ft.getWindSpeed();
            int highTemp = (int) t.getMaximumTemperature();
            int lowTemp = (int) t.getMinimumTemperature();

            DayWeather newDay = new DayWeather(date, weather, humidity, pressure, highTemp, lowTemp, wind);
            newDay.printForecast();
        }
    }

    public void getCurrent() throws IOException, JSONException {
        //CurrentWeather cwd = owm.currentWeatherByCityName("Philidelphia");
        //cwd.getMainInstance().
    }

    //public void
    public void doSomething() throws IOException, JSONException {
        //OpenWeatherMap owm = new OpenWeatherMap("457fbd57a1d3f89a7c4db388f84338bf");
        // getting current weather data for the "London" city
        //CurrentWeather cwd2 = owm.
        CurrentWeather cwd = owm.currentWeatherByCityName("Bethlehem");
        //cwd.
        //cwd.
//printing city name from the retrieved data
        System.out.println("City: " + cwd.getCityName());
// printing the max./min. temperature
        System.out.println("Temperature: " + cwd.getMainInstance().getMaxTemperature()
                + "/" + cwd.getMainInstance().getMinTemperature() + "\'F");

    }

}
