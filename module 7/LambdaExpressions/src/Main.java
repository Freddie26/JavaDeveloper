import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

public class Main
{
    private static String staffFile = "data/staff.txt";
    private static String dateFormat = "dd.MM.yyyy";

    public static void main(String[] args)
    {
        ArrayList<Employee> staff = loadStaffFromFile();

        LocalDate startDate = LocalDate.of (2017, Month.JANUARY, 01);
        LocalDate endDate = LocalDate.of(2017, Month.DECEMBER, 31);

        staff.stream()
                .filter(e -> dateToLocalDate(e.getWorkStart()).isAfter(startDate)
                        && dateToLocalDate(e.getWorkStart()).isBefore(endDate))
                .max(Comparator.comparing(Employee::getSalary))
                .ifPresent(System.out::println);
    }

    public static LocalDate dateToLocalDate(Date dateToConvert)
    {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }

    private static ArrayList<Employee> loadStaffFromFile()
    {
        ArrayList<Employee> staff = new ArrayList<>();
        try
        {
            List<String> lines = Files.readAllLines(Paths.get(staffFile));
            for(String line : lines)
            {
                String[] fragments = line.split("\t");
                if(fragments.length != 3) {
                    System.out.println("Wrong line: " + line);
                    continue;
                }
                staff.add(new Employee(
                    fragments[0],
                    Integer.parseInt(fragments[1]),
                    (new SimpleDateFormat(dateFormat)).parse(fragments[2])
                ));
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return staff;
    }
}