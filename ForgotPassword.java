import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

public class ForgotPassword extends JFrame implements ActionListener {

    JTextField username;
    JPasswordField newPass, confirmPass;
    JButton reset, back;

    // ===== THEME =====
    private static final Color FRAME_BG = new Color(245, 247, 250);
    private static final Color HEADER_BG = new Color(52, 58, 64);
    private static final Color PRIMARY   = new Color(0, 123, 255);
    private static final Color SUCCESS   = new Color(40, 167, 69);
    private static final Color MUTED     = new Color(108, 117, 125);
    private static final Color BORDER    = new Color(220, 224, 229);

    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public ForgotPassword() {

        setTitle("Reset Password");
        setSize(460, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(FRAME_BG);

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_BG);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY),
                new EmptyBorder(15, 25, 15, 25)
        ));

        JLabel heading = new JLabel("Reset Password");
        heading.setFont(TITLE_FONT);
        heading.setForeground(Color.WHITE);
        header.add(heading, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        // ================= CARD =================
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(30, 35, 30, 35)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        addRow(card, gbc, "Username", username = field());
        addRow(card, gbc, "New Password", newPass = passwordField());
        addRow(card, gbc, "Confirm Password", confirmPass = passwordField());

        // ================= BUTTONS =================
        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 0, 0);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        btnPanel.setOpaque(false);

        back  = roundedButton("Back", MUTED);
        reset = roundedButton("Reset Password", SUCCESS);

        btnPanel.add(back);
        btnPanel.add(reset);

        card.add(btnPanel, gbc);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(FRAME_BG);
        center.add(card);

        add(center, BorderLayout.CENTER);

        reset.addActionListener(this);
        back.addActionListener(this);

        setVisible(true);
    }

    // ================= ACTIONS =================
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == reset) {

            String user = username.getText().trim();
            String p1 = new String(newPass.getPassword());
            String p2 = new String(confirmPass.getPassword());

            if (user.isEmpty() || p1.isEmpty() || p2.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required");
                return;
            }

            if (!p1.equals(p2)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match");
                return;
            }

            try (Connection con = Database.getConnection()) {
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE user SET password = ? WHERE username = ?"
                )) {
                    ps.setString(1, p1);
                    ps.setString(2, user);

                    int updated = ps.executeUpdate();

                    if (updated > 0) {
                        JOptionPane.showMessageDialog(this, "Password updated successfully");
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "User not found");
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }

        if (e.getSource() == back) {
            dispose();
        }
    }

    // ================= HELPERS =================
    private void addRow(JPanel panel, GridBagConstraints gbc, String label, JComponent field) {
        JLabel l = new JLabel(label);
        l.setFont(LABEL_FONT);
        panel.add(l, gbc);

        gbc.gridy++;
        panel.add(field, gbc);
        gbc.gridy++;
    }

    private JTextField field() {
        JTextField f = new JTextField();
        f.setFont(FIELD_FONT);
        f.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(7, 10, 7, 10)
        ));
        return f;
    }

    private JPasswordField passwordField() {
        JPasswordField f = new JPasswordField();
        f.setFont(FIELD_FONT);
        f.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(7, 10, 7, 10)
        ));
        return f;
    }

    private JButton roundedButton(String text, Color color) {
        JButton b = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                super.paintComponent(g);
                g2.dispose();
            }
            public void paintBorder(Graphics g) {}
        };

        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(10, 22, 10, 22));
        return b;
    }
}
