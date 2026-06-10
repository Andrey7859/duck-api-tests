package autotests.tests.controller;

import autotests.clients.UpdateClient;
import autotests.payloads.response.UniversalMessageResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

@Epic("Тесты на duck-controller")
@Feature("Обновление характеристик утки")
@Story("Эндпоинт /api/duck/update")
public class DuckUpdateTest extends UpdateClient {
    @Test(description = "Изменяем цвет и высоту уточки")
    @CitrusTest
    public void updateColorAndHeightTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        runner.variable("duckId", "citrus:randomNumber(4,false)");
        executeDatabase(runner, "INSERT INTO Duck (id, color, height, material, sound, wings_state) \n" +
                "VALUES ('${duckId}','black', 0.2, 'wood', 'quack', 'ACTIVE');");
        UniversalMessageResponse expected = new UniversalMessageResponse()
                .message("Duck with id = ${duckId} is updated");

        // do
        updateDuck(runner, "red", 0.5, "${duckId}", "wood", "quack", "ACTIVE");

        // check
        validateDuckDatabase(runner, "${duckId}", "red", "0.5", "wood", "quack", "ACTIVE");
        validateResponsePayload(runner, HttpStatus.OK, expected);

        // repair
        executeDatabase(runner, "DELETE FROM Duck WHERE id = ${duckId}");
    }

    @Test(description = "Изменяем цвет и звук уточки")
    @CitrusTest
    public void updateColorAndSoundTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        runner.variable("duckId", "citrus:randomNumber(4,false)");
        executeDatabase(runner, "INSERT INTO Duck (id, color, height, material, sound, wings_state) \n" +
                "VALUES ('${duckId}','black', 0.2, 'wood', 'quack', 'ACTIVE');");
        UniversalMessageResponse expected = new UniversalMessageResponse()
                .message("Duck with id = ${duckId} is updated");

        // do
        updateDuck(runner, "green", 0.2, "${duckId}", "wood", "moo-moo", "ACTIVE");

        // check
        validateDuckDatabase(runner, "${duckId}", "green", "0.2", "wood", "moo-moo", "ACTIVE");
        validateResponseResources(runner, HttpStatus.OK, "response/duckUpdateTest/updateDuck.json", false);

        // repair
        executeDatabase(runner, "DELETE FROM Duck WHERE id = ${duckId}");
    }
}