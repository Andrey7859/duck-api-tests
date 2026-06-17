package autotests.clients;

import autotests.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckClient extends TestNGCitrusSpringSupport {
    @Autowired
    protected HttpClient duckService;

    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        String path = "/api/duck/create";
        String body = "{\n" +
                "\"color\": \"" + color + "\",\n" +
                "\"height\": " + height + ",\n" +
                "\"material\": \"" + material + "\",\n" +
                "\"sound\": \"" + sound + "\",\n" + "\"wingsState\": \"" + wingsState + "\"\n" + "}";

        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .post(path)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(body));
    }

    public void createDuck(TestCaseRunner runner, Object userData) {
        String path = "/api/duck/create";

        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .post(path)
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .type(MessageType.JSON)
                        .body(new ObjectMappingPayloadBuilder(userData, new ObjectMapper())));
    }

    public void deleteDuck(TestCaseRunner runner, String duckId) {
        String path = "/api/duck/delete";

        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .delete(path)
                        .queryParam("id", duckId));
    }

    public void validateResponse(TestCaseRunner runner, HttpStatus status, String valueForValidate) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(status)
                        .message()
                        .type(MessageType.JSON)
                        .body(valueForValidate));
    }

    public void validateResponsePayload(TestCaseRunner runner, HttpStatus status, Object userData) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(status)
                        .message()
                        .type(MessageType.JSON)
                        .body(new ObjectMappingPayloadBuilder(userData, new ObjectMapper())));
    }

    public void validateResponseResources(TestCaseRunner runner, HttpStatus status, String expectedPayload) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response(status)
                        .message()
                        .type(MessageType.JSON)
                        .body(new ClassPathResource(expectedPayload)));
    }

    public String getDuckId(TestCaseRunner runner) {
        runner.$(
                http()
                        .client(duckService)
                        .receive()
                        .response()
                        .message()
                        .extract(fromBody().expression("$.id", "duckId")));

        return "${duckId}";
    }
}