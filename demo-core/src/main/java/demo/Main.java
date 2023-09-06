package demo;

import demo.api.BookVertController;
import demo.utils.vertx.VertxProvider;
import demo.utils.vertx.rest.RESTfulVerticle;
import demo.utils.vertx.rest.RequestHandler;
import io.vertx.core.http.HttpMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication(exclude = {
        MongoAutoConfiguration.class, MongoDataAutoConfiguration.class,
        RestClientAutoConfiguration.class,
        QuartzAutoConfiguration.class,
})
@RequiredArgsConstructor
public class Main {

    private final BookVertController bookVertController;
    private final VertxProvider vertxProvider;
    private final RESTfulVerticle resTfulVerticle;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @PostConstruct
    public void deployServerVerticle() {
        List<RequestHandler> requestHandlers = Arrays.asList(
                RequestHandler.init(HttpMethod.POST, "/book/add", bookVertController::add),
                RequestHandler.init(HttpMethod.GET, "/book/get/:id", bookVertController::get),
                RequestHandler.init(HttpMethod.PUT, "/book/update/:id", bookVertController::update),
                RequestHandler.init(HttpMethod.DELETE, "/book/delete/:id", bookVertController::delete)
        );
        resTfulVerticle.setRequestHandlers(requestHandlers);
        vertxProvider.getVertx().deployVerticle(resTfulVerticle);


    }
}