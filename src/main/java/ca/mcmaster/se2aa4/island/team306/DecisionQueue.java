package ca.mcmaster.se2aa4.island.team306;

import java.util.ArrayList;
import java.util.List;


public class DecisionQueue {
    private List<Decision> decisions;

    /**
     * Constructs an empty DecisionQueue.
     */
    public DecisionQueue() {
        this.decisions = new ArrayList<>();
    }

    /**
     * Enqueues a decision into the queue.
     *
     * @param decision The decision to enqueue.
     */
    public void enqueue(Decision decision) {
        this.decisions.add(decision);
    }

    /**
     * Enqueues all decisions from another DecisionQueue into this queue.
     *
     * @param queue The DecisionQueue containing decisions to enqueue.
     */
    public void enqueue(DecisionQueue queue) {
        while (!queue.isEmpty()) {
            this.decisions.add(queue.dequeue());
        }
    }

    /**
     * Dequeues a decision from the queue.
     *
     * @return The dequeued decision.
     */
    public Decision dequeue() {
        return decisions.remove(0);
    }

    /**
     * Clears all decisions from the queue.
     */
    public void clear() {
        this.decisions.clear();
    }

    /**
     * Gets the length of the queue.
     *
     * @return The number of decisions in the queue.
     */
    public int length() {
        return decisions.size();
    }

    /**
     * Checks if the queue is empty.
     *
     * @return True if the queue is empty, false otherwise.
     */
    public boolean isEmpty() {
        return decisions.isEmpty();
    }
}
