/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package midproject1;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import midproject1.dao.TaskDao;
import midproject1.dao.UserDao;

/**
 * AuthorDashboardFrame for authors to view, complete, and comment on their assigned tasks.
 * @author asus
 */
public class AuthorDashboardFrame extends javax.swing.JFrame {

    private Author author;
    private TaskDao taskDao;
    private UserDao userDao;

    /**
     * Creates new form AuthorDashboardFrame
     * Default constructor attempts to retrieve the current author from Session.
     * It then initializes DAOs and loads author's tasks.
     */
    public AuthorDashboardFrame() {
        initComponents();
        initializeDaos();

        User currentUser = Session.getCurrentUser();
        if (currentUser != null && "author".equals(currentUser.getRole())) {
            this.author = (Author) userDao.findById(currentUser.getId());
            if (this.author == null) {
                JOptionPane.showMessageDialog(this, "Failed to load author details. Please log in again.", "Error", JOptionPane.ERROR_MESSAGE);
                new LoginFrame().setVisible(true);
                this.dispose();
                return;
            }
        } else {
            JOptionPane.showMessageDialog(this, "No author logged in or invalid session. Redirecting to login.", "Error", JOptionPane.ERROR_MESSAGE);
            new LoginFrame().setVisible(true);
            this.dispose();
            return;
        }
        jLabel1.setText("Book Publishing System - Author: " + author.getUsername());
        loadAuthorTasks();
    }

    /**
     * Constructor for AuthorDashboardFrame, typically used after a successful login.
     * It finds the author by username from the database and loads their tasks.
     * @param username The username of the logged-in author.
     */
    public AuthorDashboardFrame(String username) {
        initComponents();
        initializeDaos();

        User foundUser = userDao.findByUsername(username);
        if (foundUser != null && "author".equals(foundUser.getRole())) {
            this.author = (Author) foundUser;
            jLabel1.setText("Book Publishing System - Author: " + username);
            loadAuthorTasks();
        } else {
            JOptionPane.showMessageDialog(this, "Author not found or invalid role. Please log in again.", "Error", JOptionPane.ERROR_MESSAGE);
            new LoginFrame().setVisible(true);
            this.dispose();
        }
    }
    
    /**
     * Initializes DAO instances for database operations.
     */
    private void initializeDaos() {
        taskDao = new TaskDao();
        userDao = new UserDao();
    }

    /**
     * Method to load tasks for a specific author from the database.
     * It populates the tblTask table with tasks assigned to the current author.
     */
    private void loadAuthorTasks() {
        DefaultTableModel model = (DefaultTableModel) tblTask.getModel();
        model.setRowCount(0);
        
        if (author == null) {
            System.err.println("Author object is null in loadAuthorTasks. Cannot load tasks.");
            return;
        }

        List<Task> authorTasks = taskDao.findByAssignedTo(author.getId());

        for (Task task : authorTasks) {
            // 4 columns: Task Name, Book Title, Deadline, Status
            Object[] rowData = new Object[4];
            rowData[0] = task.getName();
            rowData[1] = task.getBookTitle();
            rowData[2] = task.getDeadline();
            rowData[3] = task.getStatus();
            
            model.addRow(rowData);
        }
    }
    
