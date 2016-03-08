package smarthub;

import java.awt.FlowLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import javax.swing.*;

public class Controller {

    BTConnection con;
    JFrame frame;

    
    ArrayList<User> users;
    String[] menuItems = {"Pair users", "Current paired users", "Scan area", "Weather","Calendar",
        "Financial","Speech","exit"};
    Scanner scan;

    public Controller() {
        frame = new JFrame();
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        con = new BTConnection();
        scan = new Scanner(System.in);
        users = new ArrayList<>();
        //JFrame 
        //For testing purposes
        users.add(new User("Brandon's Phone"));
    }

    public void run(){
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JLabel label = new JLabel("This is a label!");
 
        JButton button = new JButton();
        button.setText("Press me");
        panel.add(label);
        panel.add(button);
        frame.add(panel);
        

        int i=0;
        while(true){
            i=(i+1)%menuItems.length;
            button.setText(menuItems[i]);
            delay(3);
        }
    }
    
    public void run2() throws InterruptedException, IOException, JSONException {

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
                    GCalendar cal= new GCalendar(users.get(0));
                    cal.printCalendar();
                    break;
                case 6:
                    financial();
                    break;
                case 7:
                    //TextToSpeech tts=new TextToSpeech("We are going to graduate");
                    //tts.speak();
                    break;
                case 8:
                    System.out.println("goodbye");
                    System.exit(0);
                    break;
            }
        }

    }
    
    private void financial() throws IOException{
        FinancialData data= new FinancialData(users.get(0));
        data.printInfo();
        
    }
    
    private void weather() throws IOException, JSONException{
        System.out.println("testing weather function");
        MyWeather w=new MyWeather("New York");
        w.doSomething();
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
