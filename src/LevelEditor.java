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
 private JMenuItem plains, forest, smountain, road, bridge, sea;
 private JMenuItem save, load, reset, setsize;

 private List<List<Tile>> tilegrid;
 private String imgFile;
 private String curType;

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
        curType = "plains";

        setTiles();

        mb = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu tiles = new JMenu("Tiles");

        forest = new JMenuItem("Forest");
        plains = new JMenuItem("Plains");
        smountain = new JMenuItem("Small Mountain");
        road = new JMenuItem("Road");
        bridge = new JMenuItem("Bridge");
        sea = new JMenuItem("Sea");

        save = new JMenuItem("Save level");
        load = new JMenuItem("Load level");
        reset = new JMenuItem("Reset tiles");
        setsize = new JMenuItem("Set map size");

        forest.addActionListener( new MenuListener());
        plains.addActionListener( new MenuListener());
        smountain.addActionListener( new MenuListener());
        road.addActionListener(new MenuListener());
        bridge.addActionListener(new MenuListener());
        sea.addActionListener(new MenuListener());

        save.addActionListener(new MenuListener());
        load.addActionListener(new MenuListener());
        reset.addActionListener(new MenuListener());
        setsize.addActionListener(new MenuListener());

        tiles.add(plains);
        tiles.add(forest);
        tiles.add(smountain);
        tiles.add(road);
        tiles.add(bridge);
        tiles.add(sea);

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
                Tile tile = new Tile(imgFile, typeTranslater());
                tile.setXpos(x*tile.getWidth());
                tile.setYpos(y*tile.getHeight());
                tilelist.add(tile);
            }
            this.tilegrid.add(tilelist);
        }
    }

    private Type typeTranslater(){
        switch (curType){
            case "plains":
                return new Type("plains", 1, 0);
            case "forest":
                return new Type("forest", 2, 1);
            case "mountain":
                return new Type("mountain", 5, 3);
            case "road":
                return new Type("road", 0, 0);
            case "bridge":
                return new Type("bridge", 0, 0);
            case "sea":
                return new Type("sea", 0, 0);
            default:
                System.out.println("Error typetranslation type not supported");
                break;
        }
        return null;
    }

    private void changeWithNeighboursRoad(int x, int y, String type, boolean [][] checkmap){
        String path = "res/tiles/"+type+"/";
        String dir = "";
        String s = "";
        //Recursion boundary checks.
        if(x < 0 || x >= tilewidth || y < 0 || y >= tileheight){
            return;
        }
        checkmap[y][x] = true;

        // Check the neighbours.
        //Check upper neighbor
        if(y-1 >= 0) {
            s = tilegrid.get(y - 1).get(x).type.getTypename();
            if (s.equals(type) || s.equals("bridge")) {
                dir += "8";
                if (!checkmap[y - 1][x] && s.equals(type)) changeWithNeighboursRoad(x, y - 1, type, checkmap);
            }
        }
        //Check lower neighbor
        if(y+1 < tileheight) {
            s = tilegrid.get(y + 1).get(x).type.getTypename();
            if (s.equals(type) || s.equals("bridge")) {
                dir += "2";
                if (!checkmap[y + 1][x] && s.equals(type)) changeWithNeighboursRoad(x, y + 1, type, checkmap);
            }
        }
        //Check left neighbor
        if (x-1 >= 0) {
            s = tilegrid.get(y).get(x - 1).type.getTypename();
            if (s.equals(type) || s.equals("bridge")) {
                dir += "4";
                if (!checkmap[y][x - 1] && s.equals(type)) changeWithNeighboursRoad(x - 1, y, type, checkmap);
            }
        }
        //Check right neighbor
        if (x+1 < tilewidth) {
            s = tilegrid.get(y).get(x + 1).type.getTypename();
            if (s.equals(type) || s.equals("bridge")) {
                dir += "6";
                if (!checkmap[y][x + 1] && s.equals(type)) changeWithNeighboursRoad(x + 1, y, type, checkmap);
            }
        }

        switch(type){
            case "road":
                //Filter for horizontal tile
                if( dir.equals("4") || dir.equals("6") || dir.equals("")){
                    dir = "46";
                }

                //Filter for vertical tile
                if(dir.equals("8") || dir.equals("2")){
                    dir = "82";
                }
                break;

            case "bridge":
                if(dir.equals("8") || dir.equals("2") || dir.equals("82")) dir = "82";
                else dir = "46";
                break;
            default:
                System.out.println("ChangeNeighbour: Error no valid type!");
                break;
        }

        tilegrid.get(y).get(x).changeImage(path+dir+type+".png");
        tilegrid.get(y).get(x).changeType(typeTranslater());

    }
    
    private void changeWithNeighboursWater(int x, int y, String type, boolean [][] checkmap){
        String path = "res/tiles/"+type+"/";
        String dir = "";
        String s = "";
        String s1 = "";
        String s2 = "";
        //Recursion boundary checks.
        if(x < 0 || x >= tilewidth || y < 0 || y >= tileheight){
            return;
        }
        checkmap[y][x] = true;

        // Check the neighbours.
        //Check upper-left neighbor
        if(y-1 >= 0 && x-1 >= 0){
            s = tilegrid.get(y - 1).get(x-1).type.getTypename();
            s1 = tilegrid.get(y).get(x-1).type.getTypename();
            s2 = tilegrid.get(y - 1).get(x).type.getTypename();
            if (s.equals(type) || s.equals("bridge")) {
                //check connection
                if( (s1.equals(type) || s1.equals("bridge")) && (s2.equals(type) || s2.equals("bridge")))
                    dir += "7";
            }
        }
        //Check upper neighbor
        if(y-1 >= 0){
            s = tilegrid.get(y - 1).get(x).type.getTypename();
            if (s.equals(type) || s.equals("bridge")) {
                dir += "8";
                if (!checkmap[y - 1][x] && !s.equals("bridge")) changeWithNeighboursWater(x, y - 1, type, checkmap);
            }
        }
        //Check upper-right neighbor
        if(y-1 >= 0 && x+1 < tilewidth) {
            s = tilegrid.get(y - 1).get(x + 1).type.getTypename();
            s1 = tilegrid.get(y - 1).get(x).type.getTypename();
            s2 = tilegrid.get(y).get(x + 1).type.getTypename();
            if (s.equals(type) || s.equals("bridge")) {
                if ((s1.equals(type) || s1.equals("bridge")) && (s2.equals(type) || s2.equals("bridge")))
                    dir += "9";
            }
        }
        //Check left neighbor
        if (x-1 >= 0) {
            s = tilegrid.get(y).get(x - 1).type.getTypename();
            if (s.equals(type) || s.equals("bridge")) {
                dir += "4";
                if (!checkmap[y][x - 1] && !s.equals("bridge")) changeWithNeighboursWater(x - 1, y, type, checkmap);
            }
        }
        //Check right neighbor
        if (x+1 < tilewidth) {
            s = tilegrid.get(y).get(x + 1).type.getTypename();
            if (s.equals(type) || s.equals("bridge")) {
                dir += "6";
                if (!checkmap[y][x + 1] && !s.equals("bridge")) changeWithNeighboursWater(x + 1, y, type, checkmap);
            }
        }
        //Check lower-left neighbor
        if(y+1 < tileheight && x-1 >= 0) {
            s = tilegrid.get(y + 1).get(x - 1).type.getTypename();
            s1 = tilegrid.get(y).get(x - 1).type.getTypename();
            s2 = tilegrid.get(y + 1).get(x).type.getTypename();
            if (s.equals(type) || s.equals("bridge")) {
                if ((s1.equals(type) || s1.equals("bridge")) && (s2.equals(type) || s2.equals("bridge")))
                    dir += "1";
            }
        }
        //Check lower neighbor
        if(y+1 < tileheight) {
            s = tilegrid.get(y + 1).get(x).type.getTypename();
            if (s.equals(type) || s.equals("bridge")) {
                dir += "2";
                if (!checkmap[y + 1][x] && !s.equals("bridge")) changeWithNeighboursWater(x, y + 1, type, checkmap);
            }
        }
        //Check lower-right neighbor
        if(y+1 < tileheight && x+1 < tilewidth) {
            s = tilegrid.get(y + 1).get(x + 1).type.getTypename();
            s1 = tilegrid.get(y).get(x + 1).type.getTypename();
            s2 = tilegrid.get(y + 1).get(x).type.getTypename();
            if (s.equals(type) || s.equals("bridge")) {
                if ((s1.equals(type) || s1.equals("bridge")) && (s2.equals(type) || s2.equals("bridge")))
                    dir += "3";
            }
        }

        System.out.println(dir);
        tilegrid.get(y).get(x).changeImage(path+dir+type+".png");
        tilegrid.get(y).get(x).changeType(typeTranslater());
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
                    switch (curType){
                        case "forest":
                            tile.changeImage(imgFile);
                            tile.changeType(typeTranslater());
                            break;
                        case "plains":
                            tile.changeImage(imgFile);
                            tile.changeType(typeTranslater());
                            break;
                        case "mountain":
                            tile.changeImage(imgFile);
                            tile.changeType(typeTranslater());
                            break;
                        case "road":
                            changeWithNeighboursRoad(x, y, "road", checkmap);
                            break;
                        case "bridge":
                            changeWithNeighboursRoad(x, y, "bridge", checkmap);
                            break;
                        case "sea":
                            changeWithNeighboursWater(x, y, "sea", checkmap);
                            break;
                        default:
                            System.out.println("Error no valid type set!");
                            break;
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
                curType = "plains";
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
            if (e.getSource() == forest) {
                imgFile = tilepath + "forest.png";
                curType = "forest";
            }
            if (e.getSource() == plains) {
                imgFile = tilepath + "plains.png";
                curType = "plains";
            }
            if (e.getSource() == smountain){
                imgFile = tilepath + "smountain.png";
                curType = "mountain";
            }
            if (e.getSource() == road){
                imgFile = tilepath + "road.png";
                curType = "road";
            }
            if (e.getSource() == bridge){
                imgFile = tilepath + "bridge.png";
                curType = "bridge";
            }
            if(e.getSource() == sea){
                imgFile = tilepath + "sea.png";
                curType = "sea";
            }
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


