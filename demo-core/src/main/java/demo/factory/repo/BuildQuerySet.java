package demo.factory.repo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.google.gson.*;
import demo.ddd.model.TransField;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
public class BuildQuerySet {

  private static final ObjectMapper objectMapper = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .registerModule(new SimpleModule() {
        @Override
        public void setupModule(SetupContext context) {
          super.setupModule(context);
          context.addBeanSerializerModifier(new Custom2BeanSerializerModifier());
        }
      });

  private static final ObjectMapper objectMapperNonNull = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .setSerializationInclusion(JsonInclude.Include.NON_NULL)
      .registerModule(new SimpleModule() {
        @Override
        public void setupModule(SetupContext context) {
          super.setupModule(context);
          context.addBeanSerializerModifier(new Custom2BeanSerializerModifier());
        }
      });

  private static class Custom2BeanSerializerModifier extends BeanSerializerModifier {

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config,
        BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
      return beanProperties.stream()
          .filter(property -> property.getAnnotation(TransField.class) == null)
          .collect(Collectors.toList());
    }
  }

  private static final Gson gson = new GsonBuilder()
      .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
      .registerTypeAdapter(ObjectId.class,
          (JsonSerializer<ObjectId>) (src, typeOfSrc, context) -> new JsonPrimitive(
              src.toHexString()))
      .registerTypeAdapter(ObjectId.class,
          (JsonDeserializer<ObjectId>) (json, typeOfT, context) -> new ObjectId(json.getAsString()))
      .addDeserializationExclusionStrategy(new HiddenAnnotationExclusionStrategy())
      .create();

  private static final class HiddenAnnotationExclusionStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
      return f.getAnnotation(TransField.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
      return false;
    }
  }

  public static Document buildQuery(@NonNull Object object) {
    TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
    };
    Map<String, Object> data = objectMapper.convertValue(object, typeRef);
    Document queryItem = new Document();
    for (Map.Entry<String, Object> entry : data.entrySet()) {
      queryItem.put(entry.getKey(), entry.getValue());
    }
    queryItem.remove("_id");
    return queryItem;
  }

  public static Document buildQuerySet(@NonNull Object object) {
    TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
    };
    Map<String, Object> data = objectMapper.convertValue(object, typeRef);
    Document queryItem = new Document();
    for (Map.Entry<String, Object> entry : data.entrySet()) {
      queryItem.put(entry.getKey(), entry.getValue());
    }
    Document query = new Document();
    queryItem.remove("_id");
    query.put("$set", queryItem);
    return query;
  }

  public static Document buildQuerySetNonNull(@NonNull Object object) {
    TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
    };
    Map<String, Object> data = objectMapperNonNull.convertValue(object, typeRef);
    Document queryItem = new Document();
    for (Map.Entry<String, Object> entry : data.entrySet()) {
      Object value = entry.getValue();
      try {
        if (entry.getKey() != null) {
          Field field = object.getClass().getDeclaredField(entry.getKey());
          if (field.getType().equals(ObjectId.class)) {
            value = new ObjectId(entry.getValue().toString());
          }
        }
      } catch (Exception e) {
//                e.printStackTrace();
        value = entry.getValue();
      }
      queryItem.put(entry.getKey(), value);
    }
    Document query = new Document();
    queryItem.remove("_id");
    query.put("$set", queryItem);
    return query;
  }

  public static Document buildQueryUpsert(@NonNull Object object) {
    TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
    };
    Map<String, Object> data = objectMapper.convertValue(object, typeRef);
    Document queryItem = new Document();
    for (Map.Entry<String, Object> entry : data.entrySet()) {
      queryItem.put(entry.getKey(), entry.getValue());
    }
    Document query = new Document();
    queryItem.remove("_id");
    queryItem.remove("created_date");
    query.put("$set", queryItem);
    return query;
  }

  public static Map<String, Object> buildContent(@NonNull Object object) {
    TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
    };
    Map<String, Object> data = objectMapper.convertValue(object, typeRef);
    data.put("id", data.get("_id"));
    return data;
  }
}
