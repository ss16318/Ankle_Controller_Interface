import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Users2 {

    public Users2() {

        JFrame f = new JFrame();                                                // creates frame
        JPanel mainPanel = new JPanel();                                        // creates main panel

        JPanel titlePanel = new JPanel();                                       // creates title panel
        JLabel title = new JLabel("Welcome to the ankle tracker");      // creates title label with text
        title.setFont(new Font("Verdana", Font.PLAIN, 50));       // sets font and size of text
        titlePanel.add(title);                                                  // adds title to title panel

        JPanel User1Panel = new JPanel();
        JLabel user1 = new JLabel("Select Player 1 (to control Up & Down)  ");
        user1.setFont(new Font("Verdana", Font.PLAIN, 30));
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
        final JComboBox<String> cb1 = new JComboBox<String>(choices);
        cb1.setFont(new Font("Verdana", Font.PLAIN, 30));
        User1Panel.add(user1);
        User1Panel.add(cb1);

        JPanel newUser1 = new JPanel();
        JCheckBox checkbox1 = new JCheckBox("To create a new user for Player 1 please to box and fill in fields below");
        checkbox1.setFont(new Font("Verdana", Font.PLAIN, 15));
        newUser1.add(checkbox1);

        JPanel newU1 = new JPanel();
        JLabel fLabel = new JLabel("First Name:   ");
        fLabel.setFont(new Font("Verdana", Font.PLAIN, 15));
        JTextField fName = new JTextField(15);
        JLabel lLabel = new JLabel("Last Name:   ");
        lLabel.setFont(new Font("Verdana", Font.PLAIN, 15));
        JTextField lName = new JTextField(15);
        newU1.add(fLabel);
        newU1.add(fName);
        newU1.add(lLabel);
        newU1.add(lName);

        JPanel User2Panel = new JPanel();
        JLabel user2 = new JLabel("Select Player 2 (to control left & right)  ");
        user2.setFont(new Font("Verdana", Font.PLAIN, 30));
        final JComboBox<String> cb2 = new JComboBox<String>(choices);
        cb2.setFont(new Font("Verdana", Font.PLAIN, 30));
        User2Panel.add(user2);
        User2Panel.add(cb2);

        JPanel newUser2 = new JPanel();
        JCheckBox checkbox2 = new JCheckBox("To create a new user for player 2 please to box and fill in fields below");
        checkbox2.setFont(new Font("Verdana", Font.PLAIN, 15));
        newUser2.add(checkbox2);

        JPanel newU2 = new JPanel();
        JLabel fLabel2 = new JLabel("First Name:   ");
        fLabel2.setFont(new Font("Verdana", Font.PLAIN, 15));
        JTextField fName2 = new JTextField(15);
        JLabel lLabel2 = new JLabel("Last Name:   ");
        lLabel2.setFont(new Font("Verdana", Font.PLAIN, 15));
        JTextField lName2 = new JTextField(15);
        newU2.add(fLabel2);
        newU2.add(fName2);
        newU2.add(lLabel2);
        newU2.add(lName2);

        JPanel subPanel = new JPanel();
        JButton submit = new JButton("Submit");
        submit.setFont(new Font("Verdana", Font.PLAIN, 30));
        submit.addActionListener(new ActionListener() {                          // waits for button to be selected
            @Override
            public void actionPerformed(ActionEvent e) {

                Session[] session = new Session[2];

                if (checkbox1.isSelected()){

                    session[0] = new Session(fName.getText(), lName.getText());

                    try {
                        PrintWriter output = new PrintWriter(new FileWriter("UserList.txt", true));
                        output.println(fName.getText() + " " + lName.getText());
                        output.close();
                    }
                    catch(Exception ex){}
                }
                else {
                    String player = cb1.getSelectedItem().toString();
                    String[] names = player.split(" ");
                    session[0] = new Session(names[0], names[1]);
                }

                if (checkbox2.isSelected()){

                    session[1] = new Session(fName2.getText(), lName2.getText());

                    try {
                        PrintWriter output = new PrintWriter(new FileWriter("UserList.txt", true));
                        output.println(fName2.getText() + " " + lName2.getText());
                        output.close();
                    }
                    catch(Exception ex){}
                }
                else {
                    String player2 = cb2.getSelectedItem().toString();
                    String[] names2 = player2.split(" ");
                    session[1] = new Session(names2[0], names2[1]);
                }

                Calibration calib = new Calibration(session);
                f.dispose();                                                 // frame will close
            }
        });
        subPanel.add(submit);

        JPanel player1 = new JPanel();
        player1.setLayout(new GridLayout(1,2));
        player1.add(User1Panel);
        JPanel new1 = new JPanel();
        new1.setLayout(new GridLayout(2,1));
        new1.add(newUser1);
        new1.add(newU1);
        player1.add(new1);

        JPanel player2 = new JPanel();
        player2.setLayout(new GridLayout(1,2));
        player2.add(User2Panel);
        JPanel new2 = new JPanel();
        new2.setLayout(new GridLayout(2,1));
        new2.add(newUser2);
        new2.add(newU2);
        player2.add(new2);


        mainPanel.setLayout(new GridLayout(4,1));
        mainPanel.add(titlePanel);
        mainPanel.add(player1);
        mainPanel.add(player2);

        mainPanel.add(subPanel);

        f.add(mainPanel);                       // adds main panel to frame
        f.setVisible(true);                     // makes frame visible
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  // allows frame to be closed
        f.setExtendedState(Frame.MAXIMIZED_BOTH);                   // sets frame to fit screen
    }
}
