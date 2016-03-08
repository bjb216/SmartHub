package smarthub;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import javax.bluetooth.RemoteDevice;

public class User {

    public RemoteDevice device;
    public String address;
    public String name;
    public String[] stocks={"AAPL","GOOG","VMW","INTC"};
    
    //CHECK TOKEN REFRESH ISSUES
    public GoogleCredential credential;
   
    public User(RemoteDevice device,String address, String name){
        this.device=device;
        this.address=address;
        this.name=name;
        this.credential=null;
    }
    
    //For testing purposes
    public User(String name){
        this.name=name;
    }
    
    public void setCredential(GoogleCredential credential){
        this.credential=credential;
    }

}
