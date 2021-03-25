
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

public class Calibration {

    static GraphicsConfiguration gc;            // sets up display

    public Calibration(Session[] session) {

        JFrame frame = new JFrame();            // creates frame
        JPanel mainPanel = new JPanel();        // creates main panel
        JPanel welcomePanel = new JPanel();     // creates title panel
        JPanel startPanel = new JPanel();       // creates button panel

        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("We just need to do a calibration");      // creates title label with text
        title.setFont(new Font("Verdana", Font.PLAIN, 70));           // sets font and size of text
        welcomePanel.add(title);                                                // adds title label to panel

        JPanel instPanel = new JPanel();
        JLabel instruct = new JLabel("Sit down, straighten your leg and raise your toes several inches off the ground");
        instruct.setFont(new Font("Verdana", Font.PLAIN, 30));
        instPanel.add(instruct);

        JPanel vidPanel = new JPanel();
        String text = "If unsure, click here to watch intro FootFlex video";
        JLabel hyperlink = new JLabel(text);
        hyperlink.setFont(new Font("Verdana", Font.PLAIN, 20));
        hyperlink.setForeground(Color.BLUE.darker());
        hyperlink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        hyperlink.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://youtu.be/mBEtr7iHbTQ?t=39"));
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
        vidPanel.add(hyperlink);

        JButton connect = new JButton("Start Calibration");              // creates button to calibrate sensor
        connect.setFont(new Font("Verdana", Font.PLAIN, 30));     // sets font and size of text

        connect.addActionListener(new ActionListener() {        // waits for button to be clicked
            @Override
            public void actionPerformed(ActionEvent e) {

                calibNeut timer1 =  new calibNeut(session);        // when clicked the timer frames cycle will start for calibration
                try {
                    KeyStroke keyStroke = KeyStroke.getKeyStroke("3");
                    Robot robot = new Robot();
                    robot.keyPress(keyStroke.getKeyCode());
                    robot.keyRelease(keyStroke.getKeyCode());
                } catch (Exception E) {
                    E.printStackTrace();
                }
                frame.dispose();
            }
        });

        startPanel.add(connect);                            // adds connect button to start panel

        mainPanel.setLayout(new GridLayout(4,1));            // sets panel layout
        mainPanel.add(welcomePanel);   // adds welcome panel to top of main panel
        mainPanel.add(instPanel);
        mainPanel.add(vidPanel);
        mainPanel.add(startPanel);    // adds start panel to centre of main panel

        frame.add(mainPanel);                               // adds main panel to frame
        frame.setVisible(true);                             // makes main panel visible
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  // allows window to be closed
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);                   // sets frame to fit screen
    }
}
