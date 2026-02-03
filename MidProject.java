/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package midproject1;

import midproject1.dao.UserDao; // UserDao'yu içeri aktar

/**
 * Main class to launch the Book Publishing System application.
 */
public class MidProject {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        UserDao userDao = new UserDao(); // UserDao örneği oluştur

        // Test kullanıcıları oluştur ve veritabanına kaydet (eğer yoksa)
        // Her çalıştığında tekrar eklenmemesi için kontrol eklenmiştir.
        try {
            // Admin kullanıcısı
            if (userDao.findByUsername("admin") == null) {
                User admin = new Admin("admin", "admin123", "admin@rumeysa.com", "admin");
                userDao.save(admin);
                System.out.println("Admin user 'admin' saved.");
            } else {
                System.out.println("Admin user 'admin' already exists.");
            }

            // Editor kullanıcısı
            if (userDao.findByUsername("editor") == null) {
                Editor editor = new Editor("editor", "editor123", "editor@example.com", "editor");
                userDao.save(editor);
                System.out.println("Editor user 'editor' saved.");
            } else {
                System.out.println("Editor user 'editor' already exists.");
            }

            // Yazar kullanıcıları
            if (userDao.findByUsername("author") == null) {
                Author author = new Author("author", "author123", "author@example.com", "author");
                userDao.save(author);
                System.out.println("Author user 'author' saved.");
            } else {
                System.out.println("Author user 'author' already exists.");
            }

        } catch (Exception e) {
            System.err.println("Error saving test users to database: " + e.getMessage());
        }

        // Uygulama, LoginFrame'i görünür hale getirerek başlar.
        // Kullanıcı kimlik doğrulaması ve kullanıcı verilerinin yüklenmesi
        // (Yönetici, Yazar, Editör) artık UserDao ve veritabanı kullanılarak LoginFrame tarafından yapılır.
        new LoginFrame().setVisible(true);
    }
}

