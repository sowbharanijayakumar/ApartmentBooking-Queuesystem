import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class AdminPanel extends JFrame {

    // ─── COLORS ───────────────────────────────────────────────────────────────
    private static final Color BG_DARK      = new Color(13, 17, 30);
    private static final Color BG_CARD      = new Color(22, 28, 48);
    private static final Color ACCENT_GOLD  = new Color(212, 175, 55);
    private static final Color ACCENT_BLUE  = new Color(64, 145, 247);
    private static final Color ACCENT_GREEN = new Color(52, 199, 89);
    private static final Color ACCENT_RED   = new Color(255, 69, 58);
    private static final Color ACCENT_ORANGE= new Color(255, 149, 0);
    private static final Color TEXT_PRIMARY = new Color(240, 240, 245);
    private static final Color TEXT_MUTED   = new Color(140, 145, 165);
    private static final Color BORDER_COLOR = new Color(40, 50, 80);

    // ─── CORE DSA QUEUE ───────────────────────────────────────────────────────
    private final ApartmentQueue queue;
    private int availableFlats = 48;

    // ─── UI COMPONENTS ────────────────────────────────────────────────────────
    private DefaultTableModel queueTableModel;
    private DefaultTableModel confirmedTableModel;
    private JLabel lblNowServing, lblWaiting, lblConfirmed, lblFlats;
    private JLabel lblServingName, lblServingType, lblServingApt;
    private JTabbedPane tabbedPane;

    // ─── CONSTRUCTOR ──────────────────────────────────────────────────────────
    public AdminPanel(ApartmentQueue queue) {
        this.queue = queue;

        setTitle("🛠 Admin Panel — Apartment Booking System");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1150, 750);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);
        setBackground(BG_DARK);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BG_DARK);
        root.add(buildHeader(),  BorderLayout.NORTH);
        root.add(buildBody(),    BorderLayout.CENTER);
        root.add(buildFooter(),  BorderLayout.SOUTH);

        setContentPane(root);
        refreshAll();
        setVisible(true);
    }

    // ─── HEADER ───────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(20, 26, 46), getWidth(), 0, new Color(30, 20, 60));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(ACCENT_RED);
                g2.setStroke(new BasicStroke(2f));
                g2.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
            }
        };
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(16, 28, 16, 28));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        left.setOpaque(false);
        JLabel icon = new JLabel("🛠");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        JPanel titleBlock = new JPanel(new GridLayout(2,1,0,2));
        titleBlock.setOpaque(false);
        JLabel title = new JLabel("Admin Control Panel");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(TEXT_PRIMARY);
        JLabel sub = new JLabel("Queue Management • Booking Control • Live Stats");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sub.setForeground(ACCENT_GOLD);
        titleBlock.add(title); titleBlock.add(sub);
        left.add(icon); left.add(titleBlock);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);
        JButton btnRefresh = headerButton("🔄 Refresh", ACCENT_BLUE);
        btnRefresh.addActionListener(e -> refreshAll());
        JButton btnBack = headerButton("← Dashboard", ACCENT_GOLD);
        btnBack.addActionListener(e -> dispose());
        right.add(btnRefresh);
        right.add(btnBack);

        header.add(left,  BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    // ─── BODY ─────────────────────────────────────────────────────────────────
    private JPanel buildBody() {
        JPanel body = new JPanel(new BorderLayout(0, 16));
        body.setBackground(BG_DARK);
        body.setBorder(new EmptyBorder(20, 28, 10, 28));

        body.add(buildStatsRow(),    BorderLayout.NORTH);
        body.add(buildMainContent(), BorderLayout.CENTER);

        return body;
    }

    // ─── STATS ROW ────────────────────────────────────────────────────────────
    private JPanel buildStatsRow() {
        JPanel row = new JPanel(new GridLayout(1, 4, 14, 0));
        row.setOpaque(false);

        lblWaiting   = new JLabel("0");
        lblConfirmed = new JLabel("0");
        lblFlats     = new JLabel("48");
        lblNowServing= new JLabel("—");

        row.add(miniStat("⏳ In Queue",       lblWaiting,    ACCENT_RED));
        row.add(miniStat("✅ Confirmed",       lblConfirmed,  ACCENT_GREEN));
        row.add(miniStat("🏠 Flats Left",      lblFlats,      ACCENT_BLUE));
        row.add(miniStat("🔔 Now Serving",     lblNowServing, ACCENT_GOLD));

        return row;
    }

    private JPanel miniStat(String label, JLabel valLabel, Color accent) {
        JPanel card = new JPanel(new BorderLayout(0,4)) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),14,14);
                g2.setColor(accent);
                g2.fillRoundRect(0,0,getWidth(),4,4,4);
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(12,16,12,16));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(TEXT_MUTED);
        valLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valLabel.setForeground(accent);
        card.add(lbl, BorderLayout.NORTH);
        card.add(valLabel, BorderLayout.CENTER);
        return card;
    }

    // ─── MAIN CONTENT ─────────────────────────────────────────────────────────
    private JPanel buildMainContent() {
        JPanel content = new JPanel(new BorderLayout(16, 0));
        content.setOpaque(false);

        content.add(buildLeftPanel(),  BorderLayout.WEST);
        content.add(buildRightTabs(),  BorderLayout.CENTER);

        return content;
    }

    // ─── LEFT PANEL — Serving + Actions ───────────────────────────────────────
    private JPanel buildLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(260, 0));

        // Now Serving Card
        JPanel servingCard = new JPanel(new GridLayout(5, 1, 0, 6)) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),16,16);
                g2.setColor(ACCENT_GOLD);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,16,16);
            }
        };
        servingCard.setOpaque(false);
        servingCard.setBorder(new EmptyBorder(16,16,16,16));

        JLabel nowTitle = new JLabel("🔔  NOW SERVING");
        nowTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        nowTitle.setForeground(ACCENT_GOLD);

        lblServingName = new JLabel("— No Customer —");
        lblServingName.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblServingName.setForeground(TEXT_PRIMARY);

        lblServingType = new JLabel("Type: —");
        lblServingType.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblServingType.setForeground(TEXT_MUTED);

        lblServingApt = new JLabel("Apartment: —");
        lblServingApt.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblServingApt.setForeground(TEXT_MUTED);

        JLabel nextLabel = new JLabel("Next: " + getNextInQueue());
        nextLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        nextLabel.setForeground(new Color(100, 120, 180));

        servingCard.add(nowTitle);
        servingCard.add(lblServingName);
        servingCard.add(lblServingType);
        servingCard.add(lblServingApt);
        servingCard.add(nextLabel);

        // Action Buttons
        JPanel btnPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        btnPanel.setOpaque(false);

        JButton btnCallNext = actionBtn("🔔  Call Next Customer", ACCENT_GREEN);
        btnCallNext.addActionListener(e -> callNext());

        JButton btnConfirm = actionBtn("✅  Confirm Booking", ACCENT_BLUE);
        btnConfirm.addActionListener(e -> confirmCurrent());

        JButton btnCancel = actionBtn("❌  Cancel Booking", ACCENT_RED);
        btnCancel.addActionListener(e -> cancelCurrent());

        JButton btnAddCustomer = actionBtn("👤  Add Customer", ACCENT_ORANGE);
        btnAddCustomer.addActionListener(e -> addCustomerDialog());

        btnPanel.add(btnCallNext);
        btnPanel.add(btnConfirm);
        btnPanel.add(btnAddCustomer);
        btnPanel.add(btnCancel);

        panel.add(servingCard, BorderLayout.NORTH);
        panel.add(btnPanel,    BorderLayout.CENTER);

        return panel;
    }

    // ─── RIGHT TABS ───────────────────────────────────────────────────────────
    private JTabbedPane buildRightTabs() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(BG_CARD);
        tabbedPane.setForeground(TEXT_PRIMARY);
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));

        tabbedPane.addTab("⏳ Waiting Queue", buildQueueTab());
        tabbedPane.addTab("✅ Confirmed",     buildConfirmedTab());

        return tabbedPane;
    }

    // ─── QUEUE TAB ────────────────────────────────────────────────────────────
    private JPanel buildQueueTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(12, 0, 0, 0));

        String[] cols = {"#", "Token", "Name", "Age", "Type", "Apartment", "Priority", "Status"};
        queueTableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = styledTable(queueTableModel);
        JScrollPane scroll = styledScroll(table);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // ─── CONFIRMED TAB ────────────────────────────────────────────────────────
    private JPanel buildConfirmedTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(12, 0, 0, 0));

        String[] cols = {"Token", "Name", "Age", "Type", "Apartment", "Price (Lakhs)", "Status"};
        confirmedTableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = styledTable(confirmedTableModel);
        JScrollPane scroll = styledScroll(table);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // ─── FOOTER ───────────────────────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(15, 19, 34));
        footer.setBorder(new CompoundBorder(
            new MatteBorder(1,0,0,0, BORDER_COLOR),
            new EmptyBorder(10, 28, 10, 28)));

        JLabel left = new JLabel("Admin Panel  •  Apartment Booking Queue System");
        left.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        left.setForeground(TEXT_MUTED);

        JLabel right = new JLabel("DSA: PriorityQueue + FIFO  |  OOP  |  Java Swing");
        right.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        right.setForeground(TEXT_MUTED);

        footer.add(left,  BorderLayout.WEST);
        footer.add(right, BorderLayout.EAST);
        return footer;
    }

    // ─── ACTIONS ──────────────────────────────────────────────────────────────
    private void callNext() {
        Customer next = queue.dequeue();
        if (next == null) {
            showMsg("⚠ Queue is empty! No customers waiting.", "Queue Empty");
            return;
        }
        lblServingName.setText(next.getFormattedToken() + "  " + next.getName());
        lblServingType.setText("Type: " + next.getEffectiveType().getLabel());
        lblServingApt.setText("Apartment: " + next.getApartmentType().getLabel());
        lblNowServing.setText(next.getFormattedToken());
        refreshAll();
        showMsg("🔔 Now Serving:\n" + next.getName() + "\nToken: " + next.getFormattedToken()
              + "\nApartment: " + next.getApartmentType().getLabel(), "Calling Customer");
    }

    private void confirmCurrent() {
        Customer serving = queue.getCurrentlyServing();
        if (serving == null) {
            showMsg("⚠ No customer currently being served!\nClick 'Call Next' first.", "No Customer");
            return;
        }
        if (availableFlats <= 0) {
            showMsg("❌ No flats available!", "Sold Out");
            return;
        }
        queue.confirmBooking(serving);
        availableFlats--;
        lblFlats.setText(String.valueOf(availableFlats));
        lblConfirmed.setText(String.valueOf(queue.getTotalConfirmed()));
        lblServingName.setText("— No Customer —");
        lblServingType.setText("Type: —");
        lblServingApt.setText("Apartment: —");
        lblNowServing.setText("—");
        refreshAll();
        showMsg("✅ Booking Confirmed!\n" + serving.getName()
              + "\nApartment: " + serving.getApartmentType().getLabel()
              + "\nToken: " + serving.getFormattedToken(), "Booking Confirmed");
    }

    private void cancelCurrent() {
        Customer serving = queue.getCurrentlyServing();
        if (serving == null) {
            showMsg("⚠ No customer currently being served!", "No Customer");
            return;
        }
        queue.cancelBooking(serving);
        lblServingName.setText("— No Customer —");
        lblServingType.setText("Type: —");
        lblServingApt.setText("Apartment: —");
        lblNowServing.setText("—");
        refreshAll();
        showMsg("❌ Booking Cancelled for:\n" + serving.getName(), "Cancelled");
    }

    private void addCustomerDialog() {
        JDialog dialog = new JDialog(this, "Add Customer", true);
        dialog.setSize(420, 420);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(BG_CARD);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_CARD);
        panel.setBorder(new EmptyBorder(20,24,20,24));
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(6,0,6,0);

        JLabel heading = new JLabel("👤  Add New Customer");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 15));
        heading.setForeground(TEXT_PRIMARY);
        gc.gridx=0; gc.gridy=0; gc.gridwidth=2; panel.add(heading, gc);

        gc.gridwidth=1;
        String[] lbls = {"Full Name:", "Age:", "Phone:"};
        JTextField[] fields = new JTextField[3];
        for (int i=0; i<lbls.length; i++) {
            gc.gridx=0; gc.gridy=i+1; gc.weightx=0.3;
            JLabel l = new JLabel(lbls[i]);
            l.setForeground(TEXT_MUTED);
            l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            panel.add(l, gc);
            gc.gridx=1; gc.weightx=0.7;
            fields[i] = styledField();
            panel.add(fields[i], gc);
        }

        gc.gridx=0; gc.gridy=4; gc.weightx=0.3;
        JLabel tl = new JLabel("Type:"); tl.setForeground(TEXT_MUTED);
        tl.setFont(new Font("Segoe UI", Font.PLAIN, 12)); panel.add(tl, gc);
        gc.gridx=1; gc.weightx=0.7;
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"New Customer","Existing Customer","Senior Citizen (60+)"});
        styleCombo(typeBox); panel.add(typeBox, gc);

        gc.gridx=0; gc.gridy=5; gc.weightx=0.3;
        JLabel al = new JLabel("Apartment:"); al.setForeground(TEXT_MUTED);
        al.setFont(new Font("Segoe UI", Font.PLAIN, 12)); panel.add(al, gc);
        gc.gridx=1; gc.weightx=0.7;
        JComboBox<String> aptBox = new JComboBox<>(new String[]{"1BHK","2BHK","3BHK","Penthouse"});
        styleCombo(aptBox); panel.add(aptBox, gc);

        gc.gridx=0; gc.gridy=6; gc.gridwidth=2; gc.insets=new Insets(16,0,0,0);
        JButton btn = goldButton("Add to Queue");
        btn.addActionListener(e -> {
            try {
                String name  = fields[0].getText().trim();
                int age      = Integer.parseInt(fields[1].getText().trim());
                String phone = fields[2].getText().trim();

                Customer.CustomerType cType = switch (typeBox.getSelectedIndex()) {
                    case 1  -> Customer.CustomerType.EXISTING_CUSTOMER;
                    case 2  -> Customer.CustomerType.SENIOR_CITIZEN;
                    default -> Customer.CustomerType.NEW_CUSTOMER;
                };
                Customer.ApartmentType aType = switch (aptBox.getSelectedIndex()) {
                    case 1  -> Customer.ApartmentType.BHK_2;
                    case 2  -> Customer.ApartmentType.BHK_3;
                    case 3  -> Customer.ApartmentType.PENTHOUSE;
                    default -> Customer.ApartmentType.BHK_1;
                };

                Customer c = new Customer(name, age, phone, cType, aType);
                queue.enqueue(c);
                lblWaiting.setText(String.valueOf(queue.getQueueSize()));
                dialog.dispose();
                refreshAll();
                showMsg("✅ Customer Added!\nToken: " + c.getFormattedToken()
                      + "\nName: " + name + "\nType: " + cType.getLabel(), "Added to Queue");

            } catch (NumberFormatException ex) {
                showMsg("❌ Please enter valid Age!", "Input Error");
            } catch (IllegalArgumentException ex) {
                showMsg("❌ " + ex.getMessage(), "Validation Error");
            }
        });
        panel.add(btn, gc);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    // ─── REFRESH ALL ──────────────────────────────────────────────────────────
    private void refreshAll() {
        // Update stats
        lblWaiting.setText(String.valueOf(queue.getQueueSize()));
        lblConfirmed.setText(String.valueOf(queue.getTotalConfirmed()));
        lblFlats.setText(String.valueOf(availableFlats));

        // Refresh Queue Table
        queueTableModel.setRowCount(0);
        List<Customer> waitingList = queue.getQueueAsList();
        int pos = 1;
        for (Customer c : waitingList) {
            String priority = switch (c.getEffectiveType()) {
                case SENIOR_CITIZEN    -> "🔴 HIGH";
                case EXISTING_CUSTOMER -> "🟡 MED";
                default                -> "🟢 STD";
            };
            queueTableModel.addRow(new Object[]{
                pos++,
                c.getFormattedToken(),
                c.getName(),
                c.getAge(),
                c.getEffectiveType().getLabel(),
                c.getApartmentType().getLabel(),
                priority,
                c.getStatus()
            });
        }

        // Refresh Confirmed Table
        confirmedTableModel.setRowCount(0);
        for (Customer c : queue.getConfirmedList()) {
            confirmedTableModel.addRow(new Object[]{
                c.getFormattedToken(),
                c.getName(),
                c.getAge(),
                c.getEffectiveType().getLabel(),
                c.getApartmentType().getLabel(),
                c.getApartmentType().getPriceInLakhs() + "L",
                "✅ CONFIRMED"
            });
        }
    }

    private String getNextInQueue() {
        Customer next = queue.peek();
        return next != null ? next.getFormattedToken() + " - " + next.getName() : "None";
    }

    // ─── HELPERS ──────────────────────────────────────────────────────────────
    private JTable styledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setBackground(BG_DARK);
        table.setForeground(TEXT_PRIMARY);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(34);
        table.setGridColor(BORDER_COLOR);
        table.setSelectionBackground(new Color(40, 80, 140));
        table.setSelectionForeground(TEXT_PRIMARY);
        table.getTableHeader().setBackground(new Color(25, 32, 58));
        table.getTableHeader().setForeground(ACCENT_GOLD);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        table.setShowGrid(true);
        return table;
    }

    private JScrollPane styledScroll(JTable table) {
        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(BG_DARK);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        scroll.getVerticalScrollBar().setBackground(BG_CARD);
        return scroll;
    }

    private JTextField styledField() {
        JTextField f = new JTextField();
        f.setBackground(new Color(30,38,65));
        f.setForeground(TEXT_PRIMARY);
        f.setCaretColor(ACCENT_GOLD);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            new EmptyBorder(7,10,7,10)));
        return f;
    }

    private void styleCombo(JComboBox<String> box) {
        box.setBackground(new Color(30,38,65));
        box.setForeground(TEXT_PRIMARY);
        box.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    }

    private JButton actionBtn(String text, Color color) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isEnabled() ? color.darker() : Color.GRAY);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
                g2.setColor(TEXT_PRIMARY);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth()-fm.stringWidth(getText()))/2;
                int y = (getHeight()+fm.getAscent()-fm.getDescent())/2;
                g2.drawString(getText(), x, y);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setPreferredSize(new Dimension(0, 42));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton headerButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setForeground(color);
        btn.setBackground(new Color(30,38,65));
        btn.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(color, 1),
            new EmptyBorder(6,12,6,12)));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        return btn;
    }

    private JButton goldButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0,0,ACCENT_GOLD,getWidth(),0,new Color(180,140,30));
                g2.setPaint(gp);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
                g2.setColor(BG_DARK);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),(getWidth()-fm.stringWidth(getText()))/2,
                    (getHeight()+fm.getAscent()-fm.getDescent())/2);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(0,42));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void showMsg(String msg, String title) {
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    // ─── MAIN (Standalone Test) ───────────────────────────────────────────────
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        ApartmentQueue testQueue = new ApartmentQueue();

        // Add sample customers for testing
        testQueue.enqueue(new Customer("Rajan Kumar", 65, "9876543210",
            Customer.CustomerType.NEW_CUSTOMER, Customer.ApartmentType.BHK_2));
        testQueue.enqueue(new Customer("Priya S.", 35, "9123456780",
            Customer.CustomerType.EXISTING_CUSTOMER, Customer.ApartmentType.BHK_3));
        testQueue.enqueue(new Customer("Arun M.", 28, "9000011111",
            Customer.CustomerType.NEW_CUSTOMER, Customer.ApartmentType.BHK_1));
        testQueue.enqueue(new Customer("Meena R.", 62, "9555566666",
            Customer.CustomerType.NEW_CUSTOMER, Customer.ApartmentType.BHK_2));

        SwingUtilities.invokeLater(() -> new AdminPanel(testQueue));
    }
}