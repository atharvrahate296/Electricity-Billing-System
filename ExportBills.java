import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import javax.swing.*;
import javax.swing.border.*;

public class ExportBills extends JFrame implements ActionListener {

    JButton exportButton;

    public ExportBills() {

        setTitle("Export Bills");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 247, 250));

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(52, 58, 64));
        header.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0,new Color(30, 144, 255)),
                new EmptyBorder(14, 25, 14, 25)
            )
        );

        JLabel title = new JLabel("Export Bills");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        header.add(title, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // ================= CARD =================
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(210, 210, 210), 2),
                new EmptyBorder(30, 30, 30, 30)
        ));
        card.setPreferredSize(new Dimension(400, 200));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridy = 0;

        JLabel info = new JLabel(
                "<html>Choose a destination folder<br>Export all Bill data in a CSV file</html>",
                JLabel.CENTER
        );
        info.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        info.setForeground(new Color(60, 60, 60));

        card.add(info, gbc);

        gbc.gridy++;

        exportButton = new FlatRoundedButton(
                "Export Bills to CSV",
                new Color(52, 120, 220)
        );
        exportButton.addActionListener(this);

        card.add(exportButton, gbc);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.add(card);

        add(center, BorderLayout.CENTER);

        setVisible(true);
    }

    // ================= LOGIC (UNCHANGED) =================
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == exportButton) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file to save");

            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();

                try (FileWriter writer = new FileWriter(fileToSave)) {
                    writer.append("Name,Meter ID,Units,Amount,Month,Year,RMN,Address,Status\n");

                    try (java.sql.Connection conn = Database.getConnection();
                         java.sql.Statement stmt = conn.createStatement();
                         java.sql.ResultSet billRs = stmt.executeQuery("SELECT meter_id, month, year, units, amount, status FROM bill")) {

                        while (billRs.next()) {
                            int meterNumber = billRs.getInt("meter_id");
                            String month = billRs.getString("month");
                            String year = billRs.getString("year");
                            int units = billRs.getInt("units");
                            double amount = billRs.getDouble("amount");
                            String status = billRs.getString("status");

                            try (java.sql.PreparedStatement ps = conn.prepareStatement("SELECT name, rmn, address FROM user WHERE meter_id = ?")) {
                                ps.setInt(1, meterNumber);
                                java.sql.ResultSet customerRs = ps.executeQuery();

                                if (customerRs.next()) {
                                    String customerName = customerRs.getString("name");
                                    String rmn = customerRs.getString("rmn");
                                    String address = customerRs.getString("address");

                                    writer.append(String.format(
                                            "%s,%d,%d,%.2f,%s,%s,%s,%s,%s\n",
                                            customerName,
                                            meterNumber,
                                            units,
                                            amount,
                                            month,
                                            year,
                                            rmn,
                                            address,
                                            status
                                    ));
                                }
                            }
                        }
                    }

                    JOptionPane.showMessageDialog(
                            this,
                            "Bills exported successfully!"
                    );

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                            this,
                            "Error exporting bills!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }
class FlatRoundedButton extends JButton {

    private final Color base;
    private final Color hover;

    public FlatRoundedButton(String text, Color baseColor) {
        super(text);
        this.base = baseColor;
        this.hover = baseColor.darker();

        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorder(new EmptyBorder(10, 24, 10, 24));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                repaint();
            }

            public void mouseExited(MouseEvent e) {
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2.setColor(getModel().isRollover() ? hover : base);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {}
}
}
