import javax.swing.*;
import java.awt.*;
import java.util.TimerTask;
import java.util.Timer;

public class Loading {

    JFrame frame = new JFrame();             // defines frame field
    JPanel panel = new JPanel();             // defines panel field

    public Loading() {

        panel.setLayout(new GridLayout(1,1));

        try {
            Runtime.getRuntime().exec("C:\\Users\\sebas\\Desktop\\Ankle\\mainProgram.exe");
        } catch (Exception E1) {}

        JPanel tit = new JPanel();
        JLabel label = new JLabel("LOADING.....");   // creates a label and sets text
        label.setFont(new Font("Verdana", Font.PLAIN, 50));               // sets font and size
        tit.add(label);                               // adds label to panel
        panel.add(tit);
        tit.setBorder(BorderFactory.createEmptyBorder(100, 30, 100, 30));

        frame.add(panel);
        frame.setVisible(true);                         // makes frame visible
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);   // sets frame to fit screen

        Timer timer = new Timer();                // creates a timer field

        timer.schedule(new TimerTask(){           // start timer task

            int second = 15;                       // defines start of countdown
            @Override
            public void run() {
                // title of frame will show time
                frame.setTitle("Calibrating... Hold for " + second-- + " seconds.");

                if (second < 0){                            // at time less than 0s
                    Players player = new Players();   // next frame will open

                    frame.dispose();                        // this frame will close
                    timer.cancel();                         // this timer will stop
                    timer.purge();
                }
            }
        },0, 1000);     // adds a 1000ms pause between counts
    }
}
