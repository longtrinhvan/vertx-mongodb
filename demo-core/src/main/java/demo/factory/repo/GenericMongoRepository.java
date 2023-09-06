package demo.factory.repo;

import lombok.NonNull;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * An abstract class for OMI entity database operators
 *
 * @param <T>
 * @version 1.0
 */
public abstract class GenericMongoRepository<T extends PO> implements GenericRepository<T> {

  @Override
  public Optional<T> getById(@NonNull String id) {
    try {
      return Optional.ofNullable(getMongoDBOperator().find(new Document("_id", new ObjectId(id))
          .append("is_deleted", false), new Document(), new Document()));
    } catch (Throwable throwable) {
      throwable.printStackTrace();
      return Optional.empty();
    }
  }

  @Override
  public Optional<T> get(@NonNull Map<String, Object> query) {
    try {
      query.putIfAbsent("is_deleted", false);
      List<T> list = getMongoDBOperator().findMany(new Document(query), new Document(),
          new Document(), 0, 1);
      if (CollectionUtils.isNotEmpty(list)) {
        return Optional.of(list.get(0));
      }
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }
    return Optional.empty();
  }

  @Override
  public Optional<T> add(@NonNull T t) {
    try {
      t.setCreated_date(System.currentTimeMillis());
      t.setLast_updated_date(System.currentTimeMillis());
      return Optional.of(getMongoDBOperator().insert(t));
    } catch (Throwable throwable) {
      throwable.printStackTrace();
      return Optional.empty();
    }
  }

  @Override
  public Optional<T> update(@NonNull String id, @NonNull T t) {
    try {
      Document query = new Document("_id", new ObjectId(id));
      query.append("is_deleted", false);
      t.setLast_updated_date(System.currentTimeMillis());
      Document data = BuildQuerySet.buildQuerySetNonNull(t);
      return Optional.ofNullable(getMongoDBOperator().findOneAndUpdate(query, data));
    } catch (Throwable throwable) {
      throwable.printStackTrace();
      return Optional.empty();
    }
  }

  @Override
  public Optional<T> updateOne(@NonNull Map<String, Object> query,
      @NonNull Map<String, Object> update) {
    try {
      return Optional.ofNullable(
          getMongoDBOperator().findOneAndUpdate(new Document(query), new Document(update)));
    } catch (Throwable throwable) {
      throwable.printStackTrace();
      return Optional.empty();
    }
  }

  @Override
  public Optional<Boolean> update(@NonNull Map<String, Object> queryString,
      @NonNull Map<String, Object> updateString) {
    try {
      Document query = new Document(queryString);
      Document data = new Document(updateString);
      return Optional.of(getMongoDBOperator().updateMany(query, data).getModifiedCount() >= 1);
    } catch (Throwable throwable) {
      throwable.printStackTrace();
      return Optional.empty();
    }
  }

  @Override
  public Optional<Boolean> delete(@NonNull String id) {
    try {
      Document query = new Document("_id", new ObjectId(id));
      Document data = new Document("$set", new Document("is_deleted", true)
          .append("last_updated_date", System.currentTimeMillis()));
      return Optional.of(getMongoDBOperator().update(query, data).getModifiedCount() == 1);
    } catch (Throwable throwable) {
      throwable.printStackTrace();
      return Optional.of(false);
    }
  }
}
