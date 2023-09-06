package demo.utils.mongodb;

import com.mongodb.ReadConcern;
import com.mongodb.ReadConcernLevel;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * @param <ORMClass>
 */
public class KijoKokoroMongoDBOperator<ORMClass> implements MongoDBOperator<ORMClass> {

  private final MongoCollection<ORMClass> _collection;

  public KijoKokoroMongoDBOperator(KijiKokoroMongoDB mongoDB, String databaseURL, String dbName,
                                   String collection,
                                   Class<ORMClass> ormClass) {
    _collection = mongoDB.getMongoDatabase(databaseURL, dbName).getCollection(collection, ormClass);
  }

  public KijoKokoroMongoDBOperator(String databaseURL, String dbName, String collection,
                                   Class<ORMClass> ormClass) {
    _collection = KijiKokoroMongoDB.getMongoDatabase(KijiKokoroMongoDB.MongoDBConfigBuilder.config()
            .withConnectionURL(databaseURL).withDatabaseName(dbName).build())
        .getCollection(collection, ormClass);
  }

  @Override
  public ORMClass find(Document query, Document sort, Document projection) {
    return _collection.find(query).first();
  }

  @Override
  public ORMClass findWithConcern(Document query, Document sort, Document projection) {
    ReadConcern readConcern = new ReadConcern(ReadConcernLevel.MAJORITY);
    _collection.withReadConcern(readConcern).find(query).first();
    return _collection.find(query).first();
  }

  @Override
  public ORMClass find(Bson query, Document sort, Document projection) {
    return _collection.find(query).first();
  }

  @Override
  public ORMClass findOneWithProjection(Bson query, Document sort, Document projection) {
    return _collection.find(query).projection(projection).first();
  }

  @Override
  public List<ORMClass> findMany(Document query, Bson sort, Document projection, int skips,
      int limit) {
    return _collection.find(query).sort(sort).projection(projection).skip(skips).limit(limit)
        .into(new ArrayList<>());
  }

  @Override
  public List<ORMClass> findMany(Document query, Bson sort, Document projection, int skips,
      int limit, Bson hint) {
    return _collection.find(query).hint(hint).sort(sort).projection(projection).skip(skips)
        .limit(limit).into(new ArrayList<>());
  }


  @Override
  public List<ORMClass> findManyCollation(Document query, Bson sort, Document projection, int skips,
      int limit) {
    return _collection.find(query).collation(Collation.builder().locale("en").build()).sort(sort)
        .projection(projection)
        .skip(skips).limit(limit).into(new ArrayList<>());
  }

  @Override
  public FindIterable<Document> findManyV3(Document query, Bson sort, Document projection,
      int skips, int limit) {
    return _collection.find(query, Document.class).sort(sort).projection(projection).skip(skips)
        .limit(limit);
  }

  @Override
  public List<ORMClass> findAll(Document query, Bson sort, Document projection) {
    return _collection.find(query).sort(sort).projection(projection).into(new ArrayList<>());
  }

  @Override
  public Long count(Document query) {
    return _collection.countDocuments(query);
  }

  @Override
  public Long count(Bson query) {
    return _collection.countDocuments(query);
  }

  @Override
  public ORMClass findOneAndUpdate(@NotEmpty Document query, @NotEmpty Document data) {
    return _collection.findOneAndUpdate(query, data,
        new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER));
  }

  @Override
  public ORMClass findOneAndUpdate(Document query, Document data, List<Document> arrayFilters) {
    return _collection.findOneAndUpdate(query, data,
        new FindOneAndUpdateOptions().arrayFilters(arrayFilters)
            .returnDocument(ReturnDocument.AFTER));
  }

  @Override
  public UpdateResult update(Document query, Document data) {
    return _collection.updateOne(query, data);
  }

  @Override
  public ORMClass findOneAndUpsert(Document query, Document update, Document setOnInsesrt) {
    return _collection.findOneAndUpdate(query, update.append("$setOnInsert", setOnInsesrt),
        new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER).upsert(true));
  }

  @Override
  public ORMClass findOneAndUpsert(Document query, Document update) {
    return _collection.findOneAndUpdate(query, update, new FindOneAndUpdateOptions()
        .returnDocument(ReturnDocument.AFTER).upsert(true));
  }

  @Override
  public UpdateResult upsert(Document query, Document data) {
    return _collection.updateOne(query, data, new UpdateOptions().upsert(true));
  }

  @Override
  public UpdateResult updateMany(Document query, Document data) {
    return _collection.updateMany(query, data);
  }

  @Override
  public DeleteResult removeMany(Document query) {
    return _collection.deleteMany(query);
  }

  @Override
  public UpdateResult updateMany(Document query, Document data, List<Document> arrayFilters) {
    if (CollectionUtils.isEmpty(arrayFilters)) {
      return this.updateMany(query, data);
    }
    return _collection.updateMany(query, data, new UpdateOptions().arrayFilters(arrayFilters));
  }

  @Override
  public ORMClass insert(ORMClass data) {
    _collection.insertOne(data);
    return data;
  }

  @Override
  public List<ORMClass> insertMany(List<ORMClass> data) {
    _collection.insertMany(data);
    return data;
  }

  @Override
  public BulkWriteResult updateMany(List<UpdateOneModel<ORMClass>> data) {
    return _collection.bulkWrite(data);
  }

  @Override
  public AggregateIterable<Document> aggregateSpecial(List<Bson> pipeline) {
    return _collection.aggregate(pipeline, Document.class).allowDiskUse(true);
  }

  @Override
  public AggregateIterable<Document> aggregateSpecialCollation(List<Document> pipeline) {
    return _collection.aggregate(pipeline, Document.class)
        .collation(Collation.builder().locale("en_US")
            .collationStrength(CollationStrength.PRIMARY).build()).allowDiskUse(true);
  }

  @Override
  public List<ORMClass> aggregate(List<Bson> pipeline) {
    return _collection.aggregate(pipeline).allowDiskUse(true).into(new ArrayList<>());
  }

  @Override
  public BulkWriteResult updateManyBulk(List<UpdateManyModel<ORMClass>> updates) {
    return _collection.bulkWrite(updates);
  }
}
