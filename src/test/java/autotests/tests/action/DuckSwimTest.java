package autotests.tests.action;

import autotests.clients.SwimClient;
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
@Feature("Утка плывет")
@Story("Эндпоинт /api/duck/action/swim")
public class DuckSwimTest extends SwimClient {
    //TODO (ОР: Код ответа 200 и корректное сообшение ФР: Код 404 и некорректное сообщение "Paws are not found ((((")
    @Test(description = "Уточка с существующим id плывет")
    @CitrusTest
    public void SwimWithExistIdTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        runner.variable("duckId","citrus:randomNumber(4,false)");
        executeDatabase(runner, "INSERT INTO Duck (id, color, height, material, sound, wings_state) \n" +
                "VALUES ('${duckId}','black', 0.2, 'wood', 'quack', 'ACTIVE');");

        // do
        getSwim(runner, "${duckId}");

        // check
        validateResponseResources(runner, HttpStatus.NOT_FOUND, "response/duckSwimTest/swimWithExistId.json", false);

        // repair
        executeDatabase(runner, "DELETE FROM Duck WHERE id = ${duckId}");
    }

    @Test(description = "Уточка с несуществующим id плывет")
    @CitrusTest
    public void swimWithNonExistingIdTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        runner.variable("duckId","citrus:randomNumber(4,false)");
        executeDatabase(runner, "INSERT INTO Duck (id, color, height, material, sound, wings_state) \n" +
                "VALUES ('${duckId}','black', 0.2, 'wood', 'quack', 'ACTIVE');");
        executeDatabase(runner, "DELETE FROM Duck WHERE id = ${duckId}");

        // do
        getSwim(runner, "${duckId}");

        // check
        validateResponse(runner, HttpStatus.NOT_FOUND, "{\"message\": \"Paws are not found ((((\"}");
    }
}