package autotests.tests.controller;

import autotests.clients.DuckClient;
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
@Feature("Удаление утки")
@Story("Эндпоинт /api/duck/delete")
public class DuckDeleteTest extends DuckClient {

    @Test(description = "Удаление утки")
    @CitrusTest
    public void deleteDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        runner.variable("duckId", "citrus:randomNumber(4,false)");
        executeDatabase(runner, "INSERT INTO Duck (id, color, height, material, sound, wings_state) \n" +
                "VALUES ('${duckId}','black', 0.2, 'rubber', 'quack', 'ACTIVE');");
        UniversalMessageResponse expected = new UniversalMessageResponse()
                .message("Duck is deleted");

        // do
        deleteDuck(runner, "${duckId}");

        // check
        validateResponsePayload(runner, HttpStatus.OK, expected);
        validateDuckDeleteDatabase(runner, "${duckId}");
    }
}