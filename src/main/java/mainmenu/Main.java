package mainmenu;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import static io.javalin.apibuilder.ApiBuilder.*;
public class Main {
    public static void main(String[] args) {

        Javalin app = Javalin.create(
                config -> {
                    config.staticFiles.add("src/main/resources",Location.EXTERNAL);

                    config.router.apiBuilder(() -> {

                       path("/", () -> {
                           get(ctx -> ctx.redirect("/home"));
                       });

                    });
                }
        ).start(8010);

    }
}
