
    public class Customer implements Comparable<Customer> {

    // ─── ENUMS ────────────────────────────────────────────────────────────────
    public enum CustomerType {
        SENIOR_CITIZEN(1, "Senior Citizen (60+)"),
        EXISTING_CUSTOMER(2, "Existing Customer"),
        NEW_CUSTOMER(3, "New Customer");

        private final int priority;
        private final String label;

        CustomerType(int priority, String label) {
            this.priority = priority;
            this.label    = label;
        }
        public int getPriority() { return priority; }
        public String getLabel() { return label; }
    }

    public enum ApartmentType {
        BHK_1("1BHK",      25.0),
        BHK_2("2BHK",      45.0),
        BHK_3("3BHK",      70.0),
        PENTHOUSE("Penthouse", 150.0);

        private final String label;
        private final double priceInLakhs;

        ApartmentType(String label, double price) {
            this.label         = label;
            this.priceInLakhs  = price;
        }
        public String getLabel()         { return label; }
        public double getPriceInLakhs()  { return priceInLakhs; }
    }

    public enum BookingStatus {
        WAITING, SERVING, CONFIRMED, CANCELLED
    }

    // ─── FIELDS ───────────────────────────────────────────────────────────────
    private static int tokenCounter = 1;   // Auto-increment token

    private final int    tokenNumber;
    private final String name;
    private final int    age;
    private final String phone;
    private final CustomerType  customerType;
    private final ApartmentType apartmentType;
    private BookingStatus status;
    private final String  registeredTime;

    // ─── CONSTRUCTOR ──────────────────────────────────────────────────────────
    public Customer(String name, int age, String phone,
                    CustomerType customerType, ApartmentType apartmentType) {

        // Exception Handling — Validation
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("❌ Name cannot be empty!");
        if (age < 1 || age > 120)
            throw new IllegalArgumentException("❌ Invalid age: " + age);
        if (phone == null || phone.length() < 10)
            throw new IllegalArgumentException("❌ Invalid phone number!");

        this.tokenNumber   = tokenCounter++;
        this.name          = name.trim();
        this.age           = age;
        this.phone         = phone;
        this.customerType  = customerType;
        this.apartmentType = apartmentType;
        this.status        = BookingStatus.WAITING;
        this.registeredTime = new java.util.Date().toString();

        // Auto-upgrade to Senior Citizen if age >= 60
        // (handled in getEffectiveType)
    }

    // ─── PRIORITY LOGIC ───────────────────────────────────────────────────────
    // If age >= 60, treat as Senior Citizen regardless of selected type
    public CustomerType getEffectiveType() {
        if (age >= 60) return CustomerType.SENIOR_CITIZEN;
        return customerType;
    }

    // compareTo — used by PriorityQueue (lower priority number = served first)
    @Override
    public int compareTo(Customer other) {
        int myPriority    = this.getEffectiveType().getPriority();
        int otherPriority = other.getEffectiveType().getPriority();
        if (myPriority != otherPriority)
            return Integer.compare(myPriority, otherPriority);
        // Same priority → FIFO (lower token number first)
        return Integer.compare(this.tokenNumber, other.tokenNumber);
    }

    // ─── GETTERS ──────────────────────────────────────────────────────────────
    public int           getTokenNumber()   { return tokenNumber; }
    public String        getName()          { return name; }
    public int           getAge()           { return age; }
    public String        getPhone()         { return phone; }
    public CustomerType  getCustomerType()  { return customerType; }
    public ApartmentType getApartmentType() { return apartmentType; }
    public BookingStatus getStatus()        { return status; }
    public String        getRegisteredTime(){ return registeredTime; }

    public String getFormattedToken() {
        return "T" + String.format("%03d", tokenNumber);
    }

    // ─── SETTERS ──────────────────────────────────────────────────────────────
    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    // ─── DISPLAY ──────────────────────────────────────────────────────────────
    public String getSummary() {
        return String.format(
            "Token: %s | Name: %-15s | Age: %3d | Type: %-20s | Apt: %-10s | Status: %s",
            getFormattedToken(),
            name,
            age,
            getEffectiveType().getLabel(),
            apartmentType.getLabel(),
            status
        );
    }

    @Override
    public String toString() {
        return getFormattedToken() + " - " + name + " (" + getEffectiveType().getLabel() + ")";
    }

    // ─── RESET TOKEN COUNTER (for testing) ───────────────────────────────────
    public static void resetTokenCounter() {
        tokenCounter = 1;
    }

    // ─── QUICK TEST ───────────────────────────────────────────────────────────
    public static void main(String[] args) {
        System.out.println("=== Customer Class Test ===\n");

        try {
            Customer c1 = new Customer("Rajan Kumar",  65, "9876543210",
                Customer.CustomerType.NEW_CUSTOMER,
                Customer.ApartmentType.BHK_2);

            Customer c2 = new Customer("Priya S.",     35, "9123456780",
                Customer.CustomerType.EXISTING_CUSTOMER,
                Customer.ApartmentType.BHK_3);

            Customer c3 = new Customer("Arun M.",      28, "9000011111",
                Customer.CustomerType.NEW_CUSTOMER,
                Customer.ApartmentType.BHK_1);

            System.out.println(c1.getSummary());
            System.out.println(c2.getSummary());
            System.out.println(c3.getSummary());

            System.out.println("\nRajan age=65 → Auto Senior: "
                + c1.getEffectiveType().getLabel());

            System.out.println("\nPriority Order (low = served first):");
            System.out.println(c1.getFormattedToken() + " priority = "
                + c1.getEffectiveType().getPriority());
            System.out.println(c2.getFormattedToken() + " priority = "
                + c2.getEffectiveType().getPriority());
            System.out.println(c3.getFormattedToken() + " priority = "
                + c3.getEffectiveType().getPriority());

        } catch (IllegalArgumentException e) {
            System.out.println("Validation Error: " + e.getMessage());
        }

        // Test validation
        System.out.println("\n=== Validation Test ===");
        try {
            Customer bad = new Customer("", 25, "123",
                Customer.CustomerType.NEW_CUSTOMER,
                Customer.ApartmentType.BHK_1);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }
}

