import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenu {

    public StartMenu(Session[] session){

        JFrame f = new JFrame();                                                // creates frame

        JPanel mainPanel = new JPanel();                                        // creates main panel
        mainPanel.setLayout(new GridLayout(3,1));                     // sets main panel layout

        JPanel titlePanel = new JPanel();                                       // creates title panel
        JLabel title = new JLabel("WELCOME");                               // creates title label and text
        title.setFont(new Font("Verdana", Font.PLAIN, 50));           // sets font and text size
        titlePanel.add(title);                                                  // adds title to title panel

        JPanel calibPanel = new JPanel();
        JButton calib = new JButton("Let's Play!");
        calib.setFont(new Font("Verdana", Font.PLAIN, 30));
        calibPanel.add(calib);
        calib.addActionListener(new ActionListener() {                          // waits for button to be selected
            @Override
            public void actionPerformed(ActionEvent e) {

                SelectDiff select = new SelectDiff(session);
                f.dispose();                                                 // frame will close
            }
        });

        JPanel pracPanel = new JPanel();
        JButton practice = new JButton("Practice Movement");
        practice.setFont(new Font("Verdana", Font.PLAIN, 30));
        pracPanel.add(practice);
        practice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Practice pract = new Practice(session);
                f.dispose();
            }
        });

        mainPanel.add(titlePanel);              // adds panels to main panel
        mainPanel.add(calibPanel);
        mainPanel.add(pracPanel);

        f.add(mainPanel);                       // adds main panel to frame
        f.setVisible(true);                     // makes frame visible
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  // allows frame to be closed
        f.setExtendedState(Frame.MAXIMIZED_BOTH);                   // sets frame to fit screen
    }
}

