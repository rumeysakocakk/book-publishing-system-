package midproject1;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity // This class is a JPA entity, mapped to a database table.
@Table(name = "users") // Specifies the database table name this entity is mapped to.
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Specifies the inheritance strategy: All subclasses are stored in the same table.
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING) // The 'role' column distinguishes which subclass is stored.
public class User implements Serializable {

    @Id // Specifies that this field is the primary key.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Specifies that the primary key will be generated automatically (like AUTO_INCREMENT).
    private int id;

    @Column(name = "username", unique = true, nullable = false, length = 50) // Maps to the 'username' column. Must be unique and not null.
    private String username;

    @Column(name = "email", nullable = false, length = 100) // Maps to the 'email' column. Cannot be null.
    private String email;

    @Column(name = "password", nullable = false, length = 100) // Maps to the 'password' column. Cannot be null.
    private String password; // The single, correctly mapped password field
    
    // The 'role' column is managed by DiscriminatorColumn, so we use insertable=false, updatable=false
    // to prevent duplicate mapping errors with the discriminator column itself.
    @Column(name = "role", nullable = false, length = 20, insertable = false, updatable = false)
    protected String role; // Protected for access by subclasses

    public User() {
        // Default constructor is required by JPA.
    }

    public User(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role; // Set role for consistency, though DiscriminatorValue takes precedence.
    }

    // Getter and Setter Methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) { // 'void void' hatası düzeltildi
        this.password = password;
    }

    // A getter method for role is useful, as its value is automatically populated by DiscriminatorColumn.
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
}
