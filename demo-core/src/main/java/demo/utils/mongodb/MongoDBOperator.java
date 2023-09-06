package demo.utils.mongodb;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.UpdateManyModel;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public interface MongoDBOperator<ORMClass> {

    ORMClass find(@NotEmpty Document query, Document sort, Document projection);

    ORMClass findWithConcern(Document query, Document sort, Document projection);

    ORMClass find(Bson query, Document sort, Document projection);

    ORMClass findOneWithProjection(Bson query, Document sort, Document projection);

    List<ORMClass> findAll(Document query, Bson sort, Document projection);

    List<ORMClass> findMany(Document query, Bson sort, Document projection, int skips, int limit);

    List<ORMClass> findMany(Document query, Bson sort, Document projection, int skips, int limit,
                            Bson hint);

    List<ORMClass> findManyCollation(Document query, Bson sort, Document projection, int skips,
                                     int limit);

    FindIterable<Document> findManyV3(Document query, Bson sort, Document projection, int skips,
                                      int limit);

    Long count(Document query);

    Long count(Bson query);

    ORMClass findOneAndUpdate(@NotEmpty Document query, @NotEmpty Document data);

    ORMClass findOneAndUpdate(@NotEmpty Document query, @NotEmpty Document data,
                              List<Document> arrayFilters);

    ORMClass findOneAndUpsert(@NotEmpty Document query, @NotEmpty Document data,
                              Document setOnInsert);

    ORMClass findOneAndUpsert(@NotEmpty Document query, @NotEmpty Document data);

    ORMClass insert(ORMClass data);

    List<ORMClass> insertMany(List<ORMClass> data);

    BulkWriteResult updateMany(List<UpdateOneModel<ORMClass>> data);

    UpdateResult update(@NotEmpty Document query, @NotEmpty Document data);

    UpdateResult upsert(@NotEmpty Document query, @NotEmpty Document data);

    UpdateResult updateMany(@NotEmpty Document query, @NotEmpty Document data);

    DeleteResult removeMany(@NotEmpty Document query);

    UpdateResult updateMany(@NotEmpty Document query, @NotEmpty Document data,
                            List<Document> arrayFilters);

    AggregateIterable<Document> aggregateSpecial(List<Bson> pipeline);

    AggregateIterable<Document> aggregateSpecialCollation(List<Document> pipeline);

    List<ORMClass> aggregate(List<Bson> pipeline);

    BulkWriteResult updateManyBulk(List<UpdateManyModel<ORMClass>> data);
}
