package demo.utils.vertx.rest;

import demo.ioc.configuration.ENVConfig;
import demo.utils.enumeration.TimeEnum;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.TimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@Log4j2
@RequiredArgsConstructor
public class RESTfulVerticle extends AbstractVerticle {

    private static final long BODY_LIMIT = 50 * 1024 * 1024; // 50 MB
    private List<RequestHandler> requestHandlers = new ArrayList<>();
    protected final ENVConfig applicationConfig;

    private void configureRoutes(Router router) {
        this.requestHandlers.forEach(requestHandler -> {
            router.route(requestHandler.getPath())
                    .handler(h -> {
                        h.request();
                        h.next();
                    }).blockingHandler(requestHandler.handle());
            log.info("Configuring route {}:{}", requestHandler.getMethod(), requestHandler.getPath());
        });
    }

    @Override
    public void start(Future<Void> startFuture) {
        Router router = Router.router(vertx);
        router.route().handler(TimeoutHandler.create(TimeEnum.MILLISECOND_OF_FIVE_MINUTE));
        router.route().handler(BodyHandler.create()
                .setDeleteUploadedFilesOnEnd(true)
                .setBodyLimit(BODY_LIMIT)
                .setUploadsDirectory(applicationConfig.getStringProperty("application.upload.directory", "/root/tmp/")));
        router.route().handler(CorsHandler.create("*")
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.HEAD)
                .allowedMethod(HttpMethod.PUT)
                .allowedMethod(HttpMethod.DELETE)
                .allowedMethod(HttpMethod.PATCH)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedHeader("Access-Control-Allow-Methods")
                .allowedHeader("Access-Control-Allow-Origin")
                .allowedHeader("Access-Control-Allow-Credentials")
                .allowedHeader("Access-Control-Allow-Headers")
                .allowedHeader("Content-Type")
                .allowedHeader("Authorization")
                .allowedHeader("Cache-Control")
                .allowedHeader("X-Requested-With")
                .allowedHeader("Accept")
                .allowedHeader("Origin"));
        configureRoutes(router);
        vertx.createHttpServer().requestHandler(router)
                .connectionHandler(conn -> conn.exceptionHandler(err -> {
                    err.printStackTrace();
                    log.error("Vertx HTTP GOT ERROR 2: " + err.getMessage());
                }))
                .exceptionHandler(err -> log.error("Vertx HTTP GOT ERROR 1: " + err.getMessage()))
                .listen(Integer.parseInt(applicationConfig.getStringProperty("application.port", "8080")));
        startFuture.succeeded();
    }

    public void setRequestHandlers(List<RequestHandler> requestHandlers) {
        this.requestHandlers = requestHandlers;
    }
}
