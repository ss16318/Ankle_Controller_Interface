
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Players extends JPanel {


    public Players() {

        JFrame f = new JFrame();                                                // creates frame
        JPanel mainPanel = new JPanel();                                        // creates main panel
        mainPanel.setLayout(new GridLayout(4,1));

        JPanel titlePanel = new JPanel();                                       // creates title panel
        JLabel title = new JLabel("Welcome Foot Flex");      // creates title label with text
        title.setFont(new Font("Verdana", Font.PLAIN, 70));       // sets font and size of text
        title.setBorder(BorderFactory.createEmptyBorder(100, 30, 100, 30)); // adds a border for spacing
        titlePanel.add(title);// adds title to title panel


        JPanel playerPanel = new JPanel();
        JLabel selPlayer = new JLabel("Select Player Mode:   ");
        playerPanel.setBorder(BorderFactory.createEmptyBorder(100, 30, 100, 30));
        selPlayer.setFont(new Font("Verdana", Font.PLAIN, 50));       // sets font and size of text
        String[] choices = {"Single Player" , "Multiplayer"};
        final JComboBox<String> cb = new JComboBox<String>(choices);
        cb.setFont(new Font("Verdana", Font.PLAIN, 50));       // sets font and size of text
        playerPanel.add(selPlayer);
        playerPanel.add(cb);

        JPanel subPanel = new JPanel();
        JButton submit = new JButton("Proceed");
        submit.setFont(new Font("Verdana", Font.PLAIN, 30));
        subPanel.setBorder(BorderFactory.createEmptyBorder(100, 30, 100, 30));
        subPanel.add(submit);
        submit.addActionListener(new ActionListener() {                          // waits for button to be selected
            @Override
            public void actionPerformed(ActionEvent e) {

                if (cb.getSelectedItem().toString().equals("Single Player")) {
                    User user = new User();

                    KeyStroke keyStroke = KeyStroke.getKeyStroke("1");
                    try {
                        Robot robot = new Robot();
                        robot.keyPress(keyStroke.getKeyCode());
                    } catch (AWTException E) {
                        E.printStackTrace();
                    }
                }
                else {
                    Users2 users = new Users2();
                    try {
                        KeyStroke keyStroke1 = KeyStroke.getKeyStroke("2");
                        Robot robot = new Robot();
                        robot.keyPress(keyStroke1.getKeyCode());
                    } catch (AWTException E1) {
                        E1.printStackTrace();
                    }
                }


                f.dispose();                                                 // frame will close
            }
        });

        JPanel vid = new JPanel();
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

        setLayout(new FlowLayout());
        vid.add(hyperlink);

        mainPanel.add(titlePanel);
        mainPanel.add(playerPanel);
        mainPanel.add(subPanel);
        mainPanel.add(vid);

        f.add(mainPanel);                       // adds main panel to frame
        f.setVisible(true);                     // makes frame visible
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  // allows frame to be closed
        f.setExtendedState(Frame.MAXIMIZED_BOTH);                   // sets frame to fit screen
    }
}
