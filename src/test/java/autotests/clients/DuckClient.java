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
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;
import io.qameta.allure.Step;
import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = { EndpointConfig.class })
public class DuckClient extends TestNGCitrusSpringSupport {
    @Autowired
    protected HttpClient duckService;

    @Autowired
    protected SingleConnectionDataSource testDb;

    public void executeDatabase(TestCaseRunner runner, String query) {
        runner.$(sql(testDb).statement(query));
    }

    @Step("Создание утки")
    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound,
            String wingsState) {
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

    @Step("Создание утки")
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

    @Step("Удаление утки")
    public void deleteDuck(TestCaseRunner runner, String duckId) {
        String path = "/api/duck/delete";

        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .delete(path)
                        .queryParam("id", duckId));
    }

    @Step("Валидация с помощью String")
    public void validateResponse(TestCaseRunner runner, HttpStatus status, String valueForValidate, boolean extractId) {
        var response = http()
                .client(duckService)
                .receive()
                .response(status)
                .message()
                .type(MessageType.JSON)
                .body(valueForValidate);
        if (extractId) {
            response.extract(fromBody().expression("$.id", "duckId"));
        }
        runner.$(response);
    }

    @Step("Валидация с помощью Payload")
    public void validateResponsePayload(TestCaseRunner runner, HttpStatus status, Object userData, boolean extractId) {
        var response = http()
                .client(duckService)
                .receive()
                .response(status)
                .message()
                .type(MessageType.JSON)
                .body(new ObjectMappingPayloadBuilder(userData, new ObjectMapper()));
        if (extractId) {
            response.extract(fromBody().expression("$.id", "duckId"));
        }
        runner.$(response);
    }

    @Step("Валидация с помощью Resources")
    public void validateResponseResources(TestCaseRunner runner, HttpStatus status, String expectedPayload,
            boolean extractId) {
        var response = http()
                .client(duckService)
                .receive()
                .response(status)
                .message()
                .type(MessageType.JSON)
                .body(new ClassPathResource(expectedPayload));
        if (extractId) {
            response.extract(fromBody().expression("$.id", "duckId"));
        }
        runner.$(response);
    }

    @Step("Валидация утки в БД (sql)")
    public void validateDuckDatabase(TestCaseRunner runner, String id, String color, String height, String material,
            String sound, String wingsState) {
        runner.$(query(testDb)
                .statement("SELECT * FROM Duck WHERE ID=" + id)
                .validate("COLOR", color)
                .validate("HEIGHT", height)
                .validate("MATERIAL", material)
                .validate("SOUND", sound)
                .validate("WINGS_STATE", wingsState));
    }

    @Step("Удаление утки в БД (sql)")
    public void validateDuckDeleteDatabase(TestCaseRunner runner, String id) {
        runner.$(query(testDb)
                .statement("SELECT COUNT(*) FROM Duck WHERE ID=" + id)
                .validate("COUNT(*)", "0"));
    }

    @Step("Получение id утки")
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