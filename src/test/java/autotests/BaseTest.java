package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.actions.HttpClientResponseActionBuilder;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class BaseTest extends TestNGCitrusSpringSupport {
    @Autowired
    protected HttpClient duckService;

    @Autowired
    protected SingleConnectionDataSource testDb;

    public void executeDatabase(TestCaseRunner runner, String query) {
        runner.$(sql(testDb).statement(query));
    }

    public void preparePostRequest(TestCaseRunner runner, String path, Object userData) {
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

    public void prepareGetRequest(TestCaseRunner runner, String path) {
        runner.$(http().client(duckService)
                .send()
                .get(path));
    }

    public void preparePutRequest(TestCaseRunner runner, String path) {
        runner.$(http().client(duckService)
                .send()
                .put(path));
    }

    public void prepareDeleteRequest(TestCaseRunner runner, String path) {
        runner.$(http().client(duckService)
                .send()
                .delete(path));
    }

    public HttpClientResponseActionBuilder.HttpMessageBuilderSupport prepareResponse(HttpStatus status) {
        return http()
                .client(duckService)
                .receive()
                .response(status)
                .message()
                .type(MessageType.JSON);
    }

    @Step("Валидация с помощью String")
    public void validateResponse(TestCaseRunner runner, HttpStatus status, String valueForValidate) {
        var response = prepareResponse(status).body(valueForValidate);

        runner.$(response);
    }

    @Step("Валидация с помощью Payload")
    public void validateResponsePayload(TestCaseRunner runner, HttpStatus status, Object userData) {
        var response = prepareResponse(status).body(new ObjectMappingPayloadBuilder(userData, new ObjectMapper()));

        runner.$(response);
    }
}
