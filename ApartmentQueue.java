import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.List;

public class ApartmentQueue {

    // ─── DSA CORE — Priority Queue ────────────────────────────────────────────
    // PriorityQueue automatically sorts by Customer.compareTo()
    // Senior Citizen (1) → Existing (2) → New Customer (3)
    private final PriorityQueue<Customer> waitingQueue;

    // Completed bookings list
    private final ArrayList<Customer> confirmedList;
    private final ArrayList<Customer> cancelledList;
    private final ArrayList<Customer> servedHistory;

    // Currently serving
    private Customer currentlyServing;

    // Stats
    private int totalEnqueued;
    private int totalServed;
    private int totalConfirmed;
    private int totalCancelled;

    // ─── CONSTRUCTOR ──────────────────────────────────────────────────────────
    public ApartmentQueue() {
        this.waitingQueue   = new PriorityQueue<>();  // DSA: Priority Queue
        this.confirmedList  = new ArrayList<>();
        this.cancelledList  = new ArrayList<>();
        this.servedHistory  = new ArrayList<>();
        this.currentlyServing = null;
        this.totalEnqueued  = 0;
        this.totalServed    = 0;
        this.totalConfirmed = 0;
        this.totalCancelled = 0;
    }

    // ─── ENQUEUE — Add Customer to Queue ─────────────────────────────────────
    public boolean enqueue(Customer customer) {
        if (customer == null) return false;

        // Check if already in queue
        if (waitingQueue.contains(customer)) {
            System.out.println("⚠ Customer already in queue: " + customer.getFormattedToken());
            return false;
        }

        waitingQueue.offer(customer);  // DSA: offer() = enqueue
        customer.setStatus(Customer.BookingStatus.WAITING);
        totalEnqueued++;

        System.out.println("✅ Enqueued: " + customer.getSummary());
        return true;
    }

    // ─── DEQUEUE — Serve Next Customer ───────────────────────────────────────
    public Customer dequeue() {
        if (waitingQueue.isEmpty()) {
            System.out.println("⚠ Queue is empty! No customers waiting.");
            return null;
        }

        // If someone is currently being served, move to history
        if (currentlyServing != null) {
            servedHistory.add(currentlyServing);
        }

        currentlyServing = waitingQueue.poll();  // DSA: poll() = dequeue (highest priority first)
        currentlyServing.setStatus(Customer.BookingStatus.SERVING);
        totalServed++;

        System.out.println("🔔 Now Serving: " + currentlyServing.getSummary());
        return currentlyServing;
    }

    // ─── PEEK — Who is Next? ──────────────────────────────────────────────────
    public Customer peek() {
        return waitingQueue.peek();  // DSA: peek() = view front without removing
    }

    // ─── CONFIRM BOOKING ──────────────────────────────────────────────────────
    public boolean confirmBooking(Customer customer) {
        if (customer == null) return false;

        customer.setStatus(Customer.BookingStatus.CONFIRMED);
        confirmedList.add(customer);
        totalConfirmed++;

        // If confirming currently serving customer
        if (currentlyServing != null &&
            currentlyServing.getTokenNumber() == customer.getTokenNumber()) {
            servedHistory.add(currentlyServing);
            currentlyServing = null;
        }

        System.out.println("✅ Booking Confirmed: " + customer.getFormattedToken()
            + " - " + customer.getName()
            + " | Apt: " + customer.getApartmentType().getLabel());
        return true;
    }

    // ─── CANCEL BOOKING ───────────────────────────────────────────────────────
    public boolean cancelBooking(Customer customer) {
        if (customer == null) return false;

        // Remove from waiting queue if present
        boolean removedFromQueue = waitingQueue.remove(customer);

        // If currently serving, cancel
        if (currentlyServing != null &&
            currentlyServing.getTokenNumber() == customer.getTokenNumber()) {
            currentlyServing = null;
        }

        customer.setStatus(Customer.BookingStatus.CANCELLED);
        cancelledList.add(customer);
        totalCancelled++;

        System.out.println("❌ Booking Cancelled: " + customer.getFormattedToken()
            + " - " + customer.getName());
        return true;
    }

