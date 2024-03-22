package a.mcmaster.se2aa4.island.team306;

import ca.mcmaster.se2aa4.island.team306.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DecisionQueueTest {

    private static final Decision[] decisions =
            {Radar.SCAN_NORTH, PhotoScanner.getDecision(), Mover.TURN_SOUTH, Mover.FLY_SOUTH, Aborter.getDecision()};

    @Test
    public void verifyOne(){
        DecisionQueue queue = new DecisionQueue();
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.length());

       for (int i = 0; i < decisions.length; i++){
           queue.enqueue(decisions[i]);
           assertEquals(i+1, queue.length());
           assertFalse(queue.isEmpty());
       }

       for (int i = decisions.length; i > 0; i--){
           assertEquals(i, queue.length());
           assertFalse(queue.isEmpty());
           assertEquals(decisions[decisions.length - i] , queue.dequeue());
       }

       assertTrue(queue.isEmpty());
    }

    @Test
    public void verifyMultiple(){
        DecisionQueue queue1 = new DecisionQueue();
        DecisionQueue queue2 = new DecisionQueue();
        for (Decision decision : decisions) {
            queue1.enqueue(decision);
            queue2.enqueue(decision);
        }

        queue1.enqueue(queue2);

        for (int j = 1; j >=0; j--){
            for (int i = decisions.length; i > 0; i--){
                assertEquals(j*decisions.length + i, queue1.length());
                assertFalse(queue1.isEmpty());
                assertEquals(decisions[decisions.length - i] , queue1.dequeue());
            }
        }

        assertTrue(queue1.isEmpty());
        assertTrue(queue2.isEmpty());

        for (Decision decision : decisions) {
            queue2.enqueue(decision);
        }

        assertEquals(queue2.length(), decisions.length);
        queue2.clear();
        assertTrue(queue2.isEmpty());
    }
}
