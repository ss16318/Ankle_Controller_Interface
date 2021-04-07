
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.PrintWriter;

public class NewUser {


    public NewUser() {

        JFrame f = new JFrame();                                                // creates frame
        JPanel mainPanel = new JPanel();                                        // creates main panel
        mainPanel.setLayout(new GridLayout(4,1));

        JPanel titlePanel = new JPanel();                                       // creates title panel
        JLabel title = new JLabel("Create a new User");      // creates title label with text
        title.setFont(new Font("Verdana", Font.PLAIN, 70));       // sets font and size of text
        titlePanel.add(title);// adds title to title panel

        JPanel fPanel = new JPanel();
        JLabel fLabel = new JLabel("First Name:   ");
        fLabel.setFont(new Font("Verdana", Font.PLAIN, 30));
        JTextField fName = new JTextField(15);
        fPanel.add(fLabel);
        fPanel.add(fName);

        JPanel lPanel = new JPanel();
        JLabel lLabel = new JLabel("Last Name:   ");
        lLabel.setFont(new Font("Verdana", Font.PLAIN, 30));
        JTextField lName = new JTextField(20);
        lPanel.add(lLabel);
        lPanel.add(lName);

        JPanel subPanel = new JPanel();
        JButton submit = new JButton("Create user");
        submit.setFont(new Font("Verdana", Font.PLAIN, 30));
        submit.addActionListener(new ActionListener() {                          // waits for button to be selected
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    PrintWriter output = new PrintWriter(new FileWriter("UserList.txt", true));
                    output.println(fName.getText() + " " + lName.getText());
                    output.close();
                }
                catch(Exception ex){}

                Session[] session = new Session[1];
                session[0] = new Session(fName.getText(), lName.getText());
                Calibration calib = new Calibration(session);
                f.dispose();                                                 // frame will close

            }
        });

        subPanel.add(submit);

        mainPanel.setLayout(new GridLayout(4,1));

        mainPanel.add(titlePanel);
        mainPanel.add(fPanel);
        mainPanel.add(lPanel);
        mainPanel.add(subPanel);

        f.add(mainPanel);                       // adds main panel to frame
        f.setVisible(true);                     // makes frame visible
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  // allows frame to be closed
        f.setExtendedState(Frame.MAXIMIZED_BOTH);                   // sets frame to fit screen
    }
}

