import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

public class menu {

    public menu(Session[] session){

        JFrame f = new JFrame();                                                // creates frame

        JPanel mainPanel = new JPanel();                                        // creates main panel
        mainPanel.setLayout(new GridLayout(4,1));                     // sets main panel layout
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel titlePanel = new JPanel();                                       // creates title panel
        JLabel title = new JLabel("Menu");                                 // creates title label and text
        title.setFont(new Font("Verdana", Font.PLAIN, 50));           // sets font and text size
        titlePanel.add(title);                                                  // adds title to title panel


        JPanel restartPanel = new JPanel();                                        // creates easy panel
        JButton res = new JButton("Restart");                        // creates easy button
        res.setFont(new Font("Verdana", Font.PLAIN, 30));
        restartPanel.add(res);                                                  // adds easy button to easy panel
        res.addActionListener(new ActionListener() {                          // waits for button to be selected
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    KeyStroke keyStroke3 = KeyStroke.getKeyStroke("9");
                    Robot robot = new Robot();
                    robot.keyPress(keyStroke3.getKeyCode());
                }

                catch ( Exception ex){}

                f.dispose();                                                 // frame will close
                Loading load = new Loading();
            }
        });



        JPanel infoPanel = new JPanel();
        String text = "Click here to watch introductory Foot Flex videos";
        JLabel hyperlink = new JLabel(text);

        hyperlink.setFont(new Font("Verdana", Font.PLAIN, 30));
        hyperlink.setForeground(Color.BLUE.darker());
        hyperlink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        hyperlink.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://youtu.be/mBEtr7iHbTQ"));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                hyperlink.setText(text);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                hyperlink.setText("<html><a href=''>" + text + "</a></html>");
            }

        });
        infoPanel.add(hyperlink);

        JPanel exitPanel = new JPanel();
        JButton exit = new JButton("Quit");
        exit.setFont(new Font("Verdana", Font.PLAIN, 30));
        exitPanel.add(exit);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    try {
                        KeyStroke keyStroke3 = KeyStroke.getKeyStroke("9");
                        Robot robot = new Robot();
                        robot.keyPress(keyStroke3.getKeyCode());
                        System.exit(0);
                    }

                catch ( Exception ex){}
            }
        });

        mainPanel.add(titlePanel);              // adds panels to main panel
        mainPanel.add(infoPanel);
        mainPanel.add(restartPanel);
        mainPanel.add(exitPanel);

        f.add(mainPanel);                       // adds main panel to frame
        f.setVisible(true);                     // makes frame visible
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  // allows frame to be closed
        f.setExtendedState(Frame.MAXIMIZED_BOTH);                   // sets frame to fit screen
    }
}