    // ─── GET QUEUE AS LIST (for GUI Table) ───────────────────────────────────
    public List<Customer> getQueueAsList() {
        // Create sorted copy for display
        PriorityQueue<Customer> copy = new PriorityQueue<>(waitingQueue);
        List<Customer> list = new ArrayList<>();
        while (!copy.isEmpty()) {
            list.add(copy.poll());
        }
        return list;
    }

    // ─── GETTERS ──────────────────────────────────────────────────────────────
    public boolean          isEmpty()            { return waitingQueue.isEmpty(); }
    public int              getQueueSize()        { return waitingQueue.size(); }
    public Customer         getCurrentlyServing() { return currentlyServing; }
    public List<Customer>   getConfirmedList()    { return confirmedList; }
    public List<Customer>   getCancelledList()    { return cancelledList; }
    public List<Customer>   getServedHistory()    { return servedHistory; }
    public int              getTotalEnqueued()    { return totalEnqueued; }
    public int              getTotalServed()      { return totalServed; }
    public int              getTotalConfirmed()   { return totalConfirmed; }
    public int              getTotalCancelled()   { return totalCancelled; }

    // ─── PRINT QUEUE STATUS ───────────────────────────────────────────────────
    public void printQueueStatus() {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("       APARTMENT QUEUE STATUS");
        System.out.println("════════════════════════════════════════");

        if (currentlyServing != null) {
            System.out.println("🔔 NOW SERVING : " + currentlyServing);
        } else {
            System.out.println("🔔 NOW SERVING : —");
        }

        System.out.println("⏳ WAITING     : " + waitingQueue.size() + " customers");

        if (!waitingQueue.isEmpty()) {
            System.out.println("\n--- Waiting List (Priority Order) ---");
            List<Customer> list = getQueueAsList();
            int pos = 1;
            for (Customer c : list) {
                System.out.printf("  %d. %s%n", pos++, c.getSummary());
            }
        }

        System.out.println("\n--- Stats ---");
        System.out.println("Total Registered : " + totalEnqueued);
        System.out.println("Total Served     : " + totalServed);
        System.out.println("Total Confirmed  : " + totalConfirmed);
        System.out.println("Total Cancelled  : " + totalCancelled);
        System.out.println("════════════════════════════════════════\n");
    }

    // ─── QUICK TEST ───────────────────────────────────────────────────────────
    public static void main(String[] args) {
        System.out.println("=== ApartmentQueue DSA Test ===\n");

        ApartmentQueue queue = new ApartmentQueue();

        // Create test customers
        Customer c1 = new Customer("Rajan Kumar", 65, "9876543210",
            Customer.CustomerType.NEW_CUSTOMER,
            Customer.ApartmentType.BHK_2);

        Customer c2 = new Customer("Priya S.", 35, "9123456780",
            Customer.CustomerType.EXISTING_CUSTOMER,
            Customer.ApartmentType.BHK_3);

        Customer c3 = new Customer("Arun M.", 28, "9000011111",
            Customer.CustomerType.NEW_CUSTOMER,
            Customer.ApartmentType.BHK_1);

        Customer c4 = new Customer("Meena R.", 62, "9555566666",
            Customer.CustomerType.NEW_CUSTOMER,
            Customer.ApartmentType.BHK_2);

        // Enqueue all
        System.out.println("--- Enqueue Customers ---");
        queue.enqueue(c1);  // Senior (auto - age 65)
        queue.enqueue(c2);  // Existing
        queue.enqueue(c3);  // New
        queue.enqueue(c4);  // Senior (auto - age 62)

        // Show queue
        queue.printQueueStatus();

        // Dequeue — should serve Senior first
        System.out.println("--- Dequeue (Serve Next) ---");
        Customer served1 = queue.dequeue();
        queue.confirmBooking(served1);

        Customer served2 = queue.dequeue();
        queue.confirmBooking(served2);

        // Show updated queue
        queue.printQueueStatus();

        // Cancel one
        System.out.println("--- Cancel Booking ---");
        queue.cancelBooking(c3);

        // Final status
        queue.printQueueStatus();
    }
}
