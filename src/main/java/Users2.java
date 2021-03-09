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
        JLabel user1 = new JLabel("Select Player 1");
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
        User1Panel.add(user1);
        User1Panel.add(cb1);

        JPanel newUser1 = new JPanel();
        JCheckBox checkbox1 = new JCheckBox("To create a new user for Player 1 please to box and fill in fields below");
        newUser1.add(checkbox1);

        JPanel newU1 = new JPanel();
        JLabel fLabel = new JLabel("First Name:   ");
        JTextField fName = new JTextField(15);
        JLabel lLabel = new JLabel("Last Name:   ");
        JTextField lName = new JTextField(15);
        newU1.add(fLabel);
        newU1.add(fName);
        newU1.add(lLabel);
        newU1.add(lName);

        JPanel User2Panel = new JPanel();
        JLabel user2 = new JLabel("Select Player 2");
        final JComboBox<String> cb2 = new JComboBox<String>(choices);
        User2Panel.add(user2);
        User2Panel.add(cb2);

        JPanel newUser2 = new JPanel();
        JCheckBox checkbox2 = new JCheckBox("To create a new user for player 2 please to box and fill in fields below");
        newUser2.add(checkbox2);

        JPanel newU2 = new JPanel();
        JLabel fLabel2 = new JLabel("First Name:   ");
        JTextField fName2 = new JTextField(15);
        JLabel lLabel2 = new JLabel("Last Name:   ");
        JTextField lName2 = new JTextField(15);
        newU2.add(fLabel2);
        newU2.add(fName2);
        newU2.add(lLabel2);
        newU2.add(lName2);

        JPanel dirPanel = new JPanel();
        JLabel dir = new JLabel("Who is controlling up and down (the other player controls left and right");
        String[] pl = {"Player 1" , "Player 2"};
        final JComboBox<String> cb3 = new JComboBox<String>(pl);
        dirPanel.add(dir);
        dirPanel.add(cb3);

        JPanel subPanel = new JPanel();
        JButton submit = new JButton("Submit");
        submit.setFont(new Font("Verdana", Font.PLAIN, 30));
        submit.addActionListener(new ActionListener() {                          // waits for button to be selected
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = 11;
                if (cb3.getSelectedItem().toString().equals("Player 1")) {
                        i = 11;
                } else { i = 12; }
                try {
                    PrintWriter instruct = new PrintWriter("python", "UTF-8");
                    instruct.println(i);
                    instruct.close();
                } catch (Exception E) {}


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

        mainPanel.setLayout(new GridLayout(9,1));
        mainPanel.add(titlePanel);
        mainPanel.add(User1Panel);
        mainPanel.add(newUser1);
        mainPanel.add(newU1);
        mainPanel.add(User2Panel);
        mainPanel.add(newUser2);
        mainPanel.add(newU2);
        mainPanel.add(dirPanel);
        mainPanel.add(subPanel);

        f.add(mainPanel);                       // adds main panel to frame
        f.setVisible(true);                     // makes frame visible
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  // allows frame to be closed
        f.setExtendedState(Frame.MAXIMIZED_BOTH);                   // sets frame to fit screen
    }
}
