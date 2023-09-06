package demo.api;


import com.alibaba.fastjson.JSON;
import demo.ddd.model.Book;
import demo.ddd.service.IBookService;
import demo.ioc.servlet.AbstractResource;
import io.vertx.ext.web.RoutingContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class BookVertController extends AbstractResource {

    @Autowired
    IBookService ibookService;

    public void add(RoutingContext routingContext) {
        try {
            String body = routingContext.getBodyAsString();
            Book book = JSON.parseObject(body, Book.class);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(outputJson(9999, ibookService.add(book, true).orElse(null)));
        } catch (Throwable throwable) {
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(outputJson(-9999, throwable.getMessage()));
        }
    }

    public void get(RoutingContext routingContext) {
        try {
            String _id = routingContext.pathParam("id");
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(outputJson(9999, ibookService.getById(_id, true).orElse(null)));
        } catch (Throwable throwable) {
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(outputJson(-9999, throwable.getMessage()));
        }
    }

    public void delete(RoutingContext routingContext) {
        try {
            String _id = routingContext.pathParam("id");
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(outputJson(9999, ibookService.delete(_id).orElse(null)));
        } catch (Throwable throwable) {
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(outputJson(-9999, throwable.getMessage()));
        }
    }

    public void update(RoutingContext routingContext) {
        try {
            String _id = routingContext.pathParam("id");
            String body = routingContext.getBodyAsString();

            Book book = JSON.parseObject(body, Book.class);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(outputJson(9999, ibookService.update(_id, book, true).orElse(null)));
        } catch (Throwable throwable) {
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(outputJson(-9999, throwable.getMessage()));
        }
    }

}
