package ca.mcmaster.se2aa4.island.team306;

public interface GameMap {

    Coords getBase();

    MapValue checkCoords(Coords left);

    Coords findNearestTile(MapValue ground);

    MapValue nextValue();

    MapValue currentValue();

    void setReportCreek();

    void updateStatus(ParsedResult r);

}
