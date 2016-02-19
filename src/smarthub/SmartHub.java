package smarthub;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import static javax.bluetooth.DiscoveryAgent.PREKNOWN;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.microedition.io.Connector;
import javax.obex.ClientSession;

public class SmartHub {
    public static final String bjbid = "703EAC1A1042";
    public static final String mdbid = "10D542EFEC45";

    public static void main(String[] args) throws BluetoothStateException, InterruptedException, IOException {
        Controller con = new Controller();
        con.run();
       
    }

}
