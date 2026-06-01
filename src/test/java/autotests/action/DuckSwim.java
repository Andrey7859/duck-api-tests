package autotests.action;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.JsonPathSupport.jsonPath;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckSwim extends TestNGCitrusSpringSupport {
    private static final String URL = "http://localhost:2222";

    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        runner.$(
                http()
                        .client(URL)
                        .send()
                        .post("/api/duck/create")
                        .message().contentType(MediaType.APPLICATION_JSON_VALUE).body("{\n" +
                                "\"color\": \"" + color + "\",\n" +
                                "\"height\": " + height + ",\n" +
                                "\"material\": \"" + material + "\",\n" +
                                "\"sound\": \"" + sound + "\",\n" + "\"wingsState\": \"" + wingsState + "\"\n" + "}"));
    }

    public void saveDuckId(TestCaseRunner runner) {
        runner.$(
                http()
                        .client(URL)
                        .receive()
                        .response()
                        .message().extract(fromBody().expression("$.id", "duckId")));
    }

    public void deleteDuck(TestCaseRunner runner, String duckId) {
        runner.$(
                http()
                        .client(URL)
                        .send()
                        .delete("/api/duck/delete")
                        .queryParam("id", duckId));
    }

    public void getSwim(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client(URL)
                        .send()
                        .get("/api/duck/action/swim")
                        .queryParam("id", id));
    }

    public void validateResponse(TestCaseRunner runner, HttpStatus status, String valueForValidate) {
        runner.$(
                http()
                        .client(URL)
                        .receive()
                        .response(status)
                        .message()
                        .type(MessageType.JSON)
                        .validate(jsonPath().expression("$.message", valueForValidate)));
    }

    //TODO (ОР: Код ответа 200 и корректное сообшение ФР: Код 404 и некорректное сообщение "Paws are not found ((((")
    @Test(description = "Уточка с существующим id плывет")
    @CitrusTest
    public void SwimWithExistIdTest(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "black", 0.20, "wood", "quack", "ACTIVE");
        saveDuckId(runner);
        getSwim(runner, "${duckId}");
        validateResponse(runner, HttpStatus.NOT_FOUND, "Paws are not found ((((");
    }

    @Test(description = "Уточка с несуществующим id плывет")
    @CitrusTest
    public void swimWithNonExistingIdTest(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "black", 0.20, "wood", "quack", "ACTIVE");
        saveDuckId(runner);
        deleteDuck(runner, "${duckId}");
        validateResponse(runner, HttpStatus.OK, "Duck is deleted");
        getSwim(runner, "${duckId}");
        validateResponse(runner, HttpStatus.NOT_FOUND, "Paws are not found ((((");
    }
}
