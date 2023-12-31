package demo.utils.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import eu.dozd.mongo.MongoMapper;
import lombok.extern.log4j.Log4j2;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import demo.utils.crypto.SHA512;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Component
public class KijiKokoroMongoDB {

  private static final Map<String, KijiKokoroMongoDB> providers = new ConcurrentHashMap<>();
  private MongoDatabase _mongoDatabase;

  public interface factory {

    KijiKokoroMongoDB create();
  }

  @Autowired
  public KijiKokoroMongoDB() {
  }

  private KijiKokoroMongoDB(String connectionURL, String databaseName) {
    CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        CodecRegistries.fromProviders(MongoMapper.getProviders()));

    MongoClient _mongo = new MongoClient(new MongoClientURI(connectionURL));
    _mongoDatabase = _mongo.getDatabase(databaseName).withCodecRegistry(codecRegistry);
  }

  public static MongoDatabase getMongoDatabase(MongoDBConfigBuilder mongoDBConfigBuilder) {
    return getInstance(mongoDBConfigBuilder)._mongoDatabase;
  }

  public static KijiKokoroMongoDB getInstance(MongoDBConfigBuilder mongoDBConfigBuilder) {
    try {
      String key = getKeyMap(mongoDBConfigBuilder.connectionURL, mongoDBConfigBuilder.databaseName);
      if (!providers.containsKey(key)) {
        registerProvider(mongoDBConfigBuilder.connectionURL, mongoDBConfigBuilder.databaseName);
      }
      return providers.get(key);
    } catch (Throwable e) {
      e.printStackTrace();
      throw e;
    }
  }

  private static void registerProvider(String connectionURL, String databaseName) {
    String key = getKeyMap(connectionURL, databaseName);
    if (!providers.containsKey(key)) {
      providers.put(key, new KijiKokoroMongoDB(connectionURL, databaseName));
    }
  }

  MongoDatabase getMongoDatabase(String connectionURL, String dataBaseName) {
    KijiKokoroMongoDB db = providers.get(getKeyMap(connectionURL, dataBaseName));
    if (db == null) {
      log.error("can't get db with dbName: " + dataBaseName);
    }

    return db._mongoDatabase;
  }

  public static final class MongoDBConfigBuilder {

    private String connectionURL;
    private String databaseName;

    private MongoDBConfigBuilder() {
    }

    public static MongoDBConfigBuilder config() {
      return new MongoDBConfigBuilder();
    }

    public MongoDBConfigBuilder withConnectionURL(String connectionURL) {
      this.connectionURL = connectionURL;
      return this;
    }

    public MongoDBConfigBuilder withDatabaseName(String databaseName) {
      this.databaseName = databaseName;
      return this;
    }

    public MongoDBConfigBuilder build() {
      return this;
    }

  }

  private static String getKeyMap(String url, String dbName) {
    try {
      return SHA512.valueOf(url + dbName);
    } catch (Throwable e) {
      e.printStackTrace();
      return UUID.fromString(url + dbName).toString();
    }
  }
}
