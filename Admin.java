package midproject1;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import midproject1.dao.BookDao;
import midproject1.dao.TaskDao;
import midproject1.dao.UserDao;

@Entity
@DiscriminatorValue("admin")
public class Admin extends User {

    @Transient // Kalıcı olmayan alan: DB'de tutulmaz
    private ArrayList<String> systemLogs = new ArrayList<>();

    public Admin() {
        super();
    }

    public Admin(String username, String password, String email, String role) {
        super(username, password, email, role);
    }

    public Admin(String username, String password) {
        super(username, password, "admin@example.com", "admin");
    }

    public void addBook(Book book) {
        BookDao bookDao = new BookDao();
        try {
            bookDao.save(book);
            addLog("New book added: " + book.getTitle());
        } catch (Exception e) {
            addLog("Failed to add book: " + book.getTitle() + " - " + e.getMessage());
        }
    }

    public void removeBook(int bookId) {
        BookDao bookDao = new BookDao();
        try {
            Book book = bookDao.findBookById(bookId);
            if (book != null) {
                bookDao.delete(bookId);
                addLog("Book removed: " + book.getTitle());
            }
        } catch (Exception e) {
            addLog("Failed to remove book ID " + bookId + " - " + e.getMessage());
        }
    }

    public void removeBook(Book book) {
        if (book != null) removeBook(book.getId());
    }

    public void addAuthor(Author author) {
        UserDao userDao = new UserDao();
        try {
            userDao.save(author);
            addLog("New author added: " + author.getUsername());
        } catch (Exception e) {
            addLog("Failed to add author: " + author.getUsername() + " - " + e.getMessage());
        }
    }

    public void removeAuthor(int authorId) {
        UserDao userDao = new UserDao();
        try {
            User user = userDao.findById(authorId);
            if (user instanceof Author) {
                userDao.delete(authorId);
                addLog("Author removed: " + user.getUsername());
            }
        } catch (Exception e) {
            addLog("Failed to remove author ID " + authorId + " - " + e.getMessage());
        }
    }

    public void removeAuthor(Author author) {
        if (author != null) removeAuthor(author.getId());
    }

    public void addEditor(Editor editor) {
        UserDao userDao = new UserDao();
        try {
            userDao.save(editor);
            addLog("New editor added: " + editor.getUsername());
        } catch (Exception e) {
            addLog("Failed to add editor: " + editor.getUsername() + " - " + e.getMessage());
        }
    }

    public void removeEditor(int editorId) {
        UserDao userDao = new UserDao();
        try {
            User user = userDao.findById(editorId);
            if (user instanceof Editor) {
                userDao.delete(editorId);
                addLog("Editor removed: " + user.getUsername());
            }
        } catch (Exception e) {
            addLog("Failed to remove editor ID " + editorId + " - " + e.getMessage());
        }
    }

    public void removeEditor(Editor editor) {
        if (editor != null) removeEditor(editor.getId());
    }

    public void addLog(String log) {
        systemLogs.add(log);
    }

    public ArrayList<String> getSystemLogs() {
        return new ArrayList<>(systemLogs);
    }

    public String generateSystemReport() {
        BookDao bookDao = new BookDao();
        UserDao userDao = new UserDao();
        TaskDao taskDao = new TaskDao();

        StringBuilder report = new StringBuilder("=== System Report ===\n");

        List<Book> books = bookDao.findAll();
        List<User> users = userDao.findAll();
        List<Task> tasks = taskDao.findAll();

        List<Author> authors = new ArrayList<>();
        List<Editor> editors = new ArrayList<>();

        for (User user : users) {
            if (user instanceof Author) authors.add((Author) user);
            else if (user instanceof Editor) editors.add((Editor) user);
        }

        report.append("Total Books: ").append(books.size()).append("\n")
              .append("Total Authors: ").append(authors.size()).append("\n")
              .append("Total Editors: ").append(editors.size()).append("\n")
              .append("Total Tasks: ").append(tasks.size()).append("\n");

        long completed = tasks.stream().filter(t -> "Completed".equals(t.getStatus())).count();
        long pending = tasks.size() - completed;

        report.append("Completed Tasks: ").append(completed).append("\n")
              .append("Pending Tasks: ").append(pending).append("\n");

        report.append("\nBooks by Category:\n");
        java.util.Map<String, Integer> categories = new java.util.HashMap<>();
        for (Book b : books) {
            if (b.getCategory() != null)
                categories.merge(b.getCategory(), 1, Integer::sum);
        }
        categories.forEach((k, v) -> report.append("- ").append(k).append(": ").append(v).append("\n"));

        report.append("\nAuthor Performances:\n");
        for (Author author : authors) {
            author.updatePerformanceLevel();
            report.append("- ").append(author.getUsername()).append(": ").append(author.getPerformanceLevel()).append("\n");
        }

        if (!books.isEmpty()) {
            Book lastBook = books.get(books.size() - 1);
            report.append("\nLast Added Book: \"").append(lastBook.getTitle()).append("\" by ")
                  .append(lastBook.getAuthor() != null ? lastBook.getAuthor().getUsername() : "unknown").append("\n");
        }
        if (!authors.isEmpty()) report.append("Last Added Author: ").append(authors.get(authors.size() - 1).getUsername()).append("\n");
        if (!tasks.isEmpty()) report.append("Last Added Task: ").append(tasks.get(tasks.size() - 1).getName()).append("\n");

        List<LoginLog> loginLogs = LoginLogManager.getLoginLogs();
        if (!loginLogs.isEmpty()) {
            LoginLog lastLog = loginLogs.get(loginLogs.size() - 1);
            report.append("\nLast Login: ").append(lastLog.getUsername())
                  .append(" (").append(lastLog.getUserType()).append(") - ")
                  .append(lastLog.getLoginTime()).append("\n");
        }

        return report.toString();
    }

    public boolean hasPermission(String permission) {
        return true;
    }

    public List<Book> getBooksFromDb() {
        return new BookDao().findAll();
    }

    public List<Author> getAuthorsFromDb() {
        return new UserDao().findAllAuthors();
    }

    public List<Task> getTasksFromDb() {
        return new TaskDao().findAll();
    }

    public List<Editor> getEditorsFromDb() {
        return new UserDao().findAllEditors();
    }
}
