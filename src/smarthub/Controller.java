package smarthub;

import com.google.api.services.calendar.model.Event;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import javax.swing.*;

/**
 * *GENERAL NOTES*** Box layout causes size of panel to be that of contents.
 * flow layout does not. need to fix BT device already found functionality
 * Change file path
 *
 *
 */
public class Controller {

    BTConnection con;
    TTS tts;
    JFrame frame;
    JPanel body;
    JPanel user;
    JPanel sidePanel;
    JLabel time;
    boolean pause;
    boolean change;
    User currentUser;

    ArrayList<User> users;
    Scanner scan;

    //final int height = 480;
    //final int width = 800;
    //final int height = 500;
    //final int width = 700;
    int height;
    int width;
    ArrayList<String> calText;
    ArrayList<String> weatherText;
    ArrayList<String> finText;
    int finSize = 0;

    public Controller() throws InterruptedException, IOException {
        pause = false;
        tts = new TTS();
        con = new BTConnection();
        scan = new Scanner(System.in);
        users = new ArrayList<>();
        calText = new ArrayList<>();
        weatherText = new ArrayList<>();
        finText = new ArrayList<>();
        currentUser = null;

        init();
        frame = new JFrame();
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.getContentPane().setPreferredSize(screensize);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        gd.setFullScreenWindow(frame);

        width = screensize.width;
        height = screensize.height;

        //frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        //frame.setUndecorated(true);
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getContentPane().setLayout((new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS)));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(topPanel(width, height / 8));

        body = bodyPanel(width, height * 7 / 8);

        frame.getContentPane().add(body);

        user = bodyPanel(width * 7 / 8, height * 7 / 8);
        body.add(user);
        body.add(sidePanel(width / 8, height * 7 / 8));

