package autotests.tests.action;

import autotests.clients.FlyClient;
import autotests.payloads.request.PropertiesRequest;
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

@Epic("Тесты на duck-action-controller")
@Feature("Утка летит")
@Story("Эндпоинт /api/duck/action/fly")
public class DuckFlyTest extends FlyClient {
    @Test(description = "Существующий id с активными крыльями")
    @CitrusTest
    public void wingsStateActiveTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        runner.variable("duckId","citrus:randomNumber(4,false)");
        executeDatabase(runner, "INSERT INTO Duck (id, color, height, material, sound, wings_state) \n" +
                "VALUES ('${duckId}','black', 0.2, 'wood', 'quack', 'ACTIVE');");
        UniversalMessageResponse expected = new UniversalMessageResponse()
                .message("I am flying :)");

        // do
        getFly(runner, "${duckId}");

        // check
        validateResponsePayload(runner, HttpStatus.OK, expected, false);

        // repair
        executeDatabase(runner, "DELETE FROM Duck WHERE id = ${duckId}");
    }

    @Test(description = "Существующий id со связанными крыльями")
    @CitrusTest
    public void wingsStateFixedTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        runner.variable("duckId","citrus:randomNumber(4,false)");
        executeDatabase(runner, "INSERT INTO Duck (id, color, height, material, sound, wings_state) \n" +
                "VALUES ('${duckId}','black', 0.2, 'wood', 'quack', 'FIXED');");

        // do
        getFly(runner, "${duckId}");

        // check
        validateResponseResources(runner, HttpStatus.OK, "response/duckFlyTest/wingsStateFixed.json", false);

        // repair
        executeDatabase(runner, "DELETE FROM Duck WHERE id = ${duckId}");
    }

    @Test(description = "Существующий id с крыльями в неопределенном состоянии")
    @CitrusTest
    public void wingsStateUndefinedTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        runner.variable("duckId","citrus:randomNumber(4,false)");
        executeDatabase(runner, "INSERT INTO Duck (id, color, height, material, sound, wings_state) \n" +
                "VALUES ('${duckId}','black', 0.2, 'wood', 'quack', 'UNDEFINED');");

        // do
        getFly(runner,"${duckId}");

        // check
        validateResponse(runner, HttpStatus.OK, "{\"message\": \"Wings are not detected :(\"}", false);

        // repair
        executeDatabase(runner, "DELETE FROM Duck WHERE id = ${duckId}");
    }
}