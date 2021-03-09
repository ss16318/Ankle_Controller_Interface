
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

public class Players extends JPanel {


    public Players() {

        JFrame f = new JFrame();                                                // creates frame
        JPanel mainPanel = new JPanel();                                        // creates main panel
        mainPanel.setLayout(new GridLayout(3,1));

        JPanel titlePanel = new JPanel();                                       // creates title panel
        JLabel title = new JLabel("Welcome to the ankle tracker");      // creates title label with text
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
        JButton submit = new JButton("Select Users");
        submit.setFont(new Font("Verdana", Font.PLAIN, 30));
        subPanel.setBorder(BorderFactory.createEmptyBorder(100, 30, 100, 30));
        subPanel.add(submit);
        submit.addActionListener(new ActionListener() {                          // waits for button to be selected
            @Override
            public void actionPerformed(ActionEvent e) {

                if (cb.getSelectedItem().toString().equals("Single Player")){
                    User user = new User();
                    try {
                        PrintWriter instruct = new PrintWriter("python", "UTF-8");
                        instruct.println("10");
                        instruct.close();
                    }
                    catch(Exception E){}
                }
                else {
                    Users2 users = new Users2();
                    try {
                        PrintWriter instruct = new PrintWriter("python", "UTF-8");
                        instruct.println("11");
                        instruct.close();
                    }
                    catch(Exception E){}
                }

                f.dispose();                                                 // frame will close
            }
        });

        mainPanel.add(titlePanel);
        mainPanel.add(playerPanel);
        mainPanel.add(subPanel);

        f.add(mainPanel);                       // adds main panel to frame
        f.setVisible(true);                     // makes frame visible
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  // allows frame to be closed
        f.setExtendedState(Frame.MAXIMIZED_BOTH);                   // sets frame to fit screen
    }
}
