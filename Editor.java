
package midproject1;

import jakarta.persistence.Column; // JPA Column anotasyonunu dahil edin
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient; // JPA Transient anotasyonunu dahil edin
import midproject1.dao.TaskDao; // TaskDao'yu dahil edin
import java.util.List; // List interface'ini dahil edin

@Entity // Bu sınıfın bir JPA varlığı (entity) olduğunu belirtir. Veritabanındaki bir tabloya eşlenir.
@DiscriminatorValue("editor") // 'users' tablosundaki 'role' sütunu "editor" olduğunda bu entity'nin yükleneceğini belirtir.
public class Editor extends User {
    // 'name' alanı kaldırıldı, editörün adı için 'getUsername()' kullanılacak.

    @Column(name = "review_level", length = 50) // Veritabanındaki 'review_level' sütununa eşler.
    private String reviewLevel; 
    
    // 'tasks' listesi, görevler ayrı bir entity ve DAO ile yönetildiği için veritabanına eşlenmez.
    @Transient // Bu alanın veritabanı tablosuna eşlenmeyeceğini belirtir.
    private List<Task> tasks; // Aslında bu alanın doğrudan burada tutulmasına gerek yok, DAO ile çekilecek

    // Varsayılan no-arg yapıcı metot (JPA için zorunlu)
    public Editor() {
        super(); // Üst sınıfın (User) varsayılan yapıcı metodunu çağırır.
        this.role = "editor"; // Rolü burada "editor" olarak ayarlarız.
        this.reviewLevel = "Beginner"; // Varsayılan başlangıç seviyesi
    }

    // Üst sınıfın tüm parametrelerini alan yapıcı metot
    public Editor(String username, String password, String email, String role) {
        super(username, password, email, role);
        this.role = "editor"; // Rolün "editor" olduğundan emin olun.
        this.reviewLevel = "Beginner"; // Varsayılan başlangıç seviyesi
    }

    // Editörün 'reviewLevel'ını doğrudan ayarlamaya olanak tanıyan yapıcı metot
    public Editor(String username, String password, String email, String role, String reviewLevel) {
        super(username, password, email, role);
        this.role = "editor";
        this.reviewLevel = reviewLevel;
    }

    // Editörün adını döndüren metot (User sınıfındaki username'i kullanır)
    public String getName() {
        return this.getUsername(); // User sınıfından gelen 'username'i döndürür.
    }

    // Getter metotları
    public String getReviewLevel() {
        return reviewLevel;
    }

    // Setter metotları
    public void setReviewLevel(String reviewLevel) {
        this.reviewLevel = reviewLevel;
    }

    // Editörün tam bilgilerini döndüren metot
    public String getEditorInfo() {
        // 'name' yerine 'getUsername()' kullanıldı
        return "Username: " + getUsername() + ", Review Level: " + reviewLevel;
    }

    /**
     * Bu editöre atanmış tüm görevleri veritabanından çeker.
     * Bu metot, TaskDao kullanarak veritabanı erişimi sağlar.
     * @return Bu editöre atanmış görevlerin bir listesi.
     */
    public List<Task> getAssignedTasks() {
        TaskDao taskDao = new TaskDao();
        // Editor'ün ID'si ile görevleri çekeriz.
        return taskDao.findByAssignedTo(this.getId()); 
    }

    @Override
    public String toString() {
        return this.getUsername();
    }
}