    /**
     * Refreshes the task table by clearing existing rows and reloading tasks from the database.
     * This is called by btnUpdateTbl and after task actions.
     */
    private void refreshTaskTable() {
        DefaultTableModel model = (DefaultTableModel) tblTask.getModel();
        model.setRowCount(0);

        List<Task> tasks = taskDao.findByAssignedTo(author.getId());

        for (Task task : tasks) {
            Object[] rowData = new Object[4];
            rowData[0] = task.getName();
            rowData[1] = task.getBookTitle();
            rowData[2] = task.getDeadline();
            rowData[3] = task.getStatus();
            model.addRow(rowData);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTask = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        txtCommentField = new javax.swing.JTextField();
        btnSubmitComment = new javax.swing.JButton();
        btnCompleteTask = new javax.swing.JButton();
        btnUpdateTbl = new javax.swing.JButton();
        bntBack2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Book Publishing System - Author");

        jLabel2.setText("My Tasks");

        tblTask.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Task Name", "Book Title", "Deadline", "Status"
            }
        ));
        jScrollPane1.setViewportView(tblTask);

        jLabel3.setText("Comment :");

        txtCommentField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCommentFieldActionPerformed(evt);
            }
        });

        btnSubmitComment.setText("Submit Comment");
        btnSubmitComment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitCommentActionPerformed(evt);
            }
        });

        btnCompleteTask.setText("Complete Task");
        btnCompleteTask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCompleteTaskActionPerformed(evt);
            }
        });

        btnUpdateTbl.setText("Update");
        btnUpdateTbl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateTblActionPerformed(evt);
            }
        });

        bntBack2.setText("Back");
        bntBack2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntBack2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtCommentField)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(btnSubmitComment, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(btnUpdateTbl, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(bntBack2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnCompleteTask, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))))))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCommentField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSubmitComment)
                    .addComponent(btnCompleteTask))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUpdateTbl)
                    .addComponent(bntBack2))
                .addGap(21, 21, 21))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCommentFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCommentFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCommentFieldActionPerformed

    private void btnSubmitCommentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitCommentActionPerformed
      int selectedRow = tblTask.getSelectedRow();
        if (selectedRow != -1) {
            try {
                String taskName = (String) tblTask.getValueAt(selectedRow, 0);
                String bookTitle = (String) tblTask.getValueAt(selectedRow, 1);
                
                String comment = txtCommentField.getText().trim();

                if (comment.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid comment.");
                    return;
                }

                if (comment.length() > 200) {
                    JOptionPane.showMessageDialog(this, "Comment is too long. Please keep it under 200 characters.");
                    return;
                }

                Task task = taskDao.findByDetails(taskName, bookTitle, author.getId());
                if (task != null) {
                    task.setComment(comment);
                    taskDao.update(task);
                    
                    JOptionPane.showMessageDialog(this, "Comment successfully submitted.");
                    txtCommentField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "The selected task was not found (database error).");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error submitting comment: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task.");
        }
    }//GEN-LAST:event_btnSubmitCommentActionPerformed

    private void btnCompleteTaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCompleteTaskActionPerformed
        int selectedRow = tblTask.getSelectedRow();

        if (selectedRow != -1) {
            try {
                String taskName = (String) tblTask.getValueAt(selectedRow, 0);
                String bookTitle = (String) tblTask.getValueAt(selectedRow, 1);
                
                Task task = taskDao.findByDetails(taskName, bookTitle, author.getId());

                if (task != null) {
                    if ("Completed".equals(task.getStatus())) {
                        JOptionPane.showMessageDialog(this, "This task is already marked as completed.");
                    } else {
                        task.setCompletedOnTime(true);
                        taskDao.update(task);
                        refreshTaskTable();
                        JOptionPane.showMessageDialog(this, "Task marked as completed.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Selected task not found in the system (database error).");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error completing task: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to complete.");
        }
    }//GEN-LAST:event_btnCompleteTaskActionPerformed

    private void btnUpdateTblActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateTblActionPerformed
        refreshTaskTable();
    }//GEN-LAST:event_btnUpdateTblActionPerformed

    private void bntBack2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntBack2ActionPerformed
       LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_bntBack2ActionPerformed

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AuthorDashboardFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, "Please log in via LoginFrame to access Author Dashboard.", "Information", JOptionPane.INFORMATION_MESSAGE);
            new LoginFrame().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bntBack2;
    private javax.swing.JButton btnCompleteTask;
    private javax.swing.JButton btnSubmitComment;
    private javax.swing.JButton btnUpdateTbl;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblTask;
    private javax.swing.JTextField txtCommentField;
    // End of variables declaration//GEN-END:variables

}
