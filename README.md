# Book Publishing System

A comprehensive digital management system developed to streamline the book publishing lifecycle.  
This project facilitates task assignment, role-based access control, and structured communication between authors, editors, and administrators.

---

## 📌 Project Overview

The system is designed to make the writing and editing stages of books more **trackable, organized, and secure**.  
Each user role (**Admin, Author, Editor**) can only access data relevant to their responsibilities, ensuring system integrity and proper authorization.

---

## 👥 User Roles & Permissions

### 🛡️ Admin
- Acts as the system overseer  
- Manages all system resources  
- Adds, updates, and deletes books  
- Assigns tasks to authors and editors  

### ✍️ Author
- Represents content creators  
- Completes assigned tasks  
- Submits comments  
- Performance is tracked based on task completion and published works  

### 🧠 Editor
- Responsible for the review process  
- Evaluates tasks according to experience level (`reviewLevel`)  
- Generates reports and marks tasks as reviewed or completed  

---

## 🛠️ Core Objects & System Logic

### 📚 Book Management
- Full CRUD operations (Create, Read, Update, Delete)
- Category-based organization

### 📝 Task System
- Manages assignments such as writing, reviewing, and correcting books  
- Tasks are linked directly to Authors or Editors  

### ⏱️ Deadline Checker
- Prevents assigning tasks with deadlines less than **7 days** in the future  

### 🔐 Session & Security
- Handles active user sessions  
- Logs login attempts using `LoginLog` for auditing purposes  

---

## ⚖️ Business Rules & Constraints

To ensure a realistic and functional workflow, the system enforces the following rules:

### 📅 Scheduling
- An author cannot add more than **one book on the same date**
- A user cannot be assigned **more than one task per day**

### ✅ Validation
- Usernames must be **unique**
- Passwords must be at least **7 characters**
- Email addresses must contain the **"@"** symbol

### ⏳ Deadlines
- All tasks must have deadlines **at least 7 days ahead**

### 🗑️ Persistence
- Once a book is deleted, it **cannot be recovered**

---

## 🗄️ Database Architecture

The project uses **Jakarta Persistence API (JPA)** with **Hibernate** for database operations.

### 🧬 Inheritance Strategy
- `SINGLE_TABLE` inheritance for the `User` class  
- Admin, Author, and Editor are stored in a single table  
- Role differentiation via a discriminator column  

### 🔗 Entity Relationships
- **Author → Book**: One-to-Many  
- **Book → Task**: One-to-Many  
- **User → Task**: One-to-Many  

---

## 🔧 Technologies Used

- **Language:** Java  
- **UI:** Java Swing  
- **ORM:** Hibernate / Jakarta Persistence API (JPA)  
- **IDE:** NetBeans  
- **Configuration:** `persistence.xml` (MidProjectPU)
