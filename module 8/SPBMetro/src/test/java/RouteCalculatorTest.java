import core.*;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

public class RouteCalculatorTest extends TestCase {

    private StationIndex stationIndex;
    private RouteCalculator routeCalculator;

    @Override
    protected void setUp() throws Exception {
        stationIndex = new StationIndex();

        for (int i = 0; i < 3; ++i) {
            Line line = new Line(i, Integer.toString(i));
            for (int j = 0; j < 3; ++j) {
                Station station = new Station(line.getName() + j, line);
                line.addStation(station);
                stationIndex.addStation(station);
            }
            stationIndex.addLine(line);
        }

        List<Station> stations = new ArrayList<>();
        stations.add(stationIndex.getStation("01"));
        stations.add(stationIndex.getStation("10"));
        stationIndex.addConnection(stations);

        List<Station> stations2 = new ArrayList<>();
        stations2.add(stationIndex.getStation("21"));
        stations2.add(stationIndex.getStation("12"));
        stationIndex.addConnection(stations2);

        routeCalculator = new RouteCalculator(stationIndex);
    }

    private void printRoute(List<Station> route) {
        Station previousStation = null;
        for(Station station : route)
        {
            if(previousStation != null)
            {
                Line prevLine = previousStation.getLine();
                Line nextLine = station.getLine();
                if(!prevLine.equals(nextLine))
                {
                    System.out.println("\tПереход на станцию " +
                            station.getName() + " (" + nextLine.getName() + " линия)");
                }
            }
            System.out.println("\t" + station.getName());
            previousStation = station;
        }
    }

    public void testGetShortestRoute() {
        Station from;
        Station to;
        List<Station> route;
        double actual;
        double expected;

        // test 1
        from = stationIndex.getStation("00");
        to = stationIndex.getStation("02");
        route = routeCalculator.getShortestRoute(from, to);
        assertNotNull(route);
        assertEquals(3, route.size());

        actual = RouteCalculator.calculateDuration(route);
        expected = 5.;
        assertEquals(expected, actual);

        // test 2
        to = stationIndex.getStation("12");
        route = routeCalculator.getShortestRoute(from, to);
        assertNotNull(route);
        assertEquals(5, route.size());

        actual = RouteCalculator.calculateDuration(route);
        expected = 11.;
        assertEquals(expected, actual);

        // test 3
        to = stationIndex.getStation("20");
        route = routeCalculator.getShortestRoute(from, to);
        assertNotNull(route);
        assertEquals(7, route.size());

        actual = RouteCalculator.calculateDuration(route);
        expected = 17.;
        assertEquals(expected, actual);
    }

    public void testCalculateDuration() {
        double actual = RouteCalculator.calculateDuration(null);
        double expected = 8.5;
        assertEquals(expected, actual);
    }
}
