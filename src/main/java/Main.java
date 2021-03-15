import javax.swing.*;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args)  {

        try {
            PrintWriter instruct = new PrintWriter("python", "UTF-8");
            instruct.println("0");
            instruct.close();
        }
        catch(Exception E){}
        Players players = new Players();


       // net.codejava.swing.hyperlink.Demo demo = new net.codejava.swing.hyperlink.Demo();

    }
}


