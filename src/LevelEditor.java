import GameLogic.Tile;
import GameLogic.Type;

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
 private JMenuItem plains, forest, smountain, road;
 private JMenuItem save, load, reset, setsize;

 private List<List<Tile>> tilegrid;
 private String imgFile;

 public LevelEditor() {
     tilewidth = 20;
     tileheight = 20;
     initComponents();
 }

 public LevelEditor(int twidth, int theight){
     tileheight = theight;
     tilewidth = twidth;
     initComponents();
 }

    public void initComponents(){

        imgFile = "res/tiles/plains.png";

        setTiles();

        mb = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu tiles = new JMenu("Tiles");

        forest = new JMenuItem("Forest");
        plains = new JMenuItem("Plains");
        smountain = new JMenuItem("Small Mountain");
        road = new JMenuItem("Road");

        save = new JMenuItem("Save level");
        load = new JMenuItem("Load level");
        reset = new JMenuItem("Reset tiles");
        setsize = new JMenuItem("Set map size");

        forest.addActionListener( new MenuListener());
        plains.addActionListener( new MenuListener());
        smountain.addActionListener( new MenuListener());
        road.addActionListener(new MenuListener());

        save.addActionListener(new MenuListener());
        load.addActionListener(new MenuListener());
        reset.addActionListener(new MenuListener());
        setsize.addActionListener(new MenuListener());

        tiles.add(plains);
        tiles.add(forest);
        tiles.add(smountain);
        tiles.add(road);

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
        this.tilegrid = new ArrayList<>();

        for(int y = 0; y < tileheight; y++){
            List<Tile> tilelist = new ArrayList<>();
            for (int x = 0; x < tilewidth; x++){
                Tile tile = new Tile(imgFile, typeTranslater(imgFile));
                tile.setXpos(x*tile.getWidth());
                tile.setYpos(y*tile.getHeight());
                tilelist.add(tile);
            }
            this.tilegrid.add(tilelist);
        }
    }

    private Type typeTranslater(String path){
        if(path.equals("res/tiles/plains.png")) return new Type("plains", 1, 0);
        if(path.equals("res/tiles/forest.png")) return new Type("forest", 2, 1);
        if(path.equals("res/tiles/smountain.png")) return new Type("mountain", 5, 3);
        if(path.substring(path.length()-"road.png".length()).equals("road.png")) return new Type("road", 0, 0);
        return null;
    }

    private void changeWithNeighbours(int x, int y, String type, boolean [][] checkmap){
        String path = "res/tiles/"+type+"/";
        String dir = "";
        //Recursion boundary checks.
        if(x < 0 || x >= tilewidth || y < 0 || y >= tileheight){
            return;
        }
        checkmap[y][x] = true;

        // Check the neighbours.
        //Check upper neighbor
        if(y-1 >= 0)
            if (tilegrid.get(y - 1).get(x).type.getTypename().equals(type)) {
                dir += "u";
                if (!checkmap[y - 1][x]) changeWithNeighbours(x, y - 1, type, checkmap);
            }
        //Check lower neighbor
        if(y+1 < tileheight)
            if (tilegrid.get(y+1).get(x).type.getTypename().equals(type)){
                dir += "d";
                if(!checkmap[y+1][x]) changeWithNeighbours(x, y+1, type, checkmap);
            }
        //Check left neighbor
        if (x-1 >= 0)
        if (tilegrid.get(y).get(x-1).type.getTypename().equals(type)){
            dir += "l";
            if(!checkmap[y][x-1]) changeWithNeighbours(x-1, y, type, checkmap);
        }
        //Check right neighbor
        if (x+1 < tilewidth)
        if (tilegrid.get(y).get(x+1).type.getTypename().equals(type)){
            dir += "r";
            if (!checkmap[y][x+1]) changeWithNeighbours(x+1, y, type, checkmap);
        }

        System.out.println(dir);

        //Filter for horizontal tile
        if( dir.equals("l") || dir.equals("r") || dir.equals("")){
            dir = "lr";
        }

        //Filter for vertical tile
        if(dir.equals("u") || dir.equals("d")){
            dir = "ud";
        }

        tilegrid.get(y).get(x).changeImage(path+dir+type+".png");
        tilegrid.get(y).get(x).changeType(typeTranslater(path+dir+type+".png"));

    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g); //Repaint background. Removes ghosting
        for(List<Tile> tilelist : tilegrid) {
            for (Tile tile : tilelist) {
                g.drawImage(tile.img, tile.getXpos(), tile.getYpos(), this); //Draws grid of tiles.
            }
        }


        System.out.println("image drawed!");
    }

    private void musKlikket(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        System.out.println("Click!");
        boolean [] [] checkmap = new boolean[tileheight][tilewidth];
        for(int y = 0; y < tilegrid.size(); y++) {
            List<Tile> tilelist = tilegrid.get(y);
            for (int x = 0; x < tilelist.size(); x++) {
                Tile tile = tilelist.get(x);
                if (tile.getXpos() <= evt.getX()
                        && tile.getXpos() + tile.getWidth() > evt.getX()
                        && tile.getYpos() <= evt.getY()
                        && tile.getYpos() + tile.getHeight() > evt.getY()) {
                    System.out.println("changing image!");
                    if (imgFile.substring(imgFile.length()-"road.png".length()).equals("road.png")){

                        changeWithNeighbours(x, y, "road", checkmap);
                    } else {
                        tile.changeImage(imgFile);
                        tile.changeType(typeTranslater(imgFile));
                    }
                    break;
                }
            }
        }


        repaint();
    }

    private class MenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String path = "level/";

            if (e.getSource() == save) {
                String s = JOptionPane.showInputDialog("Savefile name: (.lev)\n");
                path += s;

                saveFile(tilegrid, path);
                repaint();
            }

            if (e.getSource() == load) {
                String s = JOptionPane.showInputDialog("Load a level: (.lev)\n");
                path += s;
                loadFile(path);
                repaint();
            }

            if (e.getSource() == reset){
                imgFile = "res/tiles/plains.png";
                setTiles();
                repaint();
            }

            if(e.getSource() == setsize){

                imgFile = "res/tiles/plains.png";
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
            String tilepath = "res/tiles/";
            if (e.getSource() == forest) imgFile = tilepath + "forest.png";
            if (e.getSource() == plains) imgFile = tilepath + "plains.png";
            if (e.getSource() == smountain) imgFile = tilepath + "smountain.png";
            if (e.getSource() == road) imgFile = tilepath + "road.png";
        }

        private void saveFile(List<List<Tile>> list, String path){
            try (ObjectOutputStream oos =
                         new ObjectOutputStream(new FileOutputStream(path))) {
                oos.writeInt(tilewidth);
                oos.writeInt(tileheight);
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
                tilewidth = ois.readInt();
                tileheight = ois.readInt();
                tilegrid = (List<List<Tile>>) ois.readObject();

                ois.close();
                System.out.println("Done");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}


