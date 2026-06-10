package autotests.clients;

import autotests.BaseTest;
import com.consol.citrus.TestCaseRunner;
import io.qameta.allure.Step;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;

import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;


public class DuckClient extends BaseTest {
    @Step("Создание утки")
    public void createDuck(TestCaseRunner runner, Object userData) {
        String path = "/api/duck/create";

        preparePostRequest(runner, path, userData);
    }

    @Step("Удаление утки")
    public void deleteDuck(TestCaseRunner runner, String duckId) {
        String path = "/api/duck/delete?id=" + duckId;

        prepareDeleteRequest(runner, path);
    }

    @Step("Валидация с помощью Resources")
    public void validateResponseResources(TestCaseRunner runner, HttpStatus status, String expectedPayload,
                                          boolean extractId) {
        var response = prepareResponse(status).body(new ClassPathResource(expectedPayload));
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
}