/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthub;
import java.util.Date;

public class DayWeather {
     //Initializing all global variables
    public Date date;
    public String weatherDescription;
    public float humidity;
    public float pressure;
    public float maxTemp;
    public float minTemp;
    public float windSpeed;
    
    //Creating the Day constructor
    public DayWeather(Date day, String weather, float hum, float press, float max, float min, float wind) {
        date = day;
        weatherDescription = weather;
        humidity = hum;
        pressure = press;
        maxTemp = max;
        minTemp = min;
        windSpeed = wind;
        
    }
    
    public Date getDay(){
        return date;
    }
    
    public String getWeatherDescription(){
        return weatherDescription;
    }
    
    public float getHumidity(){
        return humidity;
    }
    
    public float getPressure(){
        return pressure;
    }
    
    public float getMaxTemp(){
        return maxTemp;
    }
    
    public float getMinTemp(){
        return minTemp;
    }
    
    public float getWindSpeed(){
        return windSpeed;
    }
    
    public void printForecast(){
        System.out.println("Weather Description: " + weatherDescription);
        System.out.println("Humidity: " + humidity);
        System.out.println("Pressure: " + pressure);
        System.out.println("Date Time: " + date);
        System.out.println("Wind Speed: " + windSpeed + " MPH");
        System.out.println("High: "+ maxTemp + " °F");
        System.out.println("Low: "+ minTemp + " °F");
    }
}
