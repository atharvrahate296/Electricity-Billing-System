import java.awt.*;
import java.sql.*;
import java.util.UUID;
import javax.swing.*;
import javax.swing.border.*;

public class Profile extends JFrame {

    public Profile(String atype, String meter) {

        setTitle("Profile");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 247, 250));

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(52, 58, 64));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(30, 144, 255)),
                new EmptyBorder(16, 32, 16, 32)
        ));

        JLabel heading = new JLabel("Profile");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 24));
        heading.setForeground(Color.WHITE);
        header.add(heading, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        // ================= CARD =================
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(540, 460));
        card.setBorder(new EmptyBorder(28, 32, 28, 32));

        JPanel cardWrapper = new JPanel(new BorderLayout());
        cardWrapper.setOpaque(false);
        cardWrapper.setBorder(new CompoundBorder(
                new MatteBorder(1, 1, 3, 3, new Color(220, 220, 220)),
                new EmptyBorder(0, 0, 0, 0)
        ));
        cardWrapper.add(card);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridy = 0;

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font valueFont = new Font("Segoe UI", Font.BOLD, 15);

        // ================= AVATAR =================
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        JPanel avatarRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        avatarRow.setOpaque(false);

        Image avatarImg = new ImageIcon("lib/icon/user.png")
                .getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);

        avatarRow.add(new JLabel(new ImageIcon(avatarImg)));

        JLabel role = new JLabel(atype.equals("Admin") ? "Administrator" : "Customer");
        role.setFont(new Font("Segoe UI", Font.BOLD, 18));
        role.setForeground(new Color(40, 40, 40));

        avatarRow.add(role);
        card.add(avatarRow, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        addDivider(card, gbc);

        JLabel v1 = valueLabel(valueFont);
        JLabel v2 = valueLabel(valueFont);
        JLabel v3 = valueLabel(valueFont);
        JLabel v4 = valueLabel(valueFont);
        JLabel v5 = valueLabel(valueFont);

        // ================= DATA =================
        if (atype.equals("Admin")) {

            addRow(card, gbc, "Name", v1, labelFont);
            addRow(card, gbc, "EMP Code", v2, labelFont);
            addRow(card, gbc, "Contact", v3, labelFont);
            addRow(card, gbc, "Username", v4, labelFont);

            addDivider(card, gbc);
            addRow(card, gbc, "Admin ID", v5, labelFont);
            v5.setText(UUID.randomUUID().toString());

            try (java.sql.Connection c = Database.getConnection()) {
                if (c == null) throw new Exception("Database connection is null.");

                try (java.sql.PreparedStatement ps =
                             c.prepareStatement("SELECT * FROM admin WHERE username = ?")) {

                    ps.setString(1, meter);

                    try (java.sql.ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            v1.setText(rs.getString("name"));
                            v2.setText(rs.getString("EMP_Code"));
                            v3.setText(rs.getString("number"));
                            v4.setText(rs.getString("username"));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            addRow(card, gbc, "Name", v1, labelFont);
            addRow(card, gbc, "Meter Number", v2, labelFont);
            addRow(card, gbc, "Address", v3, labelFont);
            addRow(card, gbc, "Registered Mobile", v4, labelFont);

            addDivider(card, gbc);
            addRow(card, gbc, "Customer ID", v5, labelFont);
            v5.setText(UUID.randomUUID().toString());

            try (java.sql.Connection c = Database.getConnection()) {
                if (c == null) throw new Exception("Database connection is null.");

                try (java.sql.PreparedStatement ps =
                             c.prepareStatement("SELECT * FROM user WHERE meter_id = ?")) {

                    ps.setString(1, meter);

                    try (java.sql.ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            v1.setText(rs.getString("name"));
                            v2.setText(rs.getString("meter_id"));
                            v3.setText(rs.getString("address"));
                            v4.setText(rs.getString("rmn"));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.add(cardWrapper);
        add(center, BorderLayout.CENTER);

        // ================= FOOTER =================
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 14));
        footer.setOpaque(false);
        footer.setBorder(new MatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));

        RoundedButton backBtn = new RoundedButton(
                "Back",
                new Color(120, 120, 120),
                new Color(95, 95, 95)
        );

        RoundedButton exitBtn = new RoundedButton(
                "Exit",
                new Color(255, 69, 0),
                new Color(180, 50, 50)
        );

        backBtn.addActionListener(e -> dispose());
        exitBtn.addActionListener(e -> dispose());

        footer.add(backBtn);
        footer.add(exitBtn);
        add(footer, BorderLayout.SOUTH);

        setVisible(true);
    }

    // ================= HELPERS =================

    private static void addRow(JPanel panel, GridBagConstraints gbc,
                               String label, JLabel value, Font labelFont) {
        gbc.gridx = 0;
        JLabel l = new JLabel(label);
        l.setFont(labelFont);
        l.setForeground(new Color(90, 90, 90));
        panel.add(l, gbc);

        gbc.gridx = 1;
        panel.add(value, gbc);
        gbc.gridy++;
    }

    private static void addDivider(JPanel panel, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JSeparator sep = new JSeparator();
        sep.setBorder(new EmptyBorder(12, 0, 12, 0));
        panel.add(sep, gbc);
        gbc.gridy++;
        gbc.gridwidth = 1;
    }

    private static JLabel valueLabel(Font font) {
        JLabel l = new JLabel("");
        l.setFont(font);
        l.setOpaque(true);
        l.setBackground(new Color(248, 249, 250));
        l.setBorder(new EmptyBorder(8, 14, 8, 14));
        l.setForeground(new Color(30, 30, 30));
        return l;
    }
}

/* ============ ROUNDED BUTTON CLASS ============ */

class RoundedButton extends JButton {

    private final Color baseColor;
    private final Color hoverColor;
    private Color currentColor;

    public RoundedButton(String text, Color base, Color hover) {
        super(text);
        this.baseColor   = base;
        this.hoverColor  = hover;
        this.currentColor = base;

        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setForeground(Color.WHITE);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(new EmptyBorder(8, 24, 8, 24));

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                currentColor = hoverColor;
                repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                currentColor = baseColor;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int arc = 26;  // pill-ish

        g2.setColor(currentColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // no border
    }
}
