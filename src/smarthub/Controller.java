package smarthub;

import com.google.api.services.calendar.model.Event;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import javax.swing.*;

/**
 * *GENERAL NOTES*** Box layout causes size of panel to be that of contents.
 * flow layout does not. need to fix BT device already found functionality
 *Change file path
 *
 *
 */
public class Controller {

    BTConnection con;
    TTS tts;
    JFrame frame;
    JPanel body;
    JPanel user;
    boolean pause;

    ArrayList<User> users;
    String[] menuItems = {"Pair users", "Current paired users", "Scan area", "Weather", "Calendar",
        "Financial", "Speech", "exit"};
    Scanner scan;
    //final int height = 480;
    //final int width = 800;

    final int height = 400;
    final int width = 900;
    ArrayList<String> text;

    public Controller() throws InterruptedException, IOException {
        pause = false;
        tts = new TTS();
        con = new BTConnection();
        scan = new Scanner(System.in);
        users = new ArrayList<>();
        text = new ArrayList<>();

        init();
        frame = new JFrame();

        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
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

    public void run() throws IOException, JSONException {

        boolean change = false;

        //wont work if higher order user enters the room
        //check if user is the same as from the previous scan
        int i = 0;
        JPanel userPanel = null;
        while (true) {
            System.out.println("number of users: " + users.size());
            i = (i + 1) % 6;
            //User current = scan();
            User current = scanTest(i);
            if (current == null && pause == false && change) {
                //System.out.println("removing");
                user.remove(userPanel);
                user.revalidate();
                user.repaint();
                change = false;
            } else if (!change && pause == false && current != null) {
                text.clear();
                userPanel = userPanel(current, width * 7 / 8, height * 7 / 8);
                user.add(userPanel);
                user.revalidate();
                user.repaint();
                change = true;
                //createTTS(current);

            } else {
                //System.out.println("still in range");
            }
            delay(2);
        }
    }

    private void createTTS(User user) {
        int i = 0;
        String message = "Hello" + user.name + ". You have" + text.get(i) + " events on your calendar for the rest of today. ";
        System.out.println(text.get(i));
        if (!text.get(i).equals("0")) {
            System.out.println("in if");
            i++;
            message = message.concat("Your first event is " + text.get(i) + " at");
            i++;
            message = message.concat(text.get(i));
        }
        tts.speak(message);

    }

    private User scanTest(int i) {
        if (i < 40) {
            return users.get(0);
        } else {
            return null;
        }
    }

    private JPanel userPanel(User user, int width, int height) throws IOException, JSONException {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(width, height));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        //JPanel calendarPanel = calendarPanel(user, width, height / 2);
        //panel.add(calendarPanel);
        JPanel weatherPanel = weatherPanel(user, width, height / 2);
        panel.add(weatherPanel);

        JPanel stockPanel = stockPanel(user, width, height / 2);
        panel.add(stockPanel);

        return panel;

    }

    private JPanel topPanel(int width, int height) {
        Font font= new Font("Verdana",Font.BOLD,20);
        
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

        JLabel date = new JLabel("Thursday April 7     11:24AM");
        date.setPreferredSize(new Dimension((int) (width / 2.5), height));
        date.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        date.setFont(font);
        date.setForeground(Color.WHITE);

        panel.add(title);
        panel.add(date);

        return panel;
    }

