package hotel.payment;

import hotel.payment.signin;
import hotel.payment.HotelFacade;
import hotel.model.Customer;
import hotel.model.Reservation;
import hotel.model.Room;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

// ✅ إضافات من الأصلي
import hotel.core.DatabaseConnection;
import hotel.core.ReservationManager;
import hotel.factory.CustomerFactory;
import hotel.factory.RoomFactory;
import hotel.payment.PaymentProcessor;
import java.text.ParseException;
import javax.swing.JFrame;

public class stuff extends javax.swing.JFrame {

    // ✅ Facade object
    private HotelFacade hotelFacade;

    // ✅ Managers من الأصلي
    private ReservationManager reservationManager;
    private PaymentProcessor paymentProcessor;
    private DatabaseConnection dbConnection;
    private Object tno;

    public stuff() {
        initComponents();

        // ✅ تحميل Driver
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Cannot Load JDBC Driver!");
        }

        // ✅ Initialize Facade
        hotelFacade = new HotelFacade();

        // ✅ Initialize Managers (من الأصلي)
        try {
            dbConnection = DatabaseConnection.getInstance();
            reservationManager = ReservationManager.getInstance();
            paymentProcessor = PaymentProcessor.getInstance();

            if (dbConnection.isConnected()) {
                System.out.println("Database connected successfully");
            } else {
                System.out.println("Database connection might be unstable");
            }

            System.out.println("ReservationManager initialized");
            System.out.println("PaymentProcessor initialized");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error initializing components: " + ex.getMessage());
            ex.printStackTrace();
        }

        // ✅ Listeners من الأصلي
        jTable1.getSelectionModel().addListSelectionListener(e -> {
            int row = jTable1.getSelectedRow();
            if (row >= 0) {
                txtFname.setText(jTable1.getValueAt(row, 1).toString());
                txtLname.setText(jTable1.getValueAt(row, 2).toString());
                txtPhone.setText(jTable1.getValueAt(row, 3).toString());
                txtEmail.setText(jTable1.getValueAt(row, 4).toString());
            }
        });

        jTable2.getSelectionModel().addListSelectionListener(e -> {
            int row = jTable2.getSelectedRow();
            if (row >= 0) {
                tfname.setText(jTable2.getValueAt(row, 1).toString());
                tlname.setText(jTable2.getValueAt(row, 2).toString());
                phonee.setText(jTable2.getValueAt(row, 3).toString());
                tsdate.setText(jTable2.getValueAt(row, 4).toString());
                tedate.setText(jTable2.getValueAt(row, 5).toString());
            }
        });

        // ✅ تحميل الجداول
        loadDataToTableSQL();      // Users (SQL)
        loadbookingToTableSQL();   // Bookings (SQL)

        loadDataToTableFacade();   // Users (Facade)
        loadbookingToTableFacade(); // Bookings (Facade)
    }

    // ---------------------------------------------------------
    // ✅ Load Users Table (SQL أصلي)
    // ---------------------------------------------------------
    public void loadDataToTableSQL() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        try {
            Connection con = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT id, fname, lname, phone, email FROM Users";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String fname = rs.getString("fname");
                String lname = rs.getString("lname");
                String phone = rs.getString("phone");
                String email = rs.getString("email");

                model.addRow(new Object[]{id, fname, lname, phone, email});
            }

            rs.close();
            pst.close();
            con.close();

            jTable1.getColumnModel().getColumn(0).setMinWidth(0);
            jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
            jTable1.getColumnModel().getColumn(0).setWidth(0);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading users (SQL): " + ex.getMessage());
        }
    }

    // ---------------------------------------------------------
    // ✅ Load Reservations Table (SQL أصلي)
    // ---------------------------------------------------------
    public void loadbookingToTableSQL() {
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0);

