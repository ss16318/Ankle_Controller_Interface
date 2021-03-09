import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Practice {

    public Practice(Session[] session) {

        JFrame frame = new JFrame();                        // creates fields
        JPanel mainPanel = new JPanel();
        JPanel titlePanel = new JPanel();
        JPanel backPanel = new JPanel();

        JLabel title = new JLabel("Let's start practicing");      // creates title label
        title.setFont(new Font("Verdana", Font.PLAIN, 70));
        title.setBorder(BorderFactory.createEmptyBorder(100, 30, 100, 30)); // padding for spacing
        titlePanel.add(title);                                  // adds title label to title panel

        JButton back = new JButton("Go back");       // creates select game button
        back.setFont(new Font("Verdana", Font.PLAIN, 50));
        back.setBorder(BorderFactory.createEmptyBorder(100, 30, 100, 30));

        back.addActionListener(new ActionListener() {         // waiting for button to be clicked
            @Override
            public void actionPerformed(ActionEvent e) {
                menu Menu = new menu(session);                         // opens menu class
                frame.dispose();
            }
        });

        backPanel.add(back);                                // adds select button to select panel

        // sets main panel layout and adds sub panels
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(titlePanel , BorderLayout.NORTH);
        mainPanel.add(backPanel , BorderLayout.CENTER);

        frame.add(mainPanel);                                           // adds main panel to frame
        frame.setVisible(true);                                         // sets frame to visible
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  // allows frame to be closed
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);                   // set frame to fit screen
    }
}
