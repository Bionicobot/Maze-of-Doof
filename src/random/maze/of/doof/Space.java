/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package random.maze.of.doof;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

/**
 *
 * @author f0809552
 */
public class Space {
    
    static final int UP = 0;
    static final int DOWN = 1;
    static final int LEFT = 2;
    static final int RIGHT = 3;
    
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
    
    public void setWall(int w){
        walls[w] = false;
    }
    
    public void draw(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        if(visited){
            g.setColor(Color.WHITE);
        }
        else{
            g.setColor(Color.BLACK);
        }
        int xa = x * 6;
        int ya = y * 6;
        int xb = xa + 6;
        int yb = ya + 6;
        g2.fillRect(xa, ya, 6, 6);
        if(hasDot){
            g.setColor(Color.RED);
            g2.fillRect(xa + 2, ya + 2, 3, 3);
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
