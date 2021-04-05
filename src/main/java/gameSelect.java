import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class gameSelect {

    public gameSelect(Session[] session) {



        JFrame frame = new JFrame();                        // creates fields
        JPanel mainPanel = new JPanel();
        JPanel titlePanel = new JPanel();
        JPanel selectPanel = new JPanel();

        JLabel title = new JLabel("Choose your game");      // creates title label
        title.setFont(new Font("Verdana", Font.PLAIN, 70));
        title.setBorder(BorderFactory.createEmptyBorder(100, 30, 100, 30)); // padding for spacing
        titlePanel.add(title);                                  // adds title label to title panel

        JButton select = new JButton("Play Game");       // creates select game button
        select.setFont(new Font("Verdana", Font.PLAIN, 50));

        select.addActionListener(new ActionListener() {         // waiting for button to be clicked
            @Override
            public void actionPerformed(ActionEvent e) {

                if (session.length == 1) {

                    try {
                        Runtime.getRuntime().exec("\"C:\\Program Files\\BlueStacks\\HD-RunApp.exe\" -json \"{\\\"app_icon_url\\\":\\\"\\\",\\\"app_name\\\":\\\"Jetpack\\\",\\\"app_url\\\":\\\"\\\",\\\"app_pkg\\\":\\\"com.halfbrick.jetpackjoyride\\\"}\"");
                    } catch (Exception E1) {}

                }
                else {
                    try {
                        File file = new File("C:\\Users\\sebas\\Desktop\\Ankle\\src\\main\\java\\games.xlsx");
                        Desktop desktop = Desktop.getDesktop();
                        desktop.open(file);              //opens the specified file
                    } catch (Exception E2) {
                    }
                }
                EndGame end = new EndGame(session);
                frame.dispose();
                KeyStroke keyStroke = KeyStroke.getKeyStroke("0");
                try {
                    Robot robot = new Robot();
                    robot.keyPress(keyStroke.getKeyCode());
                } catch (AWTException E) {
                    E.printStackTrace();
                }
            }
        });

        selectPanel.add(select);                                // adds select button to select panel

        JPanel pracPanel = new JPanel();
        JButton practice = new JButton("Smooth Assessment");
        practice.setFont(new Font("Verdana", Font.PLAIN, 30));
        pracPanel.add(practice);
        practice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menu menu = new menu(session);
                frame.dispose();
                KeyStroke keyStroke = KeyStroke.getKeyStroke("7");
                try {
                    Robot robot = new Robot();
                    robot.keyPress(keyStroke.getKeyCode());
                } catch (AWTException E) {
                    E.printStackTrace();
                }
            }
        });

        // sets main panel layout and adds sub panels
        mainPanel.setLayout(new GridLayout(3,1));
        mainPanel.add(titlePanel);
        mainPanel.add(selectPanel);
        if (session.length == 2) {
            mainPanel.add(pracPanel);
        }

        frame.add(mainPanel);                                           // adds main panel to frame
        frame.setVisible(true);                                         // sets frame to visible
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  // allows frame to be closed
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);                   // set frame to fit screen
    }
}