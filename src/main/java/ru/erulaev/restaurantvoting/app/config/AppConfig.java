package ru.erulaev.restaurantvoting.app.config;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.http.ProblemDetail;
import ru.erulaev.restaurantvoting.common.util.JsonUtil;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

@Configuration
@Slf4j
public class AppConfig {

    @Profile("!test")
    @Bean(initMethod = "start", destroyMethod = "stop")
    Server h2Server() throws SQLException {
        log.info("Start H2 TCP server");
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }

    //   https://stackoverflow.com/a/74630129/548473
    @JsonAutoDetect(fieldVisibility = NONE, getterVisibility = ANY)
    interface MixIn {
        @JsonAnyGetter
        Map<String, Object> getProperties();
    }

    @Autowired
    void configureAndStoreObjectMapper(ObjectMapper objectMapper, Environment environment) {
        objectMapper.registerModule(new Hibernate6Module());
        // ErrorHandling: https://stackoverflow.com/questions/7421474/548473
        objectMapper.addMixIn(ProblemDetail.class, MixIn.class);

        if (Arrays.asList(environment.getActiveProfiles()).contains("test")) {
            objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
                @Override
                public JsonProperty.Access findPropertyAccess(Annotated m) {
                    return JsonProperty.Access.READ_WRITE;
                }
            });
        }

        JsonUtil.setMapper(objectMapper);
    }
}