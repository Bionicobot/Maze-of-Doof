package random.maze.of.doof;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.NumberFormatter;
import static random.maze.of.doof.RandomMazeOfDoof.doPaint;

public class RandomMazeOfDoof {
    
    static final int UP = 0;
    static final int DOWN = 1;
    static final int LEFT = 2;
    static final int RIGHT = 3;

    public static int max = 0;
    public static int cX;
    public static int cY;
    public static boolean doDraw = false;
    public static ArrayList<ArrayList<Space>> maze = new ArrayList<ArrayList<Space>>();
    public static Stack<Integer> sk = new Stack();
    public static boolean doLeDo = true;
    public static boolean[] did = {false,false,false,false};
    public static String output = "";
    
    public static JFrame f;
    public static JPanel draw;
    public static JScrollPane scrollPane;
    
    public static BufferedImage im;
    
    public static java.util.Timer timer;
    public static java.util.Timer timer2 = new java.util.Timer();
    
    public static boolean runLoop = true;
    
    public static int RUNS = 1;
    public static int MILL = 1;
    
    public static boolean startedBefore = false;
    
    public static void doPaint(Graphics g){
        g.setColor(Color.WHITE);
        g.clearRect(0,0, draw.getWidth(),draw.getHeight());
        Graphics2D g2 = (Graphics2D) g;
        g2.fillRect(0,0, draw.getWidth(),draw.getHeight());
        im = new BufferedImage(max * Space.VA + 1, max * Space.VA + 1, BufferedImage.TYPE_INT_RGB);
        Graphics img = im.getGraphics();
        if(doDraw){
            maze.forEach((line) -> {
                line.forEach((space) -> {
                    space.draw(img);
                });
            });
        }
        g2.drawImage(im, 0,0, null);
    }
    
    static class RemindTask extends TimerTask {
        @Override
        public void run() {
            draw.repaint();
        }
    }
    static class mazeTask extends TimerTask {
        @Override
        public void run() {
            for(int i = 0; i < RUNS; i++){
                if(!checkDirect()){
                    timer2.cancel();
                }
            }
        }
    }
    
    public static void printDebug(){
        output = "";
        maze.stream().map((line) -> {
            line.forEach((space) -> {
                output += space.pTop();
            });
            return line;
        }).map((line) -> {
            output += "\n";
            return line;
        }).map((line) -> {
            line.forEach((space) -> {
                output += space.pMid();
            });
            return line;
        }).map((line) -> {
            output += "\n";
            return line;
        }).map((line) -> {
            line.forEach((space) -> {
                output += space.pBot();
            });
            return line;
        }).forEachOrdered((_item) -> {
            output += "\n";
        });
                    System.out.println(output);
    }
    