//        reservationManager.addObserver(new SMSNotification());
        reservationManager.getAllReservations().clear();

        try (Connection con = DatabaseConnection.getInstance().getConnection()) {
            String sql = "SELECT booking_id, fname, lname, phone, sdate, edate, status, total_price, roomType, customerType FROM Booking_info";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            while (rs.next()) {
                int bID = rs.getInt("booking_id");
                String fname = rs.getString("fname");
                String lname = rs.getString("lname");
                String phone = rs.getString("phone");
                String sdateStr = rs.getString("sdate");
                String edateStr = rs.getString("edate");
                String status = rs.getString("status");
                double price = rs.getDouble("total_price");
                String rType = rs.getString("roomType");
                String cType = rs.getString("customerType");

                try {
                    java.util.Date startDate = sdf.parse(sdateStr);
                    java.util.Date endDate = sdf.parse(edateStr);

                    Customer tempCust = CustomerFactory.createCustomer(fname, lname, "", phone, cType);
                    Room tempRoom = RoomFactory.createRoom(rType, "101");

                    Reservation res = new Reservation(tempCust, tempRoom, startDate, endDate);
                    res.setReservationID(String.valueOf(bID).trim());
                    res.setStatus(status);
                    res.setTotalPrice(price);

                    reservationManager.addReservationDirectly(res);

                } catch (Exception e) {
                    System.out.println("Skip Sync for ID " + bID + ": " + e.getMessage());
                }

                model.addRow(new Object[]{
                    bID, fname, lname, phone, sdateStr, edateStr, status, price
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error Loading Bookings (SQL): " + ex.getMessage());
        }
    }

    // ---------------------------------------------------------
    // ✅ Load Users Table (Facade)
    // ---------------------------------------------------------
    public void loadDataToTableFacade() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        try {
            List<Customer> users = hotelFacade.getAllUsers();

            for (Customer c : users) {
                model.addRow(new Object[]{
                    c.getId(),
                    c.getFirstName(),
                    c.getLastName(),
                    c.getPhone(),
                    c.getEmail()
                });
            }

            jTable1.getColumnModel().getColumn(0).setMinWidth(0);
            jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
            jTable1.getColumnModel().getColumn(0).setWidth(0);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading users (Facade): " + ex.getMessage());
        }
    }

    // ---------------------------------------------------------
    // ✅ Load Reservations Table (Facade)
    // ---------------------------------------------------------
    public void loadbookingToTableFacade() {
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0);

        try {
            List<Reservation> bookings = hotelFacade.getAllReservations();

            for (Reservation r : bookings) {
                model.addRow(new Object[]{
                    r.getId(),
                    r.getFirstName(),
                    r.getLastName(),
                    r.getPhone(),
                    r.getStartDate(),
                    r.getEndDate(),
                    r.getIndividuals()
                });
            }

            jTable2.getColumnModel().getColumn(0).setMinWidth(0);
            jTable2.getColumnModel().getColumn(0).setMaxWidth(0);
            jTable2.getColumnModel().getColumn(0).setWidth(0);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading bookings (Facade): " + ex.getMessage());
        }
    }
     @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        Exit1 = new javax.swing.JButton();
        txtFname1 = new javax.swing.JTextField();
        txtLname1 = new javax.swing.JTextField();
        txtPhone1 = new javax.swing.JTextField();
        txtEmail1 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        Exit = new javax.swing.JButton();
        txtFname = new javax.swing.JTextField();
        txtLname = new javax.swing.JTextField();
        txtPhone = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        tphone = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        tfname = new javax.swing.JTextField();
        tlname = new javax.swing.JTextField();
        tsdate = new javax.swing.JTextField();
        tedate = new javax.swing.JTextField();
        phonee = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        countbtn = new javax.swing.JButton();
        Maxindividuals = new javax.swing.JButton();
        Minindividuals = new javax.swing.JButton();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Room ID ", "Room Type", "Capacity", "Price", " Status "
            }
        ));
        jScrollPane4.setViewportView(jTable4);

        jPanel8.setBackground(new java.awt.Color(179, 209, 178));

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "User-Information", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe Print", 0, 18))); // NOI18N

        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Frist-name", "Last-name", "Phone", "E-mail"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane5.setViewportView(jTable5);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 12, Short.MAX_VALUE))
        );

        jButton4.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jButton4.setText("DELETE");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton9.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jButton9.setText("ADD");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jButton10.setText("Edit");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        Exit1.setBackground(new java.awt.Color(153, 0, 0));
        Exit1.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        Exit1.setForeground(new java.awt.Color(255, 255, 255));
        Exit1.setText("LOG-OUT");
        Exit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Exit1ActionPerformed(evt);
            }
        });

        txtFname1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFname1ActionPerformed(evt);
            }
        });

        txtPhone1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPhone1ActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jLabel16.setText("First Name");

        jLabel17.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jLabel17.setText("Last Name");

        jLabel18.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jLabel18.setText("Phone");

        jLabel19.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jLabel19.setText("E-mail");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel17)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtFname1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtLname1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtEmail1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPhone1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
                        .addComponent(Exit1)
                        .addGap(26, 26, 26))))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFname1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPhone1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel18))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmail1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10)
                    .addComponent(txtLname1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel19))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(122, 122, 122)
                .addComponent(jButton4)
                .addGap(39, 39, 39)
                .addComponent(jButton9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Exit1)
                .addGap(70, 70, 70))
        );

        jTable6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane6.setViewportView(jTable6);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(179, 209, 178));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "User-Information", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe Print", 0, 18))); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Frist-name", "Last-name", "Phone", "E-mail"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 12, Short.MAX_VALUE))
        );

        jButton1.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jButton1.setText("DELETE");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jButton2.setText("ADD");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jButton3.setText("Edit");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        Exit.setBackground(new java.awt.Color(153, 0, 0));
        Exit.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        Exit.setForeground(new java.awt.Color(255, 255, 255));
        Exit.setText("LOG-OUT");
        Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitActionPerformed(evt);
            }
        });

        txtFname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFnameActionPerformed(evt);
            }
        });

        txtPhone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPhoneActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jLabel1.setText("First Name");

        jLabel2.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jLabel2.setText("Last Name");

        jLabel3.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jLabel3.setText("Phone");

        jLabel4.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jLabel4.setText("E-mail");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtFname, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtLname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                        .addComponent(Exit)
                        .addGap(26, 26, 26))))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtEmail, txtFname, txtLname, txtPhone});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel3, jLabel4});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3)
                    .addComponent(txtLname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(122, 122, 122)
                .addComponent(jButton1)
                .addGap(39, 39, 39)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Exit)
                .addGap(70, 70, 70))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {txtEmail, txtFname, txtLname, txtPhone});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel3, jLabel4});

        jTabbedPane1.addTab("USER-INFO", jPanel2);

        tphone.setBackground(new java.awt.Color(221, 215, 169));

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "User-Information", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe Print", 0, 18))); // NOI18N

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Frist-name", "Last-name", "Phone", "Start Date", "End Date", "Status", "Total Price"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setMinWidth(1);
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(100);
            jTable2.getColumnModel().getColumn(0).setMaxWidth(1);
            jTable2.getColumnModel().getColumn(6).setMinWidth(66);
            jTable2.getColumnModel().getColumn(6).setPreferredWidth(100);
            jTable2.getColumnModel().getColumn(6).setMaxWidth(66);
        }

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 673, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton5.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jButton5.setText("DELETE");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jButton6.setText("ADD");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jButton7.setText("Edit");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(153, 0, 0));
        jButton8.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("LOG-OUT");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        tfname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfnameActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jLabel5.setText("First Name");

        jLabel6.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jLabel6.setText("Start Date");

        jLabel7.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jLabel7.setText("Last Name");

        jLabel8.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jLabel8.setText("End Date");

        jLabel9.setFont(new java.awt.Font("Script MT Bold", 0, 14)); // NOI18N
        jLabel9.setText("Phone");

        javax.swing.GroupLayout tphoneLayout = new javax.swing.GroupLayout(tphone);
        tphone.setLayout(tphoneLayout);
        tphoneLayout.setHorizontalGroup(
            tphoneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tphoneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tphoneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tphoneLayout.createSequentialGroup()
                        .addGroup(tphoneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGroup(tphoneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tphoneLayout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(tfname, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tlname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(47, 47, 47)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(phonee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(tphoneLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tsdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tedate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(tphoneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tphoneLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(tphoneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                            .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(tphoneLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(tphoneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton8))))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        tphoneLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {phonee, tedate, tfname, tlname, tsdate});

        tphoneLayout.setVerticalGroup(
            tphoneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tphoneLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(tphoneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tlname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(phonee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(tphoneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tsdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tedate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel6))
                .addGap(25, 25, 25))
            .addGroup(tphoneLayout.createSequentialGroup()
                .addGap(122, 122, 122)
                .addComponent(jButton5)
                .addGap(39, 39, 39)
                .addComponent(jButton6)
                .addGap(86, 86, 86)
                .addComponent(jButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton7)
                .addGap(61, 61, 61))
        );

        tphoneLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {phonee, tedate, tfname, tlname, tsdate});

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(tphone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tphone, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Booking", jPanel3);

        jPanel6.setBackground(new java.awt.Color(204, 204, 255));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hotel/download.jpeg"))); // NOI18N
        jLabel11.setText("jLabel11");

        jLabel13.setFont(new java.awt.Font("Script MT Bold", 1, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 102, 102));
        jLabel13.setText("Number of guests");

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hotel/download.jpeg"))); // NOI18N
        jLabel12.setText("jLabel12");

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hotel/download.jpeg"))); // NOI18N
        jLabel14.setText("jLabel12");

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hotel/download.jpeg"))); // NOI18N
        jLabel15.setText("jLabel15");

        countbtn.setFont(new java.awt.Font("Segoe Print", 0, 18)); // NOI18N
        countbtn.setText("Count");
        countbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                countbtnActionPerformed(evt);
            }
        });

        Maxindividuals.setFont(new java.awt.Font("Segoe Print", 0, 18)); // NOI18N
        Maxindividuals.setText("Max Individulas");
        Maxindividuals.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MaxindividualsActionPerformed(evt);
            }
        });

        Minindividuals.setFont(new java.awt.Font("Segoe Print", 0, 18)); // NOI18N
        Minindividuals.setText("Min Individuals");
        Minindividuals.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MinindividualsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(countbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Maxindividuals, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Minindividuals, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(93, 93, 93)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2682, 2682, 2682)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)))
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(80, 80, 80)
                .addComponent(countbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(Maxindividuals, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(Minindividuals, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(138, Short.MAX_VALUE))
            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 859, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Guests", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 863, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 543, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        loadbookingToTableSQL();
        loadbookingToTableSQL();
        loadDataToTableFacade();
        loadbookingToTableFacade();
      
    }//GEN-LAST:event_formWindowOpened

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton10ActionPerformed

    private void Exit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Exit1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Exit1ActionPerformed

    private void txtFname1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFname1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFname1ActionPerformed

    private void txtPhone1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPhone1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhone1ActionPerformed

    private void MinindividualsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MinindividualsActionPerformed

        try {
            // ✅ استدعاء الـ Facade للحصول على أقل قيمة
            int minValue = hotelFacade.getMinIndividuals();

            if (minValue > 0) {
                JOptionPane.showMessageDialog(this,
                    "Minimum individuals number = " + minValue);
            } else {
                JOptionPane.showMessageDialog(this,
                    "No reservation data found.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Unexpected error occurred: " + ex.getMessage());
        }
    }//GEN-LAST:event_MinindividualsActionPerformed

    private void MaxindividualsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MaxindividualsActionPerformed

        try {
            // ✅ استدعاء الـ Facade للحصول على أعلى قيمة
            int maxValue = hotelFacade.getMaxIndividuals();

            if (maxValue > 0) {
                JOptionPane.showMessageDialog(this,
                    "Maximum individuals number = " + maxValue);
            } else {
                JOptionPane.showMessageDialog(this,
                    "No reservation data found.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Unexpected error occurred: " + ex.getMessage());
        }
    }//GEN-LAST:event_MaxindividualsActionPerformed

    private void countbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_countbtnActionPerformed
                                      
    // ============================
    // 🔹 كودك (Facade)
    // ============================
    try {
        String date = JOptionPane.showInputDialog(this, "Enter a date (YYYY-MM-DD):");

        if (date == null || date.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid date.");
            return;
        }

        date = date.trim();

        if (!date.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            JOptionPane.showMessageDialog(this, "Date must be in format YYYY-MM-DD.");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);

        try {
            sdf.parse(date);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date. Please enter a real date.");
            return;
        }

        int count = hotelFacade.countBookingsByDate(date);

        if (count > 0) {
            JOptionPane.showMessageDialog(this, "Bookings on " + date + ": " + count);
        } else {
            JOptionPane.showMessageDialog(this, "No bookings on this date.");
        }

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Unexpected error occurred: " + ex.getMessage());
    }

    // ============================
    // 🔹 الأصلي (SQL) — زي ما هو
    // ============================
    String dateSQL = JOptionPane.showInputDialog(this, "enter a date (YYYY-MM-DD):");
    if (dateSQL == null || dateSQL.isEmpty()) {
        JOptionPane.showMessageDialog(this, "please enter a valid date");
        return;
    }
    Connection con = null;
    Statement stmt = null;
    ResultSet result = null;

    try {
        con = DatabaseConnection.getInstance().getConnection();
        stmt = con.createStatement();
        String query = "SELECT COUNT(*) AS booking_count FROM Booking_info WHERE sdate = '" + dateSQL + "'";
        result = stmt.executeQuery(query);
        if (result.next()) {
            JOptionPane.showMessageDialog(this, "Bookings on " + dateSQL + ":" + result.getInt("booking_count"));
        } else {
            JOptionPane.showMessageDialog(this, "No booking on this date");
        }

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    } finally {
        try { if (result != null) result.close(); } catch (Exception e) {}
        try { if (stmt != null) stmt.close(); } catch (Exception e) {}
    }

    }//GEN-LAST:event_countbtnActionPerformed

    private void tfnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfnameActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        try {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit the application?",
                "Exit Confirmation",
                JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Unexpected error while exiting: " + ex.getMessage());
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed

    DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
    int selectedRow = jTable2.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Select a booking");
        return;
    }

    // ---------------------------------------------------------
    // 🔹 كودك (Facade) مع Validation
    // ---------------------------------------------------------
    try {
        int booking_id = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());

        String fname = tfname.getText().trim();
        String lname = tlname.getText().trim();
        String phone = phonee.getText().trim();
        String sdate = tsdate.getText().trim();
        String edate = tedate.getText().trim();

        if (fname.isEmpty() || lname.isEmpty() || phone.isEmpty() ||
            sdate.isEmpty() || edate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        if (!fname.matches("^[A-Za-z]{2,30}$")) {
            JOptionPane.showMessageDialog(this, "First name must contain only letters (2–30 characters).");
            return;
        }

        if (!lname.matches("^[A-Za-z]{2,30}$")) {
            JOptionPane.showMessageDialog(this, "Last name must contain only letters (2–30 characters).");
            return;
        }

        if (!phone.matches("^\\d{10,15}$")) {
            JOptionPane.showMessageDialog(this, "Phone number must be 10–15 digits.");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);

        if (!isValidDate(sdate, sdf)) {
            JOptionPane.showMessageDialog(this, "Invalid start date (must be yyyy-MM-dd).");
            return;
        }

        if (!isValidDate(edate, sdf)) {
            JOptionPane.showMessageDialog(this, "Invalid end date (must be yyyy-MM-dd).");
            return;
        }

        boolean updated = hotelFacade.updateReservation(
            booking_id, fname, lname, phone, sdate, edate
        );

        if (updated) {
            JOptionPane.showMessageDialog(this, "Reservation updated successfully.");

            model.setValueAt(fname, selectedRow, 1);
            model.setValueAt(lname, selectedRow, 2);
            model.setValueAt(phone, selectedRow, 3);
            model.setValueAt(sdate, selectedRow, 4);
            model.setValueAt(edate, selectedRow, 5);

        } else {
            JOptionPane.showMessageDialog(this, "Update failed. Please check the data.");
        }

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Unexpected error occurred: " + ex.getMessage());
    }

    // ---------------------------------------------------------
    // 🔹 الأصلي (SQL + ReservationManager) — زي ما هو
    // ---------------------------------------------------------
    try {
        String bookingID = model.getValueAt(selectedRow, 0).toString();
        String sdateStr = tsdate.getText().trim();
        String edateStr = tedate.getText().trim();

        if (sdateStr.isEmpty() || edateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill in dates");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date newStartDate = sdf.parse(sdateStr);
        java.util.Date newEndDate = sdf.parse(edateStr);

        Connection con = DatabaseConnection.getInstance().getConnection();

        String selectSql = "SELECT fname, lname, phone, roomType, customerType FROM dbo.Booking_info WHERE booking_id = ?";
        PreparedStatement selectPst = con.prepareStatement(selectSql);
        selectPst.setInt(1, Integer.parseInt(bookingID));

        ResultSet rs = selectPst.executeQuery();

        if (!rs.next()) {
            JOptionPane.showMessageDialog(this, "Booking not found");
            return;
        }

        String fname = rs.getString("fname");
        String lname = rs.getString("lname");
        String phone = rs.getString("phone");
        String savedRoomType = rs.getString("roomType");
        String savedCustomerType = rs.getString("customerType");

        rs.close();
        selectPst.close();

        Customer customer = CustomerFactory.createCustomer(fname, lname, "", phone, savedCustomerType);
        Room room = RoomFactory.createRoom(savedRoomType, "101");

        if (customer == null || room == null) {
            JOptionPane.showMessageDialog(this, "Error: Failed to create objects");
            return;
        }

        Reservation tempRes = new Reservation(customer, room, newStartDate, newEndDate);
        double newTotalPrice = tempRes.getTotalPrice();

        ReservationManager manager = ReservationManager.getInstance();
        boolean logicSuccess = manager.modifyReservation(bookingID, newStartDate, newEndDate);

        if (!logicSuccess) {
            JOptionPane.showMessageDialog(this, "Invalid dates or reservation already started");
            return;
        }

        String updateSql = "UPDATE dbo.Booking_info SET sdate = ?, edate = ?, total_price = ?, status = 'MODIFIED' WHERE booking_id = ?";
        PreparedStatement updatePst = con.prepareStatement(updateSql);

        updatePst.setString(1, sdateStr);
        updatePst.setString(2, edateStr);
        updatePst.setDouble(3, newTotalPrice);
        updatePst.setInt(4, Integer.parseInt(bookingID));

        int affected = updatePst.executeUpdate();

        if (affected > 0) {
            model.setValueAt(sdateStr, selectedRow, 4);
            model.setValueAt(edateStr, selectedRow, 5);

            tsdate.setText("");
            tedate.setText("");

            JOptionPane.showMessageDialog(this,
                "✅ UPDATED!\n\n" +
                "Guest: " + fname + " " + lname + "\n" +
                "New Dates: " + sdateStr + " to " + edateStr + "\n" +
                "NEW PRICE: $" + String.format("%.2f", newTotalPrice));
        }

        updatePst.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        e.printStackTrace();
    }
}

