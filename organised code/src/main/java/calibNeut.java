import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.util.TimerTask;
import java.util.Timer;

public class calibNeut {

    JFrame frame = new JFrame();             // defines frame field
    JPanel panel = new JPanel();             // defines panel field

    public calibNeut(Session[] session) {

        panel.setLayout(new GridLayout(2,1));               // sets panel layout

        JPanel tit = new JPanel();
        JLabel label = new JLabel("Hold foot still!");   // creates a label and sets text
        label.setFont(new Font("Verdana", Font.PLAIN, 50));               // sets font and size
        tit.add(label);                               // adds label to panel
        panel.add(tit);

        JPanel inst = new JPanel();
        JLabel nb = new JLabel("(Remember straight leg, heel down, toes up)");
        nb.setFont(new Font("Verdana", Font.PLAIN, 50));
        inst.add(nb);
        panel.add(inst);

        frame.add(panel);                               // adds panel to frame
        frame.setVisible(true);                         // makes frame visible
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);   // sets frame to fit screen
        
        Timer timer = new Timer();                // creates a timer field

        timer.schedule(new TimerTask(){           // start timer task

            int second = 5;                       // defines start of countdown
            @Override
            public void run() {
                // title of frame will show time
                frame.setTitle("Calibrating... Hold for " + second-- + " seconds.");

                if (second < 0){                            // at time less than 0s
                    SelectDiff sel = new SelectDiff(session);   // next frame will open

                    frame.dispose();                        // this frame will close
                    timer.cancel();                         // this timer will stop
                    timer.purge();
                }
            }
        },0, 1000);     // adds a 1000ms pause between counts
    }
}
