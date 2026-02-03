/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package midproject1;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter; // For parsing/formatting dates
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

// Import DAO classes
import midproject1.dao.BookDao;
import midproject1.dao.UserDao;

/**
 * JFrame for managing book details and operations.
 */
public class BookdetailsFrame extends javax.swing.JFrame {

    // Class fields
    private Admin admin; // Admin object, potentially passed from AdminDashboardFrame
    private Book book; // The book object currently being viewed or edited
    
    // DAO instances for database interaction
    private BookDao bookDao;
    private UserDao userDao; 

    /**
     * Default constructor. Used for creating new books.
     * Initializes components, DAOs, populates dropdowns, loads table, and sets form for new book entry.
     */
    public BookdetailsFrame() {
        initComponents();
        initializeDaos(); // Initialize DAO instances
        populateAuthorComboBox(); // Fills the author dropdown from DB
        populateCategoryComboBox(); // Fills the category dropdown (static for now)
        loadBooksTable(); // Loads all books into the table
        setFormForNewBook(); // Configures UI for adding a new book
    }

    /**
     * Constructor for viewing or editing an existing book.
     * Initializes components, DAOs, populates dropdowns, loads table, and displays book details.
     * @param book The Book object to display.
     */
    public BookdetailsFrame(Book book) {
        this.book = book;
        initComponents();
        initializeDaos();
        populateAuthorComboBox();
        populateCategoryComboBox();
        loadBooksTable(); // Loads all books into the table
        displayBookDetails(book); // Displays details of the provided book
    }

    /**
     * Constructor for Admin context without a specific book.
     * Initializes components, DAOs, populates dropdowns, loads table, and sets form for new book entry.
     * @param admin The Admin object.
     */
    public BookdetailsFrame(Admin admin) {
        this.admin = admin;
        initComponents();
        initializeDaos();
        populateAuthorComboBox();
        populateCategoryComboBox();
        loadBooksTable();
        setFormForNewBook(); // Configures UI for adding a new book by admin
    }
    
    /**
     * Constructor for Admin context with BookManagement (deprecated, kept for compatibility).
     * This constructor's `bookManagement` parameter is no longer used with DAO-based approach.
     * @param admin The Admin object.
     * @param bookManagement The BookManagement instance (not used with DAOs).
     */
    public BookdetailsFrame(Admin admin, BookManagement bookManagement) {
        this.admin = admin;
        initComponents();
        initializeDaos();
        populateAuthorComboBox();
        populateCategoryComboBox();
        loadBooksTable();
        setFormForNewBook();
    }
      
    /**
     * Initializes DAO instances.
     */
    private void initializeDaos() {
        bookDao = new BookDao();
        userDao = new UserDao();
    }
    
    /**
     * Displays details of a given book in the form fields.
     * Configures button visibility based on whether a book is being viewed/edited or added.
     * @param bookToDisplay The book whose details are to be displayed.
     */
    private void displayBookDetails(Book bookToDisplay) {
        if (bookToDisplay != null) {
            txtBookTitle.setText(bookToDisplay.getTitle());
            // Use DateTimeFormatter for robust date formatting if needed,
            // but toString() usually works for LocalDate.
            txtPublishDate.setText(bookToDisplay.getPublicationDate() != null ? bookToDisplay.getPublicationDate().toString() : "");
            txtBookDescription.setText(bookToDisplay.getDescription());
            
            // Set selected author in ComboBox
            if (bookToDisplay.getAuthor() != null) {
                // Iterate through items to find the matching Author object by ID or Username
                // This is important if `Author` objects are not exactly the same instance
                // as those populated in the combobox, but represent the same entity.
                // Assuming Author's equals() method correctly compares authors (e.g., by ID).
                for (int i = 0; i < cbAuthor.getItemCount(); i++) {
                    Author item = cbAuthor.getItemAt(i);
                    if (item != null && item.getId() == bookToDisplay.getAuthor().getId()) { // Compare by ID
                        cbAuthor.setSelectedIndex(i);
                        break;
                    }
                }
            }
            
            // Set selected category in ComboBox (category is String)
            if (bookToDisplay.getCategory() != null) {
                cbCategory.setSelectedItem(bookToDisplay.getCategory());
            }

            btnSaveBook.setVisible(false); // Hide save button for existing books
            btnDelete.setVisible(true); // Show delete and update buttons
            btnUpdateBook.setVisible(true);
        } else {
            setFormForNewBook(); // If no book is provided, set up for new book entry
        }
    }

