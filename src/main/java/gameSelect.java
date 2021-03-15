import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class gameSelect {

    public gameSelect(Session[] session) {

        JFrame frame = new JFrame();                        // creates fields
        JPanel mainPanel = new JPanel();
        JPanel titlePanel = new JPanel();
        JPanel selectPanel = new JPanel();
        File file = new File("C:/Users/sebas/Desktop/Ankle/game.xls");

        JLabel title = new JLabel("Choose your game");      // creates title label
        title.setFont(new Font("Verdana", Font.PLAIN, 70));
        title.setBorder(BorderFactory.createEmptyBorder(100, 30, 100, 30)); // padding for spacing
        titlePanel.add(title);                                  // adds title label to title panel

        JButton select = new JButton("Select Game");       // creates select game button
        select.setFont(new Font("Verdana", Font.PLAIN, 50));

        select.addActionListener(new ActionListener() {         // waiting for button to be clicked
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    File file = new File("C:\\Users\\sebas\\Desktop\\Ankle\\src\\main\\java\\games.xlsx");
                    Desktop desktop = Desktop.getDesktop();
                    desktop.open(file);              //opens the specified file
                }
                catch(Exception E)
                {}
                menu Menu = new menu(session);                         // opens menu class
                frame.dispose();

            }
        });

        selectPanel.add(select);                                // adds select button to select panel

        // sets main panel layout and adds sub panels
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(titlePanel , BorderLayout.NORTH);
        mainPanel.add(selectPanel , BorderLayout.CENTER);

        frame.add(mainPanel);                                           // adds main panel to frame
        frame.setVisible(true);                                         // sets frame to visible
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  // allows frame to be closed
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);                   // set frame to fit screen
    }
}