package autotests.controller;

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

public class DuckUpdate extends TestNGCitrusSpringSupport {
    private static final String URL = "http://localhost:2222";

    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        runner.$(
                http().client(URL)
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

    public void updateDuck(TestCaseRunner runner, String color, double height, String id, String material, String sound, String wingsState) {
        runner.$(
                http()
                        .client(URL)
                        .send()
                        .put("/api/duck/update")
                        .queryParam("color", color)
                        .queryParam("height", String.valueOf(height))
                        .queryParam("id", id)
                        .queryParam("material", material)
                        .queryParam("sound", sound)
                        .queryParam("wingsState", wingsState));
    }

    public void validateResponse(TestCaseRunner runner, String valueForValidate) {
        runner.$(
                http()
                        .client(URL)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .validate(jsonPath().expression("$.message", valueForValidate)));
    }

    @Test(description = "Изменяем цвет и высоту уточки")
    @CitrusTest
    public void updateColorAndHeightTest(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "black", 0.20, "wood", "quack", "ACTIVE");
        saveDuckId(runner);
        updateDuck(runner, "red", 0.50, "${duckId}", "wood", "quack", "ACTIVE");
        validateResponse(runner, "Duck with id = ${duckId} is updated");
    }

    @Test(description = "Изменяем цвет и звук уточки")
    @CitrusTest
    public void updateColorAndSoundTest(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "black", 0.20, "wood", "quack", "ACTIVE");
        saveDuckId(runner);
        updateDuck(runner, "green", 0.20, "${duckId}", "wood", "moo-moo", "ACTIVE");
        validateResponse(runner, "Duck with id = ${duckId} is updated");
    }
}