// ✅ دالة صغيرة للتحقق من التاريخ (من كودك)
private boolean isValidDate(String date, SimpleDateFormat sdf) {
    try {
        sdf.parse(date);
        return true;
    } catch (ParseException e) {
        return false;
    }

    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {
            // ✅ إغلاق صفحة الـ Stuff الحالية
            this.dispose();

            // ✅ فتح صفحة تسجيل الدخول
            signin signup = new signin();
            signup.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error while returning to Sign In page: " + ex.getMessage());
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

    DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
    int selectedRow = jTable2.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a reservation to delete/cancel.");
        return;
    }

    // ---------------------------------------------------------
    // 🔹 كودك (Facade)
    // ---------------------------------------------------------
    try {
        int id = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());

        int confirmFacade = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this reservation?",
            "Delete Confirmation",
            JOptionPane.YES_NO_OPTION
        );

        if (confirmFacade == JOptionPane.YES_OPTION) {
            boolean deleted = hotelFacade.deleteReservation(id);

            if (deleted) {
                JOptionPane.showMessageDialog(this, "Reservation deleted successfully (Facade).");
                model.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Reservation not found in the database (Facade).");
            }
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Unexpected error (Facade): " + ex.getMessage());
    }

    // ---------------------------------------------------------
    // 🔹 الأصلي (SQL + ReservationManager) — زي ما هو
    // ---------------------------------------------------------
    String reservationID = model.getValueAt(selectedRow, 0).toString();

    int confirmSQL = JOptionPane.showConfirmDialog(this, "Confirm cancellation?", "Confirm", JOptionPane.YES_NO_OPTION);
    if (confirmSQL != JOptionPane.YES_OPTION) return;

    try {
        boolean logicSuccess = ReservationManager.getInstance().cancelReservation(reservationID);

        Connection con = DatabaseConnection.getInstance().getConnection();
        String sql = "UPDATE Booking_info SET status = 'CANCELLED' WHERE booking_id = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, reservationID);

        int affected = pst.executeUpdate();
        if (affected > 0) {
            model.setValueAt("CANCELLED", selectedRow, 7);
            JOptionPane.showMessageDialog(this, "Reservation cancelled successfully (SQL).");
        }
        pst.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error (SQL): " + e.getMessage());
    }

    }//GEN-LAST:event_jButton5ActionPerformed

    private void txtPhoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPhoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhoneActionPerformed

    private void txtFnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFnameActionPerformed

    private void ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitActionPerformed
       JFrame frame = new JFrame("Exit");
        if (JOptionPane.showConfirmDialog(frame, "Confirm if you want to leave ", "My Contacts ",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_ExitActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
  try {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

        // ✅ التأكد من اختيار صف
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to update.");
            return;
        }

        // ✅ قراءة الـ ID
        int id;
        try {
            id = Integer.parseInt(jTable1.getValueAt(selectedRow, 0).toString());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID format.");
            return;
        }

        // ✅ قراءة البيانات من الـ TextFields
        String fname = txtFname.getText().trim();
        String lname = txtLname.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();

        // ✅ VALIDATION
        if (fname.isEmpty() || lname.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        if (!fname.matches("[a-zA-Z]+")) {
            JOptionPane.showMessageDialog(this, "First name must contain only letters.");
            return;
        }

        if (!lname.matches("[a-zA-Z]+")) {
            JOptionPane.showMessageDialog(this, "Last name must contain only letters.");
            return;
        }

        if (!phone.matches("\\d{7,15}")) {
            JOptionPane.showMessageDialog(this, "Phone number must contain only digits (7-15 digits).");
            return;
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            JOptionPane.showMessageDialog(this, "Invalid email format.");
            return;
        }

        // ✅ UPDATE باستخدام الـ Facade
        boolean updated = hotelFacade.updateUser(id, fname, lname, phone, email);

        if (updated) {
            JOptionPane.showMessageDialog(this, "User updated successfully.");
            model.setValueAt(fname, selectedRow, 1);
            model.setValueAt(lname, selectedRow, 2);
            model.setValueAt(phone, selectedRow, 3);
            model.setValueAt(email, selectedRow, 4);
        } else {
            JOptionPane.showMessageDialog(this, "Update failed. Please check the data.");
        }

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Unexpected error occurred: " + ex.getMessage());
    }

    // ---------------------------------------------------------
    // ✅ الأصلي (تعديل الحجز)
    // ---------------------------------------------------------
    try {
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel(); 
        int selectedRow = jTable2.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a reservation to modify.");
            return;
        }

        String reservationID = model.getValueAt(selectedRow, 0).toString(); 
        String newSDateStr = tsdate.getText().trim();
        String newEDateStr = tedate.getText().trim();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date newStart = sdf.parse(newSDateStr);
        java.util.Date newEnd = sdf.parse(newEDateStr);

        ReservationManager manager = ReservationManager.getInstance();
        boolean logicSuccess = manager.modifyReservation(reservationID, newStart, newEnd);

        if (logicSuccess) {
            Reservation updatedRes = manager.getReservation(reservationID);
            double updatedTotal = updatedRes.getTotalPrice();

            Connection con = DatabaseConnection.getInstance().getConnection();
            String sql = "UPDATE Booking_info SET sdate = ?, edate = ?, total_price = ?, status = 'MODIFIED' WHERE booking_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, newSDateStr);
            pst.setString(2, newEDateStr);
            pst.setDouble(3, updatedTotal);
            pst.setString(4, reservationID);

            int affected = pst.executeUpdate();

            if (affected > 0) {
                model.setValueAt(newSDateStr, selectedRow, 4); 
                model.setValueAt(newEDateStr, selectedRow, 5);
                model.setValueAt(updatedTotal, selectedRow, 7); 
                model.setValueAt("MODIFIED", selectedRow, 8);

                JOptionPane.showMessageDialog(this, "Reservation modified successfully. New Price: $" + updatedTotal);
            }
            pst.close();
            con.close();
        } else {
            JOptionPane.showMessageDialog(this, "Update failed: Check room availability or if reservation has already started.");
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        try {
            // ✅ إغلاق صفحة Stuff الحالية
            this.dispose();

            // ✅ فتح صفحة Signin
            signin signup = new signin();
            signup.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error while opening Sign In page: " + ex.getMessage());
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    int selectedRow = jTable1.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a user to delete.");
        return;
    }

    // ---------------------------------------------------------
    // 🔹 كودك (Facade)
    // ---------------------------------------------------------
    try {
        int id = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());

        int confirmFacade = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this user?",
            "Delete Confirmation",
            JOptionPane.YES_NO_OPTION
        );

        if (confirmFacade == JOptionPane.YES_OPTION) {
            boolean deleted = hotelFacade.deleteUser(id);

            if (deleted) {
                JOptionPane.showMessageDialog(this, "User deleted successfully (Facade).");
                model.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "User not found in the database (Facade).");
            }
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Unexpected error (Facade): " + ex.getMessage());
    }

    // ---------------------------------------------------------
    // 🔹 الأصلي (SQL) — زي ما هو
    // ---------------------------------------------------------
    int id = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());

    int confirmSQL = JOptionPane.showConfirmDialog(this, "Are you sure?", "Confirm", JOptionPane.YES_NO_OPTION);
    if (confirmSQL != JOptionPane.YES_OPTION) {
        return;
    }

    try {
        Connection con = DatabaseConnection.getInstance().getConnection();
        String sql = "DELETE FROM Users WHERE booking_id = ?"; // الأصلي زي ما هو
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, id);

        int affected = pst.executeUpdate();
        if (affected > 0) {
            JOptionPane.showMessageDialog(this, "Deleted successfully (SQL).");
            model.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "The row was not found in the database (SQL).");
        }

        pst.close();
        con.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error (SQL): " + e.getMessage());
    }
    }//GEN-LAST:event_jButton1ActionPerformed
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
        java.util.logging.Logger.getLogger(stuff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
        java.util.logging.Logger.getLogger(stuff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
        java.util.logging.Logger.getLogger(stuff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
        java.util.logging.Logger.getLogger(stuff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            new stuff().setVisible(true);
        }
    });
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Exit;
    private javax.swing.JButton Exit1;
    private javax.swing.JButton Maxindividuals;
    private javax.swing.JButton Minindividuals;
    private javax.swing.JButton countbtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTable6;
    private javax.swing.JTextField phonee;
    private javax.swing.JTextField tedate;
    private javax.swing.JTextField tfname;
    private javax.swing.JTextField tlname;
    private javax.swing.JPanel tphone;
    private javax.swing.JTextField tsdate;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtEmail1;
    private javax.swing.JTextField txtFname;
    private javax.swing.JTextField txtFname1;
    private javax.swing.JTextField txtLname;
    private javax.swing.JTextField txtLname1;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtPhone1;
    // End of variables declaration//GEN-END:variables

    private void loadDataToTable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void loadbookingToTable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
