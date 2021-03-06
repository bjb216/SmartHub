/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthub;

import java.util.Date;
import java.text.SimpleDateFormat;

public class DayWeather {
    //Initializing all global variables

    public Date date;
    public String weatherDescription;
    public float humidity;
    public float pressure;
    public int maxTemp;
    public int minTemp;
    public float windSpeed;
    public String imagePath;
    public String brandonPath = "/users/bartonb/Documents/GUIpics";
    public String mattPath = "/Users/Matthew/Documents/Senior Design/Weather Pics";
    public String piPath = "/home/pi/pictures";
    public String testPath =piPath;

    //Creating the Day constructor
    public DayWeather(Date day, String weather, float hum, float press, int max, int min, float wind) {
        date = day;
        weatherDescription = weather;
        humidity = hum;
        pressure = press;
        maxTemp = max;
        minTemp = min;
        windSpeed = wind;

    }

    public String getDay() {
        String dateString = null;
        //SimpleDateFormat sdfr = new SimpleDateFormat("dd/MMM/yyyy");
        SimpleDateFormat sdfr = new SimpleDateFormat("EE, MMM d");
        dateString = sdfr.format(date);
        return dateString;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public String getHumidity() {
        return Float.toString(humidity);
    }

    public String getPressure() {
        return Float.toString(pressure);
    }

    public String getMaxTemp() {
        return Integer.toString(maxTemp);
    }

    public String getMinTemp() {
        return Integer.toString(minTemp);
    }

    public String getWindSpeed() {
        return Float.toString(windSpeed);
    }

    public String getWeatherPic() {

        String description = weatherDescription;
        String pngPath = null;

        switch (description.toLowerCase()) {
            case "clear sky":
                pngPath = testPath + "/Sunny.png";
                break;
            //Snow    
            case "light snow":
            case "snow":
            case "heavy snow":
            case "sleet":
            case "shower sleet":
            case "light rain and snow":
            case "rain and snow":
            case "light shower snow":
            case "shower snow":
            case "heavy shower snow":
                pngPath =testPath +  "/Snow.png";
                break;

            case "light rain":
            case "moderate rain":
            case "heavy intensity rain":
            case "very heavy rain":
            case "extreme rain":
            case "freezing rain":
            case "light intensity shower rain":
            case "shower rain":
            case "heavy intensity shower rain":
            case "ragged shower rain":
                pngPath = testPath + "/Drizzle.png";
                break;

            case "thunderstorm with light rain":
            case "thunderstorm with rain":
            case "thunderstorm with heavy rain":
            case "light thunderstorm":
            case "thunderstorm":
            case "heavy thunderstorm":
            case "ragged thunderstorm":
            case "thunderstorm with light drizzle":
            case "thunderstorm with drizzle":
            case "thunderstorm with heavy drizzle":
                pngPath = testPath + "/Thunderstorms.png";
                break;

            case "mist":
            case "smoke":
            case "haze":
            case "sand whirls":
            case "dust whirls":
            case "fog":
            case "sand":
            case "dust":
            case "volcanic ash":
            case "squalls":
            case "tornado":
                pngPath = testPath + "/Haze.png";
                break;

            case "few clouds":
            case "scattered clouds":
            case "broken clouds":
            case "overcast clouds":
                pngPath = testPath + "/Cloudy.png";
                break;

            default:
                pngPath = testPath + "/Sunny.png";
                break;
        }

        return pngPath;
    }

    public void printForecast() {
        System.out.println("Weather Description: " + weatherDescription);
        System.out.println("Humidity: " + humidity);
        System.out.println("Pressure: " + pressure);
        System.out.println("Date Time: " + date);
        System.out.println("Wind Speed: " + windSpeed + " MPH");
        System.out.println("High: " + maxTemp + " °F");
        System.out.println("Low: " + minTemp + " °F");
    }
}
