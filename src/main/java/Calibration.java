
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


        JLabel title = new JLabel("We just need to do a calibration");      // creates title label with text
        title.setFont(new Font("Verdana", Font.PLAIN, 50));       // sets font and size of text
        title.setBorder(BorderFactory.createEmptyBorder(100, 30, 100, 30)); // adds a border for spacing
        welcomePanel.add(title);        // adds title label to panel

        JButton connect = new JButton("Calibrate sensor");              // creates button to calibrate sensor
        connect.setFont(new Font("Verdana", Font.PLAIN, 50));     // sets font and size of text
        connect.setBorder(BorderFactory.createEmptyBorder(100, 30, 100, 30));   // adds padding for spacing

        connect.addActionListener(new ActionListener() {        // waits for button to be clicked
            @Override
            public void actionPerformed(ActionEvent e) {

                calibNeut timer1 =  new calibNeut(session);        // when clicked the timer frames cycle will start for calibration
                try {
                    PrintWriter instruct = new PrintWriter("python", "UTF-8");
                    instruct.println("1");
                    instruct.close();
                }
                catch(Exception E){}
                frame.dispose();
            }
        });

        startPanel.add(connect);                            // adds connect button to start panel

        mainPanel.setLayout(new BorderLayout());            // sets panel layout
        mainPanel.add(welcomePanel , BorderLayout.NORTH);   // adds welcome panel to top of main panel
        mainPanel.add(startPanel , BorderLayout.CENTER);    // adds start panel to centre of main panel

        frame.add(mainPanel);                               // adds main panel to frame
        frame.setVisible(true);                             // makes main panel visible
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  // allows window to be closed
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);                   // sets frame to fit screen
    }
}
