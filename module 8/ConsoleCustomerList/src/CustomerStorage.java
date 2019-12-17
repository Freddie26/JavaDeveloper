import java.util.HashMap;

public class CustomerStorage {
    private final String PHONE_PATTERN = "^((8|\\+7)[\\-]?)?(\\(?\\d{3}\\)?[\\-]?)?[\\d\\-]{7,10}$";
    private final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private HashMap<String, Customer> storage;

    public CustomerStorage() {
        storage = new HashMap<>();
    }

    public void addCustomer(String data) {
        String[] components = data.split("\\s+");

        checkInputData(components);

        String name = components[0] + " " + components[1];
        storage.put(name, new Customer(name, components[3], components[2]));
    }

    private void checkInputData(String[] args) {
        checkOutOfBounds(args.length, 4);
        checkEmail(args[2]);
        checkPhoneNumber(args[3]);
    }

    private void checkOutOfBounds(int actualLength, int expectedLength) {
        if (actualLength < expectedLength)
            throw new ArrayIndexOutOfBoundsException("Expected " + expectedLength + " but found " + actualLength);
    }

    private void checkEmail(String email) {
        if (!email.matches(EMAIL_PATTERN))
            throw new IllegalArgumentException("Invalid email: " + email);
    }

    private void checkPhoneNumber(String phoneNumber) {
        if (!phoneNumber.matches(PHONE_PATTERN))
            throw new IllegalArgumentException("Invalid phone number: " + phoneNumber);
    }

    public void listCustomers() {
        storage.values().forEach(System.out::println);
    }

    public void removeCustomer(String name) {
        storage.remove(name);
    }

    public int getCount()
    {
        return storage.size();
    }
}