        //frame.setBackground(Color.blue);
        //frame.pack();
        frame.setVisible(true);

    }

    private void init() throws InterruptedException, IOException {
//        User user = con.pair();
        //      if (user != null) {
        //        users.add(user);
        //  }

        users.add(new User("Brandon"));

        users.add(new User("Matt"));
        users.add(new User("Jenna"));

    }

    public void run() throws IOException, JSONException, URISyntaxException {

        change = false;

        //wont work if higher order user enters the room
        //check if user is the same as from the previous scan
        int i = 0;
        JPanel userPanel = null;
        while (true) {
            //System.out.println("number of users: " + users.size());
            i = (i + 1) % 6;
            //User current = scan();
            scanTest(i);
            //scan();
            if (currentUser == null && pause == false && change) {
                //System.out.println("removing");
                user.remove(userPanel);
                user.revalidate();
                user.repaint();
                change = false;
            } else if (!change && pause == false && currentUser != null) {
                //text.clear();
                userPanel = userPanel(currentUser, width * 7 / 8, height * 7 / 8);
                user.add(userPanel);
                user.revalidate();
                user.repaint();
                change = true;
                //createTTS(currentUser);

            } else {
                //System.out.println("still in range");
            }
            delay(5);
            updateTime();
        }
    }

    private void createTTS(User user) {
        int i = 0;
        String message = "Hello " + user.name + ". ";
        /*System.out.println(text.get(i));
        if (!text.get(i).equals("0")) {
            System.out.println("in if");
            i++;
            message = message.concat("Your first event is " + text.get(i) + " at");
            i++;
            message = message.concat(text.get(i));
        }*/
        
        if (user.calTalk == false){
            message = message.concat("calTalk: FALSE ");
        }else{
            message = message.concat("You have " + calText.get(i) + " events on your calendar for the rest of today. ");
            if (!calText.get(i).equals("0")) {
                System.out.println("in if");
                i++;
                message = message.concat("Your first event is " + calText.get(i) + " at ");
                i++;
                message = message.concat(calText.get(i) + ". ");
            }
        }
        
        if (user.weatherTalk == false){
            message = message.concat("weatherTalk: FALSE ");
        }else{
            message = message.concat("The weather forecast for " + weatherText.get(0) + " is " +
                    weatherText.get(1) + " with a high temperature of " + weatherText.get(2) + " and a low of "+
                    weatherText.get(3) + ". ");
        }
        
        if (user.finTalk == false){
            message = message.concat("calTalk: FALSE ");
        }else{
            message = message.concat("The important updates from the financial market are: ");
            int size = finText.size();
            int fin = 0;
            for (fin = 0; fin< size; fin++){
                message = message.concat(" " + finText.get(fin));
            }
            
        }
        message = message.concat(" I hope the rest of your day goes well!");
        System.out.println("**************************************************");
        System.out.println(message);
        System.out.println("**************************************************");
        //tts.speak(message);

    }

    private void scanTest(int i) {
        if (i < 40) {
            currentUser = users.get(0);
        } else {
            //currentUser = null;
        }
    }

    private JPanel userPanel(User user, int width, int height) throws IOException, JSONException, URISyntaxException {
        JPanel panel = new JPanel();
        //panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setLayout(new FlowLayout());
        panel.setPreferredSize(new Dimension(width, height));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel calendarPanel;
        JPanel weatherPanel;
        JPanel stockPanel;

        /*
        if(user.calSmall==true)
            calendarPanel=calendarPanel(user, (int) (width/2.05), height / 2);
        else
            calendarPanel=calendarPanel(user, (int) (width/1.1), height / 2);
         */
        if (user.weatherSmall == true) {
            weatherPanel = weatherPanel(user, (int) (width / 2.05), height / 2);
        } else {
            weatherPanel = weatherPanel(user, (int) (width/1.05), height / 2);
        }

        if (user.finSmall == true) {
            stockPanel = stockPanel(user, (int) (width / 2.05), height / 2);
        } else {
            stockPanel = stockPanel(user, (int) (width/1.05), height / 2);
        }

        
        panel.add(stockPanel);
        //panel.add(stockPanel);
        panel.add(weatherPanel);
        //panel.add(calendarPanel);
        return panel;

    }

    private JPanel topPanel(int width, int height) {
        Font font = new Font("Verdana", Font.BOLD, 20);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setPreferredSize(new Dimension(width, height));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        //Dark blue
        panel.setBackground(new Color(33, 83, 129));

        JLabel title = new JLabel("SmartHub");
        title.setPreferredSize(new Dimension((int) (width / 2.5), height));
        title.setFont(font);
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        DateFormat dateFormat = new SimpleDateFormat("EEEE MM/dd/yyyy hh:mm:ss a");
        Date d = new Date();
        time = new JLabel(dateFormat.format(d));
        //date.setPreferredSize(new Dimension((int) (width / 2.5), height));
        time.setAlignmentX(Component.RIGHT_ALIGNMENT);

        time.setFont(font);
        time.setForeground(Color.WHITE);

        panel.add(title);
        panel.add(time);

        return panel;
    }

    private void updateTime(){
        DateFormat dateFormat = new SimpleDateFormat("EEEE MM/dd/yyyy hh:mm:ss a");
        Date d = new Date();
        time.setText(dateFormat.format(d));
    }
    
    private JPanel weatherPanel(User user, int width, int height) throws IOException, JSONException {
        Color c = new Color(130, 130, 130);
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setPreferredSize(new Dimension(width, height));
        panel.setBackground(c);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        //Initializations
        //Inserting Weather from the Weather class
        ArrayList<DayWeather> multiForecast = new ArrayList<DayWeather>();
        ArrayList<HourlyWeather> hourForecast = new ArrayList<HourlyWeather>();
        
        MyWeather w = new MyWeather("New York");
        //MyWeather w = new MyWeather("Bethlehem");
        
        int numDays;
        int numHours;
        double divisor;
        if(user.weatherSmall==true){
            numHours = 4;//Getting the number of hours for the forecast panel
            numDays = 1;
            divisor = (numDays + 1) * 1.4;
            
            w.multiDayForecast(multiForecast, numDays);
            w.hourlyForecast(hourForecast, numHours);
            w.printHourlyForecast(hourForecast);
            
            //Creating the Left side day Panel
            for (int i = 0; i < numDays; i++) {
                JPanel dayPanel = new JPanel();
                dayPanel.setLayout(new BoxLayout(dayPanel, BoxLayout.PAGE_AXIS));
                dayPanel.setPreferredSize(new Dimension((int) (width / divisor), height));
                dayPanel.setBackground(c);
                dayPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
                panel.add(dayPanel);

                //Getting the Date
                if (i == 0) {
                    weatherText.add(multiForecast.get(i).getDay());
                }
                JLabel dayLabel = new JLabel(multiForecast.get(i).getDay());
                dayLabel.setPreferredSize(new Dimension((int) (width / divisor), height / 10));
                dayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                dayPanel.add(dayLabel);

                //Getting the Forecast
                if (i == 0) {
                    weatherText.add((multiForecast.get(i).getWeatherDescription()));
                }
                dayLabel = new JLabel((multiForecast.get(i).getWeatherDescription()));
                dayLabel.setPreferredSize(new Dimension((int) (width / divisor), height / 10));
                dayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                dayPanel.add(dayLabel);

                //Getting the Max and Min Temp
                if (i == 0) {
                    weatherText.add(multiForecast.get(i).getMaxTemp() + " °F");
                    weatherText.add(multiForecast.get(i).getMinTemp() + " °F");
                }
                dayLabel = new JLabel(multiForecast.get(i).getMaxTemp() + "°F / " + multiForecast.get(i).getMinTemp() + " °F");
                dayLabel.setPreferredSize(new Dimension((int) (width / divisor), height / 10));
                dayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                dayPanel.add(dayLabel);

                //Adding the Icon to the JPanel
                String picPath = multiForecast.get(i).getWeatherPic();
                ImageIcon weather = new ImageIcon(picPath);
                JLabel picLabel = new JLabel(weather);
                picLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                dayPanel.add(picLabel);
            }
            //Creating the Hourly Panel
            JPanel hourPanel = new JPanel();
            //hourPanel.setLayout(new BoxLayout(hourPanel, BoxLayout.PAGE_AXIS));
            hourPanel.setLayout(new FlowLayout());
            hourPanel.setPreferredSize(new Dimension((int) (width / divisor), height));
            hourPanel.setBackground(c);
            hourPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(hourPanel);
            
             for (int j=0; j < numHours; j++){
                JLabel hourLabel = new JLabel(hourForecast.get(j).getDay());
                hourLabel.setPreferredSize(new Dimension((int) ((width / divisor)/1.5), height / 10));
                hourLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                hourPanel.add(hourLabel);
                
                hourLabel = new JLabel((hourForecast.get(j).getTemp()));
                hourLabel.setPreferredSize(new Dimension((int) (width / divisor), height / 10));
                hourLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                hourPanel.add(hourLabel);
                
                //Adding the Icon to the JPanel
                String picPath = hourForecast.get(j).getWeatherPic();
                ImageIcon weather = new ImageIcon(picPath);
                JLabel picLabel = new JLabel(weather);
                picLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                hourPanel.add(picLabel);
             }

        }else{
            numDays = 7;//Getting the full week's Forecast
            divisor = numDays * 1.05;

            //Initilaizing the array
            w.multiDayForecast(multiForecast, numDays);
            w.printMultiDayForecast(multiForecast);

            

            for (int i = 0; i < numDays; i++) {
                JPanel dayPanel = new JPanel();
                dayPanel.setLayout(new BoxLayout(dayPanel, BoxLayout.PAGE_AXIS));
                dayPanel.setPreferredSize(new Dimension((int) (width / divisor), height));
                dayPanel.setBackground(c);
                dayPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
                panel.add(dayPanel);

                //Getting the Date
                if (i == 0) {
                    weatherText.add(multiForecast.get(i).getDay());
                }
                JLabel dayLabel = new JLabel(multiForecast.get(i).getDay());
                dayLabel.setPreferredSize(new Dimension((int) (width / divisor), height / 10));
                dayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                dayPanel.add(dayLabel);

                //Getting the Forecast
                if (i == 0) {
                    weatherText.add((multiForecast.get(i).getWeatherDescription()));
                }
                dayLabel = new JLabel((multiForecast.get(i).getWeatherDescription()));
                dayLabel.setPreferredSize(new Dimension((int) (width / divisor), height / 10));
                dayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                dayPanel.add(dayLabel);

                //Getting the Max and Min Temp
                if (i == 0) {
                    weatherText.add(multiForecast.get(i).getMaxTemp() + " °F");
                    weatherText.add(multiForecast.get(i).getMinTemp() + " °F");
                }
                dayLabel = new JLabel(multiForecast.get(i).getMaxTemp() + "°F / " + multiForecast.get(i).getMinTemp() + " °F");
                dayLabel.setPreferredSize(new Dimension((int) (width / divisor), height / 10));
                dayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                dayPanel.add(dayLabel);

                //Adding the Icon to the JPanel
                String picPath = multiForecast.get(i).getWeatherPic();
                ImageIcon weather = new ImageIcon(picPath);
                JLabel picLabel = new JLabel(weather);
                picLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                dayPanel.add(picLabel);
                
               
            }

        }
        
        
        return panel;
    }

    //Yellow
    private JPanel bodyPanel(int width, int height) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setPreferredSize(new Dimension(width, height));

        //Light gray
        //panel.setBackground(new Color(217, 217, 217));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    private JPanel sidePanel(int width, int height) {
        sidePanel = new JPanel();
        //panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        sidePanel.setLayout(new FlowLayout());
        sidePanel.setPreferredSize(new Dimension(width, height));

        //Slightly lighter blue than top
        sidePanel.setBackground(new Color(46, 117, 182));
        sidePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        //JLabel label = new JLabel("initial");
        //panel.add(label);
        for (int i = 0; i < users.size(); i++) {
            JButton button = new JButton(users.get(i).name);
            sidePanel.add(button);

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    loadPreferences(getUserFromString(button.getText()));
                }
            });
        }
        JButton button = new JButton("Pair");
        sidePanel.add(button);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (button.getText().equals("Pair")) {
                    pairUser();
                }
                //tts.speak(button.getText());
                //label.setText(button.getText() + " pressed");
            }
        });

        return sidePanel;
    }

    private User getUserFromString(String str) {
        if (users.isEmpty()) {
            return null;
        } else {
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).name.equalsIgnoreCase(str)) {
                    return users.get(i);
                }
            }
            return null;
        }
    }

    private void loadPreferences(User usr) {
        pause = true;
        user.removeAll();
        user.revalidate();
        user.repaint();

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setPreferredSize(new Dimension(user.getPreferredSize()));
        panel.setBackground(new Color(100, 100, 100));

        JLabel title = new JLabel(usr.name + "'s Preferences");
        title.setPreferredSize(new Dimension(400, 20));
        panel.add(title);
        
        //panel.add(addSpace(user.getWidth(), (int) (user.getHeight() / 2.2)));
        JButton inRoom = new JButton("I am in the Room");
        panel.add(inRoom);

        JButton close = new JButton("Close");
        panel.add(close);

        JLabel finLabel = new JLabel("Financial data:");
        JLabel calLabel = new JLabel("Calendar data:");
        JLabel weatherLabel = new JLabel("Weather data:");

        //finLabel.setPreferredSize(new Dimension(user.getWidth()/8,10));
        //calLabel.setPreferredSize(new Dimension(user.getWidth()/8,10));
        //weatherLabel.setPreferredSize(new Dimension(user.getWidth()/8,10));
        JButton finState = new JButton(usr.getState("financial"));
        JButton calState = new JButton(usr.getState("calendar"));
        JButton weatherState = new JButton(usr.getState("weather"));
        
        finState.setPreferredSize(new Dimension(115,40));
        calState.setPreferredSize(new Dimension(115,40));
        weatherState.setPreferredSize(new Dimension(115,40));

        JButton finTalk = new JButton(usr.getTalk("financial"));
        JButton calTalk = new JButton(usr.getTalk("calendar"));
        JButton weatherTalk = new JButton(usr.getTalk("weather"));
        
        finTalk.setPreferredSize(new Dimension(115,40));
        calTalk.setPreferredSize(new Dimension(115,40));
        weatherTalk.setPreferredSize(new Dimension(115,40));

        panel.add(finLabel);
        panel.add(finState);
        panel.add(finTalk);
        panel.add(addSpace(300, 60));

        panel.add(calLabel);
        panel.add(calState);
        panel.add(calTalk);
        panel.add(addSpace(300, 60));

        panel.add(weatherLabel);
        panel.add(weatherState);
        panel.add(weatherTalk);
        panel.add(addSpace(300, 60));

        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pause = false;
                change = false;
                user.remove(panel);
                user.revalidate();
                user.repaint();

            }
        });

        finState.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                usr.changeState("financial");
                finState.setText(usr.getState("financial"));

            }
        });

        weatherState.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                usr.changeState("weather");
                weatherState.setText(usr.getState("weather"));

            }
        });

        calState.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                usr.changeState("calendar");
                calState.setText(usr.getState("calendar"));

            }
        });

        finTalk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                usr.changeTalk("financial");
                finTalk.setText(usr.getTalk("financial"));

            }
        });

        weatherTalk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                usr.changeTalk("weather");
                weatherTalk.setText(usr.getTalk("weather"));

            }
        });

        calTalk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                usr.changeTalk("calendar");
                calTalk.setText(usr.getTalk("calendar"));

            }
        });

        inRoom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentUser = usr;
                pause = false;
                change = false;
                user.remove(panel);
                user.revalidate();
                user.repaint();

            }
        });

        user.add(panel);
        user.revalidate();
        user.repaint();
    }
    
    private JLabel addSpace(int width, int height) {
        JLabel space = new JLabel("");
        space.setPreferredSize(new Dimension(width, height));
        return space;
    }

    private void pairUser() {
        try {
            user.removeAll();
            user.revalidate();
            user.repaint();
            pause = true;
            User usr = con.pair(user, frame);
            System.out.println("returning from pair");
            if (usr != null) {
                users.add(usr);
                addButton(usr);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            pause = false;
        }
    }
    
    private void addButton(User user){
        JButton button = new JButton(user.name);
            sidePanel.add(button);

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    loadPreferences(getUserFromString(button.getText()));
                }
            });
    }

    //Orange
    private JPanel calendarPanel(User user, int width, int height) throws IOException, URISyntaxException {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setPreferredSize(new Dimension(width, height));
        panel.setBackground(new Color(125, 125, 125));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        GCalendar cal = new GCalendar(user);
        ArrayList<Event> meetings = cal.meetings;

        int w;
        if(user.calSmall==false){
            w=width/4;
        }
        else{
            w=width/2;
        }
        
        JLabel label = new JLabel("Time");
        label.setPreferredSize(new Dimension(width / 4, height / 10));
        panel.add(label);

        label = new JLabel("Title");
        label.setPreferredSize(new Dimension(w, height / 10));
        panel.add(label);

        if(user.calSmall==false){
        label = new JLabel("Location");
        label.setPreferredSize(new Dimension(width / 4, height / 10));
        panel.add(label);
        }

        //text.add(String.valueOf(meetings.size()));
        for (int i = 0; i < meetings.size(); i++) {
            String time;
            if(user.calSmall)
                time=cal.getTime(meetings.get(i),false);
            else
                time=cal.getTime(meetings.get(i),true);
            
            label = new JLabel(time);
            label.setPreferredSize(new Dimension(width / 4, height / 10));
            panel.add(label);

            label = new JLabel(meetings.get(i).getSummary());
            label.setPreferredSize(new Dimension(w, height / 10));
            panel.add(label);

            if(user.calSmall==false){
            label = new JLabel(meetings.get(i).getLocation());
            label.setPreferredSize(new Dimension(width / 4, height / 10));
            panel.add(label);
            }
            
            
            if (i == 0) {
                //text.add(meetings.get(i).getSummary());
                //text.add(cal.getTime(meetings.get(i)));
            }
        }

        return panel;
    }

    private JPanel stockPanel(User user, int width, int height) throws IOException {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setPreferredSize(new Dimension(width, height));

        //Making the stock arrayList
        ArrayList<SingleStock> stockList = new ArrayList<SingleStock>();
        FinancialData test = new FinancialData(user);
        test.populateStockArray(user.stocks, stockList);
        test.printInfo();

        //slightly darker gray
        panel.setBackground(new Color(148, 148, 148));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        int h=25;
        if(user.finSmall==false){
        panel.add(addSpace(300,10));
        JLabel label = new JLabel("% chg");
        label.setPreferredSize(new Dimension(85,h));
        panel.add(label);
        label = new JLabel("YTD chg");
        label.setPreferredSize(new Dimension(90,h));
        panel.add(label);
        label = new JLabel("Div yield");
        label.setPreferredSize(new Dimension(75,h));
        panel.add(label);
        }
        
        
        for (int i = 0; i < stockList.size(); i++) {
            if (user.finSmall == false) {
                JLabel label = new JLabel(stockList.get(i).name);
                label.setPreferredSize(new Dimension(150,h));
                panel.add(label);
            }

            JLabel label = new JLabel(stockList.get(i).getTicker());
            label.setPreferredSize(new Dimension(75,h));
            panel.add(label);

            label = new JLabel("$" + stockList.get(i).getCost());
            label.setPreferredSize(new Dimension(75,h));
            panel.add(label);

            //Adding the Icon to the JPanel
            String picPath = stockList.get(i).getStockPic(stockList.get(i).stockPercentChange);
            ImageIcon stockDirection = new ImageIcon(picPath);
            JLabel picLabel = new JLabel(stockDirection);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(picLabel);

            label = new JLabel(stockList.get(i).getPercentChange() + "%");
            label.setPreferredSize(new Dimension(75,h));
            panel.add(label);

            if (user.finSmall == false) {
                picPath = stockList.get(i).getStockPic(stockList.get(i).ytdChange);
                stockDirection = new ImageIcon(picPath);
                picLabel = new JLabel(stockDirection);
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                panel.add(picLabel);

                label = new JLabel(stockList.get(i).ytdChange + "%");
                label.setPreferredSize(new Dimension(75,h));
                panel.add(label);
                
                label = new JLabel(stockList.get(i).divYield + "%");
                label.setPreferredSize(new Dimension(75,h));
                panel.add(label);
            }

            //label = addSpace(4 * (width / 4), height / 10);
            panel.add(label);

        }

        return panel;
    }

    private void scan() {
//        return users.get(0);
        for (int i = 0; i < users.size(); i++) {
            if (con.connect(users.get(i))) {
                //System.out.println("found a user");
                currentUser = users.get(i);
                return;
            }
        }
        currentUser = null;
    }

    public static void delay(int time) {
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            //Handle exception
        }
    }

}
