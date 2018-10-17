/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package random.maze.of.doof;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.text.NumberFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.NumberFormatter;

/**
 *
 * @author f0809552
 */
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
    public static Object[] ord = {0,1,2,3};
    public static boolean doLeDo = true;
    public static boolean[] did = {false,false,false,false};
    public static Random rnd = new Random(4);
    public static int size = 4;
    public static int re = 0;
    
    /**
     * @param args the command line arguments
     */
    public static void shuffle() {

        // Shuffle array
        for (int i=size; i>1; i--){
            re = rnd.nextInt(i);
            swap(ord, i-1, re);
        }

        
    }
    private static void swap(Object[] arr, int i, int j) {
        Object tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
    
    public static void doPaint(Graphics g){
        g.setColor(Color.WHITE);
        Graphics2D g2 = (Graphics2D) g;
        g2.fillRect(0, 0, 2000 + max * 2, 2000 + max * 2);
        if(doDraw){
            for(ArrayList<Space> line : maze){
                for(Space space : line){
                    space.draw(g);
                }
            }
        }
    }
    
    public static JFrame f;
    public static java.util.Timer timer;
    public static java.util.Timer timer2 = new java.util.Timer();
    
    static class RemindTask extends TimerTask {
        public void run() {
            draw.repaint();
        }
    }
    
    public static int RUNS = 10;
    
    static class mazeTask extends TimerTask {
        public void run() {
            for(int i = 0; i < RUNS; i++){
                if(!checkDirect()){
                    timer2.cancel();
                }
            }
        }
    }
    
    public static JPanel draw;
    
    public static void main(String[] args) {
        // TODO code application logic here
        f = new JFrame("The Random Maze of Doof");
        draw = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                g.setColor(Color.WHITE);
                Graphics2D g2 = (Graphics2D) g;
                g2.fillRect(0, 0, 2000 + max * 2, 2000 + max * 2);
                doPaint(g);
            }
        };
        
        draw.setBounds(0,0,10000,10000);
        draw.setPreferredSize(new Dimension(10000,10000));
        JScrollPane scrollPane = new JScrollPane(draw);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton b = new JButton("Gen Maze");
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        JFormattedTextField tf = new JFormattedTextField(formatter);
        tf.setBounds(50,50, 150,20);  
        b.setBounds(50,100,95,30);
        b.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){  
                try {  
                    genMaze(Integer.parseInt(tf.getText().replace(",", "")));
                } catch (InterruptedException ex) {
                    Logger.getLogger(RandomMazeOfDoof.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        f.setPreferredSize(new Dimension(400, 500));
        f.setSize(new Dimension(400, 500));
        f.setLayout(new BorderLayout()); 
        f.add(tf, BorderLayout.NORTH);
        f.add(b, BorderLayout.AFTER_LAST_LINE);//adding button in JFrame  
        f.add(scrollPane, BorderLayout.CENTER);
        f.setVisible(true);//making the frame visible
        timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new RemindTask(), 1, 1);
    }
    
    public static boolean startedBefore = false;
    
    public static String genMaze(int size) throws InterruptedException{
        if(startedBefore){
            timer2.cancel();
        }
        startedBefore = true;
        doDraw = true;
        max = size;
        draw.setPreferredSize(new Dimension(max * 2, max * 2));
        maze = new ArrayList<ArrayList<Space>>();
        for(int a = 0; a < size; a++){
            maze.add(new ArrayList<Space>());
            for(int b = 0; b < max; b++){
                maze.get(a).add(new Space(b,a));
            }
        }
        cX = (int)(Math.random() * max);
        cY = (int)(Math.random() * max);
        maze.get(cY).get(cX).visited = true;
        
        String output = "";
        sk.push(5);
        
        boolean theConsoleHasYelledAtMe = false;
        timer2 = new java.util.Timer();
        timer2.scheduleAtFixedRate(new mazeTask(), 1, 1);
        
        
        /*for(ArrayList<Space> line : maze){
            for(Space space : line){
                output += space.pTop();
            }
            output += "\n";
            for(Space space : line){
                output += space.pMid();
            }
            output += "\n";
            for(Space space : line){
                output += space.pBot();
            }
            output += "\n";
        }*/
        System.out.println(output);
        return output;
    }
    
    public static void p(Object p){
        System.out.println(p);
    }
    
    public static void setWall(int dir){
        Space space = maze.get(cY).get(cX);
        space.walls[dir] = false;
        ArrayList<Space> temp = maze.get(space.y);
        temp.set(space.x, space);
        maze.set(space.y, temp);
    }
    
    public static void setVisited(){
        Space space = maze.get(cY).get(cX);
        space.visited = true;
        space.hasDot = true;
        ArrayList<Space> temp = maze.get(space.y);
        temp.set(space.x, space);
        maze.set(space.y, temp);
    }
    public static void setDot(){
        Space space = maze.get(cY).get(cX);
        space.hasDot = true;
        ArrayList<Space> temp = maze.get(space.y);
        temp.set(space.x, space);
        maze.set(space.y, temp);
    }
    public static void setNot(){
        Space space = maze.get(cY).get(cX);
        space.hasDot = false;
        ArrayList<Space> temp = maze.get(space.y);
        temp.set(space.x, space);
        maze.set(space.y, temp);
    }
    public static boolean checkDirect(){
        doLeDo = true;
        did = new boolean[4];
        shuffle();
        for(int i = 0; i < 4 && doLeDo; i++){
            int dir = (int)ord[i];
            switch(dir){
                case UP:
                    if(!did[UP] && cY - 1 >= 0){
                        if(!maze.get(cY - 1).get(cX).visited && !maze.get(cY - 1).get(cX).hasDot){
                            setWall(UP);
                            cY--;
                            setVisited();
                            setWall(DOWN);
                            sk.push(UP);
                            doLeDo = false;
                        }
                    }
                    did[UP] = true;
                    break;
                case DOWN:
                    if(!did[DOWN] && cY + 1 < max){
                        if(!maze.get(cY + 1).get(cX).visited && !maze.get(cY + 1).get(cX).hasDot){
                            setWall(DOWN);
                            cY++;
                            setVisited();
                            setWall(UP);
                            sk.push(DOWN);
                            doLeDo = false;
                        }
                    }
                    did[DOWN] = true;
                    break;
                case LEFT:
                    if(!did[LEFT] && cX - 1 >= 0){
                        if(!maze.get(cY).get(cX - 1).visited && !maze.get(cY).get(cX - 1).hasDot){
                            setWall(LEFT);
                            cX--;
                            setVisited();
                            setWall(RIGHT);
                            sk.push(LEFT);
                            doLeDo = false;
                        }
                    }
                    did[LEFT] = true;
                    break;
                case RIGHT:
                    if(!did[RIGHT] && cX + 1 < max){
                        if(!maze.get(cY).get(cX + 1).visited && !maze.get(cY).get(cX + 1).hasDot){
                            setWall(RIGHT);
                            cX++;
                            setVisited();
                            setWall(LEFT);
                            sk.push(RIGHT);
                            doLeDo = false;
                        }
                    }
                    did[RIGHT] = true;
                    break;
            }
        }
        if(!did[0] || !did[1] || !did[2] || !did[3]){
            return true;
        }
        else{
            setNot();
            int temp = sk.pop();
            switch(temp){
                case UP:
                    cY++;
                    return true;
                case DOWN:
                    cY--;
                    return true;
                case LEFT:
                    cX++;
                    return true;
                case RIGHT:
                    cX--;
                    return true;
                case 5:
                    //checkDirect();
                    return false;
            }
        }
        return false;
    }
    
}
