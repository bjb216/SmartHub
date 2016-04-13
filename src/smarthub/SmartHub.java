package smarthub;

import java.io.IOException;
import java.net.URISyntaxException;
import javax.bluetooth.BluetoothStateException;
import org.json.JSONException;

public class SmartHub {
    public static void main(String[] args) throws BluetoothStateException, InterruptedException, IOException, JSONException, URISyntaxException {
        
        //BTConnection con = new BTConnection();
        
        Controller con = new Controller();
        con.run();
    }
}
