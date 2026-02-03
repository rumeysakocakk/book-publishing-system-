
package midproject1;

import midproject1.Book;
import midproject1.Author;
import java.util.ArrayList;

public class BookManagement {
    private ArrayList<Book> books; // List to store books
    private ArrayList<Category> categories; // List to store categories

    // Constructor: Initializes books and categories lists
    public BookManagement() {
        this.books = new ArrayList<>(); // Creates a new list for books
        this.categories = new ArrayList<>(); // Creates a new list for categories
        initializeDefaultCategories(); // Adds default categories
    }

    // Method to add default categories
    private void initializeDefaultCategories() {
        // Adding default categories to the list
        categories.add(new Category("Novel"));
        categories.add(new Category("Poetry"));
        categories.add(new Category("Science"));
        categories.add(new Category("History"));
        categories.add(new Category("Biography"));
        categories.add(new Category("Philosophy"));
        categories.add(new Category("Psychology"));
        categories.add(new Category("Art"));
        categories.add(new Category("Travel"));
        categories.add(new Category("Fantasy"));
        categories.add(new Category("Science Fiction"));
        categories.add(new Category("Education"));
        categories.add(new Category("Religion"));
        categories.add(new Category("Health"));
        categories.add(new Category("Fashion"));
        categories.add(new Category("Horror"));
        categories.add(new Category("Adventure"));
        categories.add(new Category("Detective"));
        categories.add(new Category("Children's Books"));
        categories.add(new Category("Story"));
        categories.add(new Category("Humor"));
    }

    // Method to add a book to the list
    public void addBook(Book book) {
        // Check if the author already has a book on the same date
        if (!isAuthorBookConflict(book)) {
            books.add(book); // Add the book to the list
        } else {
            // If there is a conflict, throw an error
            throw new IllegalArgumentException("Author cannot write another book on the same date!");
        }
    }

    // Method to check if the author has a book on the same date
    private boolean isAuthorBookConflict(Book newBook) {
        // Loop through all books to check for conflict
        for (Book book : books) {
            // If the author and publication date match, it's a conflict
            if (book.getAuthor().equals(newBook.getAuthor()) &&
                book.getPublicationDate().equals(newBook.getPublicationDate())) {
                return true; // Return true if there is a conflict
            }
        }
        return false; // Return false if there is no conflict
    }

    // Method to remove a book from the list
    public void removeBook(Book book) {
        books.remove(book); // Remove the book from the list
    }

    // Method to update an existing book
    public void updateBook(Book oldBook, Book newBook) {
        // Check if the author can write another book on the same date
        if (!isAuthorBookConflict(newBook)) {
            // Find the index of the old book and replace it with the new one
            for (int i = 0; i < books.size(); i++) {
                if (books.get(i).equals(oldBook)) {
                    books.set(i, newBook); // Replace the old book with the new book
                    return;
                }
            }
        } else {
            // If there is a conflict, throw an error
            throw new IllegalArgumentException("Author cannot write another book on the same date!");
        }
    }

    // Method to get books by category
    public ArrayList<Book> getBooksByCategory(Category category) {
        ArrayList<Book> result = new ArrayList<>(); // Create a new list to store the results
        for (Book book : books) {
            // If the book's category matches, add it to the result list
            if (book.getCategory().equals(category)) {
                result.add(book);
            }
        }
        return result; // Return the list of books
    }

    // Method to get books by author
    public ArrayList<Book> getBooksByAuthor(Author author) {
        ArrayList<Book> result = new ArrayList<>(); // Create a new list to store the results
        for (Book book : books) {
            // If the book's author matches, add it to the result list
            if (book.getAuthor().equals(author)) {
                result.add(book);
            }
        }
        return result; // Return the list of books
    }

    // Method to get all books
    public ArrayList<Book> getAllBooks() {
        return new ArrayList<>(books); // Return a copy of the books list
    }

    // Method to get all categories
    public ArrayList<Category> getAllCategories() {
        return new ArrayList<>(categories); // Return a copy of the categories list
    }

    // Method to add a new category
    public void addCategory(Category category) {
        // Check if the category is not already in the list
        for (Category cat : categories) {
            if (cat.equals(category)) {
                return; // If the category is already in the list, do nothing
            }
        }
        categories.add(category); // Add the category to the list
    }

    // Method to remove a category
    public void removeCategory(Category category) {
        categories.remove(category); // Remove the category from the list
    }
}
