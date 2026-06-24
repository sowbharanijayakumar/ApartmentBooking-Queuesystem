import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class MainDashboard extends JFrame {

    // Color Palette - Modern Real Estate Theme
    private static final Color BG_DARK       = new Color(13, 17, 30);       // Deep navy
    private static final Color BG_CARD       = new Color(22, 28, 48);       // Card navy
    private static final Color ACCENT_GOLD   = new Color(212, 175, 55);     // Premium gold
    private static final Color ACCENT_BLUE   = new Color(64, 145, 247);     // Info blue
    private static final Color ACCENT_GREEN  = new Color(52, 199, 89);      // Success green
    private static final Color ACCENT_RED    = new Color(255, 69, 58);      // Alert red
    private static final Color TEXT_PRIMARY  = new Color(240, 240, 245);    // White text
    private static final Color TEXT_MUTED    = new Color(140, 145, 165);    // Muted text
    private static final Color BORDER_COLOR  = new Color(40, 50, 80);       // Subtle border

    // Stats
    private int totalRegistered = 0;
    private int nowServing      = 0;
    private int availableFlats  = 48;
    private int waitingCount    = 0;

    // Stat labels for live update
    private JLabel lblRegistered, lblServing, lblFlats, lblWaiting;

    public MainDashboard() {
        setTitle("🏢 Apartment Booking Queue System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 720);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setBackground(BG_DARK);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BG_DARK);

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildBody(),   BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);

        setContentPane(root);
        setVisible(true);
    }

    // ─── HEADER ──────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(20, 26, 46),
                    getWidth(), 0, new Color(30, 20, 60));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Gold accent line at bottom
                g2.setColor(ACCENT_GOLD);
                g2.setStroke(new BasicStroke(2f));
                g2.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
            }
        };
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(20, 32, 20, 32));

        // Left: Logo + Title
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        left.setOpaque(false);

        JLabel logo = new JLabel("🏢");
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 38));

        JPanel titleBlock = new JPanel(new GridLayout(2,1,0,2));
        titleBlock.setOpaque(false);
        JLabel title = new JLabel("Apartment Booking System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT_PRIMARY);
        JLabel subtitle = new JLabel("Queue Management • Priority Booking");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(ACCENT_GOLD);
        titleBlock.add(title);
        titleBlock.add(subtitle);

        left.add(logo);
        left.add(titleBlock);

        // Right: Status badge
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        JLabel badge = new JLabel("  ● SYSTEM ACTIVE  ");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setForeground(ACCENT_GREEN);
        badge.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(ACCENT_GREEN, 1, 20),
            new EmptyBorder(6, 10, 6, 10)));
        right.add(badge);

        header.add(left,  BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    // ─── BODY ────────────────────────────────────────────────────────────────
    private JPanel buildBody() {
        JPanel body = new JPanel(new BorderLayout(0, 24));
        body.setBackground(BG_DARK);
        body.setBorder(new EmptyBorder(28, 32, 10, 32));

        body.add(buildStatsRow(),   BorderLayout.NORTH);
        body.add(buildActionsGrid(), BorderLayout.CENTER);

        return body;
    }

    // ─── STATS ROW ───────────────────────────────────────────────────────────
    private JPanel buildStatsRow() {
        JPanel row = new JPanel(new GridLayout(1, 4, 16, 0));
        row.setOpaque(false);

        lblRegistered = new JLabel("0");
        lblServing    = new JLabel("—");
        lblFlats      = new JLabel("48");
        lblWaiting    = new JLabel("0");

        row.add(statCard("Registered",     lblRegistered, ACCENT_BLUE,  "👤"));
        row.add(statCard("Now Serving",    lblServing,    ACCENT_GOLD,  "🔔"));
        row.add(statCard("Flats Available",lblFlats,      ACCENT_GREEN, "🏠"));
        row.add(statCard("In Queue",       lblWaiting,    ACCENT_RED,   "⏳"));

        return row;
    }

    private JPanel statCard(String label, JLabel valueLabel, Color accent, String icon) {
        JPanel card = new JPanel(new BorderLayout(0, 6)) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(accent);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
                // Top accent bar
                g2.setColor(accent);
                g2.fillRoundRect(0, 0, getWidth(), 4, 4, 4);
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(18, 20, 18, 20));

        JLabel iconLabel = new JLabel(icon + "  " + label.toUpperCase());
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        iconLabel.setForeground(TEXT_MUTED);
        iconLabel.putClientProperty("label", label);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(accent);

        card.add(iconLabel,   BorderLayout.NORTH);
        card.add(valueLabel,  BorderLayout.CENTER);
        return card;
    }

    // ─── ACTIONS GRID ────────────────────────────────────────────────────────
    private JPanel buildActionsGrid() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 16));
        wrapper.setOpaque(false);

        JLabel sectionTitle = new JLabel("Quick Actions");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sectionTitle.setForeground(TEXT_MUTED);
        wrapper.add(sectionTitle, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 3, 16, 16));
        grid.setOpaque(false);

        grid.add(actionCard("👤", "Register Customer",
            "Add new customer to booking queue",
            ACCENT_BLUE, e -> openRegisterDialog()));

        grid.add(actionCard("📋", "View Queue",
            "See all customers in waiting list",
            ACCENT_GOLD, e -> openQueueView()));

        grid.add(actionCard("🔔", "Call Next",
            "Serve next customer from queue",
            ACCENT_GREEN, e -> callNextCustomer()));

        grid.add(actionCard("🏠", "Flat Inventory",
            "View available flats by type",
            new Color(150, 100, 220), e -> openFlatInventory()));

        grid.add(actionCard("✅", "Confirm Booking",
            "Finalize apartment booking",
            new Color(255, 149, 0), e -> openConfirmBooking()));

        grid.add(actionCard("🛠", "Admin Panel",
            "Manage bookings & reports",
            ACCENT_RED, e -> openAdminPanel()));

        wrapper.add(grid, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel actionCard(String icon, String title, String desc, Color accent, ActionListener action) {
        JPanel card = new JPanel(new BorderLayout(0, 10)) {
            private boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); }
                    public void mouseExited (MouseEvent e) { hovered = false; repaint(); setCursor(Cursor.getDefaultCursor()); }
                    public void mouseClicked(MouseEvent e) { action.actionPerformed(new ActionEvent(this, 0, "")); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = hovered ? accent.darker() : BG_CARD;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.setColor(hovered ? accent : BORDER_COLOR);
                g2.setStroke(new BasicStroke(hovered ? 2f : 1.2f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 18, 18);
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(22, 22, 22, 22));

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLbl.setForeground(TEXT_PRIMARY);

        JLabel descLbl = new JLabel("<html>" + desc + "</html>");
        descLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        descLbl.setForeground(TEXT_MUTED);

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        textPanel.setOpaque(false);
        textPanel.add(titleLbl);
        textPanel.add(descLbl);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        top.setOpaque(false);

        JLabel accentDot = new JLabel("●");
        accentDot.setFont(new Font("Segoe UI", Font.BOLD, 10));
        accentDot.setForeground(accent);
        top.add(accentDot);

        card.add(iconLbl,   BorderLayout.NORTH);
        card.add(textPanel, BorderLayout.CENTER);
        card.add(top,       BorderLayout.SOUTH);

        return card;
    }

    // ─── FOOTER ──────────────────────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(15, 19, 34));
        footer.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, BORDER_COLOR),
            new EmptyBorder(12, 32, 12, 32)));

        JLabel left = new JLabel("Apartment Booking Queue System  •  Java Swing + DSA Project");
        left.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        left.setForeground(TEXT_MUTED);

        JLabel right = new JLabel("Queue: FIFO + Priority  |  OOP  |  JavaFX/Swing");
        right.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        right.setForeground(TEXT_MUTED);

        footer.add(left,  BorderLayout.WEST);
        footer.add(right, BorderLayout.EAST);
        return footer;
    }

    // ─── ACTION HANDLERS ─────────────────────────────────────────────────────
    private void openRegisterDialog() {
        JDialog dialog = new JDialog(this, "Register Customer", true);
        dialog.setSize(440, 480);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(BG_CARD);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_CARD);
        panel.setBorder(new EmptyBorder(24, 28, 24, 28));
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(6, 0, 6, 0);

        JLabel heading = new JLabel("👤  New Customer Registration");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 16));
        heading.setForeground(TEXT_PRIMARY);
        gc.gridx=0; gc.gridy=0; gc.gridwidth=2; panel.add(heading, gc);

        gc.gridwidth=1;
        String[] labels = {"Full Name:", "Age:", "Phone Number:"};
        JTextField[] fields = new JTextField[3];
        for (int i = 0; i < labels.length; i++) {
            gc.gridx=0; gc.gridy=i+1; gc.weightx=0.3;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setForeground(TEXT_MUTED);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            panel.add(lbl, gc);
            gc.gridx=1; gc.weightx=0.7;
            fields[i] = styledField();
            panel.add(fields[i], gc);
        }

        // Customer Type
        gc.gridx=0; gc.gridy=4; gc.weightx=0.3;
        JLabel typeLabel = new JLabel("Customer Type:");
        typeLabel.setForeground(TEXT_MUTED);
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(typeLabel, gc);
        gc.gridx=1; gc.weightx=0.7;
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"New Customer", "Existing Customer", "Senior Citizen (60+)"});
        styleCombo(typeBox);
        panel.add(typeBox, gc);

        // Apartment Type
        gc.gridx=0; gc.gridy=5; gc.weightx=0.3;
        JLabel aptLabel = new JLabel("Apartment Type:");
        aptLabel.setForeground(TEXT_MUTED);
        aptLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(aptLabel, gc);
        gc.gridx=1; gc.weightx=0.7;
        JComboBox<String> aptBox = new JComboBox<>(new String[]{"1BHK", "2BHK", "3BHK", "Penthouse (VIP)"});
        styleCombo(aptBox);
        panel.add(aptBox, gc);

        // Button
        gc.gridx=0; gc.gridy=6; gc.gridwidth=2; gc.insets = new Insets(18,0,0,0);
        JButton btn = goldButton("Generate Token & Join Queue");
        btn.addActionListener(e -> {
            totalRegistered++;
            waitingCount++;
            lblRegistered.setText(String.valueOf(totalRegistered));
            lblWaiting.setText(String.valueOf(waitingCount));
            dialog.dispose();
            showInfo("✅ Token #T" + String.format("%03d", totalRegistered) + " generated!\n"
                   + "Customer: " + fields[0].getText() + "\n"
                   + "Type: " + typeBox.getSelectedItem() + "\n"
                   + "Apartment: " + aptBox.getSelectedItem());
        });
        panel.add(btn, gc);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void openQueueView() {
        JDialog dialog = new JDialog(this, "Queue Status", true);
        dialog.setSize(500, 380);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(BG_CARD);

        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(BG_CARD);
        panel.setBorder(new EmptyBorder(24, 28, 24, 28));

        JLabel heading = new JLabel("📋  Current Waiting Queue");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 16));
        heading.setForeground(TEXT_PRIMARY);
        panel.add(heading, BorderLayout.NORTH);

        String[] cols = {"Token", "Name", "Type", "Apartment", "Priority"};
        Object[][] data = {
            {"T001", "Rajan Kumar",  "Senior Citizen", "2BHK",  "🔴 HIGH"},
            {"T002", "Priya S.",     "Existing",       "3BHK",  "🟡 MED"},
            {"T003", "Arun M.",      "New Customer",   "1BHK",  "🟢 STD"},
        };
        JTable table = new JTable(data, cols);
        table.setBackground(BG_DARK);
        table.setForeground(TEXT_PRIMARY);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(32);
        table.getTableHeader().setBackground(new Color(30,38,65));
        table.getTableHeader().setForeground(ACCENT_GOLD);
        table.setGridColor(BORDER_COLOR);
        table.setSelectionBackground(ACCENT_BLUE.darker());

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(BG_DARK);
        scroll.setBorder(new RoundedBorder(BORDER_COLOR, 1, 10));
        panel.add(scroll, BorderLayout.CENTER);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void callNextCustomer() {
        nowServing++;
        if (waitingCount > 0) waitingCount--;
        lblServing.setText("#T" + String.format("%03d", nowServing));
        lblWaiting.setText(String.valueOf(waitingCount));
        showInfo("🔔 Now Serving: Token #T" + String.format("%03d", nowServing) + "\nPlease proceed to the counter.");
    }

    private void openFlatInventory() {
        showInfo("🏠 Flat Inventory\n\n1BHK  — 12 available\n2BHK  — 18 available\n3BHK  — 14 available\nPenthouse — 4 available\n\nTotal: " + availableFlats + " flats");
    }

    private void openConfirmBooking() {
        if (availableFlats > 0) {
            availableFlats--;
            lblFlats.setText(String.valueOf(availableFlats));
            showInfo("✅ Booking Confirmed!\nFlat allocated successfully.\nRemaining flats: " + availableFlats);
        } else {
            showInfo("❌ No flats available at this time.");
        }
    }

    private void openAdminPanel() {
      ApartmentQueue q = new ApartmentQueue();
      new AdminPanel(q);
}

    // ─── HELPERS ─────────────────────────────────────────────────────────────
    private JTextField styledField() {
        JTextField f = new JTextField();
        f.setBackground(new Color(30, 38, 65));
        f.setForeground(TEXT_PRIMARY);
        f.setCaretColor(ACCENT_GOLD);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(new CompoundBorder(new RoundedBorder(BORDER_COLOR, 1, 8), new EmptyBorder(8, 10, 8, 10)));
        return f;
    }

    private void styleCombo(JComboBox<String> box) {
        box.setBackground(new Color(30, 38, 65));
        box.setForeground(TEXT_PRIMARY);
        box.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }

    private JButton goldButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0,0, ACCENT_GOLD, getWidth(), 0, new Color(180,140,30));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(BG_DARK);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(0, 44));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Apartment Booking System", JOptionPane.INFORMATION_MESSAGE);
    }

    // ─── ROUNDED BORDER HELPER ───────────────────────────────────────────────
    static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int thickness, radius;
        RoundedBorder(Color c, int t, int r) { color=c; thickness=t; radius=r; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x, y, w-1, h-1, radius, radius);
        }
        @Override public Insets getBorderInsets(Component c) { return new Insets(radius/2,radius/2,radius/2,radius/2); }
    }

    // ─── MAIN ────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(MainDashboard::new);
    }
}
