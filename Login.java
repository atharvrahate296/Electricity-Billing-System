import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

public class Login extends JFrame implements ActionListener {

    JButton login, signup;
    JTextField username;
    JPasswordField password;
    Choice logginin;
    JLabel forgotPassword;

    private static final Color PRIMARY_COLOR = new Color(0, 123, 255);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color FRAME_BG = new Color(245, 247, 250);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_GRAY = new Color(52, 58, 64);
    private static final Color BORDER_GRAY = new Color(206, 212, 218);

    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    public Login() {
        super("Login Page");
        setSize(850, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(FRAME_BG);
        setLayout(new BorderLayout());

        // LEFT IMAGE
        JPanel left = new JPanel(new GridBagLayout());
        left.setBackground(FRAME_BG);
        try {
            ImageIcon img = new ImageIcon("lib/icon/login.png");
            Image scaled = img.getImage().getScaledInstance(280, 280, Image.SCALE_SMOOTH);
            left.add(new JLabel(new ImageIcon(scaled)));
        } catch (Exception e) {
            left.add(new JLabel("Login"));
        }

        JPanel leftWrapper = new JPanel(new BorderLayout());
        leftWrapper.setPreferredSize(new Dimension(300, 480));
        leftWrapper.setOpaque(false);
        leftWrapper.add(left);
        add(leftWrapper, BorderLayout.WEST);

        // RIGHT CARD
        JPanel cardWrapper = new JPanel(new GridBagLayout());
        cardWrapper.setBackground(FRAME_BG);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(CARD_BG);
        card.setPreferredSize(new Dimension(380, 400));
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 224, 229), 1, true),
                new EmptyBorder(35, 20, 35, 45)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.insets = new Insets(6, 0, 6, 0);

        gbc.gridy = 0;
        JLabel title = new JLabel("Login to Your Account", JLabel.CENTER);
        title.setFont(TITLE_FONT);
        title.setForeground(TEXT_GRAY);
        card.add(title, gbc);

        gbc.gridy++;
        JLabel lblusername = new JLabel("Username");
        lblusername.setFont(LABEL_FONT);
        card.add(lblusername, gbc);

        gbc.gridy++;
        username = roundedTextField();
        card.add(username, gbc);

        gbc.gridy++;
        JLabel lblpassword = new JLabel("Password");
        lblpassword.setFont(LABEL_FONT);
        card.add(lblpassword, gbc);

        gbc.gridy++;
        password = roundedPasswordField();
        card.add(password, gbc);

        // FORGOT PASSWORD
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;

        JButton forgotPasswordButton = new JButton("Forgot password?");
        forgotPasswordButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        forgotPasswordButton.setForeground(PRIMARY_COLOR);
        forgotPasswordButton.setBorderPainted(false);
        forgotPasswordButton.setContentAreaFilled(false);
        forgotPasswordButton.setFocusPainted(false);
        forgotPasswordButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotPasswordButton.setPreferredSize(new Dimension(280, 22));
        forgotPasswordButton.setHorizontalAlignment(SwingConstants.LEFT);
        forgotPasswordButton.addActionListener(e -> {
            setVisible(false);
            new ForgotPassword().setVisible(true);
        });
        card.add(forgotPasswordButton, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridy++;
        JLabel role = new JLabel("Login as");
        role.setFont(LABEL_FONT);
        card.add(role, gbc);

        gbc.gridy++;
        logginin = new Choice();
        logginin.add("Admin");
        logginin.add("Customer");
        logginin.setFont(LABEL_FONT);
        logginin.setPreferredSize(new Dimension(280, 30));
        card.add(logginin, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 0, 0);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setOpaque(false);

        login = roundedButton("Login", SUCCESS_COLOR);
        signup = roundedButton("Signup", PRIMARY_COLOR);

        btnPanel.add(login);
        btnPanel.add(signup);
        card.add(btnPanel, gbc);

        cardWrapper.add(card);
        add(cardWrapper, BorderLayout.CENTER);

        login.addActionListener(this);
        signup.addActionListener(this);

        setVisible(true);
    }

    private JTextField roundedTextField() {
        JTextField f = new JTextField();
        f.setBorder(new CompoundBorder(new LineBorder(BORDER_GRAY, 1, true), new EmptyBorder(5, 10, 5, 10)));
        f.setPreferredSize(new Dimension(280, 36));
        f.setFont(LABEL_FONT);
        return f;
    }

    private JPasswordField roundedPasswordField() {
        JPasswordField f = new JPasswordField();
        f.setBorder(new CompoundBorder(new LineBorder(BORDER_GRAY, 1, true), new EmptyBorder(5, 10, 5, 10)));
        f.setPreferredSize(new Dimension(280, 36));
        f.setFont(LABEL_FONT);
        return f;
    }

    private JButton roundedButton(String text, Color color) {
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
        b.setFont(BUTTON_FONT);
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(10, 22, 10, 22));
        return b;
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == login) {
            String susername = username.getText();
            String spassword = String.valueOf(password.getPassword());
            String userRole = logginin.getSelectedItem();

            try (Connection c = Database.getConnection()) {
                // Fetch the user data including the HASHED password using only username
                String query = "Admin".equals(userRole) 
                    ? "select * from admin where username = ?" 
                    : "select * from user where username = ?";

                try (PreparedStatement ps = c.prepareStatement(query)) {
                    ps.setString(1, susername);
                    
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            // Retrieve the hashed password from the database
                            String storedHash = rs.getString("password");

                            // --- BCrypt Verification Logic ---
                            if (PasswordHasher.check(spassword, storedHash)) {
                                // Match found
                                String type = "Admin".equals(userRole) ? "Admin" : "Customer";
                                String meter = "Admin".equals(userRole) ? susername : rs.getString("meter_id");

                                setVisible(false);
                                new App(type, meter).setVisible(true);
                            } else {
                                // Password mismatch
                                JOptionPane.showMessageDialog(null, "Invalid Password");
                                password.setText("");
                            }
                        } else {
                            // User not found
                            JOptionPane.showMessageDialog(null, "Username not found");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
            }
        } else if (ae.getSource() == signup) {
            setVisible(false);
            new Signup().setVisible(true);
        }
    }
}