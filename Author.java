package midproject1;

import jakarta.persistence.Column; 
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient; 
import midproject1.dao.BookDao; 
import midproject1.dao.TaskDao; 
import java.util.List; 

@Entity 
@DiscriminatorValue("author") 
public class Author extends User {

    // Bu alanlar artık veritabanında saklanacağı için @Column olarak işaretlendi.
    @Column(name = "author_title", length = 100) 
    private String authorTitle; 

    @Column(name = "genre", length = 50) 
    private String genre;

    @Transient 
    private int bookCount; 
    
    @Transient 
    private String performanceLevel; 


    public Author() {
        super();
        this.role = "author"; 
        this.bookCount = 0;
        this.performanceLevel = calculatePerformanceLevel(); 
    }

    public Author(String username, String password, String email, String role) {
        super(username, password, email, role); 
        this.role = "author"; 
        this.authorTitle = ""; 
        this.genre = ""; 
        this.bookCount = 0;
        this.performanceLevel = calculatePerformanceLevel();
    }
    
    public Author(String username, String password, String email, String role, String authorTitle, String genre) {
        super(username, password, email, role);
        this.role = "author";
        this.authorTitle = authorTitle;
        this.genre = genre;
        this.bookCount = 0;
        this.performanceLevel = calculatePerformanceLevel();
    }

    public void updatePerformanceLevel() {
        this.performanceLevel = calculatePerformanceLevel(); 
    }

    private String calculatePerformanceLevel() {
        TaskDao taskDao = new TaskDao();
        BookDao bookDao = new BookDao();

        List<Task> authorTasks = taskDao.findByAssignedTo(this.getId());
        List<Book> booksByAuthor = bookDao.findBooksByAuthorId(this.getId());

        int completedOnTimeTasks = 0;
        for (Task task : authorTasks) {
            if (Boolean.TRUE.equals(task.getCompletedOnTime())) { 
                completedOnTimeTasks++;
            }
        }
        
        this.bookCount = booksByAuthor.size();

        if (this.bookCount >= 5 && completedOnTimeTasks >= 3) {
            return "HIGH"; 
        } else if (this.bookCount >= 3 && completedOnTimeTasks >= 2) {
            return "MEDIUM"; 
        } else {
            return "LOW"; 
        }
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getBookCount() {
        return bookCount; 
    }

    public String getPerformanceLevel() {
        return performanceLevel;
    }

    public void setPerformanceLevel(String performanceLevel) {
        this.performanceLevel = performanceLevel;
    }

    public String getAuthorTitle() {
        return authorTitle;
    }

    public void setAuthorTitle(String authorTitle) {
        this.authorTitle = authorTitle;
    }
    
    public String getName() {
        return this.getUsername(); 
    }

    public String getAuthorInfo() {
        return getUsername() + ", Genre: " + genre + ", Performance Level: " + getPerformanceLevel();
    }

    public String getCategory() {
        return this.getGenre();
    }

    @Override
    public String toString() {
        return this.getUsername();
    }
}