    private JPanel weatherPanel(User user, int width, int height) throws IOException, JSONException {
        Color c=new Color(130,130,130);
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setPreferredSize(new Dimension(width, height));
        panel.setBackground(c);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        //Inserting Weather from the Weather class
        ArrayList<DayWeather> multiForecast = new ArrayList<DayWeather>();
        MyWeather w = new MyWeather("New York");
        w.multiDayForecast(multiForecast, 7);
        w.printMultiDayForecast(multiForecast);

        for (int i = 0; i < multiForecast.size(); i++) {
            JPanel dayPanel = new JPanel();
            dayPanel.setLayout(new BoxLayout(dayPanel, BoxLayout.PAGE_AXIS));
            //dayPanel.setLayout(new FlowLayout());
            dayPanel.setPreferredSize(new Dimension(width / 8, height));
            dayPanel.setBackground(c);
            dayPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(dayPanel);

            //Getting the Date
            JLabel dayLabel = new JLabel(multiForecast.get(i).getDay());
            dayLabel.setPreferredSize(new Dimension((width / 8), height / 10));
            dayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            dayPanel.add(dayLabel);

            //Getting the Forecast
            dayLabel = new JLabel((multiForecast.get(i).getWeatherDescription()).toUpperCase());
            dayLabel.setPreferredSize(new Dimension(width / 8, height / 10));
            dayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            dayPanel.add(dayLabel);

            //Getting the Max and Min Temp
            dayLabel = new JLabel(multiForecast.get(i).getMaxTemp() + "°F / " + multiForecast.get(i).getMinTemp() + " °F");
            dayLabel.setPreferredSize(new Dimension(width / 8, height / 10));
            dayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            dayPanel.add(dayLabel);

            //Adding the Icon to the JPanel
            String picPath = multiForecast.get(i).getWeatherPic();
            ImageIcon weather = new ImageIcon(picPath);
            JLabel picLabel = new JLabel(weather);
            dayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            //dayLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            dayPanel.add(picLabel);
            /*
             //Getting the Humidity
             label = new JLabel(multiForecast.get(i).getHumidity());
             label.setPreferredSize(new Dimension(width / 8, height / 10));
             panel.add(label);
             
             //Getting the Wind Speed
             label = new JLabel(multiForecast.get(i).getWindSpeed());
             label.setPreferredSize(new Dimension(width / 8, height / 10));
             panel.add(label);
             
             //Getting the Pressure
             label = new JLabel(multiForecast.get(i).getPressure());
             label.setPreferredSize(new Dimension(width / 8, height / 10));
             panel.add(label);
             */

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
        JPanel panel = new JPanel();
        //panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setLayout(new FlowLayout());
        panel.setPreferredSize(new Dimension(width, height));

        //Slightly lighter blue than top
        panel.setBackground(new Color(46, 117, 182));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        //JLabel label = new JLabel("initial");
        //panel.add(label);
        for (int i = 0; i < users.size(); i++) {
            JButton button = new JButton(users.get(i).name);
            panel.add(button);

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //label.setText(button.getText() + " pressed");
                }
            });
        }
        JButton button = new JButton("Pair");
        panel.add(button);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (button.getText().equals("Pair")) {
                    pairUser();
                }
                //tts.speak(button.getText());
                //label.setText(button.getText() + " pressed");
            }
        });

        return panel;
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
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            pause = false;
        }
    }

    //Orange
    private JPanel calendarPanel(User user, int width, int height) throws IOException {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setPreferredSize(new Dimension(width, height));
        panel.setBackground(new Color(125,125,125));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        GCalendar cal = new GCalendar(user);
        ArrayList<Event> meetings = cal.meetings;

        JLabel label = new JLabel("Time");
        label.setPreferredSize(new Dimension(width / 4, height / 10));
        panel.add(label);

        label = new JLabel("Title");
        label.setPreferredSize(new Dimension(width / 4, height / 10));
        panel.add(label);

        label = new JLabel("Location");
        label.setPreferredSize(new Dimension(width / 4, height / 10));
        panel.add(label);

        text.add(String.valueOf(meetings.size()));
        for (int i = 0; i < meetings.size(); i++) {
            label = new JLabel(cal.getTime(meetings.get(i)));
            label.setPreferredSize(new Dimension(width / 4, height / 10));
            panel.add(label);

            label = new JLabel(meetings.get(i).getSummary());
            label.setPreferredSize(new Dimension(width / 4, height / 10));
            panel.add(label);

            label = new JLabel(meetings.get(i).getLocation());
            label.setPreferredSize(new Dimension(width / 4, height / 10));
            panel.add(label);

            if (i == 0) {
                text.add(meetings.get(i).getSummary());
                text.add(cal.getTime(meetings.get(i)));
            }
        }

        return panel;
    }

    private JPanel stockPanel(User user, int width, int height) throws IOException {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setPreferredSize(new Dimension(width, height));
        FinancialData test= new FinancialData(user);
        test.printInfo();

        //slightly darker gray
        panel.setBackground(new Color(148, 148, 148));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (int i = 0; i < user.stocks.length; i++) {
            JLabel label = new JLabel(user.stocks[i]);
            panel.add(label);
        }

        return panel;
    }

    private User scan() {
//        return users.get(0);
        for (int i = 0; i < users.size(); i++) {
            if (con.connect(users.get(i))) {
                System.out.println("found a user");
                return users.get(i);
            }
        }
        return null;
    }

    public static void delay(int time) {
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            //Handle exception
        }
    }

}
