package random.maze.of.doof;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import static random.maze.of.doof.RandomMazeOfDoof.moveEnem;

public class Space {
    
    static final int UP = 0;
    static final int DOWN = 1;
    static final int LEFT = 2;
    static final int RIGHT = 3;
    
    public static int VA = 16;
    
    public int x = 0;
    public int y = 0;
    
    public int timer = 0;
    
    public boolean hasDot = false;
    public boolean visited = false;
    public boolean isBad = false;
    public boolean isCurrent = false;
    public boolean[] walls =
    {
        true, // UP
        true, // DOWN
        true, // LEFT
        true  // RIGHT
    };
    
    public Space(int xp, int yp){
        x = xp;
        y = yp;
    }
    
    public String pTop(){
        String temp = "";
        if(walls[LEFT] || walls[UP]){
            temp += "#";
        }
        else{
            temp += " ";
        }
        if(walls[UP]){
            temp += "#";
        }
        else{
            temp += " ";
        }
        if(walls[RIGHT] || walls[UP]){
            temp += "#";
        }
        else{
            temp += " ";
        }
        return temp;
    }
    public String pMid(){
        String temp = "";
        if(walls[LEFT]){
            temp += "#";
        }
        else{
            temp += " ";
        }
        if(visited){
            temp += " ";
        }
        else{
            temp += "#";
        }
        if(walls[RIGHT]){
            temp += "#";
        }
        else{
            temp += " ";
        }
        return temp;
    }
    public String pBot(){
        String temp = "";
        if(walls[LEFT] || walls[DOWN]){
            temp += "#";
        }
        else{
            temp += " ";
        }
        if(walls[DOWN]){
            temp += "#";
        }
        else{
            temp += " ";
        }
        if(walls[RIGHT] || walls[DOWN]){
            temp += "#";
        }
        else{
            temp += " ";
        }
        return temp;
    }
    
    public int state = 0;
    
    public void runLogic(){
        if(isBad){
            if(state == 0){
                if(timer < 1000){
                    timer++;
                }
                else{
                    timer = 0;
                    state = 1;
                }
            }
            else if(state == 1){
                if(timer < 100){
                    timer++;
                }
                else{
                    timer = 0;
                    state = 0;
                    moveEnem(x,y);
                }
            }
        }
    }

    public void draw(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        
        int xa = x * VA;
        int ya = y * VA;
        int xb = xa + VA;
        int yb = ya + VA;
        
        g.setColor(Color.WHITE);
        g2.fillRect(xa, ya, VA, VA);
        
        if(visited){
            g.setColor(Color.WHITE);
        }
        else{
            g.setColor(Color.BLACK);
        }
        if(hasDot){
            g.setColor(Color.RED);
        }
        if(isCurrent){
            g.setColor(Color.GREEN);
        }
        g2.fillRect(xa, ya, VA, VA);
        
        if(isBad){
            g.setColor(Color.MAGENTA);
            int off1 = 0;
            int off2 = 0;
            if(state == 1){
                off1 = (int)(Math.random() * 4 - 2);
                off2 = (int)(Math.random() * 4 - 2);
            }
            g2.fillRect(xa + 3 + off1, ya + 3 + off2, VA - 6, VA - 6);
        }
        
        g.setColor(Color.BLACK);
        
        if(walls[UP]){
            g2.drawLine(xa, ya, xb, ya);
        }
        if(walls[DOWN]){
            g2.drawLine(xa, yb, xb, yb);
        }
        if(walls[LEFT]){
            g2.drawLine(xa, ya, xa, yb);
        }
        if(walls[RIGHT]){
            g2.drawLine(xb, ya, xb, yb);
        }
    }
    
    
}
