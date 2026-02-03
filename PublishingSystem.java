package midproject1;

import java.util.ArrayList;

public class PublishingSystem {
    private Admin admin;  // Admin of the system

    // Static list to store all tasks in the system
    public static ArrayList<Task> allTasks = new ArrayList<>();

    // Constructor: create a system with an admin
    public PublishingSystem(Admin admin) {
        this.admin = admin;
    }

    // Method to start the system
    public void startSystem() {
        System.out.println("Publishing System Started!");  // Indicate system startup
        System.out.println(admin.generateSystemReport());   // Print admin's report
    }

    // Getter method to get the admin
    public Admin getAdmin() {
        return admin;
    }

    // Static method to add a task to the system
    public static void addTask(Task task) {
        allTasks.add(task);
    }

    // Static method to get all tasks in the system
    public static ArrayList<Task> getAllTasks() {
        return allTasks;
    }
}
