package midproject1;

import java.time.LocalDate;
import jakarta.persistence.*; // JPA anotasyonları için

/**
 * Represents a book in the publishing system.
 */
@Entity
@Table(name = "books") // Kitaplar tablosu
public class Book {
    // idCounter kaldırıldı, çünkü ID veritabanı tarafından otomatik artırılıyor.
    // private static int idCounter = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // Book ID (Veritabanı tarafından otomatik oluşturulacak)

    @Column(name = "title", nullable = false, length = 100)
    private String title; // Kitabın başlığı

    // 'author' alanı, 'users' tablosundaki id ile ilişkili foreign key.
    // Author sınıfının User sınıfından extend ettiğini varsayarak Author tipini koruyoruz.
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author; // Kitabın yazarı (Author bir Entity ve User'dan türemiş olmalı)

    // Kategori artık String olarak tutulacak ve veritabanındaki 'category' sütununa doğrudan haritalanacak
    @Column(name = "category", nullable = false, length = 50)
    private String category; // Kitabın kategorisi/türü (String tipi)

    @Column(name = "publication_date") // nullable true ise belirtmeye gerek yok
    private LocalDate publicationDate; // Kitabın yayınlanma tarihi

    @Column(name = "description", length = 255) // nullable true ise belirtmeye gerek yok
    private String description; // Kitabın açıklaması veya özeti

    @Column(name = "performance_level", nullable = false, length = 50)
    private String performanceLevel; // performance_level alanı

    // JPA için boş yapıcı metod (çok önemli!)
    public Book() {}

    /**
     * Constructs a new Book object with all fields.
     * ID'nin veritabanı tarafından otomatik atandığını varsayar.
     *
     * @param title            The title of the book
     * @param author           The author of the book (Author nesnesi)
     * @param category         The category of the book (String olarak)
     * @param publicationDate  The publication date (as LocalDate)
     * @param description      The description or summary of the book
     * @param performanceLevel Kitabın performans seviyesi
     */
    public Book(String title, Author author, String category, LocalDate publicationDate, String description, String performanceLevel) {
        // this.id = idCounter++; // Bu satır kaldırıldı, ID veritabanı tarafından yönetilecek
        this.title = title;
        this.author = author;
        this.category = category; // String olarak alacak
        this.publicationDate = publicationDate;
        this.description = description;
        this.performanceLevel = performanceLevel;
    }

    // İkinci yapıcı metodunuzu da güncelliyorum (id ataması olmayan versiyon)
    public Book(String title, Author author, String category, LocalDate publicationDate) {
        // this.id = idCounter++; // Bu satır kaldırıldı, ID veritabanı tarafından yönetilecek
        this.title = title;
        this.author = author;
        this.category = category;
        this.publicationDate = publicationDate;
        this.description = ""; // Varsayılan boş açıklama
        this.performanceLevel = "N/A"; // Varsayılan performans seviyesi
    }


    // Getters and setters...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }
    public void setAuthor(Author author) {
        this.author = author;
    }

    // Kategori getter ve setter'ı String tipine göre güncellendi
    public String getCategory() { // Artık String dönecek
        return category;
    }
    public void setCategory(String category) { // String alacak
        this.category = category;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }
    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getPerformanceLevel() {
        return performanceLevel;
    }
    public void setPerformanceLevel(String performanceLevel) {
        this.performanceLevel = performanceLevel;
    }

    @Override
    public String toString() {
        String authorName = (author != null) ? author.getName() : "Bilinmeyen Yazar";
        // Category artık doğrudan String olduğu için .getName() çağırmaya gerek yok
        return title + " by " + authorName + " (" + category + ", " + publicationDate + ")";
    }
}
