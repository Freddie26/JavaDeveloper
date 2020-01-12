import core.*;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RouteCalculatorTest extends TestCase {

    private StationIndex stationIndex;
    private RouteCalculator routeCalculator;

    /**  line - "0"
     *
     *      00             20
     *      |              |
     *    01/10 -- 11 -- 12/21   line - "1"
     *      |              |
     *      02             22
     *
     *                 line - "2"
     */


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

    public void test_on_single_line() {
        List<Station> expectedRoute = Arrays.asList(stationIndex.getStation("00"),
                stationIndex.getStation("01"),
                stationIndex.getStation("02"));

        Station from = stationIndex.getStation("00");
        Station to = stationIndex.getStation("02");
        List<Station> actualRoute = routeCalculator.getShortestRoute(from, to);

        assertEquals("[test_on_single_line][getShortestRoute]",
                expectedRoute, actualRoute);

        double actualDuration = RouteCalculator.calculateDuration(actualRoute);
        double expectedDuration = 5f;

        assertEquals("[test_on_single_line][calculateDuration]",
                expectedDuration, actualDuration);
    }

    public void test_with_one_transfer() {
        List<Station> expectedRoute = Arrays.asList(stationIndex.getStation("00"),
                stationIndex.getStation("01"),
                stationIndex.getStation("10"),
                stationIndex.getStation("11"),
                stationIndex.getStation("12"));

        Station from = stationIndex.getStation("00");
        Station to = stationIndex.getStation("12");
        List<Station> actualRoute = routeCalculator.getShortestRoute(from, to);

        assertEquals("[test_with_one_transfer][getShortestRoute]",
                expectedRoute, actualRoute);

        double actualDuration = RouteCalculator.calculateDuration(actualRoute);
        double expectedDuration = 11f;

        assertEquals("[test_with_one_transfer][calculateDuration]",
                expectedDuration, actualDuration);
    }

    public void test_with_double_transfer() {
        List<Station> expectedRoute = Arrays.asList(stationIndex.getStation("00"),
                stationIndex.getStation("01"),
                stationIndex.getStation("10"),
                stationIndex.getStation("11"),
                stationIndex.getStation("12"),
                stationIndex.getStation("21"),
                stationIndex.getStation("20"));

        Station from = stationIndex.getStation("00");
        Station to = stationIndex.getStation("20");
        List<Station> actualRoute = routeCalculator.getShortestRoute(from, to);

        assertEquals("[test_with_double_transfer][getShortestRoute]",
                expectedRoute, actualRoute);

        double actualDuration = RouteCalculator.calculateDuration(actualRoute);
        double expectedDuration = 17f;

        assertEquals("[test_with_double_transfer][calculateDuration]",
                expectedDuration, actualDuration);
    }
}
