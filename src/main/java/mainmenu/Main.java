package mainmenu;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(
                config -> {
                    config.staticFiles.add("src/main/resources",Location.EXTERNAL);
                }
        );

        app.start(8010);
    }
}
