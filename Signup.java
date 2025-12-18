import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Signup extends JFrame implements ActionListener {

    // --- UI Components ---
    JButton create, back;
    Choice accountType;
    JTextField meter, username, name, address, rmn, empCode, number;
    JPasswordField password, confirmPassword;
    JLabel meterLabel, usernameLabel, nameLabel, addressLabel, rmnLabel, empCodeLabel, numberLabel;
    JCheckBox showPassword;
    JProgressBar strengthMeter;
    
    // --- Colors & Fonts ---
    private static final Color PRIMARY_COLOR = new Color(0, 123, 255); 
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69); 
    private static final Color FRAME_BG = new Color(245, 247, 250); 
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_GRAY = new Color(52, 58, 64);
    private static final Color BORDER_GRAY = new Color(206, 212, 218);

    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    
    private JPanel dynamicFieldsPanel;
    
    private static final int INPUT_WIDTH = 250; 
    private static final int INPUT_HEIGHT = 36;
    private static final int FRAME_WIDTH = 950;
    private static final int FRAME_HEIGHT = 700;

    public Signup() {
        super("Create New Account");
        
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(FRAME_BG);
        setLayout(new BorderLayout());

        // ------------------ LEFT IMAGE PANEL ------------------
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(FRAME_BG);
        leftPanel.setPreferredSize(new Dimension(FRAME_WIDTH / 3, FRAME_HEIGHT));
        
        try {
            ImageIcon img = new ImageIcon("lib/icon/signup.png"); 
            Image scaled = img.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
            leftPanel.add(new JLabel(new ImageIcon(scaled)));
        } catch (Exception e) {
            JLabel placeholder = new JLabel("Signup Icon", JLabel.CENTER);
            placeholder.setForeground(TEXT_GRAY);
            leftPanel.add(placeholder);
        }
        add(leftPanel, BorderLayout.WEST);

        // ------------------ RIGHT FORM PANEL ------------------
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(FRAME_BG);
        add(centerWrapper, BorderLayout.CENTER);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GRAY, 1),
            new EmptyBorder(25, 40, 25, 40)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.gridwidth = 2; 
        gbc.gridx = 0;

        // TITLE
        JLabel title = new JLabel("Create New Account", JLabel.CENTER);
        title.setFont(TITLE_FONT);
        title.setForeground(TEXT_GRAY);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 15, 0);
        formPanel.add(title, gbc);

        // Account Type Row
        gbc.gridwidth = 1; 
        gbc.insets = new Insets(5, 0, 5, 15);
        gbc.gridy = 1;
        gbc.gridx = 0;
        formPanel.add(createLabel("Account Type"), gbc);

        gbc.gridx = 1;
        accountType = new Choice();
        accountType.add("Customer");
        accountType.add("Admin");
        accountType.setFont(FIELD_FONT);
        accountType.setPreferredSize(new Dimension(INPUT_WIDTH, INPUT_HEIGHT));
        formPanel.add(accountType, gbc);

        // --- Dynamic Fields Container ---
        dynamicFieldsPanel = new JPanel(new GridBagLayout());
        dynamicFieldsPanel.setBackground(CARD_BG);
        GridBagConstraints dgbc = new GridBagConstraints();
        dgbc.fill = GridBagConstraints.HORIZONTAL;
        dgbc.insets = new Insets(5, 0, 5, 15);
        
        meterLabel = createLabel("Meter Number");
        empCodeLabel = createLabel("EMP Code");
        meter = roundedTextField();
        empCode = roundedTextField();
        
        dgbc.gridy = 0; dgbc.gridx = 0;
        dynamicFieldsPanel.add(meterLabel, dgbc);
        dynamicFieldsPanel.add(empCodeLabel, dgbc);
        dgbc.gridx = 1;
        dynamicFieldsPanel.add(meter, dgbc);
        dynamicFieldsPanel.add(empCode, dgbc);

        usernameLabel = createLabel("Username");
        username = roundedTextField();
        dgbc.gridy = 1; dgbc.gridx = 0;
        dynamicFieldsPanel.add(usernameLabel, dgbc);
        dgbc.gridx = 1;
        dynamicFieldsPanel.add(username, dgbc);
        
        nameLabel = createLabel("Name");
        name = roundedTextField();
        dgbc.gridy = 2; dgbc.gridx = 0;
        dynamicFieldsPanel.add(nameLabel, dgbc);
        dgbc.gridx = 1;
        dynamicFieldsPanel.add(name, dgbc);
        
        addressLabel = createLabel("Address");
        numberLabel = createLabel("Number");
        address = roundedTextField();
        number = roundedTextField();
        dgbc.gridy = 3; dgbc.gridx = 0;
        dynamicFieldsPanel.add(addressLabel, dgbc);
        dynamicFieldsPanel.add(numberLabel, dgbc);
        dgbc.gridx = 1;
        dynamicFieldsPanel.add(address, dgbc);
        dynamicFieldsPanel.add(number, dgbc);

        rmnLabel = createLabel("Mobile Number");
        rmn = roundedTextField();
        dgbc.gridy = 4; dgbc.gridx = 0;
        dynamicFieldsPanel.add(rmnLabel, dgbc);
        dgbc.gridx = 1;
        dynamicFieldsPanel.add(rmn, dgbc);

        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 0, 5, 0);
        formPanel.add(dynamicFieldsPanel, gbc);

        // --- Password Fields ---
        gbc.gridwidth = 1;
        String pwHint = "8+ chars, 1 Uppercase, 1 Number, 1 Special Char";

        gbc.gridy = 3; gbc.gridx = 0;
        gbc.insets = new Insets(5, 0, 2, 15);
        formPanel.add(createLabel("Password"), gbc);
        
        gbc.gridx = 1;
        password = roundedPasswordField();
        password.setToolTipText(pwHint);
        formPanel.add(password, gbc);

        // Strength Meter (Small bar under password)
        gbc.gridy = 4; gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 8, 15);
        strengthMeter = new JProgressBar(0, 4);
        strengthMeter.setPreferredSize(new Dimension(INPUT_WIDTH, 4));
        strengthMeter.setStringPainted(false);
        strengthMeter.setBorderPainted(false);
        strengthMeter.setBackground(new Color(235, 235, 235));
        formPanel.add(strengthMeter, gbc);

        gbc.gridy = 5; gbc.gridx = 0;
        gbc.insets = new Insets(5, 0, 5, 15);
        formPanel.add(createLabel("Confirm Password"), gbc);
        
        gbc.gridx = 1;
        confirmPassword = roundedPasswordField();
        confirmPassword.setToolTipText(pwHint);
        formPanel.add(confirmPassword, gbc);

        // Show Password Toggle
        gbc.gridy = 6; gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 5, 15);
        showPassword = new JCheckBox("Show Password");
        showPassword.setBackground(CARD_BG);
        showPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(showPassword, gbc);

        // --- Buttons ---
        gbc.gridy = 7; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0); 
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 5));
        btnPanel.setBackground(CARD_BG);
        create = roundedButton("Create", SUCCESS_COLOR);
        back = roundedButton("Back", PRIMARY_COLOR);
        btnPanel.add(create); btnPanel.add(back);
        formPanel.add(btnPanel, gbc);

        centerWrapper.add(formPanel, new GridBagConstraints());

        // --- Listeners & Logic ---
        setupPasswordLogic();
        accountType.addItemListener(e -> updateVisibility(accountType.getSelectedItem()));
        create.addActionListener(this);
        back.addActionListener(this);

        updateVisibility("Customer");
        setVisible(true);
    }

    private void setupPasswordLogic() {
        // Toggle Visibility
        showPassword.addActionListener(e -> {
            char echo = showPassword.isSelected() ? (char) 0 : '•';
            password.setEchoChar(echo);
            confirmPassword.setEchoChar(echo);
        });

        // Real-time Strength Check
        password.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { update(); }
            public void removeUpdate(DocumentEvent e) { update(); }
            public void changedUpdate(DocumentEvent e) { update(); }
            private void update() {
                String pass = new String(password.getPassword());
                int score = 0;
                if (pass.length() >= 8) score++;
                if (pass.matches(".*[A-Z].*")) score++;
                if (pass.matches(".*[0-9].*")) score++;
                if (pass.matches(".*[@#$%^&+=!].*")) score++;

                strengthMeter.setValue(score);
                if (score <= 1) strengthMeter.setForeground(Color.RED);
                else if (score <= 3) strengthMeter.setForeground(Color.ORANGE);
                else strengthMeter.setForeground(SUCCESS_COLOR);
            }
        });
    }

    private boolean isValidPassword(String password) {
        String pattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$";
        return password.matches(pattern);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == create) {
            String atype = accountType.getSelectedItem();
            String spassword = new String(password.getPassword());
            String sconfirmPassword = new String(confirmPassword.getPassword());

            if (!spassword.equals(sconfirmPassword)) {
                JOptionPane.showMessageDialog(null, "Passwords do not match!");
                return;
            }

            if (!isValidPassword(spassword)) {
                JOptionPane.showMessageDialog(null, 
                    "Password must contain:\n- At least 8 characters\n- One Uppercase letter\n- One Number\n- One Special character (@#$%^&+=!)", 
                    "Weak Password", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Hashing and DB Logic
            try (Connection c = Database.getConnection()) {
                String hashedPw = PasswordHasher.hash(spassword);
                String query;
                PreparedStatement ps;

                if (atype.equals("Admin")) {
                    if (empCode.getText().isEmpty() || username.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill all fields!"); return;
                    }
                    query = "INSERT INTO admin (EMP_Code, username, name, number, password, admin_id) VALUES(?, ?, ?, ?, ?, ?)";
                    ps = c.prepareStatement(query);
                    ps.setString(1, empCode.getText());
                    ps.setString(2, username.getText());
                    ps.setString(3, name.getText());
                    ps.setString(4, number.getText());
                    ps.setString(5, hashedPw);
                    ps.setString(6, generateAdminId());
                } else {
                    if (meter.getText().isEmpty() || username.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill all fields!"); return;
                    }
                    query = "INSERT INTO user (meter_id, username, name, address, rmn, password) VALUES(?, ?, ?, ?, ?, ?)";
                    ps = c.prepareStatement(query);
                    ps.setString(1, meter.getText());
                    ps.setString(2, username.getText());
                    ps.setString(3, name.getText());
                    ps.setString(4, address.getText());
                    ps.setString(5, rmn.getText());
                    ps.setString(6, hashedPw);
                }
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Account Created Successfully");
                setVisible(false);
                new Login(); 
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        } else if (ae.getSource() == back) {
            setVisible(false);
            new Login(); 
        }
    }

    // --- UI Helper Methods ---
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        label.setForeground(TEXT_GRAY);
        label.setPreferredSize(new Dimension(150, 20));
        return label;
    }

    private JTextField roundedTextField() {
        JTextField f = new JTextField();
        f.setBorder(BorderFactory.createCompoundBorder(new LineBorder(BORDER_GRAY, 1, true), new EmptyBorder(5, 10, 5, 10)));
        f.setPreferredSize(new Dimension(INPUT_WIDTH, INPUT_HEIGHT)); 
        f.setFont(FIELD_FONT);
        return f;
    }

    private JPasswordField roundedPasswordField() {
        JPasswordField f = new JPasswordField();
        f.setBorder(BorderFactory.createCompoundBorder(new LineBorder(BORDER_GRAY, 1, true), new EmptyBorder(5, 10, 5, 10)));
        f.setPreferredSize(new Dimension(INPUT_WIDTH, INPUT_HEIGHT)); 
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
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setFont(BUTTON_FONT);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void updateVisibility(String user) {
        boolean isCust = user.equals("Customer");
        meterLabel.setVisible(isCust); meter.setVisible(isCust);
        addressLabel.setVisible(isCust); address.setVisible(isCust);
        rmnLabel.setVisible(isCust); rmn.setVisible(isCust);
        empCodeLabel.setVisible(!isCust); empCode.setVisible(!isCust);
        numberLabel.setVisible(!isCust); number.setVisible(!isCust);
        dynamicFieldsPanel.revalidate();
        dynamicFieldsPanel.repaint();
    }

    private String generateAdminId() {
        return Long.toHexString(Double.doubleToLongBits(Math.random())).substring(0, 16);
    }
}