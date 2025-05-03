package mainmenu;
import controller.adminController;
import controller.authController;
import controller.employeeController;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

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
                            //test
                            get("hello", ctx -> ctx.json(Map.of("message", "Hello")));

                            // Authentication endpoints
                            get("login", authorization::login);

                            post("register", authorization::register);
                            
                            // Admin endpoints - protected for admin access only
                            path("admin", () -> {
                                before(authorization::adminAuthMiddleware);
                                
                                // CRUD operations for employees
                                get("employees", adminFunctions::getAllEmployees);
                                get("employees/{id}", adminFunctions::getEmployeeById);
                                post("employees", adminFunctions::createEmployee);
                                put("employees/{id}", adminFunctions::updateEmployee);
                                delete("employees/{id}", adminFunctions::deleteEmployee);
                                
                                // Search endpoints
                                get("search", adminFunctions::searchEmployees);
                                
                                // Salary update endpoint
                                post("salary/update-range", adminFunctions::updateSalaryRange);
                                
                                // Reports
                                get("reports/paystatements", adminFunctions::getAllPayStatements);
                                get("reports/pay-by-title", adminFunctions::getTotalPayByJobTitle);
                                get("reports/pay-by-division", adminFunctions::getTotalPayByDivision);
                            });
                            
                            // Employee endpoints - protected for employee access
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

        app.before(ctx -> {
            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            ctx.header("Access-Control-Allow-Headers", "*");
        });

        // Handle OPTIONS requests for CORS preflight
        app.options("/*", ctx -> {
            ctx.status(200);
        });
    }
}
