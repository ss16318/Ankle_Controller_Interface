import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

public class SelectDiff {

    public SelectDiff(Session[] session){

        JFrame f = new JFrame();                                                // creates frame

        JPanel mainPanel = new JPanel();                                        // creates main panel
        mainPanel.setLayout(new GridLayout(4,1));                     // sets main panel layout

        JPanel titlePanel = new JPanel();                                       // creates title panel
        JLabel title = new JLabel("Select Difficulty");                     // creates title label and text
        title.setFont(new Font("Verdana", Font.PLAIN, 50));           // sets font and text size
        titlePanel.add(title);                                                  // adds title to title panel

        JPanel easyPanel = new JPanel();                                        // creates easy panel
        JButton easy = new JButton("EASY");                                 // creates easy button
        easy.setFont(new Font("Verdana", Font.PLAIN, 30));
        easyPanel.add(easy);                                                    // adds easy button to easy panel
        easy.addActionListener(new ActionListener() {                           // waits for button to be selected
            @Override
            public void actionPerformed(ActionEvent e) {

                gameSelect choose = new gameSelect(session);// when selected games select option will appear
                for (int i = 0 ; i < session.length ; i++) {
                    session[i].setDiff(0.5);
                }
                try {
                    PrintWriter instruct = new PrintWriter("python", "UTF-8");
                    instruct.println("6");
                    instruct.close();
                }
                catch(Exception E){}
                f.dispose();                                                    // frame will close
            }
        });

        JPanel mediumPanel = new JPanel();
        JButton medium = new JButton("MEDIUM");
        medium.setFont(new Font("Verdana", Font.PLAIN, 30));
        mediumPanel.add(medium);
        medium.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                gameSelect choose = new gameSelect(session);
                for (int i = 0 ; i < session.length ; i++) {
                    session[i].setDiff(0.7);
                }
                try {
                    PrintWriter instruct = new PrintWriter("python", "UTF-8");
                    instruct.println("7");
                    instruct.close();
                }
                catch(Exception E){}
                f.dispose();
            }
        });


        JPanel hardPanel = new JPanel();
        JButton hard = new JButton("HARD");
        hard.setFont(new Font("Verdana", Font.PLAIN, 30));
        hardPanel.add(hard);
        hard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                gameSelect choose = new gameSelect(session);
                for (int i = 0 ; i < session.length ; i++) {
                    session[i].setDiff(0.9);
                }
                try {
                    PrintWriter instruct = new PrintWriter("python", "UTF-8");
                    instruct.println("8");
                    instruct.close();
                }
                catch(Exception E){}
                f.dispose();
            }
        });

        mainPanel.add(titlePanel);              // adds panels to main panel
        mainPanel.add(easyPanel);
        mainPanel.add(mediumPanel);
        mainPanel.add(hardPanel);

        f.add(mainPanel);                       // adds main panel to frame
        f.setVisible(true);                     // makes frame visible
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  // allows frame to be closed
        f.setExtendedState(Frame.MAXIMIZED_BOTH);                   // sets frame to fit screen
    }
}
