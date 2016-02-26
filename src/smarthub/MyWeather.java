/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author bartonb
 */
public class MyWeather {
    public OpenWeatherMap owm;
    String city;
    //Constructer
    public MyWeather(String city) {
        owm=new OpenWeatherMap("457fbd57a1d3f89a7c4db388f84338bf");
        this.city=city;
    }

    public void getHourly(){
        
    }
    
    public void getWeekly() throws IOException, JSONException{
        System.out.println("weekly");
        byte size=0;
        DailyForecast daily = owm.dailyForecastByCityName(city, size);
        City c=daily.getCityInstance();
        System.out.println(c.getCityName()+" "+c.getCountryCode());
        //System.out.println(daily.getCityInstance());
        
        int count=daily.getForecastCount();
        for(int i=0;i<count;i++){
            Forecast ft=daily.getForecastInstance(i);
            Weather w=ft.getWeatherInstance(0);
            Temperature t=ft.getTemperatureInstance();
            
            System.out.println("Day "+i);
            System.out.println(w.getWeatherDescription());
            System.out.println("High: "+t.getMaximumTemperature());
            System.out.println("Low: "+t.getMinimumTemperature());
            System.out.println();
            //System.out.println(w.getWeatherName());
            
        }

        //System.out.println("forecasts available: "+daily.getForecastCount());

        //System.out.println("weather available: "+ft.getWeatherCount());
       //ft.
    }
    
    public void getCurrent() throws IOException, JSONException{
        //CurrentWeather cwd = owm.currentWeatherByCityName("Philidelphia");
        //cwd.getMainInstance().
    }
    
    //public void
    
    public void doSomething() throws IOException, JSONException {
        OpenWeatherMap owm = new OpenWeatherMap("457fbd57a1d3f89a7c4db388f84338bf");
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
