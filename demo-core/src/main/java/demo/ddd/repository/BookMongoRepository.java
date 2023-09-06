package demo.ddd.repository;

import demo.ddd.model.Book;
import demo.ioc.configuration.ENVConfig;
import demo.factory.repo.GenericMongoRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import demo.utils.enumeration.ApplicationProperties;
import demo.utils.enumeration.DBTableMapping;
import demo.utils.mongodb.KijoKokoroMongoDBOperator;
import demo.utils.mongodb.MongoDBOperator;

@Log4j2
@Component
public class BookMongoRepository extends GenericMongoRepository<Book> implements BookRepository {
    private final MongoDBOperator<Book> mongoDBOperator;

    @Autowired
    public BookMongoRepository(ENVConfig applicationConfig) {
        mongoDBOperator = new KijoKokoroMongoDBOperator<>(
                applicationConfig.getStringProperty(ApplicationProperties.MONGODB),
                DBTableMapping.DATABESE,
                DBTableMapping._LIST_TABLE.BOOK,
                Book.class);
    }

    @Override
    public MongoDBOperator<Book> getMongoDBOperator() {
        return mongoDBOperator;
    }
}
