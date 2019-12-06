import com.skillbox.airport.Airport;
import com.skillbox.airport.Flight;
import com.skillbox.airport.Terminal;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class TryAirport {
    public static void main(String[] args) {
        Airport airport = Airport.getInstance();


        //* Список самолётов можно получить и так, но я сознательно
        // пошел сложным путём чтобы поиграться со StreamAPI
//        List<Aircraft> aircrafts = airport.getAllAircrafts();
//        System.out.println("Aircraft count: " + aircrafts.size());

        List<Terminal> terminals = airport.getTerminals();

        // распечатать время вылета и модели самолётов, вылетающие в ближайшие 2 часа.

        Instant now = LocalDateTime.now().toInstant(ZoneOffset.ofHours(3));
        Instant then = LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.ofHours(3));

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");

        terminals.stream()
                .map(Terminal::getFlights)
                .flatMap(Collection::stream)
                .filter(f -> f.getType() == Flight.Type.DEPARTURE
                        && f.getDate().toInstant().isAfter(now)
                        && f.getDate().toInstant().isBefore(then))
                .sorted(Comparator.comparing(Flight::getDate))
                .forEach(f -> System.out.println(dateFormat.format(f.getDate()) + " / " + f.getType()));
    }
}
