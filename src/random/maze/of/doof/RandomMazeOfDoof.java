/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package random.maze.of.doof;

import java.util.*;

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
    public static ArrayList<ArrayList<Space>> maze = new ArrayList<ArrayList<Space>>();
    public static Stack<Integer> sk = new Stack();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
    public static void genMaze(int size){
        max = size;
        maze = new ArrayList<ArrayList<Space>>();
        for(int a = 0; a < size; a++){
            maze.add(new ArrayList<Space>());
            for(int b = 0; b < max; b++){
                maze.get(a).add(new Space(a,b));
            }
        }
        int cX = (int)(Math.random() * max);
        int cY = (int)(Math.random() * max);
        
        //while(){
            
        //}
    }
    
    public static void checkDirect(){
        while(true){
            int dir = (int)(Math.random() * 4);
            //if(){
                
            //}
        }
    }
    
}
