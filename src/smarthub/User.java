package smarthub;

import javax.bluetooth.RemoteDevice;

public class User {

    public RemoteDevice device;
    public String address;
    public String name;
    
    
    
    public User(RemoteDevice device,String address, String name){
        this.device=device;
        this.address=address;
        this.name=name;
    }

}
