import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

public class menu {

    public menu(Session[] session){

        JFrame f = new JFrame();                                                // creates frame

        JPanel mainPanel = new JPanel();                                        // creates main panel
        mainPanel.setLayout(new GridLayout(6,1));                     // sets main panel layout
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel titlePanel = new JPanel();                                       // creates title panel
        JLabel title = new JLabel("Menu");                                 // creates title label and text
        title.setFont(new Font("Verdana", Font.PLAIN, 50));           // sets font and text size
        titlePanel.add(title);                                                  // adds title to title panel

        JPanel calibPanel = new JPanel();                                        // creates easy panel
        JButton recalib = new JButton("Recalibrate");                        // creates easy button
        recalib.setFont(new Font("Verdana", Font.PLAIN, 30));
        calibPanel.add(recalib);                                                  // adds easy button to easy panel
        recalib.addActionListener(new ActionListener() {                          // waits for button to be selected
            @Override
            public void actionPerformed(ActionEvent e) {

                calibNeut recalib = new calibNeut(session);                   // when selected games select option will appear
                try {
                    PrintWriter instruct = new PrintWriter("python", "UTF-8");
                    instruct.println("1");
                    instruct.close();
                }
                catch(Exception E){}
                f.dispose();                                                 // frame will close
            }
        });

        JPanel diffPanel = new JPanel();
        JButton diffChange = new JButton("Change Difficulty");
        diffChange.setFont(new Font("Verdana", Font.PLAIN, 30));
        diffPanel.add(diffChange);
        diffChange.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectDiff change = new SelectDiff(session);
                f.dispose();
            }
        });

        JPanel pracPanel = new JPanel();
        JButton practice = new JButton("Practice Movement");
        practice.setFont(new Font("Verdana", Font.PLAIN, 30));
        pracPanel.add(practice);
        practice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Practice pract = new Practice(session);
                f.dispose();
            }
        });

        JPanel gamePanel = new JPanel();
        JButton game = new JButton("Choose Game");
        game.setFont(new Font("Verdana", Font.PLAIN, 30));
        gamePanel.add(game);
        game.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameSelect game = new gameSelect(session);
                f.dispose();
            }
        });

        JPanel trackPanel = new JPanel();
        JButton track = new JButton("Performance Tracker");
        track.setFont(new Font("Verdana", Font.PLAIN, 30));
        trackPanel.add(track);

        JPanel exitPanel = new JPanel();
        JButton exit = new JButton("Save and Quit");
        exit.setFont(new Font("Verdana", Font.PLAIN, 30));
        exitPanel.add(exit);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    try {
                        PrintWriter instruct = new PrintWriter("python", "UTF-8");
                        instruct.println("9");
                        instruct.close();
                    }
                    catch(Exception E){}

                    try {
                        File myObj = new File("User1Data.txt");
                        Scanner myReader = new Scanner(myObj);
                        while (myReader.hasNextLine()) {
                            String data = myReader.nextLine();
                            String[] max = data.split(",");
                            session[0].setDor( Integer.parseInt(max[0]));
                            session[0].setPla( Integer.parseInt(max[1]));
                            session[0].setInv( Integer.parseInt(max[2]));
                            session[0].setEve( Integer.parseInt(max[3]));
                        }
                        myReader.close();
                    } catch (FileNotFoundException ex) {
                        System.out.println("An error occurred.");
                        ex.printStackTrace();
                    }

                    String userFile = session[0].getFname() + "_" +session[0].getLname() +".txt";

                    File f = new File(userFile);
                    if(!f.exists()) {
                        PrintWriter writer = new PrintWriter(userFile, "UTF-8");
                        writer.println(
                                java.time.LocalDate.now() + " " +
                                session[0].getMaxDor() + " " +
                                session[0].getMaxPla() + " " +
                                session[0].getMaxInv() + " " +
                                session[0].getMaxEve());
                        writer.close();
                    }

                    else {
                        PrintWriter writer = new PrintWriter(new FileWriter(userFile, true));
                        writer.println(
                                java.time.LocalDate.now() + " " +
                                session[0].getMaxDor() + " " +
                                session[0].getMaxPla() + " " +
                                session[0].getMaxInv() + " " +
                                session[0].getMaxEve());
                        writer.close();
                    }


                    if (session.length == 2 ) {
                        try {
                            File myObj2 = new File("User2Data.txt");
                            Scanner myReader2 = new Scanner(myObj2);
                            while (myReader2.hasNextLine()) {
                                String data2 = myReader2.nextLine();
                                String[] max2 = data2.split(",");
                                session[1].setDor( Integer.parseInt(max2[0]));
                                session[1].setPla( Integer.parseInt(max2[1]));
                                session[1].setInv( Integer.parseInt(max2[2]));
                                session[1].setEve( Integer.parseInt(max2[3]));
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
                                            session[1].getMaxDor() + " " +
                                            session[1].getMaxPla() + " " +
                                            session[1].getMaxInv() + " " +
                                            session[1].getMaxEve());
                            writer2.close();
                        }
                        else {
                            PrintWriter writer2 = new PrintWriter(new FileWriter(user2File, true));
                            writer2.println(
                                    java.time.LocalDate.now() + " " +
                                            session[1].getMaxDor() + " " +
                                            session[1].getMaxPla() + " " +
                                            session[1].getMaxInv() + " " +
                                            session[1].getMaxEve());
                            writer2.close();
                        }
                    }
                }

                catch ( Exception ex){}
                System.exit(0);
            }
        });

        mainPanel.add(titlePanel);              // adds panels to main panel
        mainPanel.add(calibPanel);
        mainPanel.add(diffPanel);
        mainPanel.add(trackPanel);
        mainPanel.add(pracPanel);
        mainPanel.add(exitPanel);

        f.add(mainPanel);                       // adds main panel to frame
        f.setVisible(true);                     // makes frame visible
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  // allows frame to be closed
        f.setExtendedState(Frame.MAXIMIZED_BOTH);                   // sets frame to fit screen
    }
}
