package demo.utils.vertx;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Data

@Component
public class VertxProvider {

  private Vertx vertx;

  @Autowired
  public VertxProvider() {
    VertxOptions options = new VertxOptions();
    // warn if an worker thread handler took more than 2s to execute
    options.setMaxWorkerExecuteTime(2);
    options.setWorkerPoolSize(2000);
    options.setMaxWorkerExecuteTimeUnit(TimeUnit.SECONDS);
    options.setBlockedThreadCheckInterval(1000 * 60 * 60);
    vertx = Vertx.vertx(options);
  }
}
