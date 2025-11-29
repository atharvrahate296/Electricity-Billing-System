import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class BillDetails extends JFrame {

    String meter;

    // ---- THEME ----
    private static final Color FRAME_BG  = new Color(245, 247, 250);
    private static final Color HEADER_BG = new Color(52, 58, 64);
    private static final Color PRIMARY   = new Color(30, 144, 255);
    private static final Color BORDER    = new Color(220, 224, 229);
    private static final Color TEXT      = new Color(33, 37, 41);

    private static final Font TITLE_FONT  = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font TABLE_FONT  = new Font("Segoe UI", Font.PLAIN, 13);

    public BillDetails(String meter) {
        this.meter = meter;

        setTitle("Bill Details");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(FRAME_BG);

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_BG);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY),
                new EmptyBorder(14, 25, 14, 25)
        ));

        JLabel heading = new JLabel("Bill Details");
        heading.setFont(TITLE_FONT);
        heading.setForeground(Color.WHITE);
        header.add(heading, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        // ================= TABLE =================
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Meter No", "Month", "Year", "Units", "Total Bill", "Status"}, 0
        );

        try (java.sql.Connection c = Database.getConnection()) {
            if (c == null) throw new Exception("Database connection is null.");

            try (java.sql.PreparedStatement ps = c.prepareStatement("SELECT meter_id, month, year, units, amount, status FROM bill WHERE meter_id = ?")) {

                ps.setInt(1, Integer.parseInt(meter));

                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        model.addRow(new Object[]{
                                rs.getInt("meter_id"),
                                rs.getString("month"),
                                rs.getString("year"),
                                rs.getInt("units"),
                                String.format("%.2f", rs.getDouble("amount")),
                                rs.getString("status")
                        });
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching bill details: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        JTable table = new JTable(model);
        table.setFont(TABLE_FONT);
        table.setRowHeight(32);
        table.setSelectionBackground(new Color(215, 230, 255));
        table.setGridColor(BORDER);
        table.setShowVerticalLines(true);

        JTableHeader th = table.getTableHeader();
        th.setFont(HEADER_FONT);
        th.setBackground(new Color(235, 237, 240));
        th.setForeground(TEXT);
        th.setReorderingAllowed(false);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(new EmptyBorder(10, 10, 10, 10));
        sp.getViewport().setBackground(Color.WHITE);

        // ================= CARD =================
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        if (model.getRowCount() == 0) {
            JLabel empty = new JLabel("No bills found for this meter", JLabel.CENTER);
            empty.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            empty.setForeground(Color.GRAY);
            card.add(empty, BorderLayout.CENTER);
        } else {
            card.add(sp, BorderLayout.CENTER);
        }

        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(new EmptyBorder(20, 20, 20, 20));
        center.setOpaque(false);
        center.add(card, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);

        setVisible(true);
    }
}