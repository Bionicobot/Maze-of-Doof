package random.maze.of.doof;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import static random.maze.of.doof.RandomMazeOfDoof.doPaint;
import static random.maze.of.doof.RandomMazeOfDoof.handleInput;

public class RandomMazeOfDoof {
    
    static final int UP = 0;
    static final int DOWN = 1;
    static final int LEFT = 2;
    static final int RIGHT = 3;
    
    static final int INIT_TIME = 30;
    
    public static int inviTim = 0;
    public static int resetTime = 0;
    public static int health = 3;
    public static long start = System.nanoTime();
    public static long stop = System.nanoTime();
    public static int maxTime = INIT_TIME;
    public static double currentTimer = INIT_TIME;

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
    
    public static int facing = 0;
    
    public static java.util.Timer timer;
    public static java.util.Timer timer2 = new java.util.Timer();
    
    public static boolean runLoop = true;
    
    public static int RUNS = 1;
    public static int MILL = 10;
    
    public static boolean startedBefore = false;
    public static boolean mazeDone = false;
    
    public static int score = 0;
    
    
    public static long elapsed(){
        return ( stop - start );}
    
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
        g2.drawImage(im, 0,32, null);
        g.setColor(Color.BLACK);
        g2.drawString("Health : " + health, 5,10);
        g2.drawString("Level : " + (max - 9), 5, 25);
        g2.drawString("Time : " + (int)(Math.floor(currentTimer)), 75,10);
        g2.drawString("Score : " + score, 75,25);
    }
    
    static class RemindTask extends TimerTask {
        @Override
        public void run() {
            start = System.nanoTime();
            draw.repaint();
            if(currentTimer > 0 && mazeDone){
                currentTimer += ((double)elapsed() / 1000000000.0);
            }
            stop = System.nanoTime();
        }
    }
    static class mazeTask extends TimerTask {
        @Override
        public void run() {
            if(!mazeDone){
                for(int i = 0; i < RUNS; i++){
                    if(!checkDirect()){
                        mazeDone = true;
                        genEn();
                        p("Done");
                    }
                }
            }
            else{
                if(inviTim > 0 && resetTime == 0){
                    inviTim--;
                }
                int cnt = 0;
                for(int i = 0; i < max; i++){
                    for(int r = 0; r < max; r++){
                        if(maze.get(i).get(r).isBad){
                            maze.get(i).get(r).runLogic();
                            cnt++;
                        }
                    }
                }
                if(cnt <= 0 && resetTime == 0){
                    mazeDone = false;
                    inviTim = 0;
                    score += (int)(Math.floor(currentTimer));
                    timer2.cancel();
                    if(Space.timerSpeed - 10 >= 0){
                        Space.timerSpeed -= 10;
                    }
                    maxTime += 5;
                    currentTimer = maxTime;
                    try {
                        genMaze(max + 1);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(RandomMazeOfDoof.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if(health <= 0 || currentTimer <= 0){
                    mazeDone = false;
                    inviTim = 0;
                    health = 3;
                    score = 0;
                    
                    timer2.cancel();
                    Space.timerSpeed = 250;
                    maxTime = INIT_TIME;
                    currentTimer = INIT_TIME;
                    max = 10;
                    try {
                        genMaze(max);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(RandomMazeOfDoof.class.getName()).log(Level.SEVERE, null, ex);
                    }
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
    public static void p(Object b){System.out.println(b);}
    
    public static void main(String[] args) throws InterruptedException {
        f = new JFrame("The Random Maze of Doof");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        draw = new InputPanel();
        draw.setBounds(0,0,10000,10000);
        draw.setBackground(Color.BLACK);
        draw.setPreferredSize(new Dimension(100,100));
        
        draw.setFocusable(true);
        draw.requestFocusInWindow();
        
        f.setPreferredSize(new Dimension(max * Space.VA, max * Space.VA));
        f.setSize(new Dimension(max * Space.VA, max * Space.VA));
        
        f.setLayout(new BorderLayout());
        f.add(draw, BorderLayout.CENTER);
        
        f.setVisible(true);
        
        timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new RemindTask(), 1, 16);
        genMaze(10);
    }
    
    public static void genMaze(int size) throws InterruptedException{
        draw.setFocusable(true);
        draw.requestFocusInWindow();
        
        mazeDone = false;
        if(startedBefore){
            timer2.cancel();
        }
        startedBefore = true;
        doDraw = true;
        
        max = size;
        
        draw.setPreferredSize(new Dimension(max * Space.VA, max * Space.VA));
        f.setPreferredSize(new Dimension(max * Space.VA + f.getInsets().left + f.getInsets().right + 1, max * Space.VA + f.getInsets().top + f.getInsets().bottom + 1 + 32));
        f.setSize(new Dimension(max * Space.VA + f.getInsets().left + f.getInsets().right + 1, max * Space.VA + f.getInsets().top + f.getInsets().bottom + 1 + 32));
        
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
    
    public static void genEn(){
        for(int i = 0; i < max - 5; i++){
            int aa = (int)(Math.random() * max);
            int bb = (int)(Math.random() * max);
            if(!maze.get(aa).get(bb).isBad && (cY != aa  && cX != bb)){
                maze.get(aa).get(bb).isBad = true;
            }
            else{
                i--;
            }
        }
    }
    
    public static void kill(){
                        switch(facing){
                        
                        case UP:
                            if(cY - 1 >= 0 && !maze.get(cY).get(cX).walls[UP] && maze.get(cY - 1).get(cX).isBad){
                                maze.get(cY - 1).get(cX).isBad = false;
                                maze.get(cY - 1).get(cX).didJustMove = false;
                                score += 1;
                            }
                            break;
                        case DOWN:
                            if(cY + 1 < max && !maze.get(cY).get(cX).walls[DOWN] && maze.get(cY + 1).get(cX).isBad){
                                maze.get(cY + 1).get(cX).isBad = false;
                                maze.get(cY + 1).get(cX).didJustMove = false;
                                score += 1;
                            }
                            break;
                        case LEFT:
                            if(cX - 1 >= 0 && !maze.get(cY).get(cX).walls[LEFT] && maze.get(cY).get(cX - 1).isBad){
                                maze.get(cY).get(cX - 1).isBad = false;
                                maze.get(cY).get(cX - 1).didJustMove = false;
                                score += 1;
                            }
                            break;
                        case RIGHT:
                            if(cX + 1 < max && !maze.get(cY).get(cX).walls[RIGHT] && maze.get(cY).get(cX + 1).isBad){
                                maze.get(cY).get(cX + 1).isBad = false;
                                maze.get(cY).get(cX + 1).didJustMove = false;
                                score += 1;
                            }
                            break;
                        }
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
    
    public static void moveEnem(int x, int y){
        boolean done = false;
        while(!done){
                    int dir = (int)(Math.random() * 4);
                    switch(dir){
                        case UP:
                            if(!maze.get(y).get(x).walls[UP] && y - 1 > -1 && !maze.get(y - 1).get(x).didJustMove){
                                maze.get(y).get(x).isBad = false;
                                maze.get(y - 1).get(x).isBad = true;
                                maze.get(y - 1).get(x).didJustMove = true;
                                done = true;
                            }
                            break;
                        case DOWN:
                            if(!maze.get(y).get(x).walls[DOWN] && y + 1 < max && !maze.get(y + 1).get(x).didJustMove){
                                maze.get(y).get(x).isBad = false;
                                maze.get(y + 1).get(x).isBad = true;
                                maze.get(y + 1).get(x).didJustMove = true;
                                done = true;
                            }
                            break;
                        case LEFT:
                            if(!maze.get(y).get(x).walls[LEFT] && x - 1 > -1 && !maze.get(y).get(x - 1).didJustMove){
                                maze.get(y).get(x).isBad = false;
                                maze.get(y).get(x - 1).isBad = true;
                                maze.get(y).get(x - 1).didJustMove = true;
                                done = true;
                            }
                            break;
                        case RIGHT:
                            if(!maze.get(y).get(x).walls[RIGHT] && x + 1 < max && !maze.get(y).get(x + 1).didJustMove){
                                maze.get(y).get(x).isBad = false;
                                maze.get(y).get(x + 1).isBad = true;
                                maze.get(y).get(x + 1).didJustMove = true;
                                done = true;
                            }
                            break;
                    }
        }
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
    
    public static boolean shiftHeld = false;
    public static boolean[] heldDir = {false, false, false, false};
    
    public static void setAllNoDot(){
        if(cY - 1 >= 0){
            maze.get(cY - 1).get(cX).hasDot = false;
        }
        if(cY + 1 < max){
            maze.get(cY + 1).get(cX).hasDot = false;
        }
        if(cX - 1 >= 0){
            maze.get(cY).get(cX - 1).hasDot = false;
        }
        if(cX + 1 < max){
            maze.get(cY).get(cX + 1).hasDot = false;
        }
    }
    
    public static void handleInput(KeyEvent e, boolean pressed) throws InterruptedException{
        if(mazeDone){
            if(pressed){
                switch(e.getKeyCode()){
                    case KeyEvent.VK_R:
                        genMaze(10);
                        break;
                    
                    case KeyEvent.VK_SHIFT:
                        shiftHeld = true;
                        setAllNoDot();
                        switch(facing){
                        
                        case UP:
                            if(cY - 1 >= 0 && !maze.get(cY).get(cX).walls[UP]){
                                maze.get(cY - 1).get(cX).hasDot = true;
                            }
                            break;
                        case DOWN:
                            if(cY + 1 < max && !maze.get(cY).get(cX).walls[DOWN]){
                                maze.get(cY + 1).get(cX).hasDot = true;
                            }
                            break;
                        case LEFT:
                            if(cX - 1 >= 0 && !maze.get(cY).get(cX).walls[LEFT]){
                                maze.get(cY).get(cX - 1).hasDot = true;
                            }
                            break;
                        case RIGHT:
                            if(cX + 1 < max && !maze.get(cY).get(cX).walls[RIGHT]){
                                maze.get(cY).get(cX + 1).hasDot = true;
                            }
                            break;
                        }
                        break;
                        
                    case KeyEvent.VK_UP:
                        facing = UP;
                        if(cY - 1 >= 0 && !maze.get(cY).get(cX).walls[UP] && !shiftHeld && !heldDir[UP]){
                            heldDir[UP] = true;
                            notCur();
                            cY--;
                            setCur();
                        }
                        else if(shiftHeld && cY - 1 >= 0 && !maze.get(cY).get(cX).walls[UP]){
                            setAllNoDot();
                            maze.get(cY - 1).get(cX).hasDot = true;
                            heldDir[UP] = true;
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        facing = DOWN;
                        if(cY + 1 < max && !maze.get(cY).get(cX).walls[DOWN] && !shiftHeld && !heldDir[DOWN]){
                            heldDir[DOWN] = true;
                            notCur();
                            cY++;
                            setCur();
                        }
                        else if(shiftHeld && cY + 1 >= 0 && !maze.get(cY).get(cX).walls[DOWN]){
                            setAllNoDot();
                            maze.get(cY + 1).get(cX).hasDot = true;
                            heldDir[DOWN] = true;
                        }
                        break;
                    case KeyEvent.VK_LEFT:
                        facing = LEFT;
                        if(cX - 1 >= 0 && !maze.get(cY).get(cX).walls[LEFT] && !(shiftHeld) && !heldDir[LEFT]){
                            heldDir[LEFT] = true;
                            notCur();
                            cX--;
                            setCur();
                        }
                        else if(shiftHeld && cX - 1 >= 0 && !maze.get(cY).get(cX).walls[LEFT]){
                            setAllNoDot();
                            maze.get(cY).get(cX - 1).hasDot = true;
                            heldDir[LEFT] = true;
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        facing = RIGHT;
                        if(cX + 1 < max && !maze.get(cY).get(cX).walls[RIGHT] && !(shiftHeld) && !heldDir[RIGHT]){
                            heldDir[RIGHT] = true;
                            notCur();
                            cX++;
                            setCur();
                        }
                        else if(shiftHeld && cX + 1 >= 0 && !maze.get(cY).get(cX).walls[RIGHT]){
                            setAllNoDot();
                            maze.get(cY).get(cX + 1).hasDot = true;
                            heldDir[RIGHT] = true;
                        }
                        break;
                }
            }
            else{
                switch(e.getKeyCode()){
                    case KeyEvent.VK_SHIFT:
                        shiftHeld = false;
                        kill();
                        setAllNoDot();
                        break;
                        
                    case KeyEvent.VK_UP:
                        facing = 5;
                        if(cY - 1 >= 0){
                            maze.get(cY - 1).get(cX).hasDot = false;
                        }
                        heldDir[UP] = false;
                        break;
                    case KeyEvent.VK_DOWN:
                        facing = 5;
                        if(cY + 1 < max){
                            maze.get(cY + 1).get(cX).hasDot = false;
                        }
                        heldDir[DOWN] = false;
                        break;
                    case KeyEvent.VK_LEFT:
                        facing = 5;
                        if(cX - 1 >= 0){
                            maze.get(cY).get(cX - 1).hasDot = false;
                        }
                        heldDir[LEFT] = false;
                        break;
                    case KeyEvent.VK_RIGHT:
                        facing = 5;
                        if(cX + 1 < max){
                            maze.get(cY).get(cX + 1).hasDot = false;
                        }
                        heldDir[RIGHT] = false;
                        break;
                }
            }
        }
    }
    
}

class InputPanel extends JPanel implements KeyListener{

    private static final long serialVersionUID = -5295084871399194260L;
    @Override
    public void keyTyped(KeyEvent e) {
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        try {
            handleInput(e, true);
        } catch (InterruptedException ex) {
            Logger.getLogger(InputPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        try {
            handleInput(e, false);
        } catch (InterruptedException ex) {
            Logger.getLogger(InputPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void paintComponent(Graphics g){
        g.setColor(Color.WHITE);
        doPaint(g);
    }
    
    public InputPanel(){
        super();
        addKeyListener(this);
    }
}


