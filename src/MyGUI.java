import javax.swing.*;
import java.awt.*;


public class MyGUI {
JFrame frame;
JPanel panel;

public MyGUI(String title){
    frame = new JFrame (title);
    panel = new LevelEditor();

    frame.add(panel);
}

    public void startFrame (){
        frame.setPreferredSize(new Dimension(1280, 760));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // reagér paa luk
        frame.pack();                       // saet vinduets stoerrelse
        frame.setVisible(true);                      // aabn vinduet
    }

    public void closeFrame(){
        frame.dispose();
    }
}
