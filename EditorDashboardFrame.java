/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package midproject1;

import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

// Import DAO classes
import midproject1.dao.TaskDao;
import midproject1.dao.UserDao;

/**
 * EditorDashboardFrame for editors to view and manage their assigned tasks,
 * and generate reports.
 * @author asus
 */
public class EditorDashboardFrame extends javax.swing.JFrame {

    private Editor editor; // The logged-in editor object
    private TaskDao taskDao; // DAO for task operations
    private UserDao userDao; // DAO for user operations (to find editor by username/ID)

    /**
     * Default constructor for EditorDashboardFrame.
     * Attempts to retrieve the current editor from the session (if available)
     * and initializes DAOs and displays assigned tasks.
     */
    public EditorDashboardFrame() {
        initComponents();
        initializeDaos();

        User currentUser = Session.getCurrentUser();
        if (currentUser != null && "editor".equals(currentUser.getRole())) {
            this.editor = (Editor) userDao.findById(currentUser.getId());
            if (this.editor == null) {
                JOptionPane.showMessageDialog(this, "Failed to load editor details. Please log in again.", "Error", JOptionPane.ERROR_MESSAGE);
                new LoginFrame().setVisible(true);
                this.dispose();
                return;
            }
        } else {
            JOptionPane.showMessageDialog(this, "No editor logged in or invalid session. Redirecting to login.", "Error", JOptionPane.ERROR_MESSAGE);
            new LoginFrame().setVisible(true);
            this.dispose();
            return;
        }

        jLabel1.setText("Book Publishing System - Editor: " + editor.getUsername());
        displayAssignedTasks(); // Show tasks when the frame opens
        btnViewTaskDetails.setVisible(true); // Make sure this button is visible
    }

