package random.maze.of.doof;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import static random.maze.of.doof.RandomMazeOfDoof.cX;
import static random.maze.of.doof.RandomMazeOfDoof.cY;
import static random.maze.of.doof.RandomMazeOfDoof.health;
import static random.maze.of.doof.RandomMazeOfDoof.moveEnem;
import static random.maze.of.doof.RandomMazeOfDoof.inviTim;
import static random.maze.of.doof.RandomMazeOfDoof.score;

public class Space {
    
    static final int UP = 0;
    static final int DOWN = 1;
    static final int LEFT = 2;
    static final int RIGHT = 3;
    
    public static int VA = 16;
    
    public int x = 0;
    public int y = 0;
    
    public static int timerSpeed = 250;
    
    public int timer = 0;
    
    public boolean hasDot = false;
    public boolean visited = false;
    public boolean isBad = false;
    public boolean isHealth = false;
    public boolean didJustMove = false;
    public boolean isCurrent = false;
    public int state = 0;
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
    
    public void runLogic(){
        if(isBad){
            if(state == 0){
                didJustMove = false;
                if(timer + 1 < timerSpeed){
                    timer++;
                }
                else{
                    timer = 0;
                    state = 1;
                }
            }
            else if(state == 1){
                if(timer + 1 < timerSpeed / 5){
                    timer++;
                }
                else{
                    timer = 0;
                    state = 0;
                    moveEnem(x,y);
                }
            }
            if(isCurrent && inviTim == 0){
                health--;
                inviTim = 100;
            }
        }
        if(isHealth && x == cX && y == cY){
            health++;
            score++;
            isHealth = false;
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
            if(inviTim % 2 == 0){
                g.setColor(Color.GREEN);
            }
            else{
                g.setColor(Color.DARK_GRAY);
            }
        }
        g2.fillRect(xa, ya, VA, VA);
        
        if(isBad){
            g.setColor(Color.MAGENTA);
            if(isHealth){
                isHealth = false;
            }
            int off1 = 0;
            int off2 = 0;
            if(state == 1){
                off1 = (int)(Math.random() * 4 - 2);
                off2 = (int)(Math.random() * 4 - 2);
            }
            g2.fillRect(xa + 3 + off1, ya + 3 + off2, VA - 6, VA - 6);
        }
        else if(isHealth){
            g.setColor(Color.CYAN);
            g2.fillRect(xa + 3, ya + 3, VA - 6, VA - 6);
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
