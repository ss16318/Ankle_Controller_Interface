
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

public class Calibration {

    static GraphicsConfiguration gc;            // sets up display

    public Calibration(Session[] session) {

        JFrame frame = new JFrame();            // creates frame
        JPanel mainPanel = new JPanel();        // creates main panel
        JPanel welcomePanel = new JPanel();     // creates title panel
        JPanel startPanel = new JPanel();       // creates button panel

        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("We just need to do a calibration");      // creates title label with text
        title.setFont(new Font("Verdana", Font.PLAIN, 70));       // sets font and size of text
        welcomePanel.add(title);        // adds title label to panel

        JButton connect = new JButton("Calibrate sensor");              // creates button to calibrate sensor
        connect.setFont(new Font("Verdana", Font.PLAIN, 50));     // sets font and size of text

        connect.addActionListener(new ActionListener() {        // waits for button to be clicked
            @Override
            public void actionPerformed(ActionEvent e) {

                calibNeut timer1 =  new calibNeut(session);        // when clicked the timer frames cycle will start for calibration
                try {
                    KeyStroke keyStroke = KeyStroke.getKeyStroke("3");
                    Robot robot = new Robot();
                    robot.keyPress(keyStroke.getKeyCode());
                } catch (AWTException E) {
                    E.printStackTrace();
                }
                frame.dispose();
            }
        });

        startPanel.add(connect);                            // adds connect button to start panel

        mainPanel.setLayout(new GridLayout(2,1));            // sets panel layout
        mainPanel.add(welcomePanel);   // adds welcome panel to top of main panel
        mainPanel.add(startPanel);    // adds start panel to centre of main panel

        frame.add(mainPanel);                               // adds main panel to frame
        frame.setVisible(true);                             // makes main panel visible
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  // allows window to be closed
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);                   // sets frame to fit screen
    }
}
