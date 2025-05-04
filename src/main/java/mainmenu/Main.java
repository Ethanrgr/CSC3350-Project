package mainmenu;
import controller.adminController;
import controller.authController;
import controller.employeeController;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.http.Context;

import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;
public class Main {
    public static void main(String[] args) {
        authController authorization = new authController();
        adminController adminFunctions = new adminController();
        employeeController employeeFunctions = new employeeController();

        Javalin app = Javalin.create(
                config -> {
                    config.staticFiles.add("src/main/resources",Location.EXTERNAL);

                    config.router.apiBuilder(() -> {
                        path("/api", () -> {
                            // Authentication endpoints
                            post("login", authorization::login);

                            post("register", authorization::register);
                            
                            // Admin endpoints - protected for admin access only
                            path("admin", () -> {
                                before(authorization::adminAuthMiddleware);
                                
                                // Employee management
                                get("employees", adminFunctions::getAllEmployees);
                                get("employees/{id}", adminFunctions::getEmployeeById);
                                post("employees", adminFunctions::createEmployee);
                                put("employees/{id}", adminFunctions::updateEmployee);
                                delete("employees/{id}", adminFunctions::deleteEmployee);

                                get("search", adminFunctions::searchEmployees);
                                
                                // Salary management
                                put("employees/salary/range", adminFunctions::updateSalaryRange);
                                
                                // Reports
                                get("reports/paystatements", adminFunctions::getAllPayStatements);
                                get("reports/pay/jobtitle", adminFunctions::getTotalPayByJobTitle);
                                get("reports/pay/division", adminFunctions::getTotalPayByDivision);
                            });
                            
                            path("employee", () -> {
                                before(authorization::employeeAuthMiddleware);
                                
                                // Personal data view endpoint
                                get("profile", employeeFunctions::getEmployeeProfile);
                                
                                // Personal pay statements
                                get("paystatements", employeeFunctions::getEmployeePayStatements);
                            });
                        });
                    });
                }
        ).start(8010);

        // Add CORS headers for all requests
        app.before(ctx -> {
            addCorsHeaders(ctx);
        });

        // Handle OPTIONS requests for CORS preflight - must respond with 200 OK
        app.options("/*", ctx -> {
            addCorsHeaders(ctx);
            ctx.status(200).result("");
        });
    }
    
    // Helper method to add CORS headers
    private static void addCorsHeaders(Context ctx) {
        ctx.header("Access-Control-Allow-Origin", "http://localhost:5173"); // Specific origin instead of wildcard
        ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH");
        ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With, Accept");
        ctx.header("Access-Control-Allow-Credentials", "true");
        ctx.header("Access-Control-Max-Age", "86400");
    }
}
