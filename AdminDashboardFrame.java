/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package midproject1;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

// Import DAO classes
import midproject1.dao.BookDao;
import midproject1.dao.UserDao;
import midproject1.dao.TaskDao;

/**
 * AdminDashboardFrame for managing users, books, and tasks.
 * @author asus
 */
public class AdminDashboardFrame extends javax.swing.JFrame {
    private Admin admin;
    private RegisterFrame registerFrame;  // RegisterFrame

    // DAO instances for database interaction
    private UserDao userDao;
    private BookDao bookDao;
    private TaskDao taskDao;

    // Default constructor
    public AdminDashboardFrame() {
        // You might want to get the admin user from the database here,
        // or ensure a default admin exists and is retrieved.
        // For now, keeping the existing logic.
        this(new Admin("rumeysa", "rumeysa123")); 
    }

    public AdminDashboardFrame(Admin admin) {
        this.admin = admin;
        
        initializeDaos(); // Initialize DAO instances
        initComponents();

        // Fill the author info table with data from the database
        fillAuthorTable();

        // Fill the registered users table with data from the database
        fillRegisteredUsersTable();
        
        // Fill the task assignment combobox with users from the database
        fillTaskerBox();
    }

    /**
     * Initializes DAO instances for database operations.
     */
    private void initializeDaos() {
        userDao = new UserDao();
        bookDao = new BookDao();
        taskDao = new TaskDao();
    }

    // Bu metodu RegisterFrame içinde çağırmak için kullanacağız (This method will be used to call from RegisterFrame)
    public void setRegisterFrame(RegisterFrame registerFrame) {
        this.registerFrame = registerFrame;
    }

