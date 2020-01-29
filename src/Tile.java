import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class Tile implements Serializable {
    transient public BufferedImage img;
    private int width;
    private int height;
    private int xpos;
    private int ypos;


    public Tile(String path) {
        File imagebmp = new File(path);
        try {
            this.img = ImageIO.read(imagebmp);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        this.width = this.img.getWidth(null);
        this.height = this.img.getHeight(null);
    }

    public Tile(BufferedImage image) {
        this.img = image;
        this.width = image.getWidth(null);
        this.height = image.getHeight(null);
    }

    public void paintComponent(Graphics g) {
        g.drawImage(img, xpos, ypos, null);
    }

    public void changeImage(String path){
        File imagebmp = new File(path);
        try {
            this.img = ImageIO.read(imagebmp);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    public int getWidth(){ return width; }

    public int getHeight(){ return height; }

    public void setXpos(int x){ xpos = x; }

    public void setYpos(int y){ ypos = y; }

    public int getXpos(){ return xpos; }

    public int getYpos(){ return ypos; }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        ImageIO.write(img, "png", out); // png is lossless
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        img = ImageIO.read(in);

    }
}
