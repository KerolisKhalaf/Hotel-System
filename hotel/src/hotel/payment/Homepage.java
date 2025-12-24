package hotel.payment;

import java.sql.*;
import javax.swing.JOptionPane;
import java.text.SimpleDateFormat;
import java.text.ParseException;

// ✅ ربط بالـ Facade
import hotel.payment.HotelFacade;

// ✅ ربط بالـ Managers والـ Models والـ Services (من النسخة الأصلية)
import hotel.model.Customer;
import hotel.model.Room;
import hotel.core.ReservationManager;
import hotel.model.Reservation;
import hotel.services.RoomService;
import hotel.factory.CustomerFactory;
import hotel.factory.RoomFactory;
import hotel.payment.PaymentProcessor;
import hotel.payment.PaymentStrategy;

public class Homepage extends javax.swing.JFrame {

    // ✅ Database URL
    private final String url =
        "jdbc:sqlserver://localhost\\MSSQLSERVER14;databaseName=Hotel;user=sa;password=123456;";

    // ✅ Facade object
    private HotelFacade hotelFacade;

    public Homepage() {
        initComponents();

        // ✅ تحميل Driver
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

    // ✅ Constructor إضافي من شغلك (يمرر Customer)
    Homepage(Customer customer) {
        // هنا تقدر تستخدمي بيانات الـ Customer لو محتاجة تعرضها
        JOptionPane.showMessageDialog(this, "Welcome " + customer.getFirstName());
        initComponents();
        setLocationRelativeTo(null);
    }

    // ✅ Helper: Validate date format (موجود في النسختين)
    private boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        firstname = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        lastname = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        Email = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        phoneNumber = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        start = new javax.swing.JTextField();
        end = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        creditNum = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        creditPassword = new javax.swing.JPasswordField();
        confirmation = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        customerTypeCombo = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        roomType = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(225, 229, 192));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.MatteBorder(null), "Booking information", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Century Schoolbook", 1, 14))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jLabel1.setText("Frist-Name");

        jLabel11.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jLabel11.setText("Last-Name");

        jLabel12.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jLabel12.setText("E-mail");

        jLabel13.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jLabel13.setText("Phone Number");

        jLabel14.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jLabel14.setText("Start-Date");

        jLabel15.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jLabel15.setText("End-Date");

        start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jLabel16.setText("Enter your Credit Card number:");

        jLabel17.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jLabel17.setText("credit card-Password");

        confirmation.setBackground(new java.awt.Color(153, 51, 0));
        confirmation.setFont(new java.awt.Font("Script MT Bold", 0, 12)); // NOI18N
        confirmation.setForeground(new java.awt.Color(255, 255, 255));
        confirmation.setText("Confirm");
        confirmation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmationActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jLabel18.setText("Room Type");

        customerTypeCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "REGULAR", "VIP", "CORPORATE" }));
        customerTypeCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerTypeComboActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jLabel19.setText("Customer Type");

        roomType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Standard", "Deluxe", "Suite" }));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(162, 162, 162)
                                .addComponent(jLabel11))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(customerTypeCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Email, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(start, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(creditNum, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(firstname, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(44, 44, 44)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lastname, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(phoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(end, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(creditPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(roomType, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(182, 182, 182)
                        .addComponent(confirmation, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(firstname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lastname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(phoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(start, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(end, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(creditNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(creditPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(customerTypeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(roomType, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(confirmation)
                        .addContainerGap())))
        );

        jPanel4.setBackground(new java.awt.Color(153, 51, 0));
        jPanel4.setForeground(new java.awt.Color(204, 51, 0));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 741, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
        );

        jPanel5.setBackground(new java.awt.Color(51, 51, 0));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 741, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(169, 169, 169))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Booking", jPanel2);

        jPanel1.setBackground(new java.awt.Color(242, 225, 213));

        jLabel3.setFont(new java.awt.Font("Engravers MT", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 51, 0));
        jLabel3.setText("Choose Your Stay ");

        jLabel4.setFont(new java.awt.Font("Engravers MT", 0, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(153, 51, 0));
        jLabel4.setText("LILIY, A Four Seasons Resort");

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hotel/le.jpg"))); // NOI18N
        jLabel5.setText("jLabel5");

        jTextField1.setBackground(new java.awt.Color(204, 204, 204));
        jTextField1.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jTextField1.setText("CALL US :808-468-6578");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Script MT Bold", 0, 12)); // NOI18N
        jLabel2.setText("On the secluded Hawaiian island ");

        jLabel6.setFont(new java.awt.Font("Script MT Bold", 0, 12)); // NOI18N
        jLabel6.setText(", immerse yourself in healthier living practices and integrate the Sensei paths of Move,");

        jLabel7.setFont(new java.awt.Font("Script MT Bold", 0, 12)); // NOI18N
        jLabel7.setText(" Nourish and Rest into your wellbeing journey. Beginning with a complimentary, ");

        jLabel8.setFont(new java.awt.Font("Script MT Bold", 0, 12)); // NOI18N
        jLabel8.setText(" semi-private flight from Honolulu,");

        jLabel9.setFont(new java.awt.Font("Script MT Bold", 0, 12)); // NOI18N
        jLabel9.setText("every stay includes luxurious accommodations and a rotating collection of daily activities such as yoga, meditation,");

        jLabel10.setFont(new java.awt.Font("Script MT Bold", 0, 12)); // NOI18N
        jLabel10.setText("fitness classes and lectures.");

        jTextField2.setBackground(new java.awt.Color(204, 204, 204));
        jTextField2.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jTextField2.setText("MEET OUR EXPERTS");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 612, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel8))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(160, 160, 160)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(31, 31, 31)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 17, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(73, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Home", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 737, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here: لياسمين هينقله لصفحه اللوجين
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void confirmationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmationActionPerformed
String fname = firstname.getText().trim();
String lname = lastname.getText().trim();
String email = Email.getText().trim();
String phone = phoneNumber.getText().trim();
String sdate = start.getText().trim();
String edate = end.getText().trim();
String creditno = creditNum.getText().trim();
String creditpass = creditPassword.getText().trim();
String selectedType = customerTypeCombo.getSelectedItem().toString();
String selectedRoomType = roomType.getSelectedItem().toString();

// ----------------------------------------------------
// 🔹 شغلك (Validation متقدم + Facade)
// ----------------------------------------------------
if (fname.isEmpty() || !fname.matches("^[A-Za-z]{2,30}$")) {
    JOptionPane.showMessageDialog(this, "Please enter a valid first name (only letters, 2–30 chars)");
    return;
}
if (lname.isEmpty() || !lname.matches("^[A-Za-z]{2,30}$")) {
    JOptionPane.showMessageDialog(this, "Please enter a valid last name (only letters, 2–30 chars)");
    return;
}
if (email.isEmpty() || !email.matches("^[\\w-.]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
    JOptionPane.showMessageDialog(this, "Please enter a valid email address");
    return;
}
if (phone.isEmpty() || !phone.matches("^\\d{10,15}$")) {
    JOptionPane.showMessageDialog(this, "Phone number must be 10–15 digits");
    return;
}
if (sdate.isEmpty() || !isValidDate(sdate)) {
    JOptionPane.showMessageDialog(this, "Start date must be in format yyyy-MM-dd");
    return;
}
if (edate.isEmpty() || !isValidDate(edate)) {
    JOptionPane.showMessageDialog(this, "End date must be in format yyyy-MM-dd");
    return;
}
if (creditno.isEmpty() || !creditno.matches("^\\d{12,19}$")) {
    JOptionPane.showMessageDialog(this, "Credit card number must be 12–19 digits");
    return;
}
if (creditpass.isEmpty() || !creditpass.matches("^[A-Za-z0-9]{4,}$")) {
    JOptionPane.showMessageDialog(this, "Credit password must be at least 4 characters (letters or digits)");
    return;
}
if (sdate.compareTo(edate) >= 0) {
    JOptionPane.showMessageDialog(this, "End date must be after start date");
    return;
}
String today = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
if (sdate.compareTo(today) < 0) {
    JOptionPane.showMessageDialog(this, "Start date cannot be in the past");
    return;
}

Connection con = null;
PreparedStatement pstmt = null;

try {
    // ✅ Factory لإنشاء Customer و Room
    Customer customer = CustomerFactory.createCustomer(fname, lname, email, phone, selectedType);
    Room myRoom = RoomFactory.createRoom(selectedRoomType, "101");

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    java.util.Date startDate = sdf.parse(sdate);
    java.util.Date endDate = sdf.parse(edate);

    // ✅ ReservationManager لإنشاء الحجز
    ReservationManager manager = ReservationManager.getInstance();
    Reservation myRes = manager.createReservation(customer, myRoom, startDate, endDate);

    if (myRes == null) {
        JOptionPane.showMessageDialog(this, "Room not available or invalid dates");
        return;
    }

    double finalPrice = myRes.getTotalPrice();

    // ----------------------------------------------------
    // 🔹 شغلك (Facade بدل SQL مباشر)
    // ----------------------------------------------------
    boolean success = hotelFacade.createReservation(
        fname, lname, email, phone,
        sdate, edate, creditno, creditpass
    );

    // ----------------------------------------------------
    // 🔹 الأصلي (SQL مباشر + INSERT)
    // ----------------------------------------------------
    con = DriverManager.getConnection(url);
    String sql = "INSERT INTO Booking_info (fname, lname, email, phone, sdate, edate, creditno, creditpass, roomType, status, customerType, total_price) "
               + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    pstmt = con.prepareStatement(sql);
    pstmt.setString(1, fname);
    pstmt.setString(2, lname);
    pstmt.setString(3, email);
    pstmt.setString(4, phone);
    pstmt.setString(5, sdate);
    pstmt.setString(6, edate);
    pstmt.setString(7, creditno);
    pstmt.setString(8, creditpass);
    pstmt.setString(9, selectedRoomType);
    pstmt.setString(10, "Active");
    pstmt.setString(11, selectedType);
    pstmt.setDouble(12, finalPrice);

    int affectedRows = pstmt.executeUpdate();

    // ----------------------------------------------------
    // 🔹 PaymentProcessor مع Strategy
    // ----------------------------------------------------
    if (success || affectedRows == 1) {
        PaymentProcessor processor = PaymentProcessor.getInstance();
        processor.setPaymentStrategy(new CreditCardPayment());
        processor.processPayment(myRes.getReservationID(), finalPrice, "Credit Card");

        JOptionPane.showMessageDialog(this, "Success\nRoom: " + selectedRoomType +
            "\nCustomer Type: " + customer.getCustomerType() +
            "\nTotal after Discount: $" + finalPrice);

        // ✅ Reset fields
        firstname.setText("");
        lastname.setText("");
        Email.setText("");
        phoneNumber.setText("");
        start.setText("");
        end.setText("");
        creditNum.setText("");
        creditPassword.setText("");

        this.dispose();
        stuff s = new stuff();
        s.setVisible(true);
    } else {
        JOptionPane.showMessageDialog(this, "Registration failed! Please try again.");
    }

} catch (Exception ex) {
    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
} finally {
    if (con != null) try { con.close(); } catch (Exception ex) {}
    if (pstmt != null) try { pstmt.close(); } catch (Exception ex) {}
}
    }//GEN-LAST:event_confirmationActionPerformed

    private void startActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startActionPerformed
        this.dispose();

        Login log = new Login();
        log.setVisible(true);
    }//GEN-LAST:event_startActionPerformed

    private void customerTypeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerTypeComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customerTypeComboActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Homepage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Homepage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Homepage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Homepage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Homepage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Email;
    private javax.swing.JButton confirmation;
    private javax.swing.JTextField creditNum;
    private javax.swing.JPasswordField creditPassword;
    private javax.swing.JComboBox<String> customerTypeCombo;
    private javax.swing.JTextField end;
    private javax.swing.JTextField firstname;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField lastname;
    private javax.swing.JTextField phoneNumber;
    private javax.swing.JComboBox<String> roomType;
    private javax.swing.JTextField start;
    // End of variables declaration//GEN-END:variables

}
