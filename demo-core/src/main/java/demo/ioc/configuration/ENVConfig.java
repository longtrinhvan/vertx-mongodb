package demo.ioc.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ENVConfig {

    private final Environment env;

    public String getStringProperty(String key) {
        String result = env.getProperty(key);
        return result == null ? key : result;
    }

    public String getStringProperty(String key, String defaultValue) {
        return env.getProperty(key, defaultValue);
    }
}
