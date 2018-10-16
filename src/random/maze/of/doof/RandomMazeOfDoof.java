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
    public static int cX;
    public static int cY;
    public static ArrayList<ArrayList<Space>> maze = new ArrayList<ArrayList<Space>>();
    public static Stack<Integer> sk = new Stack();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        genMaze(20);
    }
    
    public static void genMaze(int size){
        max = size;
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
        while(!theConsoleHasYelledAtMe){
            try{
                sk.peek();
                checkDirect();
            }
            catch(EmptyStackException e){
                theConsoleHasYelledAtMe = true;
            }
        }
        for(ArrayList<Space> line : maze){
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
        }
        System.out.println(output);
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
    
    public static void checkDirect(){
        boolean doLeDo = true;
        p(sk);
        boolean[] did = {false,false,false,false};
        while(doLeDo && (!did[0] || !did[1] || !did[2] || !did[3])){
            int dir = (int)(Math.random() * 4);
            switch(dir){
                case UP:
                    if(!did[UP] && cY - 1 >= 0){
                        if(!maze.get(cY - 1).get(cX).visited){
                            setWall(UP);
                            cY--;
                            setWall(DOWN);
                        }
                        sk.push(UP);
                        doLeDo = false;
                    }
                    did[UP] = true;
                    break;
                case DOWN:
                    if(!did[DOWN] && cY + 1 < max){
                        if(!maze.get(cY + 1).get(cX).visited){
                            setWall(DOWN);
                            cY++;
                            setWall(UP);
                        }
                        sk.push(DOWN);
                        doLeDo = false;
                    }
                    did[DOWN] = true;
                    break;
                case LEFT:
                    if(!did[LEFT] && cX - 1 >= 0){
                        if(!maze.get(cY).get(cX - 1).visited){
                            setWall(LEFT);
                            cX--;
                            setWall(RIGHT);
                        }
                        sk.push(LEFT);
                        doLeDo = false;
                    }
                    did[LEFT] = true;
                    break;
                case RIGHT:
                    if(!did[RIGHT] && cX + 1 < max){
                        if(!maze.get(cY).get(cX + 1).visited){
                            setWall(RIGHT);
                            cX++;
                            setWall(LEFT);
                        }
                        sk.push(RIGHT);
                        doLeDo = false;
                    }
                    did[RIGHT] = true;
                    break;
            }
        }
        if(!did[0] || !did[1] || !did[2] || !did[3]){
            
        }
        else{
            int temp = sk.pop();
            switch(temp){
                case UP:
                    cY++;
                    break;
                case DOWN:
                    cY--;
                    break;
                case LEFT:
                    cX++;
                    break;
                case RIGHT:
                    cX--;
                    break;
                case 5:
                    break;
            }
        }
    }
    
}
