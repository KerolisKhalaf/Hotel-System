package hotel.payment;

import hotel.payment.Homepage;
import java.sql.*;
import javax.swing.JOptionPane;

// ✅ إضافات للـ Facade والـ Customer
import hotel.payment.HotelFacade;
import hotel.model.Customer;

public class Login extends javax.swing.JFrame {

    // ✅ Database URL (موجود زي ما هو)
    private final String url =
        "jdbc:sqlserver://localhost\\MSSQLSERVER14;databaseName=Hotel;user=sa;password=123456;";

    // ✅ Facade object
    private HotelFacade hotelFacade;

    public Login() {
        initComponents();

        // ✅ تحميل Driver (موجود زي ما هو)
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Cannot Load JDBC Driver !!!");
        }

        // ✅ نهيّأ الـ Facade
        hotelFacade = new HotelFacade();

        // ✅ نخلي الصفحة تظهر في النص
        setLocationRelativeTo(null);
    }

    // ----------------------------------------------------
    // ✅ نسخة الأصلي (Check_Login بالـ SQL المباشر)
    // ----------------------------------------------------
    public boolean Check_Login(String username, String password) {
        boolean flag = false;

        Connection con = null;
        Statement stmt = null;
        ResultSet result = null;

        try {
            con = DriverManager.getConnection(url);
            stmt = con.createStatement();
            String query = "SELECT * FROM Users where username='" + username + "' "
                         + "AND password='" + password + "'";
            result = stmt.executeQuery(query);

            if (result.next()) {
                JOptionPane.showMessageDialog(this, "welcome ");

                this.dispose();
                Homepage homepage = new Homepage();
                homepage.setVisible(true);
                flag = true;
            } else {
                JOptionPane.showMessageDialog(this, "invalid Username or Password ");
                flag = false;
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } finally {
            if (con != null) { try { con.close(); } catch (Exception ex) {} }
            if (stmt != null) { try { stmt.close(); } catch (Exception ex) {} }
            if (result != null) { try { result.close(); } catch (Exception ex) {} }
        }
        return flag;
    }

    // ----------------------------------------------------
    // ✅ نسخة شغلك (Facade)
    // ----------------------------------------------------
    public Customer loginWithFacade(String username, String password) {
        return hotelFacade.login(username, password);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        usernameText = new javax.swing.JTextField();
        loginpassword = new javax.swing.JPasswordField();
        jCheckBox1 = new javax.swing.JCheckBox();
        Usernamelogin = new javax.swing.JLabel();
        passwordlogin = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        Login_btnActionPerformed = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hotel/wi.jpg"))); // NOI18N
        jLabel1.setText("jLabel1");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hotel/wi.jpg"))); // NOI18N
        jLabel2.setText("jLabel2");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hotel/wi.jpg"))); // NOI18N
        jLabel3.setText("jLabel3");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(235, 235, 218));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hotel/wi.jpg"))); // NOI18N
        jLabel4.setText("jLabel4");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 51, 0), 5, true));

        loginpassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginpasswordActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Remember Me");

        Usernamelogin.setFont(new java.awt.Font("Lucida Calligraphy", 1, 14)); // NOI18N
        Usernamelogin.setText("User-Name :");

        passwordlogin.setFont(new java.awt.Font("Lucida Calligraphy", 1, 14)); // NOI18N
        passwordlogin.setText("Password :");

        jLabel9.setFont(new java.awt.Font("Bauhaus 93", 0, 12)); // NOI18N
        jLabel9.setText("Forget Your Password?");
        jLabel9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel9MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(usernameText)
                                    .addComponent(loginpassword, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE))
                                .addComponent(jCheckBox1)
                                .addComponent(passwordlogin, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(43, 43, 43))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(Usernamelogin, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(Usernamelogin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(usernameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(passwordlogin)
                .addGap(12, 12, 12)
                .addComponent(loginpassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox1)
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Login_btnActionPerformed.setBackground(new java.awt.Color(153, 51, 0));
        Login_btnActionPerformed.setFont(new java.awt.Font("Segoe Script", 0, 14)); // NOI18N
        Login_btnActionPerformed.setForeground(new java.awt.Color(255, 255, 255));
        Login_btnActionPerformed.setText("Log-In");
        Login_btnActionPerformed.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        Login_btnActionPerformed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Login_btnActionPerformedActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Bauhaus 93", 0, 12)); // NOI18N
        jLabel5.setText("Dont have account?");

        jButton2.setBackground(new java.awt.Color(153, 51, 0));
        jButton2.setFont(new java.awt.Font("Segoe Script", 0, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Sign-Up");
        jButton2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Footlight MT Light", 1, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(153, 51, 0));
        jLabel6.setText("WELCOME");

        jLabel7.setFont(new java.awt.Font("Freestyle Script", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(153, 51, 0));
        jLabel7.setText("Find and book");

        jLabel8.setFont(new java.awt.Font("Freestyle Script", 1, 36)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(153, 51, 0));
        jLabel8.setText(" your perfect stay ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(63, 63, 63)
                                .addComponent(jLabel6))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(388, 388, 388)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Login_btnActionPerformed, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(79, 79, 79))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(360, 360, 360)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(61, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Login_btnActionPerformed, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void Login_btnActionPerformedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Login_btnActionPerformedActionPerformed
                         
    // -------------------------------
    // 🔹 الأصلي (SQL مباشر)
    // -------------------------------
    String username_sql = usernameText.getText();
    String password_sql = loginpassword.getText();

    if (username_sql.isEmpty() || !username_sql.matches("^[A-Za-z]{2,30}$")) {
        JOptionPane.showMessageDialog(this, "Please enter a valid name (only letters, 2–30 chars)");
        return;
    }

    if (password_sql.isEmpty()) {
        JOptionPane.showMessageDialog(this, "please , Enter your password ");
        return;
    }

    boolean checkLoginSQL = Check_Login(username_sql, password_sql);

    // -------------------------------
    // 🔹 شغلك (Facade)
    // -------------------------------
    String username = usernameText.getText().trim();
    String password = new String(loginpassword.getPassword()).trim();

    if (username.isEmpty() || !username.matches("^[A-Za-z]{2,30}$")) {
        JOptionPane.showMessageDialog(this, "Please enter a valid name (only letters, 2–30 chars)");
        return;
    }

    if (password.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter your password");
        return;
    }

    Customer customer = hotelFacade.login(username, password);

    if (customer != null) {
        JOptionPane.showMessageDialog(this, "Welcome " + customer.getFirstName());

        this.dispose();
        Homepage homepage = new Homepage(customer); // مرر الـ Customer لو محتاج تعرض بياناته
        homepage.setVisible(true);

    } else {
        JOptionPane.showMessageDialog(this, "Invalid username or password");
    }

    }//GEN-LAST:event_Login_btnActionPerformedActionPerformed

    private void loginpasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginpasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_loginpasswordActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        this.dispose();
        signin signup = new signin();
        signup.setVisible(true);                                     
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseClicked

   this.dispose();

                            signin signup = new signin();
                             signup.setVisible(true);


    }//GEN-LAST:event_jLabel9MouseClicked
public static void main(String args[]) {

    // ✅ تفعيل Nimbus Look & Feel
    try {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (Exception ex) {
        // ✅ لو Nimbus مش موجود، نستخدم الـ default
        System.out.println("Nimbus not available, using default Look & Feel.");
    }

    // ✅ تشغيل صفحة Login
    java.awt.EventQueue.invokeLater(() -> {
        new Login().setVisible(true);
    });
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Login_btnActionPerformed;
    private javax.swing.JLabel Usernamelogin;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPasswordField loginpassword;
    private javax.swing.JLabel passwordlogin;
    private javax.swing.JTextField usernameText;
    // End of variables declaration//GEN-END:variables
}
