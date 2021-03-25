import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class EndGame {

    public EndGame(Session[] session) {

        JFrame frame = new JFrame();                        // creates fields
        JPanel mainPanel = new JPanel();
        JPanel titlePanel = new JPanel();


        JLabel title = new JLabel("Enjoy the game.....");      // creates title label
        title.setFont(new Font("Verdana", Font.PLAIN, 70));
        title.setBorder(BorderFactory.createEmptyBorder(100, 30, 100, 30)); // padding for spacing
        titlePanel.add(title);                                  // adds title label to title panel

        JPanel endPanel = new JPanel();
        JButton end = new JButton("Save and go to Menu");       // creates select game button
        end.setFont(new Font("Verdana", Font.PLAIN, 50));

        end.addActionListener(new ActionListener() {         // waiting for button to be clicked
            @Override
            public void actionPerformed(ActionEvent e) {
                menu menu = new menu(session);                         // opens menu class
                frame.dispose();
                try {
                    KeyStroke keyStroke3 = KeyStroke.getKeyStroke("8");
                    Robot robot = new Robot();
                    robot.keyPress(keyStroke3.getKeyCode());

                    TimeUnit.SECONDS.sleep(1);

                    try {
                        File myObj = new File("User1.txt");
                        Scanner myReader = new Scanner(myObj);
                        while (myReader.hasNextLine()) {
                            String data = myReader.nextLine();
                            String[] max = data.split(",");
                            session[0].setGameMin( Double.parseDouble(max[0]));
                            session[0].setGameMax( Double.parseDouble(max[1]));
                            session[0].setMaxCount( Integer.parseInt(max[2]));
                            session[0].setMinCount( Integer.parseInt(max[3]));
                            session[0].setGameScore( Double.parseDouble(max[4]));
                            session[0].setGameTime( Double.parseDouble(max[5]));

                        }
                        myReader.close();
                    } catch (FileNotFoundException ex) {
                        System.out.println("An error occurred.");
                        ex.printStackTrace();
                    }

                    String userFile = session[0].getFname() + "_" +session[0].getLname() +".txt";

                    File f = new File(userFile);

                        if (!f.exists()) {
                                PrintWriter writer = new PrintWriter(userFile, "UTF-8");
                                writer.println(
                                        java.time.LocalDate.now() + " " +
                                                session[0].getGameMin() + " " +
                                                session[0].getGameMax() + " " +
                                                session[0].getMaxCount() + " " +
                                                session[0].getMinCount() + " " +
                                                session[0].getGameScore() + " " +
                                                session[0].getGameTime() );

                                writer.close();
                            }

                        else {
                            PrintWriter writer = new PrintWriter(new FileWriter(userFile, true));
                            writer.println(
                                    java.time.LocalDate.now() + " " +
                                            session[0].getGameMin() + " " +
                                            session[0].getGameMax() + " " +
                                            session[0].getMaxCount() + " " +
                                            session[0].getMinCount() + " " +
                                            session[0].getGameScore() + " " +
                                            session[0].getGameTime() );
                            writer.close();
                        }



                    if (session.length == 2 ) {
                        try {
                            File myObj2 = new File("User2.txt");
                            Scanner myReader2 = new Scanner(myObj2);
                            while (myReader2.hasNextLine()) {
                                String data2 = myReader2.nextLine();
                                String[] max2 = data2.split(",");
                                session[1].setGameMin( Double.parseDouble(max2[0]));
                                session[1].setGameMax( Double.parseDouble(max2[1]));
                                session[1].setMaxCount( Integer.parseInt(max2[2]));
                                session[1].setMinCount( Integer.parseInt(max2[3]));
                                session[1].setGameScore( Double.parseDouble(max2[4]));
                                session[1].setGameTime( Double.parseDouble(max2[5]));
                            }
                            myReader2.close();
                        } catch (FileNotFoundException ex2) {
                            System.out.println("An error occurred.");
                            ex2.printStackTrace();
                        }

                        String user2File = session[1].getFname() + "_" +session[1].getLname() +".txt";

                        File f2 = new File(user2File);
                        if(!f2.exists()) {
                            PrintWriter writer2 = new PrintWriter(user2File, "UTF-8");
                            writer2.println(
                                    java.time.LocalDate.now() + " " +
                                            session[1].getGameMin() + " " +
                                            session[1].getGameMax() + " " +
                                            session[1].getMaxCount() + " " +
                                            session[1].getMinCount() + " " +
                                            session[1].getGameScore() + " " +
                                            session[1].getGameTime() );
                            writer2.close();
                        }
                        else {
                            PrintWriter writer2 = new PrintWriter(new FileWriter(user2File, true));
                            writer2.println(
                                    java.time.LocalDate.now() + " " +
                                            session[1].getGameMin() + " " +
                                            session[1].getGameMax() + " " +
                                            session[1].getMaxCount() + " " +
                                            session[1].getMinCount() + " " +
                                            session[1].getGameScore() + " " +
                                            session[1].getGameTime() );
                            writer2.close();
                        }
                    }
                } catch (Exception E) {
                    E.printStackTrace();
                }
            }
        });

        endPanel.add(end);                                // adds select button to select panel

        // sets main panel layout and adds sub panels
        mainPanel.setLayout(new GridLayout(2,1));
        mainPanel.add(titlePanel);
        mainPanel.add(endPanel);

        frame.add(mainPanel);                                           // adds main panel to frame
        frame.setVisible(true);                                         // sets frame to visible
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  // allows frame to be closed
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);                   // set frame to fit screen
    }
}