    public static void main(String[] args) {
        f = new JFrame("The Random Maze of Doof");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        draw = new InputPanel();
        draw.setBounds(0,0,10000,10000);
        draw.setBackground(Color.BLACK);
        draw.setPreferredSize(new Dimension(100,100));
        
        scrollPane = new JScrollPane(draw);
        
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        
        JFormattedTextField tf = new JFormattedTextField(formatter);
        tf.setBounds(50,50, 150,20);
        
        JButton b = new JButton("Gen Maze");
        b.setBounds(50,100,95,30);
        b.addActionListener((ActionEvent e) -> {
            try {
                genMaze(Integer.parseInt(tf.getText().replace(",", "")));
            } catch (InterruptedException ex) {
                Logger.getLogger(RandomMazeOfDoof.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        f.setPreferredSize(new Dimension(1000, 1000));
        f.setSize(new Dimension(1000, 1000));
        
        f.setLayout(new BorderLayout());
        f.add(tf, BorderLayout.NORTH);
        f.add(scrollPane, BorderLayout.CENTER);
        f.add(b, BorderLayout.PAGE_END);
        
        f.setVisible(true);
        
        timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new RemindTask(), 1, 16);
    }
    
    public static void genMaze(int size) throws InterruptedException{
        if(startedBefore){
            timer2.cancel();
        }
        startedBefore = true;
        doDraw = true;
        
        max = size;
        
        draw.setPreferredSize(new Dimension(max * Space.VA, max * Space.VA));
        scrollPane.setViewportView(draw);
        
        maze = new ArrayList<>();
        for(int a = 0; a < size; a++){
            maze.add(new ArrayList<>());
            for(int b = 0; b < max; b++){
                maze.get(a).add(new Space(b,a));
            }
        }
        
        cX = (int)(Math.random() * max);
        cY = (int)(Math.random() * max);
        
        setVisited();
        
        sk.push(5);
        
        timer2 = new java.util.Timer();
        timer2.schedule(new mazeTask(), 1, MILL);
    }
    
    public static void setWall(int dir){
        maze.get(cY).get(cX).walls[dir] = false;
    }
    public static void setVisited(){
        maze.get(cY).get(cX).visited = true;
        maze.get(cY).get(cX).hasDot = true;
    }
    public static void setDot(){
        maze.get(cY).get(cX).hasDot = true;
    }
    public static void setNot(){
        maze.get(cY).get(cX).hasDot = false;
    }
    public static void setCur(){
        maze.get(cY).get(cX).isCurrent = true;
    }
    public static void notCur(){
        maze.get(cY).get(cX).isCurrent = false;
    }
    
    public static boolean checkDirect(){
        doLeDo = true;
        did = new boolean[4];
        runLoop = true;
        while(doLeDo && runLoop){
            if(did[UP] && did[DOWN] && did[LEFT] && did[RIGHT]){
                runLoop = false;
            }
            int dir = (int)(Math.random() * 4);
            switch(dir){
                case UP:
                    if(!did[0] && cY - 1 >= 0){
                        if(!maze.get(cY - 1).get(cX).visited){
                            notCur();
                            setWall(UP);
                            cY--;
                            setVisited();
                            setWall(DOWN);
                            sk.push(UP);
                            setCur();
                            doLeDo = false;
                        }
                    }
                    did[UP] = true;
                    break;
                case DOWN:
                    if(!did[1] && cY + 1 < max){
                        if(!maze.get(cY + 1).get(cX).visited){
                            notCur();
                            setWall(DOWN);
                            cY++;
                            setVisited();
                            setWall(UP);
                            sk.push(DOWN);
                            setCur();
                            doLeDo = false;
                        }
                    }
                    did[DOWN] = true;
                    break;
                case LEFT:
                    if(!did[2] && cX - 1 >= 0){
                        if(!maze.get(cY).get(cX - 1).visited){
                            notCur();
                            setWall(LEFT);
                            cX--;
                            setVisited();
                            setWall(RIGHT);
                            sk.push(LEFT);
                            setCur();
                            doLeDo = false;
                        }
                    }
                    did[LEFT] = true;
                    break;
                case RIGHT:
                    if(!did[3] && cX + 1 < max){
                        if(!maze.get(cY).get(cX + 1).visited){
                            notCur();
                            setWall(RIGHT);
                            cX++;
                            setVisited();
                            setWall(LEFT);
                            sk.push(RIGHT);
                            setCur();
                            doLeDo = false;
                        }
                    }
                    did[RIGHT] = true;
                    break;
            }
        }
        if(runLoop){
            return true;
        }
        else{
            setNot();
            notCur();
            if(!sk.empty()){
                switch(sk.pop()){
                    case UP:
                        cY++;
                        setCur();
                        return true;
                    case DOWN:
                        cY--;
                        setCur();
                        return true;
                    case LEFT:
                        cX++;
                        setCur();
                        return true;
                    case RIGHT:
                        cX--;
                        setCur();
                        return true;
                    case 5:
                        setCur();
                        return false;
                }
            }
            else{
                return false;
            }
        }
        return true;
    }
    
}

class InputPanel extends JPanel implements KeyListener{
    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("keyTyped: "+e);
    }
    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("keyPressed: "+e);
    }
    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("keyReleased: "+e);
    }
    @Override
    public void paintComponent(Graphics g){
        g.setColor(Color.WHITE);
        doPaint(g);
    }
}