    /**
     * Displays login logs. This currently uses an in-memory manager.
     */
    private void showLoginLogs() {
        // Get the login logs from LoginLogManager (assumed to be an in-memory list)
        List<LoginLog> loginLogs = LoginLogManager.getLoginLogs();

        // Format the logs as a string
        StringBuilder logs = new StringBuilder("Login Logs:\n");
        if (loginLogs.isEmpty()) {
            logs.append("No login logs available.");
        } else {
            for (LoginLog log : loginLogs) {
                logs.append("Username: ").append(log.getUsername())
                    .append(" - Login Time: ").append(log.getLoginTime())
                    .append("\n");
            }
        }

        // Show the logs in an OptionPane
        JOptionPane.showMessageDialog(null, logs.toString(), "Login Logs", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Fills the author information table (tblAuthorInfo) with book data from the database.
     */
    private void fillAuthorTable() {
        DefaultTableModel model = (DefaultTableModel) tblAuthorInfo.getModel();
        model.setRowCount(0); // Clear the table
        // Disable direct cell editing in the table
        tblAuthorInfo.setDefaultEditor(Object.class, null); 

        // Fetch all books from the database
        List<Book> books = bookDao.findAllBooks();

        for (Book bookItem : books) {
            Author author = bookItem.getAuthor();
            // Ensure author is not null before trying to access its properties
            String authorUsername = (author != null) ? author.getUsername() : "N/A";
            String authorPerformance = (author != null) ? author.getPerformanceLevel() : "N/A";

            model.addRow(new Object[]{
                authorUsername,
                authorPerformance,
                bookItem.getTitle(),
                bookItem.getCategory(),
                bookItem.getId() // Add book ID for easier retrieval later
            });
        }
        
        // Remove existing mouse listeners to prevent multiple listeners being added
        for (java.awt.event.MouseListener listener : tblAuthorInfo.getMouseListeners()) {
            if (listener instanceof java.awt.event.MouseAdapter) {
                tblAuthorInfo.removeMouseListener(listener);
            }
        }

        // Add a mouse listener for double-clicking on table rows to view book details
        tblAuthorInfo.addMouseListener(new java.awt.event.MouseAdapter() {
            private int doubleClicked = 0; // Local counter for double-click

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Detect double click
                    int row = tblAuthorInfo.rowAtPoint(evt.getPoint());
                    if (row >= 0) {
                        try {
                            // Get Book ID from the table (assuming it's in the last column now)
                            int bookId = (int) tblAuthorInfo.getValueAt(row, 4); 
                            Book selectedBook = bookDao.findBookById(bookId);

                            if (selectedBook != null) {
                                dispose(); // Close current frame
                                // Open BookdetailsFrame with the selected book
                                new BookdetailsFrame(selectedBook).setVisible(true);
                            } else {
                                JOptionPane.showMessageDialog(null, "Book not found in database.");
                            }
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Error retrieving book details: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    /**
     * Fills the registered users table (tblRegisteredUsers) with user data from the database.
     * Skips admin users.
     */
    public static void fillRegisteredUsersTable() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"ID", "Username", "Email", "Role"}); // Added ID column

        // Create a new UserDao instance within this static method if it's truly static,
        // or pass it as a parameter if possible. For simplicity here, instantiating.
        // NOTE: Instantiating DAOs repeatedly can be inefficient. Consider passing it.
        UserDao staticUserDao = new UserDao(); 
        
        // Iterate through all users from the database
        List<User> users = staticUserDao.findAll(); // Fetch all users using DAO

        for (User user : users) {
            // Skip the admin user (assuming role "admin")
            if (!"admin".equals(user.getRole())) {
                model.addRow(new Object[]{
                    user.getId(), // Add User ID
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole()
                });
            }
        }

        // Set the model for the table
        tblRegisteredUsers.setModel(model);
    }

    /**
     * Fills the user type combobox for task assignment (cbUserTypeTask) with Authors and Editors from the database.
     */
    private void fillTaskerBox() {
        cbUserTypeTask.removeAllItems();

        // Fetch all users from the database
        List<User> allUsers = userDao.findAll();

        for (User user : allUsers) {
            // Only add users with roles "author" or "editor" to the task assignment combobox
            if ("author".equals(user.getRole()) || "editor".equals(user.getRole())) {
                cbUserTypeTask.addItem(user); // User objects are added directly
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btnViewLoginLogs = new javax.swing.JButton();
        btnManageBooks = new javax.swing.JButton();
        btnAssignTask = new javax.swing.JButton();
        cbCategory = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAuthorInfo = new javax.swing.JTable();
        btnGenerateReport = new javax.swing.JButton();
        btnLogOut = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtTaskNamee = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cbUserTypeTask = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        txtDueDatee = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblRegisteredUsers = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Category : ");

        jLabel1.setBackground(new java.awt.Color(255, 255, 204));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setText("Book Publishing System -Admin");

        btnViewLoginLogs.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnViewLoginLogs.setForeground(new java.awt.Color(0, 0, 0));
        btnViewLoginLogs.setText("View Login Logs");
        btnViewLoginLogs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewLoginLogsActionPerformed(evt);
            }
        });

        btnManageBooks.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnManageBooks.setForeground(new java.awt.Color(0, 0, 0));
        btnManageBooks.setText("Manage Books");
        btnManageBooks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManageBooksActionPerformed(evt);
            }
        });

        btnAssignTask.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAssignTask.setForeground(new java.awt.Color(0, 0, 0));
        btnAssignTask.setText("Assign Task ");
        btnAssignTask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAssignTaskActionPerformed(evt);
            }
        });

        cbCategory.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cbCategory.setForeground(new java.awt.Color(51, 51, 51));
        cbCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Fiction", "Non-Fiction", "Science", "Biography", "Fantasy", "Mystery", "Romance", "Thriller", "History", "Philosophy", "Poetry", "Self-Help", "Health", "Cookbooks", "Travel", "Children's Books", "Art & Photography", "Science Fiction", "Religion", "Technology" }));
        cbCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbCategoryActionPerformed(evt);
            }
        });

        tblAuthorInfo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tblAuthorInfo.setForeground(new java.awt.Color(51, 51, 51));
        tblAuthorInfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Author Name ", "Author Performance ", "Book Title", "Book Category"
            }
        ));
        jScrollPane1.setViewportView(tblAuthorInfo);

        btnGenerateReport.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnGenerateReport.setForeground(new java.awt.Color(0, 0, 0));
        btnGenerateReport.setText("Generate Report ");
        btnGenerateReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateReportActionPerformed(evt);
            }
        });

        btnLogOut.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLogOut.setForeground(new java.awt.Color(255, 0, 51));
        btnLogOut.setText("Log out ");
        btnLogOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogOutActionPerformed(evt);
            }
        });

        btnRefresh.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnRefresh.setForeground(new java.awt.Color(0, 153, 51));
        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Task Name:");

        txtTaskNamee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTaskNameeActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Assign To :");

        fillTaskerBox();
        cbUserTypeTask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbUserTypeTaskActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Due Date :");

        txtDueDatee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDueDateeActionPerformed(evt);
            }
        });

        tblRegisteredUsers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Username", "Email", "Role"
            }
        ));
        jScrollPane2.setViewportView(tblRegisteredUsers);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Registered Users");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnLogOut, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(85, 85, 85))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnViewLoginLogs)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnManageBooks, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnGenerateReport)
                            .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(215, 215, 215)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtTaskNamee, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cbUserTypeTask, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtDueDatee, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel5)
                                        .addComponent(btnAssignTask, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(163, 163, 163)
                                    .addComponent(jLabel2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(cbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(18, 18, 18)))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(76, 76, 76)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnViewLoginLogs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnManageBooks, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnGenerateReport, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTaskNamee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDueDatee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbUserTypeTask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAssignTask, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLogOut)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewLoginLogsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewLoginLogsActionPerformed
    // Get login logs from LoginLogManager
    List<LoginLog> loginLogs = LoginLogManager.getLoginLogs(); 
     // Combine the logs using a StringBuilder
    StringBuilder logs = new StringBuilder("Login Logs:\n");
    for (LoginLog log : loginLogs) {
        logs.append("Username: ").append(log.getUsername())
             .append(" - Login Time: ").append(log.getLoginTime())
             .append("\n");
    }

    // Display the logs in a JOptionPane
    JOptionPane.showMessageDialog(null, logs.toString(), "Login Logs", JOptionPane.INFORMATION_MESSAGE); 
    showLoginLogs();
    }//GEN-LAST:event_btnViewLoginLogsActionPerformed

    private void btnManageBooksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnManageBooksActionPerformed
         BookdetailsFrame bookDetailsFrame = new BookdetailsFrame(admin);
    bookDetailsFrame.setVisible(true);
    this.setVisible(false);
    }//GEN-LAST:event_btnManageBooksActionPerformed

    private void btnAssignTaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAssignTaskActionPerformed
      // Get input values from the form
        String taskName = txtTaskNamee.getText().trim();
        User assignTo = (User) cbUserTypeTask.getSelectedItem(); // The selected User object
        String dueDateStr = txtDueDatee.getText().trim();  // Due date as string

        // Check if any field is empty
        if (taskName.isEmpty() || assignTo == null || dueDateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!");
            return;
        }

        // Regex to validate the date format (YYYY-MM-DD)
        String regex = "\\d{4}-\\d{2}-\\d{2}";
        if (!dueDateStr.matches(regex)) {
            JOptionPane.showMessageDialog(this, "Invalid date format! Please use YYYY-MM-DD.");
            return;
        }

        LocalDate parsedDate;
        try {
            // Parse the date in YYYY-MM-DD format
            parsedDate = LocalDate.parse(dueDateStr);

            LocalDate minDate = LocalDate.now().plusDays(7);
            if (parsedDate.isBefore(minDate)) {
                JOptionPane.showMessageDialog(this, "You cannot assign a task with a duration of less than 7 days.");
                return;
            }

            // Check if the selected user already has a task on the same date (from database)
            List<Task> existingTasksForUser = taskDao.findByAssignedToAndDueDate(assignTo.getId(), parsedDate);
            if (!existingTasksForUser.isEmpty()) {
                JOptionPane.showMessageDialog(this, "This " + assignTo.getUsername().toLowerCase() + " already has a task on the selected date!");
                return;
            }

            // Get selected book from tblAuthorInfo
            int selectedRow = tblAuthorInfo.getSelectedRow();
            String bookTitle = null;
            if (selectedRow != -1) {
                // Assuming Book Title is at column index 2 in tblAuthorInfo
                bookTitle = (String) tblAuthorInfo.getValueAt(selectedRow, 2); 
            } else {
                JOptionPane.showMessageDialog(this, "Please select a book from the 'Author Info' table to associate with the task.");
                return;
            }
            
            System.out.println(taskName + " " + bookTitle);
            
            // Create a new Task object
            Task newTask = new Task(
                taskName,
                bookTitle, // Book title from the selected row
                parsedDate,
                assignTo.getId(),          // assignedTo (int user ID)
                assignTo.getUsername()     // assignedUsername (String for display)
            );
            
            // Save the new task to the database
            taskDao.save(newTask);

            JOptionPane.showMessageDialog(this, "Task assigned successfully!");

            // Clear input fields after successful assignment
            txtTaskNamee.setText("");
            txtDueDatee.setText("");
            cbUserTypeTask.setSelectedIndex(-1); // Reset combobox selection

          
            ViewAllTaskFrame viewAllTaskFrame = new ViewAllTaskFrame(admin); // Assuming admin object is needed
            viewAllTaskFrame.setVisible(true);
            this.dispose();

        } catch (java.time.format.DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format! Please use YYYY-MM-DD.");
        } catch (Exception e) {
            System.out.println("Error assigning task: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Something went wrong when trying to assign a task! Error: " + e.getMessage());
        }
    }//GEN-LAST:event_btnAssignTaskActionPerformed

    private void cbCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCategoryActionPerformed
        // Get the selected category from the ComboBox
        String selectedCategory = (String) cbCategory.getSelectedItem();

        // Get the table model
        DefaultTableModel model = (DefaultTableModel) tblAuthorInfo.getModel();
        model.setRowCount(0); // Clear the table

        // Filter and add only books with the selected category from the database
        List<Book> allBooks = bookDao.findAllBooks(); // Fetch all books
        for (Book bookItem : allBooks) {
            // Check if the book's category matches the selected category (case-insensitive)
            if (bookItem.getCategory() != null && bookItem.getCategory().equalsIgnoreCase(selectedCategory)) {
                Author author = bookItem.getAuthor();
                 String authorUsername = (author != null) ? author.getUsername() : "N/A";
                 String authorPerformance = (author != null) ? author.getPerformanceLevel() : "N/A";

                model.addRow(new Object[]{
                    authorUsername,
                    authorPerformance,
                    bookItem.getTitle(),
                    bookItem.getCategory(),
                    bookItem.getId() // Include ID for consistency
                });
            }
        }
    }//GEN-LAST:event_cbCategoryActionPerformed

    private void btnGenerateReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateReportActionPerformed
        // Generate the system report using the admin object
    String report = admin.generateSystemReport();

    // Show the report in a message dialog
    JOptionPane.showMessageDialog(this, report, "System Report", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnGenerateReportActionPerformed

    private void btnLogOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogOutActionPerformed
        this.dispose();
        new LoginFrame().setVisible(true);
    }//GEN-LAST:event_btnLogOutActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
       fillAuthorTable(); // Refresh author/book table
        fillRegisteredUsersTable(); // Refresh registered users table
        fillTaskerBox(); // Refresh task assignment combobox
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void cbUserTypeTaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbUserTypeTaskActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbUserTypeTaskActionPerformed

    private void txtTaskNameeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTaskNameeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTaskNameeActionPerformed

    private void txtDueDateeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDueDateeActionPerformed
        
    }//GEN-LAST:event_txtDueDateeActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
       try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AdminDashboardFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminDashboardFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminDashboardFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminDashboardFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Ensure a valid admin object or a way to retrieve one is used for the main constructor
                new AdminDashboardFrame().setVisible(true); 
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAssignTask;
    private javax.swing.JButton btnGenerateReport;
    private javax.swing.JButton btnLogOut;
    private javax.swing.JButton btnManageBooks;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnViewLoginLogs;
    private javax.swing.JComboBox<String> cbCategory;
    private javax.swing.JComboBox<User> cbUserTypeTask;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblAuthorInfo;
    private static javax.swing.JTable tblRegisteredUsers;
    private javax.swing.JTextField txtDueDatee;
    private javax.swing.JTextField txtTaskNamee;
    // End of variables declaration//GEN-END:variables
 
}