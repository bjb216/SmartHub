/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthub;

/**
 *
 * @author Matthew
 */
import java.util.Date;
import java.text.SimpleDateFormat;

public class HourlyWeather {

    public Date timeOne;
    public String weatherOne;
    public Float tempOne;
    public String imagePath;
    public String brandonPath = "/users/bartonb/Documents/GUIpics";
    public String mattPath = "/Users/Matthew/Documents/Senior Design/Condensed Weather Pics";
    public String piPath = "/home/pi/pictures/condensed";
    public String testPath = piPath;

    //public HourlyWeather(Date tOne, String wOne, Date tTwo, String wTwo,
    //                          Date tThree, String wThree, Date tFour, String wFour) {
    public HourlyWeather(Date tOne, String wOne, Float temOne) {
        timeOne = tOne;
        weatherOne = wOne;
        tempOne = temOne;

        /* 
        timeTwo = tTwo;
        timeThree = tThree;
        timeFour = tFour;
       
        weatherTwo = wTwo;
        weatherThree = wThree;
        weatherFour = wFour;
         */
    }

    public void printHourForecast() {
        System.out.println("Date Time: " + timeOne);
        System.out.println("Weather Description: " + weatherOne);

    }

    /*public String getTimeString(){
         String[] timeString = null;
         String tString = null;
         
         timeString = timeOne.split(" ");
         tString = timeString[1];
         
         String[] tempString = null;
         tempString = tString.split(":");
         tString = tempString[0] + ":" + tempString[1];
         tempString = tString.split(":");
         tString = tempString[0];
         
         int tempInt = Integer.parseInt(tString);
         if (tempInt > 12){
             tempInt = tempInt - 12;
             tString = Integer.toString(tempInt) + ":00 PM";
         }else if(tempInt == 24){
             tString = "12:00 AM";
         } else if (tempInt == 12) {
             tString = "12:00 PM";
         } else if (tempInt == 0) {
             tString = "12:00 AM";
         } else {
             tString = Integer.toString(tempInt) + ":00 AM";
         }
         
         
         
         return tString;
     }
     */
    public String getDay() {
        String dateString = null;
        //SimpleDateFormat sdfr = new SimpleDateFormat("dd/MMM/yyyy");
        SimpleDateFormat sdfr = new SimpleDateFormat("h:mm a");
        dateString = sdfr.format(timeOne);
        return dateString;
    }

    public String getTemp() {
        /*String[] timeString = null;
        String tString = Float.toString(tempOne);
        String tempString = Float.toString(tempOne);*/

        int temp = Math.round(tempOne);

        /*timeString = tString.split(".");
        tString = timeString[1];*/
        return Integer.toString(temp) + " °F";
    }

    public String getWeatherPic() {

        String description = weatherOne;
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
                pngPath = testPath + "/Snow.png";
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

}
