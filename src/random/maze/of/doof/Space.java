/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package random.maze.of.doof;

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
        String temp = "#";
        if(walls[UP]){
            temp += "#";
        }
        else{
            temp += " ";
        }
        temp += "#";
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
        temp += "#";
        if(walls[LEFT]){
            temp += "#";
        }
        else{
            temp += " ";
        }
        return temp;
    }
    
    public String pBot(){
        String temp = "#";
        if(walls[DOWN]){
            temp += "#";
        }
        else{
            temp += " ";
        }
        temp += "#";
        return temp;
    }
    
    public void setWall(int w){
        walls[w] = false;
    }
    
    
}
