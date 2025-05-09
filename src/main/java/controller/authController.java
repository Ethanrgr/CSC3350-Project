package controller;

import dao.userDAOImpl;
import dto.registrationRequest;
import io.javalin.http.Context;
import model.Role;
import model.User;
import java.util.HashMap;
import java.util.Map;

public class authController {
    private final String url = "jdbc:mysql://localhost:3306/employeedata";
    private final String sql_user = "root";
    private final String sql_password = "jinash123";

    public boolean register(Context ctx){
        registrationRequest dto = ctx.bodyAsClass(registrationRequest.class);

        if (!dto.isValid()) {
            ctx.status(400).result(dto.getValidationMessage());
            return false;
        }

        User user = new User(dto.getEmail(), dto.getPassword(), dto.getRole());
        userDAOImpl userdao = new userDAOImpl();
        boolean created = userdao.register(user, url, sql_user, sql_password);
        
        if (!created) {
            if (user.getRole() == Role.EMPLOYEE) {
                ctx.status(400).result("Registration failed. Please verify your email is in the employee database and not already registered.");
            } else {
                ctx.status(500).result("Could not register user. Please try again.");
            }
            return false;
        }
        
        ctx.status(201).json(user);
        return true;
    }
    
    public boolean login(Context ctx){
        User loginUser = ctx.bodyAsClass(User.class);
        
        if (loginUser.getEmail() == null || loginUser.getEmail().isBlank() 
                || loginUser.getPassword() == null || loginUser.getPassword().isBlank()) {
            ctx.status(400).result("Please enter email and password");
            return false;
        }
        
        userDAOImpl userDao = new userDAOImpl();
        User authenticatedUser = userDao.login(loginUser.getEmail(), loginUser.getPassword(), url, sql_user, sql_password);
        
        if (authenticatedUser == null) {
            ctx.status(401).result("Error: Invalid credentials or the user account does not exist");
            return false;
        }
        
        // Generate session token and store in context attribute
        String sessionToken = generateSessionToken(authenticatedUser);
        ctx.sessionAttribute("user", authenticatedUser);
        ctx.sessionAttribute("token", sessionToken);
        
        // Create response with token and role for frontend
        Map<String, Object> response = new HashMap<>();
        response.put("token", sessionToken);
        response.put("role", authenticatedUser.getRole().toString());
        response.put("email", authenticatedUser.getEmail());
        response.put("empid", authenticatedUser.getEmpid());
        
        ctx.status(200).json(response);
        return true;
    }
    
    private String generateSessionToken(User user) {
        return user.getEmail() + "_" + System.currentTimeMillis();
    }
    
    public void adminAuthMiddleware(Context ctx) {
        // Skip auth check for OPTIONS requests
        if (ctx.method().equals("OPTIONS")) {
            return;
        }
        
        User user = ctx.sessionAttribute("user");
        if (user == null || user.getRole() != Role.ADMIN) {
            ctx.status(403).result("Unauthorized: Admin access required");
        }
    }
    
    public void employeeAuthMiddleware(Context ctx) {
        // Skip auth check for OPTIONS requests (CORS preflight)
        if (ctx.method().equals("OPTIONS")) {
            return;
        }
        
        User user = ctx.sessionAttribute("user");
        if (user == null) {
            ctx.status(401).result("Unauthorized: Authentication required");
        }
    }
}
