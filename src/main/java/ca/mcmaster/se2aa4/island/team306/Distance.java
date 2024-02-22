package ca.mcmaster.se2aa4.island.team306;

public class Distance {

    public static float calculateDistance(Coords c1, Coords c2){
        int dx = c2.x - c1.x;
        int dy = c2.y - c1.y;
        return (float) Math.sqrt(dx^2 + dy^2);
    }
}
