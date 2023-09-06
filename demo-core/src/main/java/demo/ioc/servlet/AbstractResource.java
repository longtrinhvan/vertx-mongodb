package demo.ioc.servlet;

import demo.ioc.configuration.ENVConfig;
import demo.utils.JSONUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.WeakHashMap;

@Log4j2
public abstract class AbstractResource {

    protected enum ReturnType {
        JACKSON,
        GSON
    }

    @Autowired
    private ENVConfig applicationConfig;

    private String refactorOutput(int code, Object entityDTO, ReturnType returnType) {
        WeakHashMap<String, Object> result = new WeakHashMap<>();
        result.put("status_code", code);
        result.put("instance_id", applicationConfig.getStringProperty("application.env", UUID.randomUUID().toString()));
        result.put("instance_version", applicationConfig.getStringProperty("info.app.version", UUID.randomUUID().toString()));
        result.put("key_enabled", false);
        result.put("payload", entityDTO);
        if (ReturnType.JACKSON.equals(returnType)) {
            return JSONUtils.objectToJson(result);
        } else if (ReturnType.GSON.equals(returnType)) {
            return JSONUtils.gsonToJSON(result);
        } else {
            return JSONUtils.toJSON(result);
        }
    }

    // default response JACKSON
    protected String outputJson(int code, Object entityDTO) {
        return this.refactorOutput(code, entityDTO, ReturnType.JACKSON);
    }
}
