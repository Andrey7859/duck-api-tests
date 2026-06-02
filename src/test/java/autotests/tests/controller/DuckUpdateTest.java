package autotests.tests.controller;

import autotests.clients.UpdateClient;
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
        createDuck(runner, "black", 0.20, "wood", "quack", "ACTIVE");
        String id = getDuckId(runner);

        // do
        updateDuck(runner, "red", 0.50, id, "wood", "quack", "ACTIVE");

        // check
        validateResponse(runner, HttpStatus.OK, "{\"message\": \"Duck with id = ${duckId} is updated\"}");

        // repair
        deleteDuck(runner, id);
    }

    @Test(description = "Изменяем цвет и звук уточки")
    @CitrusTest
    public void updateColorAndSoundTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        createDuck(runner, "black", 0.20, "wood", "quack", "ACTIVE");
        String id = getDuckId(runner);

        // do
        updateDuck(runner, "green", 0.20, id, "wood", "moo-moo", "ACTIVE");

        // check
        validateResponse(runner, HttpStatus.OK, "{\"message\": \"Duck with id = ${duckId} is updated\"}");

        // repair
        deleteDuck(runner, id);
    }
}