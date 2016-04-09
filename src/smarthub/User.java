package smarthub;



import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import javax.bluetooth.RemoteDevice;

public class User {

    public RemoteDevice device;
    public String address;
    public String name;
    public String[] stocks={"AAPL","GOOG","VMW","INTC"};
    
    
    //PREFERENCES
    public boolean finSmall;
    public boolean weatherSmall;
    public boolean calSmall;
    public boolean finTalk;
    public boolean weatherTalk;
    public boolean calTalk;
    
    //CHECK TOKEN REFRESH ISSUES
    public GoogleCredential credential;
   
    public User(RemoteDevice device,String address, String name){
        this.device=device;
        this.address=address;
        this.name=name;
        this.credential=null;
        finSmall=true;
        weatherSmall=true;
        calSmall=true;
        finTalk=true;
        weatherTalk=true;
        calTalk=true;
        
    }
    
    //For testing purposes
    public User(String name){
        this.name=name;
    }
    
    public void setCredential(GoogleCredential credential){
        this.credential=credential;
    }
    
    public String getState(String str){
        switch (str) {
            case "financial":
                if(finSmall==true)
                    return "Condensed";
                else
                    return "Extended";
            case "weather":
                if(weatherSmall==true)
                    return "Condensed";
                else
                    return "Extended";
            case "calendar":
                if(calSmall==true)
                    return "Condensed";
                else
                    return "Extended";
            default:
                break;
        }
        return null;
    }
    
    public String getTalk(String str){
        switch (str) {
            case "financial":
                if(finTalk==true)
                    return "Audio";
                else
                    return "No Audio";
            case "weather":
                if(weatherTalk==true)
                    return "Audio";
                else
                    return "No Audio";
            case "calendar":
                if(calTalk==true)
                    return "Audio";
                else
                    return "No Audio";
            default:
                break;
        }
        return null;
    }
    
    public void changeState(String str){
        switch (str) {
            case "financial":
                finSmall = !finSmall;
                break;
            case "calendar":
                calSmall = !calSmall;
                break;
            case "weather":
                weatherSmall = !weatherSmall;
                break;
            default:
                break;
        }
        
    }
    
    public void changeTalk(String str){
        switch (str) {
            case "financial":
                finTalk = !finTalk;
                break;
            case "calendar":
                calTalk = !calTalk;
                break;
            case "weather":
                weatherTalk = !weatherTalk;
                break;
            default:
                break;
        }
        
    }

}
