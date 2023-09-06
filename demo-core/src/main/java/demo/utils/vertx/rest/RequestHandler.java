

package demo.utils.vertx.rest;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

public interface RequestHandler {
    HttpMethod getMethod();

    String getPath();

    Handler<RoutingContext> handle();

    static RequestHandler init(HttpMethod httpMethod, String path, Handler<RoutingContext> handler) {
        return new RequestHandler() {
            @Override
            public HttpMethod getMethod() {
                return httpMethod;
            }

            @Override
            public String getPath() {
                return path;
            }

            @Override
            public Handler<RoutingContext> handle() {
                return handler;
            }
        };
    }
}
