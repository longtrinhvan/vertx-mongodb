package demo.factory.repo;

import lombok.NonNull;
import demo.utils.mongodb.MongoDBOperator;

import java.util.Map;
import java.util.Optional;

/**
 * An interface for OMI entity database operators
 *
 * @param <T>
 * @version 1.0
 */
public interface GenericRepository<T> {

  MongoDBOperator<T> getMongoDBOperator();

  Optional<T> getById(@NonNull String id);

  Optional<T> get(@NonNull Map<String, Object> query);


  /* INSERT */
  Optional<T> add(@NonNull T t);

  Optional<T> update(@NonNull String id, @NonNull T t);

  Optional<T> updateOne(@NonNull Map<String, Object> query, @NonNull Map<String, Object> update);

  Optional<Boolean> update(@NonNull Map<String, Object> queryString,
      @NonNull Map<String, Object> updateString);

  Optional<Boolean> delete(@NonNull String id);

}
