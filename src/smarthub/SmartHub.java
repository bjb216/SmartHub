package smarthub;

import java.io.IOException;
import javax.bluetooth.BluetoothStateException;
import org.json.JSONException;



public class SmartHub {
    public static void main(String[] args) throws BluetoothStateException, InterruptedException, IOException, JSONException {
        Controller con = new Controller();
        con.run();
    }
}
