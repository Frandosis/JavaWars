import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public class LevelEditor extends javax.swing.JPanel {
 private int tilewidth;
 private int tileheight;

 private JMenuBar mb;
 private JMenuItem grass, forest, smountain;
 private JMenuItem save, load, reset, setsize;

 private List<Tile> tilegrid;
 private String imgFile;

 public LevelEditor(String path) {
     tilewidth = 20;
     tileheight = 20;
     initComponents(path);
 }

 public LevelEditor(String path, int twidth, int theight){
     tileheight = theight;
     tilewidth = twidth;
     initComponents(path);
 }

    public void initComponents(String path){

        imgFile = path;

        setTiles();

        mb = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu tiles = new JMenu("Tiles");

        forest = new JMenuItem("Forest");
        grass = new JMenuItem("Grass");
        smountain = new JMenuItem("Small Mountain");

        save = new JMenuItem("Save level");
        load = new JMenuItem("Load level");
        reset = new JMenuItem("Reset tiles");
        setsize = new JMenuItem("Set map size");

        forest.addActionListener( new MenuListener());
        grass.addActionListener( new MenuListener());
        smountain.addActionListener( new MenuListener());

        save.addActionListener(new MenuListener());
        load.addActionListener(new MenuListener());
        reset.addActionListener(new MenuListener());
        setsize.addActionListener(new MenuListener());

        tiles.add(grass);
        tiles.add(forest);
        tiles.add(smountain);

        file.add(save);
        file.add(load);
        file.add(reset);
        file.add(setsize);

        mb.add(file);
        mb.add(tiles);

        this.add(mb);

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                musKlikket(e);
            }
        });
    }

    public void setTiles() {
        this.tilegrid = new ArrayList<Tile>();

        for(int y = 0; y < tileheight; y++){
            for (int x = 0; x < tilewidth; x++){
                Tile tile = new Tile(imgFile);
                tile.setXpos(x*tile.getWidth());
                tile.setYpos(y*tile.getHeight());
                tilegrid.add(tile);
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); //Repaint background. Removes ghosting

        for(Tile tile : tilegrid){
            g.drawImage(tile.img, tile.getXpos(), tile.getYpos(),this); //Draws grid of tiles.
        }


        System.out.println("image drawed!");
    }

    private void musKlikket(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        System.out.println("Click!");
        for(Tile tile : tilegrid){
            if (    tile.getXpos() <= evt.getX()
                &&  tile.getXpos() + tile.getWidth() > evt.getX()
                &&  tile.getYpos() <= evt.getY()
                &&  tile.getYpos() + tile.getHeight() > evt.getY()) {
                System.out.println("changing image!");
                tile.changeImage(imgFile);
                break;
            }
        }


        repaint();
    }

    private class MenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String path = "level/";

            if (e.getSource() == save) {
                String s = (String)JOptionPane.showInputDialog("Savefile name: (.lev)\n");
                path += s;

                saveFile(tilegrid, path);
                repaint();
            }

            if (e.getSource() == load) {
                String s = (String)JOptionPane.showInputDialog("Load a level: (.lev)\n");
                path += s;
                loadFile(path);
                repaint();
            }

            if (e.getSource() == reset){
                imgFile = "res/tiles/grass.png";
                for (Tile tile : tilegrid){
                    tile.changeImage(imgFile);
                }
                repaint();
            }

            if(e.getSource() == setsize){
                JTextField text0 = new JTextField();
                JTextField text1 = new JTextField();

                Object [] inputFields = {"Enter Map Width: ", text0, "Enter Map Height: ", text1};
                int option = JOptionPane.showConfirmDialog(null, inputFields, "Multiple Inputs", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if(option == JOptionPane.OK_OPTION){
                    tilewidth = Integer.parseInt(text0.getText());
                    tileheight = Integer.parseInt(text1.getText());
                }
                setTiles();
                repaint();
            }

            if (e.getSource() == forest) imgFile = "res/tiles/forest.png";
            if (e.getSource() == grass) imgFile = "res/tiles/grass.png";
            if (e.getSource() == smountain) imgFile = "res/tiles/smountain.png";
        }

        private void saveFile(List<Tile> list, String path){
            try (ObjectOutputStream oos =
                         new ObjectOutputStream(new FileOutputStream(path))) {

                oos.writeObject(list);
                oos.close();
                System.out.println("Done");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private void loadFile(String path){
            try (ObjectInputStream ois =
                         new ObjectInputStream(new FileInputStream(path))) {

                tilegrid = (List<Tile>) ois.readObject();

                ois.close();
                System.out.println("Done");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}