    /**
     * Configures the form for adding a new book (clears fields and sets button visibility).
     */
    private void setFormForNewBook() {
        btnDelete.setVisible(false);
        btnUpdateBook.setVisible(false);
        btnSaveBook.setVisible(true);
        txtBookTitle.setText("");
        txtBookDescription.setText("");
        txtPublishDate.setText("");
        cbAuthor.setSelectedIndex(-1); // Resets selection
        cbCategory.setSelectedIndex(-1); // Resets selection
    }

    /**
     * Loads all books from the database into the JTable.
     * The table model expects columns: ID, Author Name, Book Title, Category.
     */
    private void loadBooksTable() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);  // Clear existing rows

        // Fetch books from the database using BookDao
        List<Book> books = bookDao.findAllBooks();  
        
        for (Book bookItem : books) {  
            Author author = bookItem.getAuthor();
            // System.out.println("Loading book: " + bookItem.getTitle()); // Debugging line

            // Ensure the author object is not null to avoid NullPointerException
            String authorName = (author != null) ? author.getUsername() : "N/A";
            
            // Assuming jTable1 columns are: ID, Author Name, Book Title, Category, Publication Date
            model.addRow(new Object[]{
                bookItem.getId(), // Add Book ID
                authorName,  
                bookItem.getTitle(),
                bookItem.getCategory(), // Category is a String
                bookItem.getPublicationDate() != null ? bookItem.getPublicationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "N/A"
            });
        }
    }

    /**
     * Populates the Author ComboBox with users having the 'author' role from the database.
     */
    private void populateAuthorComboBox() {
        cbAuthor.removeAllItems();  
        List<User> allUsers = userDao.findAll(); // Fetch all users from DB

        for (User user : allUsers) {
            // Check if the user has the 'author' role AND is an instance of the Author class.
            // This ensures proper casting and that only actual Author objects are added.
            if ("author".equals(user.getRole()) && user instanceof Author) {    
                cbAuthor.addItem((Author) user);
            } 
            // The previous incomplete line "else if ("author".equals(user.getR" is removed.
            // No need to handle other roles here, as this combobox is for authors.
        }
        // Select the first item by default if no specific book is being edited (i.e., new book mode)
        if (cbAuthor.getItemCount() > 0 && book == null) { 
            cbAuthor.setSelectedIndex(0);
        }
    }

    /**
     * Populates the Category ComboBox with predefined String categories.
     * This is static as the category is stored as a String in the Book entity.
     */
    private void populateCategoryComboBox() {
        cbCategory.removeAllItems();
        String[] categories = new String[] { 
            "Fiction", "Non-Fiction", "Science", "Biography", "Fantasy", 
            "Mystery", "Romance", "Thriller", "History", "Philosophy", 
            "Poetry", "Self-Help", "Health", "Cookbooks", "Travel", 
            "Children's Books", "Art & Photography", "Science Fiction", 
            "Religion", "Technology"
        };
        for (String cat : categories) {
            cbCategory.addItem(cat);
        }
        // Select the first item by default if no specific book is being edited (i.e., new book mode)
        if (cbCategory.getItemCount() > 0 && book == null) { 
            cbCategory.setSelectedIndex(0);
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

        button1 = new java.awt.Button();
        jLabel1 = new javax.swing.JLabel();
        txtBookTitle = new javax.swing.JTextField();
        cbAuthor = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        cbCategory = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        txtPublishDate = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtBookDescription = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnUpdateBook = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnBack4 = new javax.swing.JButton();
        btnSaveBook = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();

        button1.setLabel("button1");
        button1.setName("Save Book"); // NOI18N
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                formCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setText("Book Publishing System ");

        txtBookTitle.setText("Book Title");
        txtBookTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBookTitleActionPerformed(evt);
            }
        });

        cbAuthor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbAuthorActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Book Title : ");

        String[] categories = new String[] { "Fiction", "Non-Fiction", "Science ", "Biography", "Fantasy ", "Mystery", "Romance", "Thriller", "History", "Philosophy", "Poetry", "Self-Help", "Health", "Cookbooks", "Travel", "Children's Books", "Art & Photography", "Science Fiction", "Religion", "Technology", " " };

        cbCategory.setModel(new javax.swing.DefaultComboBoxModel<>(categories));

        if (book != null) {
            for (int i = 0; i < categories.length; i++) {
                Category category = new Category(categories[i]);
                if (book.getCategory().equals(category)) cbCategory.setSelectedIndex(i);
            }
        }

        cbCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbCategoryActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Author :");

        txtPublishDate.setText("Publish Date ");
        txtPublishDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPublishDateActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Category :");

        txtBookDescription.setColumns(20);
        txtBookDescription.setRows(5);
        jScrollPane1.setViewportView(txtBookDescription);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Publish Date :");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Book Description :");

        btnUpdateBook.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnUpdateBook.setForeground(new java.awt.Color(0, 0, 0));
        btnUpdateBook.setText("Update Book");
        btnUpdateBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateBookActionPerformed(evt);
            }
        });

        btnDelete.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(0, 0, 0));
        btnDelete.setText("Delete Book");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnBack4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBack4.setForeground(new java.awt.Color(51, 51, 51));
        btnBack4.setText("Back ");
        btnBack4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBack4ActionPerformed(evt);
            }
        });

        btnSaveBook.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSaveBook.setForeground(new java.awt.Color(0, 0, 0));
        btnSaveBook.setText("Save Book");
        btnSaveBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveBookActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Author Name ", "Book Title", "Category"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Books");

        if (book != null) {
            txtBookTitle.setText(book.getTitle());
            txtPublishDate.setText(book.getPublicationDate().toString());
            txtBookDescription.setText(book.getDescription());
            btnSaveBook.setVisible(false);
        } else {
            btnDelete.setVisible(false);
            btnUpdateBook.setVisible(false);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(104, 104, 104)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(29, 29, 29)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel7)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cbAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtBookTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtPublishDate, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGap(34, 34, 34)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(24, 24, 24)
                            .addComponent(btnUpdateBook)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(btnSaveBook, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(btnBack4, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(47, 47, 47))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtBookTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(cbAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(cbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(txtPublishDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(17, 17, 17)
                        .addComponent(jLabel7))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDelete)
                    .addComponent(btnUpdateBook)
                    .addComponent(btnSaveBook)
                    .addComponent(btnBack4))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBookTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBookTitleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBookTitleActionPerformed

    private void btnUpdateBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateBookActionPerformed
       // 1. Read input fields
        String bookTitle = txtBookTitle.getText().trim();
        String bookDescription = txtBookDescription.getText().trim();
        String publishDate = txtPublishDate.getText().trim();
        Author selectedAuthor = (Author) cbAuthor.getSelectedItem();  
        String selectedCategory = (String) cbCategory.getSelectedItem(); 

        // 2. Validate input fields
        if (bookTitle.isEmpty() || bookDescription.isEmpty() || publishDate.isEmpty() ||
                selectedAuthor == null || selectedCategory == null) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!");
            return;
        }

        // 3. Parse and validate publication date format
        LocalDate publicationDate;
        try {
            publicationDate = LocalDate.parse(publishDate);
        } catch (java.time.format.DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Publish date must be in YYYY-MM-DD format!");
            return;
        }

        // 4. Update the existing Book object with new values
        if (book != null) { // Ensure a book is selected for update
            book.setTitle(bookTitle);
            book.setAuthor(selectedAuthor); // Set the Author object
            book.setCategory(selectedCategory);
            book.setPublicationDate(publicationDate);
            book.setDescription(bookDescription);
            // Assuming performance_level is not directly editable in this form
            // book.setPerformanceLevel(...); 
        } else {
            JOptionPane.showMessageDialog(this, "No book selected for update!");
            return;
        }

        // 5. Update the book in the database using BookDao
        try {
            bookDao.updateBook(book);   
            JOptionPane.showMessageDialog(this, "Book updated successfully!");
            
            loadBooksTable(); // Reload table with updated data
            // Option to close this frame and return to admin dashboard:
            // dispose();  
            // new AdminDashboardFrame(admin).setVisible(true);  
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating book: " + ex.getMessage());
            ex.printStackTrace();   
        }
    }//GEN-LAST:event_btnUpdateBookActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed

     // 1. Confirm deletion with user
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this book?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (book != null) { // Ensure a book is selected for deletion
                    bookDao.deleteBook(book.getId()); // Delete from database using BookDao
                    JOptionPane.showMessageDialog(this, "Book deleted successfully!");
                    
                    // Clear form fields and reset for new book entry
                    setFormForNewBook();  
                    loadBooksTable(); // Reload table
                    // Clear the current book object after deletion
                    this.book = null; 
                } else {
                    JOptionPane.showMessageDialog(this, "No book selected for deletion!");
                }
            }
            // Catching generic Exception for broader error handling. Specific exceptions can be added if needed.
            catch (Exception ex) {  
                JOptionPane.showMessageDialog(this, "Error deleting book: " + ex.getMessage());
                ex.printStackTrace(); // Print full stack trace for debugging
            }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnBack4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBack4ActionPerformed
     // Close this frame
        this.dispose();

        // Open the Admin Dashboard frame
        // Ensure 'admin' object is not null, otherwise pass a default or handle error.
        if (admin != null) {
            new AdminDashboardFrame(admin).setVisible(true);
        } else {
            // If somehow 'admin' is null, open a default AdminDashboard (might happen if opened directly)
            new AdminDashboardFrame().setVisible(true);
        }
    }//GEN-LAST:event_btnBack4ActionPerformed

    private void txtPublishDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPublishDateActionPerformed
          String publishDate = txtPublishDate.getText().trim();
        // Check if the date matches the required format
        if (!publishDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Publish date must be in YYYY-MM-DD format!");
            txtPublishDate.requestFocus(); // Set focus back to the text field
        }
    }//GEN-LAST:event_txtPublishDateActionPerformed

    private void cbCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCategoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbCategoryActionPerformed

    private void cbAuthorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbAuthorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbAuthorActionPerformed

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_button1ActionPerformed

    private void formCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_formCaretPositionChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_formCaretPositionChanged

    private void btnSaveBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveBookActionPerformed
    String bookTitle = txtBookTitle.getText().trim();
        String bookDescription = txtBookDescription.getText().trim();
        String publishDate = txtPublishDate.getText().trim();
        
        // Get selected Author object from ComboBox
        Author selectedAuthor = (Author) cbAuthor.getSelectedItem();  
        
        if (selectedAuthor == null) {
            JOptionPane.showMessageDialog(this, "Please select a valid author.");
            return;
        }

        String selectedCategory = (String) cbCategory.getSelectedItem();  
        
        // Validate all fields are filled
        if (bookTitle.isEmpty() || bookDescription.isEmpty() || publishDate.isEmpty() ||
            selectedAuthor == null || selectedCategory == null) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!");
            return;
        }

        // Validate date format
        if (!publishDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Publish date must be in YYYY-MM-DD format!");
            return;
        }

        LocalDate publicationDate;
        try {
            publicationDate = LocalDate.parse(publishDate);
        } catch (java.time.format.DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Publish date must be in YYYY-MM-DD format!");
            return;
        }

        // Check for existing book with same title, author, and publish date
        // This check can be optimized by adding a specific method to BookDao
        // For now, iterating through all books is kept as per original logic.
        List<Book> existingBooks = bookDao.findAllBooks();  
        for (Book existingBook : existingBooks) {
            // Check if existingBook.getAuthor() is not null before accessing its properties
            if (existingBook.getTitle().equalsIgnoreCase(bookTitle) &&
                existingBook.getAuthor() != null && existingBook.getAuthor().getId() == selectedAuthor.getId() && // Compare authors by ID
                existingBook.getPublicationDate().equals(publicationDate)) {
                JOptionPane.showMessageDialog(this, "This book already exists for this author on the same date!");
                return;
            }
        }

        System.out.println("Book Author is: " + selectedAuthor.getUsername() + " " + selectedAuthor.getEmail());
        
        // Create new Book object (assuming "New" as default performance level)
        // Ensure that the Book constructor matches these parameters.
        Book newBook = new Book(bookTitle, selectedAuthor, selectedCategory, publicationDate, bookDescription, "New");  

        try {
            bookDao.saveBook(newBook); // Save to database using BookDao
            JOptionPane.showMessageDialog(this, "Book added successfully!");
            
            // Clear fields and refresh UI
            setFormForNewBook();  
            loadBooksTable(); // Reload table with new data
        }   
        // Catching generic Exception for broader error handling. Specific exceptions can be added if needed.
        catch (Exception ex) {  
            JOptionPane.showMessageDialog(this, "Error adding book: " + ex.getMessage());
            ex.printStackTrace(); // Print full stack trace for debugging
        }
    }//GEN-LAST:event_btnSaveBookActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BookdetailsFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack4;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnSaveBook;
    private javax.swing.JButton btnUpdateBook;
    private java.awt.Button button1;
    private javax.swing.JComboBox<Author> cbAuthor;
    private javax.swing.JComboBox<String> cbCategory;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea txtBookDescription;
    private javax.swing.JTextField txtBookTitle;
    private javax.swing.JTextField txtPublishDate;
    // End of variables declaration//GEN-END:variables

    
}
