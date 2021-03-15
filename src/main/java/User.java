import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class User {

    public User() {

        JFrame f = new JFrame();                                                // creates frame
        JPanel mainPanel = new JPanel();                                        // creates main panel

        JPanel titlePanel = new JPanel();                                       // creates title panel
        JLabel title = new JLabel("Welcome to the ankle tracker");      // creates title label with text
        title.setFont(new Font("Verdana", Font.PLAIN, 70));       // sets font and size of text
        //title.setBorder(BorderFactory.createEmptyBorder(100, 30, 100, 30)); // adds a border for spacing
        titlePanel.add(title);                                                  // adds title to title panel

        JPanel UserPanel = new JPanel();
        JLabel user = new JLabel("Select User:  ");
        user.setFont(new Font("Verdana", Font.PLAIN, 30));
        ArrayList<String> userL = new ArrayList<String>();
        try {
            File myObj = new File("UserList.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                userL.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        String[] choices = userL.toArray(new String[userL.size()]);
        final JComboBox<String> cb = new JComboBox<String>(choices);
        cb.setFont(new Font("Verdana", Font.PLAIN, 30));       // sets font and size of text
        UserPanel.add(user);
        UserPanel.add(cb);

        JPanel subPanel = new JPanel();
        JButton submit = new JButton("Continue with selected user");
        submit.setFont(new Font("Verdana", Font.PLAIN, 30));
        subPanel.add(submit);
        submit.addActionListener(new ActionListener() {                          // waits for button to be selected
            @Override
            public void actionPerformed(ActionEvent e) {

                String player = cb.getSelectedItem().toString();
                String[] names = player.split(" ");

                Session[] session = new Session[1];
                session[0] = new Session( names[0] , names[1]);
                Calibration calib = new Calibration(session);
                f.dispose();                                                 // frame will close
            }
        });

        JPanel NewUserPanel = new JPanel();
        JButton NewUser = new JButton("Create New User");
        NewUser.setFont(new Font("Verdana", Font.PLAIN, 30));
        NewUserPanel.add(NewUser);
        NewUser.addActionListener(new ActionListener() {                          // waits for button to be selected
            @Override
            public void actionPerformed(ActionEvent e) {

                NewUser newU = new NewUser();
                f.dispose();                                                 // frame will close
            }
        });

        mainPanel.setLayout(new GridLayout(4,1));            // sets panel layout
        mainPanel.add(titlePanel);
        mainPanel.add(UserPanel);
        mainPanel.add(subPanel);
        mainPanel.add(NewUserPanel);

        f.add(mainPanel);                       // adds main panel to frame
        f.setVisible(true);                     // makes frame visible
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  // allows frame to be closed
        f.setExtendedState(Frame.MAXIMIZED_BOTH);                   // sets frame to fit screen
    }
}
