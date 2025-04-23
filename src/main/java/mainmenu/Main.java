package mainmenu;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;
public class Main {
    public static void main(String[] args) {

        Javalin app = Javalin.create(
                config -> {
                    config.staticFiles.add("src/main/resources",Location.EXTERNAL);

                    config.router.apiBuilder(() -> {
                        path("api", () -> {
                            get("hello", ctx -> ctx.json(Map.of("message", "Hello")));
                        });

                    });
                }
        ).start(8010);

        app.before(ctx -> {
            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            ctx.header("Access-Control-Allow-Headers", "*");
        });

    }
}