    /**
     * Constructor for EditorDashboardFrame, typically used after a successful login.
     * It finds the editor by username from the database and loads their tasks.
     * @param admin An Admin instance (might be used for context, but not directly for editor data loading here).
     * @param editorUsername The username of the logged-in editor.
     */
    public EditorDashboardFrame(Admin admin, String editorUsername) {
        initComponents();
        initializeDaos();

        User foundUser = userDao.findByUsername(editorUsername);
        if (foundUser != null && "editor".equals(foundUser.getRole())) {
            this.editor = (Editor) foundUser;
            jLabel1.setText("Book Publishing System - Editor: " + editorUsername);
            displayAssignedTasks(); // Load tasks for this editor
            btnViewTaskDetails.setVisible(true); // Make sure this button is visible
        } else {
            JOptionPane.showMessageDialog(this, "Editor not found or invalid role. Please log in again.", "Error", JOptionPane.ERROR_MESSAGE);
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
     * Displays tasks assigned to the current editor by fetching them from the database.
     * Populates the tblAssignedTasks table.
     */
    private void displayAssignedTasks() {
        DefaultTableModel model = (DefaultTableModel) tblAssignedTasks.getModel();
        model.setRowCount(0); // Clear the table

        if (editor == null) {
            System.err.println("Editor object is null in displayAssignedTasks. Cannot load tasks.");
            return;
        }

        List<Task> editorTasks = taskDao.findByAssignedTo(editor.getId());

        for (Task task : editorTasks) {
            // 4 columns: Task Name, Book Title, Deadline, Status
            Object[] rowData = new Object[4];
            rowData[0] = task.getName();
            rowData[1] = task.getBookTitle();
            rowData[2] = task.getDeadline();
            rowData[3] = task.getStatus();
            
            model.addRow(rowData);
        }

        if (editorTasks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tasks assigned to you yet.");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnLogout2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAssignedTasks = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        btnCompleteTask = new javax.swing.JButton();
        btnViewTaskDetails = new javax.swing.JButton();
        btnGenerateReport = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnLogout2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLogout2.setText("Log out ");
        btnLogout2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogout2ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setText("Book Publishing System  - Editor");

        tblAssignedTasks.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Task Name", "Book Title ", "Deadline", "Status "
            }
        ));
        jScrollPane1.setViewportView(tblAssignedTasks);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 51, 51));
        jLabel2.setText("Assigned Tasks");

        btnCompleteTask.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCompleteTask.setText("Complete Task");
        btnCompleteTask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCompleteTaskActionPerformed(evt);
            }
        });

        btnViewTaskDetails.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnViewTaskDetails.setText("View Task Details ");
        btnViewTaskDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewTaskDetailsActionPerformed(evt);
            }
        });

        btnGenerateReport.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnGenerateReport.setText("Generate Report ");
        btnGenerateReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateReportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(btnGenerateReport, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                                    .addComponent(btnCompleteTask, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(38, 38, 38)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnViewTaskDetails)
                                    .addComponent(btnLogout2, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(99, 99, 99)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCompleteTask)
                    .addComponent(btnViewTaskDetails))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGenerateReport)
                    .addComponent(btnLogout2))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogout2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogout2ActionPerformed
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Log Out", JOptionPane.YES_NO_OPTION);
        
        if (response == JOptionPane.YES_OPTION) {
            DefaultTableModel model = (DefaultTableModel) tblAssignedTasks.getModel();
            model.setRowCount(0);
            
            this.setVisible(false);
            new LoginFrame().setVisible(true);
        }
    }//GEN-LAST:event_btnLogout2ActionPerformed

    private void btnViewTaskDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewTaskDetailsActionPerformed
      int selectedRow = tblAssignedTasks.getSelectedRow();

        if (selectedRow != -1) {
            try {
                String taskName = (String) tblAssignedTasks.getValueAt(selectedRow, 0);
                String bookTitle = (String) tblAssignedTasks.getValueAt(selectedRow, 1);
                
                Task selectedTask = taskDao.findByDetails(taskName, bookTitle, editor.getId());

                if (selectedTask != null) {
                    new TaskdetailsFrame(selectedTask).setVisible(true);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Selected task details not found in the database.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error retrieving task details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to view its details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnViewTaskDetailsActionPerformed

    private void btnGenerateReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateReportActionPerformed
   if (editor == null) {
            JOptionPane.showMessageDialog(this, "Editor details not loaded. Cannot generate report.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Task> editorTasks = taskDao.findByAssignedTo(editor.getId());

        if (editorTasks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tasks assigned to you to generate a report.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int totalTasks = 0;
        int completedTasks = 0;
        int pendingTasks = 0;
        int approachingDeadlineTasks = 0;

        LocalDate currentDate = LocalDate.now();

        for (Task task : editorTasks) {
            totalTasks++;
            
            if ("Completed".equals(task.getStatus())) {
                completedTasks++;
            } else {
                pendingTasks++;
                
                if (task.getDeadline() != null && task.getDeadline().isBefore(currentDate.plusDays(3))) {
                    approachingDeadlineTasks++;
                }
            }
        }

        String report = "Editor Task Report for " + editor.getUsername() + ":\n" +
                        "Total Tasks: " + totalTasks + "\n" +
                        "Completed Tasks: " + completedTasks + "\n" +
                        "Pending Tasks: " + pendingTasks + "\n" +
                        "Approaching Deadline Tasks: " + approachingDeadlineTasks;
        
        JOptionPane.showMessageDialog(this, report, "Editor Task Report", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnGenerateReportActionPerformed

    private void btnCompleteTaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCompleteTaskActionPerformed
    if (tblAssignedTasks.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "There are no tasks to complete.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int selectedRow = tblAssignedTasks.getSelectedRow();
        if (selectedRow != -1) {
            try {
                String taskName = (String) tblAssignedTasks.getValueAt(selectedRow, 0);
                String bookTitle = (String) tblAssignedTasks.getValueAt(selectedRow, 1);
                
                Task task = taskDao.findByDetails(taskName, bookTitle, editor.getId());

                if (task != null) {
                    int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to mark this task as completed?", "Complete Task", JOptionPane.YES_NO_OPTION);
                    
                    if (response == JOptionPane.YES_OPTION) {
                        if ("Completed".equals(task.getStatus())) {
                            JOptionPane.showMessageDialog(this, "This task is already marked as completed.", "Information", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            task.setCompletedOnTime(true);
                            taskDao.update(task);
                            displayAssignedTasks();
                            JOptionPane.showMessageDialog(this, "Task has been marked as completed.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Task completion has been canceled.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Selected task not found in the system (database error).", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error completing task: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to complete.", "Warning", JOptionPane.WARNING_MESSAGE);
    }                                               
    
    
            
        
       
    }//GEN-LAST:event_btnCompleteTaskActionPerformed

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
            java.util.logging.Logger.getLogger(EditorDashboardFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, "Please log in via LoginFrame to access Editor Dashboard.", "Information", JOptionPane.INFORMATION_MESSAGE);
            new LoginFrame().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCompleteTask;
    private javax.swing.JButton btnGenerateReport;
    private javax.swing.JButton btnLogout2;
    private javax.swing.JButton btnViewTaskDetails;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblAssignedTasks;
    // End of variables declaration//GEN-END:variables



}
