package ca.mcmaster.se2aa4.island.team306;

public class Path {
    Direction heading, goTowards;
    Coords start, pos, end;

    public Path(Coords start, Coords end, Direction heading){
        this.heading = heading;
        this.start = this.pos = start;
        this.end = end;
        goTowards = null;
    }

    public DecisionQueue findPath(){
        DecisionQueue queue = new DecisionQueue();
        if(end.x>start.x){
            goTowards = Direction.EAST;
        }else{
            goTowards = Direction.WEST;
        }
        while(start.x != end.x){
            if(heading != goTowards){
                if(heading.getRight() == goTowards){
                    queue.enqueue(turnRight());
                }else if(heading.getLeft() == goTowards){
                    queue.enqueue(turnLeft());
                }else{
                    queue.enqueue(turnAround());
                }
            }

            queue.enqueue(moveForward());
        }

        if(end.y>start.y){
            goTowards = Direction.NORTH;
        }else{
            goTowards = Direction.SOUTH;
        }
        while(start.y != end.y){
            if(heading != goTowards){
                if(heading.getRight() == goTowards){
                    queue.enqueue(turnRight());
                }else if(heading.getLeft() == goTowards){
                    queue.enqueue(turnLeft());
                }else{
                    queue.enqueue(turnAround());
                }
            }

            queue.enqueue(moveForward());
        }
        return queue;
    }

    public DecisionQueue turnRight(){
        DecisionQueue queue = new DecisionQueue();
        queue.enqueue(new Decision(DecisionType.FLY_FORWARD, heading));

        queue.enqueue(new Decision(DecisionType.TURN, heading.getLeft()));
        heading = heading.getLeft();

        queue.enqueue(new Decision(DecisionType.TURN, heading.getLeft()));
        heading = heading.getLeft();

        queue.enqueue(new Decision(DecisionType.TURN, heading.getLeft()));
        heading = heading.getLeft();

        queue.enqueue(new Decision(DecisionType.FLY_FORWARD, heading));
        return queue;
        
    }

    public DecisionQueue turnLeft(){
        DecisionQueue queue = new DecisionQueue();
        queue.enqueue(new Decision(DecisionType.FLY_FORWARD, heading));

        queue.enqueue(new Decision(DecisionType.TURN, heading.getRight()));
        heading = heading.getRight();

        queue.enqueue(new Decision(DecisionType.TURN, heading.getRight()));
        heading = heading.getRight();

        queue.enqueue(new Decision(DecisionType.TURN, heading.getRight()));
        heading = heading.getRight();

        queue.enqueue(new Decision(DecisionType.FLY_FORWARD, heading));
        return queue;

    }
    
    public DecisionQueue turnAround(){
        DecisionQueue queue = new DecisionQueue();
        queue.enqueue(new Decision(DecisionType.TURN, heading.getRight()));
        heading = heading.getRight();

        queue.enqueue(new Decision(DecisionType.TURN, heading.getLeft()));
        heading = heading.getLeft();

        queue.enqueue(new Decision(DecisionType.TURN, heading.getLeft()));
        heading = heading.getLeft();

        queue.enqueue(new Decision(DecisionType.TURN, heading.getLeft()));
        heading = heading.getLeft();

        queue.enqueue(new Decision(DecisionType.FLY_FORWARD, heading));

        queue.enqueue(new Decision(DecisionType.FLY_FORWARD, heading));
        return queue;
    }

    public DecisionQueue moveForward(){
        DecisionQueue queue = new DecisionQueue();
        queue.enqueue(new Decision(DecisionType.FLY_FORWARD, heading));
        pos = pos.step(heading);
        return queue;
    } 
    
}
