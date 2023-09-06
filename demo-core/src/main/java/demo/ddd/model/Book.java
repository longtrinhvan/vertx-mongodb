package demo.ddd.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import demo.factory.repo.PO;
import eu.dozd.mongo.annotation.Entity;
import eu.dozd.mongo.annotation.Id;
import lombok.*;
import org.bson.types.ObjectId;

import java.io.Serializable;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Book extends PO implements Serializable {
    @JsonSerialize(using = ToStringSerializer.class)
    @Id
    public ObjectId _id;
    public String title;
    public String author;
    public Long year;
    public String publisher;
    public double cost;
    public Boolean is_deleted = false;
}
