import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Random;

public class Signup extends JFrame implements ActionListener {

    // --- UI Components ---
    JButton create, back;
    Choice accountType;
    JTextField meter, username, name, address, rmn, empCode, number;
    JPasswordField password, confirmPassword;
    JLabel meterLabel, usernameLabel, nameLabel, addressLabel, rmnLabel, empCodeLabel, numberLabel;
    
    // --- Colors & Fonts for a modern look ---
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
    private static final int FRAME_WIDTH = 900;
    private static final int FRAME_HEIGHT = 650;

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
            JLabel image = new JLabel(new ImageIcon(scaled));
            leftPanel.add(image);
        } catch (Exception e) {
            JLabel placeholder = new JLabel("Signup Icon", JLabel.CENTER);
            placeholder.setPreferredSize(new Dimension(280, 280));
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
            new EmptyBorder(30, 40, 30, 40)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.gridwidth = 2; 
        gbc.gridx = 0;

        // TITLE
        JLabel title = new JLabel("Create New Account", JLabel.CENTER);
        title.setFont(TITLE_FONT);
        title.setForeground(TEXT_GRAY);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(title, gbc);

        // Account Type Row
        gbc.gridwidth = 1; 
        gbc.insets = new Insets(5, 0, 5, 15);
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel heading = new JLabel("Account Type");
        heading.setFont(LABEL_FONT);
        heading.setForeground(TEXT_GRAY);
        gbc.anchor = GridBagConstraints.WEST; 
        formPanel.add(heading, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.EAST; 
        accountType = new Choice();
        accountType.add("Customer");
        accountType.add("Admin");
        accountType.setFont(FIELD_FONT);
        accountType.setPreferredSize(new Dimension(INPUT_WIDTH, INPUT_HEIGHT));
        formPanel.add(accountType, gbc);
        gbc.weightx = 0;

        // --- Dynamic Fields Container ---
        dynamicFieldsPanel = new JPanel(new GridBagLayout());
        dynamicFieldsPanel.setBackground(CARD_BG);
        
        GridBagConstraints dgbc = new GridBagConstraints();
        dgbc.fill = GridBagConstraints.HORIZONTAL;
        dgbc.insets = new Insets(5, 0, 5, 15);
        
        // Meter/EMP Code
        meterLabel = createLabel("Meter Number");
        empCodeLabel = createLabel("EMP Code");
        meter = roundedTextField();
        empCode = roundedTextField();
        
        dgbc.gridy = 0;
        dgbc.gridx = 0;
        dgbc.anchor = GridBagConstraints.WEST;
        dynamicFieldsPanel.add(meterLabel, dgbc);
        dynamicFieldsPanel.add(empCodeLabel, dgbc);
        dgbc.gridx = 1;
        dgbc.anchor = GridBagConstraints.EAST;
        dynamicFieldsPanel.add(meter, dgbc);
        dynamicFieldsPanel.add(empCode, dgbc);

        // Username
        usernameLabel = createLabel("Username");
        username = roundedTextField();
        dgbc.gridy = 1;
        dgbc.gridx = 0;
        dgbc.anchor = GridBagConstraints.WEST;
        dynamicFieldsPanel.add(usernameLabel, dgbc);
        dgbc.gridx = 1;
        dgbc.anchor = GridBagConstraints.EAST;
        dynamicFieldsPanel.add(username, dgbc);
        
        // Name
        nameLabel = createLabel("Name");
        name = roundedTextField();
        dgbc.gridy = 2;
        dgbc.gridx = 0;
        dgbc.anchor = GridBagConstraints.WEST;
        dynamicFieldsPanel.add(nameLabel, dgbc);
        dgbc.gridx = 1;
        dgbc.anchor = GridBagConstraints.EAST;
        dynamicFieldsPanel.add(name, dgbc);
        
        // Address/Number
        addressLabel = createLabel("Address");
        numberLabel = createLabel("Number");
        address = roundedTextField();
        number = roundedTextField();
        
        dgbc.gridy = 3;
        dgbc.gridx = 0;
        dgbc.anchor = GridBagConstraints.WEST;
        dynamicFieldsPanel.add(addressLabel, dgbc);
        dynamicFieldsPanel.add(numberLabel, dgbc);
        dgbc.gridx = 1;
        dgbc.anchor = GridBagConstraints.EAST;
        dynamicFieldsPanel.add(address, dgbc);
        dynamicFieldsPanel.add(number, dgbc);

        // RMN
        rmnLabel = createLabel("Mobile Number");
        rmn = roundedTextField();
        dgbc.gridy = 4;
        dgbc.gridx = 0;
        dgbc.anchor = GridBagConstraints.WEST;
        dynamicFieldsPanel.add(rmnLabel, dgbc);
        dgbc.gridx = 1;
        dgbc.anchor = GridBagConstraints.EAST;
        dynamicFieldsPanel.add(rmn, dgbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 0, 10, 0);
        formPanel.add(dynamicFieldsPanel, gbc);

        // --- Common Password Fields ---
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel passwordLabel = createLabel("Password");
        password = roundedPasswordField();
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.insets = new Insets(5, 0, 5, 15);
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(password, gbc);

        JLabel confirmPasswordLabel = createLabel("Confirm Password");
        confirmPassword = roundedPasswordField();
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(confirmPasswordLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(confirmPassword, gbc);

        // --- Buttons ---
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0); 
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 5));
        btnPanel.setBackground(CARD_BG);
        
        create = roundedButton("Create", SUCCESS_COLOR);
        back = roundedButton("Back", PRIMARY_COLOR);

        btnPanel.add(create);
        btnPanel.add(back);
        formPanel.add(btnPanel, gbc);

        centerWrapper.add(formPanel, new GridBagConstraints());

        setupInitialVisibility();

        accountType.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateVisibility(accountType.getSelectedItem());
            }
        });
        create.addActionListener(this);
        back.addActionListener(this);

        setVisible(true);
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        label.setForeground(TEXT_GRAY);
        label.setPreferredSize(new Dimension(150, 20));
        return label;
    }

    private JTextField roundedTextField() {
        JTextField f = new JTextField();
        f.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_GRAY, 1, true),
            new EmptyBorder(5, 10, 5, 10) 
        ));
        f.setPreferredSize(new Dimension(INPUT_WIDTH, INPUT_HEIGHT)); 
        f.setFont(FIELD_FONT);
        f.setForeground(TEXT_GRAY);
        return f;
    }

    private JPasswordField roundedPasswordField() {
        JPasswordField f = new JPasswordField();
        f.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_GRAY, 1, true),
            new EmptyBorder(5, 10, 5, 10) 
        ));
        f.setPreferredSize(new Dimension(INPUT_WIDTH, INPUT_HEIGHT)); 
        f.setFont(FIELD_FONT);
        f.setForeground(TEXT_GRAY);
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
        b.setBorder(new EmptyBorder(10, 25, 10, 25)); 
        b.setFont(BUTTON_FONT);
        return b;
    }

    private void setupInitialVisibility() {
        updateVisibility("Customer"); 
    }

    private void updateVisibility(String user) {
        meterLabel.setVisible(false); meter.setVisible(false);
        addressLabel.setVisible(false); address.setVisible(false);
        rmnLabel.setVisible(false); rmn.setVisible(false);
        empCodeLabel.setVisible(false); empCode.setVisible(false);
        numberLabel.setVisible(false); number.setVisible(false);
        
        if (user.equals("Customer")) {
            meterLabel.setVisible(true); meter.setVisible(true);
            addressLabel.setVisible(true); address.setVisible(true);
            rmnLabel.setVisible(true); rmn.setVisible(true);
        } else { 
            empCodeLabel.setVisible(true); empCode.setVisible(true);
            numberLabel.setVisible(true); number.setVisible(true);
        }
        
        dynamicFieldsPanel.revalidate();
        dynamicFieldsPanel.repaint();
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

            // --- PASSWORD HASHING LOGIC ---
            String hashedPw = PasswordHasher.hash(spassword);

            try (Connection c = Database.getConnection()) {
                if (atype.equals("Admin")) {
                    String empCodeVal = empCode.getText();
                    String usernameVal = username.getText();
                    String nameVal = name.getText();
                    String numberVal = number.getText();

                    if (empCodeVal.isEmpty() || usernameVal.isEmpty() || nameVal.isEmpty() || numberVal.isEmpty() || spassword.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill all fields!");
                        return;
                    }

                    String query = "INSERT INTO admin (EMP_Code, username, name, number, password, admin_id) VALUES(?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement ps = c.prepareStatement(query)) {
                        ps.setString(1, empCodeVal);
                        ps.setString(2, usernameVal);
                        ps.setString(3, nameVal);
                        ps.setString(4, numberVal);
                        ps.setString(5, hashedPw); // Store the hash
                        ps.setString(6, generateAdminId());
                        ps.executeUpdate();
                    }
                } else {
                    String meterVal = meter.getText();
                    String usernameVal = username.getText();
                    String nameVal = name.getText();
                    String addressVal = address.getText();
                    String rmnVal = rmn.getText();

                    if (meterVal.isEmpty() || usernameVal.isEmpty() || nameVal.isEmpty() || addressVal.isEmpty() || rmnVal.isEmpty() || spassword.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill all fields!");
                        return;
                    }

                    String query = "INSERT INTO user (meter_id, username, name, address, rmn, password) VALUES(?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement ps = c.prepareStatement(query)) {
                        ps.setString(1, meterVal);
                        ps.setString(2, usernameVal);
                        ps.setString(3, nameVal);
                        ps.setString(4, addressVal);
                        ps.setString(5, rmnVal);
                        ps.setString(6, hashedPw); // Store the hash
                        ps.executeUpdate();
                    }
                }

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

    private String generateAdminId() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(16);
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}