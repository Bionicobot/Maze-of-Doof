package random.maze.of.doof;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class Space {
    
    static final int UP = 0;
    static final int DOWN = 1;
    static final int LEFT = 2;
    static final int RIGHT = 3;
    
    static final int VA = 2;
    
    public int x = 0;
    public int y = 0;
    
    public boolean hasDot = false;
    public boolean visited = false;
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

    public void draw(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        
        int xa = x * VA;
        int ya = y * VA;
        int xb = xa + VA;
        int yb = ya + VA;
        
        if(visited){
            g.setColor(Color.WHITE);
        }
        else{
            g.setColor(Color.BLACK);
        }
        
        if(hasDot){
            g.setColor(Color.RED);
        }
        
        g2.fillRect(xa, ya, VA, VA);
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
