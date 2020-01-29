import javax.swing.*;
import java.awt.*;


public class MyGUI {
JFrame frame = null;
JPanel panel = null;

public MyGUI(String title, String path){
    frame = new JFrame (title);
    panel = new LevelEditor(path);

    frame.add(panel);
}

    public void startFrame (){
        frame.setPreferredSize(new Dimension(1280, 760));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // reagér paa luk
        frame.pack();                       // saet vinduets stoerrelse
        frame.setVisible(true);                      // aabn vinduet
    }
    /*
    public void displayImage(String path){
        File imagebmp = new File(path);

        BufferedImage image = null;
        try {
            image = ImageIO.read(imagebmp);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        ImageIcon icon = new ImageIcon(image);
        label = new JLabel(icon);
        panel.add(label);
    }
    public void changeImage(String path){
    if(label != null) panel.remove(label);
    displayImage(path);
    }
    */

    public void closeFrame(){
        frame.dispose();
    }
}
