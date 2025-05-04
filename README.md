# Employee Management System

A full-stack employee management system built with Java (Javalin) backend and React/Vite frontend.

## Features

### Authentication
- User login with role-based access (ADMIN or EMPLOYEE)
- Registration system that links employee accounts to the employee database

### Admin functionality
- View all employee data in a tabular format
- Create new employee records
- Update existing employee information
- Delete employee records
- Search for employees by name, date of birth, SSN, or employee ID
- Update salary for employees within a specific salary range (percentage increase)
- View reports:
  - All employee pay statements
  - Total pay by job title for a selected month/year
  - Total pay by division for a selected month/year

### Employee functionality
- View personal profile information
- View personal pay statements with optional date filtering

## Technology Stack

### Backend
- Java 11+
- Javalin web framework
- MySQL database

### Frontend
- React 19
- React Router 7
- Axios for API requests
- Custom CSS for styling

## Directory Structure

```
├── frontend/               # React frontend
│   ├── src/
│   │   ├── components/     # React components
│   │   │   ├── admin/      # Admin-specific components
│   │   │   └── employee/   # Employee-specific components
│   │   ├── context/        # React context providers
│   │   ├── pages/          # Page components
│   │   └── main.jsx        # Entry point
│   ├── package.json        # Frontend dependencies
│   └── vite.config.js      # Vite configuration
│
├── src/main/               # Java backend
│   ├── java/
│   │   ├── controller/     # API controllers
│   │   ├── dao/            # Data access objects
│   │   ├── dto/            # Data transfer objects
│   │   ├── mainmenu/       # Application entry point
│   │   └── model/          # Data models
│   └── resources/          # Static resources
│
├── schema.sql              # Database schema and sample data
└── README.md               # This file
```

### Backend Setup
1. Open the project in your Java IDE (IntelliJ IDEA, Eclipse, etc.)
2. Ensure you have JDK 11 or higher installed
3. Update the database connection details in `src/main/java/dao` files if needed:
   - Default URL: `jdbc:mysql://localhost:3306/employeedata`
   - Default Username: `root`
   - Default Password: `jinash123`
4. Run the `Main.java` class in the `mainmenu` package

### Frontend Setup
1. Navigate to the `frontend` directory
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the development server:
   ```bash
   npm run dev
   ```
4. The frontend will be available at `http://localhost:5173` (or similar port)

