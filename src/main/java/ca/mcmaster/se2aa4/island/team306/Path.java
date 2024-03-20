package ca.mcmaster.se2aa4.island.team306;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Path {
    

    Direction heading, goTowards;
    Coords start, pos, end;
    Map map;

    public Path(Coords start, Coords end, Direction heading, Map map){
        this.heading = heading;
        this.start = this.pos = start;
        this.end = end;
        goTowards = null;
        this.map = map;
    }

    public DecisionQueue findPath(){
        DecisionQueue queue = new DecisionQueue();
        if(end.x>start.x){
            goTowards = Direction.EAST;
        }else{
            goTowards = Direction.WEST;
        }
        while(pos.x != end.x){
            if(heading != goTowards){
                if(heading.getRight() == goTowards){
                    queue.enqueue(turnRight());
                    heading = heading.getRight();
                }else if(heading.getLeft() == goTowards){
                    queue.enqueue(turnLeft());
                    heading = heading.getLeft();
                }else{
                    queue.enqueue(turnAround());
                }
            }

            if(pos.x != end.x){
                queue.enqueue(moveForward());
            }

        }

        if(end.y>start.y){
            goTowards = Direction.NORTH;
        }else{
            goTowards = Direction.SOUTH;
        }
        while(pos.y != end.y){
            if(heading != goTowards){
                if(heading.getRight() == goTowards){
                    queue.enqueue(turnRight());
                    heading = heading.getRight();
                }else if(heading.getLeft() == goTowards){
                    queue.enqueue(turnLeft());
                    heading = heading.getLeft();
                }else{
                    queue.enqueue(turnAround());
                }
            }
            if(pos.y != end.y){
                queue.enqueue(moveForward());
                
            }
            
        }
        return queue;
    }

    private DecisionQueue turnRight(){
        DecisionQueue queue = new DecisionQueue();
        Direction facing = heading;

        MapValue left = map.checkCoords(pos.step(facing.getLeft()));
        MapValue left_2 = map.checkCoords(pos.step(facing.getLeft()).step(facing.getLeft()));
        MapValue up_2 = map.checkCoords(pos.step(facing).step(facing));
        if(left == MapValue.OUT_OF_RANGE ){
            queue.enqueue(repositionRight());
        }
        if (left_2 == MapValue.OUT_OF_RANGE){
            queue.enqueue(repositionRight());
        }
        if (up_2 == MapValue.OUT_OF_RANGE){
            queue.enqueue(flatRight());
            return queue;
        }
        
        queue.enqueue(new Decision(DecisionType.FLY_FORWARD, facing));

        queue.enqueue(new Decision(DecisionType.TURN, facing.getLeft()));
        facing = facing.getLeft();

        queue.enqueue(new Decision(DecisionType.TURN, facing.getLeft()));
        facing = facing.getLeft();

        queue.enqueue(new Decision(DecisionType.TURN, facing.getLeft()));
        facing = facing.getLeft();

        queue.enqueue(new Decision(DecisionType.FLY_FORWARD, facing));
        return queue;
        
    }

    private DecisionQueue turnLeft(){
        //2 right = unsafe
        DecisionQueue queue = new DecisionQueue();
        Direction facing = heading;

        MapValue right = map.checkCoords(pos.step(facing.getRight()));
        MapValue right_2 = map.checkCoords(pos.step(facing.getRight()).step(facing.getRight()));
        MapValue up_2 = map.checkCoords(pos.step(facing).step(facing));
        if(right == MapValue.OUT_OF_RANGE ){
            queue.enqueue(repositionLeft());
        }
        if (right_2 == MapValue.OUT_OF_RANGE){
            queue.enqueue(repositionLeft());
        }
        if (up_2 == MapValue.OUT_OF_RANGE){
            queue.enqueue(flatLeft());
            return queue;
        }


        queue.enqueue(new Decision(DecisionType.FLY_FORWARD, facing));

        queue.enqueue(new Decision(DecisionType.TURN, facing.getRight()));
        facing = facing.getRight();

        queue.enqueue(new Decision(DecisionType.TURN, facing.getRight()));
        facing = facing.getRight();

        queue.enqueue(new Decision(DecisionType.TURN, facing.getRight()));
        facing = facing.getRight();

        queue.enqueue(new Decision(DecisionType.FLY_FORWARD, facing));
        return queue;

    }
    
    private DecisionQueue turnAround(){
        DecisionQueue queue = new DecisionQueue();
        queue.enqueue(turnRight());
        heading = heading.getRight();
        queue.enqueue(turnRight());
        heading = heading.getRight();
        return queue;
    }

    private DecisionQueue moveForward(){
        DecisionQueue queue = new DecisionQueue();
        queue.enqueue(new Decision(DecisionType.FLY_FORWARD, heading));
        pos = pos.step(heading);
        return queue;
    } 
    
    private DecisionQueue repositionRight(){
        /*
         * Moves 1 square right or left if turn will go out of bounds
         */
        DecisionQueue queue = new DecisionQueue();
        Direction facing = heading;
        queue.enqueue(new Decision(DecisionType.TURN, facing.getRight()));
        facing = facing.getRight();
        queue.enqueue(new Decision(DecisionType.FLY_FORWARD, facing));
        queue.enqueue(new Decision(DecisionType.TURN, facing.getRight()));
        facing = facing.getRight();
        queue.enqueue(new Decision(DecisionType.TURN, facing.getRight()));
        facing = facing.getRight();
        queue.enqueue(new Decision(DecisionType.TURN, facing.getRight()));
        facing = facing.getRight();

        pos = pos.step(heading.getRight());
        
        return queue;
    }

    private DecisionQueue repositionLeft(){
        DecisionQueue queue = new DecisionQueue();
        Direction facing = heading;
        queue.enqueue(new Decision(DecisionType.TURN, facing.getLeft()));
        facing = facing.getLeft();
        queue.enqueue(new Decision(DecisionType.FLY_FORWARD, facing));
        queue.enqueue(new Decision(DecisionType.TURN, facing.getLeft()));
        facing = facing.getLeft();
        queue.enqueue(new Decision(DecisionType.TURN, facing.getLeft()));
        facing = facing.getLeft();
        queue.enqueue(new Decision(DecisionType.TURN, facing.getLeft()));
        facing = facing.getLeft();

        pos = pos.step(heading.getLeft());
        
        return queue;
    }

    private DecisionQueue flatLeft() {
        DecisionQueue queue = new DecisionQueue();
        Direction facing = heading;
        MapValue right = map.checkCoords(pos.step(facing.getRight()));
        MapValue right_2 = map.checkCoords(pos.step(facing.getRight()).step(facing.getRight()));

        if(right == MapValue.OUT_OF_RANGE ){
            queue.enqueue(repositionLeft());
        }
        if (right_2 == MapValue.OUT_OF_RANGE){
            queue.enqueue(repositionLeft());
        }
        queue.enqueue(new Decision(DecisionType.TURN, facing.getRight()));
        facing = facing.getRight();
        queue.enqueue(new Decision(DecisionType.FLY_FORWARD, facing));
        queue.enqueue(new Decision(DecisionType.TURN, facing.getRight()));
        facing = facing.getRight();
        queue.enqueue(new Decision(DecisionType.FLY_FORWARD, facing));
        queue.enqueue(new Decision(DecisionType.TURN, facing.getRight()));
        facing = facing.getRight();
        queue.enqueue(new Decision(DecisionType.TURN, facing.getRight()));
        facing = facing.getRight();
        queue.enqueue(new Decision(DecisionType.TURN, facing.getLeft()));
        facing = facing.getLeft();
        
        return queue;
    }

    private DecisionQueue flatRight() {
        DecisionQueue queue = new DecisionQueue();
        Direction facing = heading;
        MapValue left = map.checkCoords(pos.step(facing.getLeft()));
        MapValue left_2 = map.checkCoords(pos.step(facing.getLeft()).step(facing.getLeft()));

        if(left == MapValue.OUT_OF_RANGE ){
            queue.enqueue(repositionRight());
        }
        if (left_2 == MapValue.OUT_OF_RANGE){
            queue.enqueue(repositionRight());
        }
        queue.enqueue(new Decision(DecisionType.TURN, facing.getLeft()));
        facing = facing.getLeft();
        queue.enqueue(new Decision(DecisionType.FLY_FORWARD, facing));
        queue.enqueue(new Decision(DecisionType.TURN, facing.getLeft()));
        facing = facing.getLeft();
        queue.enqueue(new Decision(DecisionType.FLY_FORWARD, facing));
        queue.enqueue(new Decision(DecisionType.TURN, facing.getLeft()));
        facing = facing.getLeft();
        queue.enqueue(new Decision(DecisionType.TURN, facing.getLeft()));
        facing = facing.getLeft();
        queue.enqueue(new Decision(DecisionType.TURN, facing.getRight()));
        facing = facing.getRight();
        
        return queue;
    }

    
}
