package smarthub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.BluetoothStateException;
import org.json.JSONException;

public class Controller {

    BTConnection con;
    ArrayList<User> users;
    String[] menuItems = {"Pair users", "Current paired users", "Scan area", "Weather","Calendar", "exit"};
    Scanner scan;

    public Controller() {
        con = new BTConnection();
        //initialize();
        scan = new Scanner(System.in);
        users = new ArrayList<>();
    }

    public void run() throws InterruptedException, IOException, JSONException {

        while (true) {
            System.out.println("Welcome to SmartHub");
            for (int i = 0; i < menuItems.length; i++) {
                System.out.println((i + 1) + ": " + menuItems[i]);
            }
            int choice = scan.nextInt();
            switch (choice) {
                case 1:
                    User user = con.pair();
                    if (user != null) {
                        users.add(user);
                    }
                    break;
                case 2:
                    printCurrentUsers();
                    break;
                case 3:
                    scan();
                    break;
                case 4:
                    weather();
                    break;
                case 5:
                    GCalendar cal= new GCalendar();
                    cal.doStuff();
                    break;
                case 6:
                    System.out.println("goodbye");
                    System.exit(0);
            }
        }

    }
    private void weather() throws IOException, JSONException{
        System.out.println("testing weather function");
        MyWeather w=new MyWeather("New York");
        //w.doSomething();
        w.getWeekly();
    }
    
    private void scan(){
        while(true){
            for(int i=0;i<users.size();i++){
                if(con.connect(users.get(i))){
                    System.out.println(users.get(i).name+" in range");
                }
                else{
                    System.out.println(users.get(i).name+" not in range");
                }
                    
            }
            delay(2);
        }
    }
    private void delay(int time) {
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            //Handle exception
        }
    }

    private void printCurrentUsers() {
        if (users.isEmpty()) {
            System.out.println("No current users paired");
        }
        System.out.println("Current paired users");
        for (int i = 0; i < users.size(); i++) {
            System.out.println("Device: " + users.get(i).device);
            System.out.println("Address: " + users.get(i).address);
            System.out.println("Name: " + users.get(i).name);
            System.out.println("\n");
        }

    }

}
