
package midproject1;

public class Category {
    private String name; // Variable to store the name of the category

    // Constructor to initialize Category with name
    public Category(String name) {
        this.name = name; // Set the category name when creating a new category
    }

    // Getter for name
    public String getName() {
        return name; // Return the name of the category
    }

    // Setter for name
    public void setName(String name) {
        this.name = name; // Set a new name for the category
    }

    // toString method to represent Category as a string
    @Override
    public String toString() {
        return name; // When printing the category, just show the name
    }

    // equals method to compare Category objects based on the name
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;  // If both objects are the same reference, return true
        if (obj == null || getClass() != obj.getClass()) return false;  // If the other object is null or not the same class, return false
        Category category = (Category) obj;  // Cast the object to a Category
        return name != null ? name.equals(category.name) : category.name == null;  // If names are equal, return true; otherwise, false
    }
}