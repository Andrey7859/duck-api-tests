package autotests.tests.controller;

import autotests.clients.DuckClient;
import autotests.payloads.request.PropertiesRequest;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

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
        validateResponseResources(runner, HttpStatus.OK, "response/duckCreateTest/createDuckRubber.json");
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
        validateResponseResources(runner, HttpStatus.OK, "response/duckCreateTest/createDuckWood.json");
    }
}