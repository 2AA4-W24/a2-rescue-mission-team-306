package ca.mcmaster.se2aa4.island.team306;

import java.util.LinkedList;
import java.util.Queue;

public class Path {
    Direction heading, goTowards;
    Coords start, pos, end;
    Queue<Direction> path = new LinkedList<>();

    public Path(Coords start, Coords end, Direction heading){
        this.heading = heading;
        this.start = this.pos = start;
        this.end = end;
        goTowards = null;
    }

    public Queue<Direction> findPath(){
        if(end.x>start.x){
            goTowards = Direction.EAST;
        }else{
            goTowards = Direction.WEST;
        }
        while(start.x != end.x){
            if(heading != goTowards){
                if(heading.getRight() == goTowards){
                    turnRight();
                }else if(heading.getLeft() == goTowards){
                    turnLeft();
                }else{
                    turnAround();
                }
            }

            moveForward();
        }

        if(end.y>start.y){
            goTowards = Direction.NORTH;
        }else{
            goTowards = Direction.SOUTH;
        }
        while(start.y != end.y){
            if(heading != goTowards){
                if(heading.getRight() == goTowards){
                    turnRight();
                }else if(heading.getLeft() == goTowards){
                    turnLeft();
                }else{
                    turnAround();
                }
            }

            moveForward();
        }
        return this.path;
    }

    private void turnRight(){
        path.add(heading);

        path.add(heading.getLeft());
        heading = heading.getLeft();

        path.add(heading.getLeft());
        heading = heading.getLeft();

        path.add(heading.getLeft());
        heading = heading.getLeft();

        path.add(heading);
        
    }

    private void turnLeft(){
        path.add(heading);

        path.add(heading.getRight());
        heading = heading.getRight();

        path.add(heading.getRight());
        heading = heading.getRight();

        path.add(heading.getRight());
        heading = heading.getRight();

        path.add(heading);

    }
    
    private void turnAround(){
        path.add(heading.getRight());
        heading = heading.getRight();

        path.add(heading.getLeft());
        heading = heading.getLeft();

        path.add(heading.getLeft());
        heading = heading.getLeft();

        path.add(heading.getLeft());
        heading = heading.getLeft();

        path.add(heading);

        path.add(heading);
    }

    private void moveForward(){
        path.add(heading);
        pos = pos.step(heading);
    } 
    
}
