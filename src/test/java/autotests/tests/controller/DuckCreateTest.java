package autotests.tests.controller;

import autotests.clients.CreateClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckCreateTest extends CreateClient {
    @Test(description = "Создание утки с material = rubber")
    @CitrusTest
    public void createDuckRubberTest(@Optional @CitrusResource TestCaseRunner runner) {
        // do
        createDuck(runner, "black", 0.2, "rubber", "quack", "ACTIVE");

        // check
        validateResponse(runner, HttpStatus.OK, "{" +
                "\"id\": \"@ignore@\"," +
                "\"color\":\"black\"," +
                "\"height\": 0.2," +
                "\"material\":\"rubber\"," +
                "\"sound\":\"quack\"," +
                "\"wingsState\":\"ACTIVE\"" +
                "}");
    }

    @Test(description = "Создание утки с material = wood")
    @CitrusTest
    public void createDuckWoodTest(@Optional @CitrusResource TestCaseRunner runner) {
        // do
        createDuck(runner, "black", 0.2, "wood", "quack", "ACTIVE");

        // check
        validateResponse(runner, HttpStatus.OK, "{" +
                "\"id\": \"@ignore@\"," +
                "\"color\":\"black\"," +
                "\"height\": 0.2," +
                "\"material\":\"wood\"," +
                "\"sound\":\"quack\"," +
                "\"wingsState\":\"ACTIVE\"" +
                "}");
    }
}