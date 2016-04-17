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
import net.aksingh.owmjapis.HourlyForecast;

public class MyWeather {

    public OpenWeatherMap owm;
    String city;
    
    //Constructor
    public MyWeather(String city) {
        owm = new OpenWeatherMap("457fbd57a1d3f89a7c4db388f84338bf");
        this.city = city;
    }

    /*Create a day object that will have high temp, low temp, boolean precipitation, wind
      buld a day object, build a weekly array
     */
    public void multiDayForecast(ArrayList forecastArray, int dayNumber) throws IOException, JSONException {
        System.out.println("weekly");
        byte size = 0;
        int index = 0;
        DailyForecast daily = owm.dailyForecastByCityName(city, size);
        //HourlyForecast h = owm.hourlyForecastByCityName(city);
        HourlyForecast hourly = owm.hourlyForecastByCityName(city);
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
        
       /* System.out.println("***********HOURLY TEST************");
        System.out.println(hourly.getForecastInstance(0).getMainInstance().getMaxTemperature());
        System.out.println(hourly.getForecastInstance(0).getDateTimeText());
        System.out.println(hourly.getForecastInstance(1).getDateTimeText());
        System.out.println(hourly.getForecastInstance(2).getDateTimeText());
        System.out.println(hourly.getForecastInstance(0).getMainInstance().getTemperature());
        System.out.println(hourly.getForecastInstance(0).getWeatherInstance(0).getWeatherDescription());
        System.out.println(hourly.getForecastInstance(0).getWeatherInstance(0).getWeatherName());
        System.out.println("**********************************");
        */
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

    //Hourly Forecast
    public void hourlyForecast(ArrayList forecastArray, int hourNumber) throws IOException, JSONException {
        byte size = 0;
        int index = 0;
        DailyForecast daily = owm.dailyForecastByCityName(city, size);
        HourlyForecast hourly = owm.hourlyForecastByCityName(city);
        City c = daily.getCityInstance();
        int count = hourNumber;
        
        //Weather input variables
        String weather;
        Date hourTime;
        Float temp = null;

        System.out.println(c.getCityName() + " " + c.getCountryCode());
        
        /*System.out.println("***********HOURLY TEST************");
        System.out.println(hourly.getForecastInstance(0).getMainInstance().getMaxTemperature());
        System.out.println(hourly.getForecastInstance(0).getDateTimeText());
        System.out.println(hourly.getForecastInstance(1).getDateTimeText());
        System.out.println(hourly.getForecastInstance(2).getDateTimeText());
        System.out.println(hourly.getForecastInstance(0).getMainInstance().getTemperature());
        System.out.println(hourly.getForecastInstance(0).getWeatherInstance(0).getWeatherDescription());
        System.out.println(hourly.getForecastInstance(0).getWeatherInstance(0).getWeatherName());
        System.out.println("**********************************");
        */
        
        //Changed the i from beign 0 to 1
        for (int i = 0; i < (hourNumber); i++) {
            /*Forecast ft = daily.getForecastInstance(0);//Having the problem here
            Weather w = ft.getWeatherInstance(0);
            Temperature t = ft.getTemperatureInstance();*/
           
            hourTime = hourly.getForecastInstance(i).getDateTime();
            weather = hourly.getForecastInstance(i).getWeatherInstance(0).getWeatherDescription();
            temp = hourly.getForecastInstance(i).getMainInstance().getTemperature();
          

            HourlyWeather tempHour = new HourlyWeather(hourTime, weather, temp);

            forecastArray.add(tempHour);
            
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
    
    public void printHourlyForecast(ArrayList<HourlyWeather> forecast) {
        //Changed i to be a 1 instead of a 0
        for (int i = 0; i < forecast.size(); i++) {
            System.out.println("");
            System.out.println("Hour Number: " + (i));
            forecast.get(i).printHourForecast();
            System.out.println("");
        }
        //System.out.println("The Size of the Arraylist is: " + forecast.size());
    }

    public void getCurrent() throws IOException, JSONException {
        //CurrentWeather cwd = owm.currentWeatherByCityName("Philidelphia").;
        //cwd.getMainInstance().
        //cwd.
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
