import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class App extends JFrame implements ActionListener {

    String atype;
    String meter_pass;

    // ---- SAME THEME AS LOGIN ----
    private static final Color FRAME_BG = new Color(245, 247, 250);
    private static final Color PRIMARY_COLOR = new Color(0, 123, 255);
    private static final Color ACCENT_COLOR = new Color(255, 69, 0);
    private static final Color TEXT_GRAY = new Color(52, 58, 64);

    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 26);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font MENU_FONT = new Font("Segoe UI", Font.BOLD, 14);

    public App(String atype, String meter_pass) {
        this.atype = atype;
        this.meter_pass = meter_pass;

        setTitle("Electricity Billing System - " + atype);
        setSize(750, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(FRAME_BG);

        // ---- CENTER CARD ----
        add(createDashboardCard(), BorderLayout.CENTER);

        // ---- MENU BAR ----
        JMenuBar mb = new JMenuBar();
        mb.setBackground(Color.WHITE);
        mb.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220,224,229)));
        setJMenuBar(mb);



        if (atype.equals("Admin")) {
            JMenu master = createMenu("Master");
            JMenuItem newCustomer = createMenuItem("New Customer");
            master.add(newCustomer);
            mb.add(master);

            JMenu bill = createMenu("Bill");
            bill.add(createMenuItem("Generate Bill"));
            bill.add(createMenuItem("Manage Bills"));
            bill.add(createMenuItem("Export Bills"));
            mb.add(bill);
        } else {
            JMenu actions = createMenu("Actions");
            actions.add(createMenuItem("Pay Bill"));
            actions.add(createMenuItem("Bill Details"));
            mb.add(actions);
        }

        JMenu exit = createMenu("Exit");
        exit.add(createMenuItem("Exit"));
        mb.add(exit);

        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

            }
        });
    }

    // ---- DASHBOARD CARD (SAME STYLE AS LOGIN) ----
    private JPanel createDashboardCard() {
    JPanel wrapper = new JPanel(new GridBagLayout());
    wrapper.setBackground(FRAME_BG);

    JPanel card = new JPanel(new BorderLayout());
    card.setBackground(Color.WHITE);
    card.setPreferredSize(new Dimension(600, 300));
    card.setOpaque(true);

    card.setBorder(
        BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 224, 229), 1, true),
            new EmptyBorder(20, 20, 20, 20)
        )
    );

    // ---------- LEFT IMAGE ----------
    JLabel imageLabel;
    try {
        ImageIcon icon = new ImageIcon("lib/icon/dashboard.png"); // ✅ add your image here
        Image img = icon.getImage().getScaledInstance(230, 200, Image.SCALE_SMOOTH);
        imageLabel = new JLabel(new ImageIcon(img));
    } catch (Exception e) {
        imageLabel = new JLabel(); // fail silently
    }

    JPanel imagePanel = new JPanel(new GridBagLayout());
    imagePanel.setOpaque(false);
    imagePanel.setPreferredSize(new Dimension(250, 260));
    imagePanel.add(imageLabel);

    card.add(imagePanel, BorderLayout.WEST);

    // ---------- RIGHT CONTENT ----------
    JPanel content = new JPanel(new GridBagLayout());
    content.setOpaque(false);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(15, 15, 15, 15);
    gbc.gridx = 0;
    gbc.gridwidth = 2;

    JLabel header = new JLabel("Hello, " + atype + "!", JLabel.CENTER);
    header.setFont(TITLE_FONT);
    header.setForeground(TEXT_GRAY);
    gbc.gridy = 0;
    content.add(header, gbc);

    gbc.gridwidth = 1;
    gbc.gridy = 1;
    gbc.gridx = 0;

    if (atype.equals("Admin")) {
        content.add(createCardButton("View Customers", "Customer Details", PRIMARY_COLOR), gbc);
        gbc.gridx = 1;
        content.add(createCardButton("My Profile", "View Profile", ACCENT_COLOR), gbc);
    } else {
        content.add(createCardButton("Pay Bill", "Pay Bill", ACCENT_COLOR), gbc);
        gbc.gridx = 1;
        content.add(createCardButton("My Profile", "View Profile", PRIMARY_COLOR), gbc);
    }

    card.add(content, BorderLayout.CENTER);

    wrapper.add(card);
    return wrapper;
}


    // ---- MODERN ROUNDED BUTTON (SAME AS LOGIN) ----
    private JButton createCardButton(String text, String action, Color color) {
        JButton b = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                super.paintComponent(g);
                g2.dispose();
            }
            public void paintBorder(Graphics g) {}
        };

        b.setActionCommand(action);
        b.setFont(BUTTON_FONT);
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(12, 20, 12, 20));
        b.setPreferredSize(new Dimension(160, 50));
        b.addActionListener(this);

        return b;
    }

    // ---- MENU HELPERS ----
    private JMenu createMenu(String title) {
        JMenu m = new JMenu(title);
        m.setFont(MENU_FONT);
        m.setForeground(TEXT_GRAY);
        return m;
    }

    private JMenuItem createMenuItem(String title) {
        JMenuItem item = new JMenuItem(title);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        item.addActionListener(this);
        return item;
    }

    // ---- ACTION HANDLING ----
    public void actionPerformed(ActionEvent ae) {
        String cmd = ae.getActionCommand();

        if (cmd.equals("New Customer")) new Signup();
        else if (cmd.equals("Customer Details")) new CustomerDetails().setVisible(true);
        else if (cmd.equals("Generate Bill")) new GenerateBill().setVisible(true);
        else if (cmd.equals("Manage Bills")) new ManageBills().setVisible(true);
        else if (cmd.equals("Export Bills")) new ExportBills().setVisible(true);
        else if (cmd.equals("Pay Bill")) new PayBill(meter_pass);
        else if (cmd.equals("Bill Details")) new BillDetails(meter_pass);
        else if (cmd.equals("View Profile")) new Profile(atype, meter_pass);
        else if (cmd.equals("Exit")) {
            setVisible(false);
            // changed exit functionality
            new Login().setVisible(true);
        }
    }
    public static void main(String[] args) {
        new Login();
    }
}
