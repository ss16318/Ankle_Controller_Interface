import javax.swing.*;
import java.awt.*;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

public class startGame {

    public startGame(Session[] session) {

        JFrame frame = new JFrame();                        // creates fields
        JPanel mainPanel = new JPanel();
        JPanel titlePanel = new JPanel();


        JLabel title = new JLabel("Starting Game...");      // creates title label
        title.setFont(new Font("Verdana", Font.PLAIN, 70));
        title.setBorder(BorderFactory.createEmptyBorder(100, 30, 100, 30)); // padding for spacing
        titlePanel.add(title);                                  // adds title label to title panel
         // adds select button to select panel

        // sets main panel layout and adds sub panels
        mainPanel.setLayout(new GridLayout(1,1));
        mainPanel.add(titlePanel);

        frame.add(mainPanel);                                           // adds main panel to frame
        frame.setVisible(true);                                         // sets frame to visible
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  // allows frame to be closed
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);                   // set frame to fit screen

        java.util.Timer timer = new Timer();                // creates a timer field

        timer.schedule(new TimerTask(){           // start timer task

            int second = 1;                       // defines start of countdown
            @Override
            public void run() {
                // title of frame will show time
                frame.setTitle("Calibrating... Hold for " + second-- + " seconds.");

                if (second < 0){                            // at time less than 0s
                    EndGame endGame = new EndGame(session);

                    frame.dispose();                        // this frame will close
                    timer.cancel();                         // this timer will stop
                    timer.purge();
                }
            }
        },0, 1000);     // adds a 1000ms pause between counts
    }
}
