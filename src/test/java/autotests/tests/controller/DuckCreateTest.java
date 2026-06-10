package autotests.tests.controller;

import autotests.clients.DuckClient;
import autotests.payloads.request.PropertiesRequest;
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
@Feature("Создание утки")
@Story("Эндпоинт /api/duck/create")
public class DuckCreateTest extends DuckClient {
    @Test(description = "Создание утки с material = rubber")
    @CitrusTest
    public void createDuckRubberTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        PropertiesRequest properties = new PropertiesRequest()
                .color("black")
                .height(0.2)
                .material("rubber")
                .sound("quack")
                .wingsState("ACTIVE");

        // do
        createDuck(runner, properties);

        // check
        validateResponseResources(runner, HttpStatus.OK, "response/duckCreateTest/createDuckRubber.json", true);
        validateDuckDatabase(runner,
                "${duckId}",
                properties.color(),
                String.valueOf(properties.height()),
                properties.material(),
                properties.sound(),
                properties.wingsState());

        // repair
        executeDatabase(runner, "DELETE FROM duck WHERE id = ${duckId}");
    }

    @Test(description = "Создание утки с material = wood")
    @CitrusTest
    public void createDuckWoodTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        PropertiesRequest properties = new PropertiesRequest()
                .color("black")
                .height(0.2)
                .material("wood")
                .sound("quack")
                .wingsState("ACTIVE");

        // do
        createDuck(runner, properties);

        // check
        validateResponseResources(runner, HttpStatus.OK, "response/duckCreateTest/createDuckWood.json", true);
        validateDuckDatabase(runner,
                "${duckId}",
                properties.color(),
                String.valueOf(properties.height()),
                properties.material(),
                properties.sound(),
                properties.wingsState());

        // repair
        executeDatabase(runner, "DELETE FROM duck WHERE id = ${duckId}");
    }
}