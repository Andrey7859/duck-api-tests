package autotests.tests.controller;

import autotests.clients.UpdateClient;
import autotests.payloads.request.PropertiesRequest;
import autotests.payloads.response.UniversalMessageResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckUpdateTest extends UpdateClient {
    @Test(description = "Изменяем цвет и высоту уточки")
    @CitrusTest
    public void updateColorAndHeightTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        PropertiesRequest properties = new PropertiesRequest()
                .color("black")
                .height(0.20)
                .material("wood")
                .sound("quack")
                .wingsState("ACTIVE");
        createDuck(runner, properties);
        String id = getDuckId(runner);
        UniversalMessageResponse expected = new UniversalMessageResponse()
                .message("Duck with id = ${duckId} is updated");

        // do
        updateDuck(runner, "red", 0.50, id, "wood", "quack", "ACTIVE");

        // check
        validateResponsePayload(runner, HttpStatus.OK, expected);

        // repair
        deleteDuck(runner, id);
    }

    @Test(description = "Изменяем цвет и звук уточки")
    @CitrusTest
    public void updateColorAndSoundTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        PropertiesRequest properties = new PropertiesRequest()
                .color("black")
                .height(0.20)
                .material("wood")
                .sound("quack")
                .wingsState("ACTIVE");
        createDuck(runner, properties);
        String id = getDuckId(runner);

        // do
        updateDuck(runner, "green", 0.20, id, "wood", "moo-moo", "ACTIVE");

        // check
        validateResponseResources(runner, HttpStatus.OK, "response/duckUpdateTest/updateDuck.json");

        // repair
        deleteDuck(runner, id);
    }
}