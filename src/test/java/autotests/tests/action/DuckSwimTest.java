package autotests.tests.action;

import autotests.clients.SwimClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckSwimTest extends SwimClient {
    //TODO (ОР: Код ответа 200 и корректное сообшение ФР: Код 404 и некорректное сообщение "Paws are not found ((((")
    @Test(description = "Уточка с существующим id плывет")
    @CitrusTest
    public void SwimWithExistIdTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        createDuck(runner, "black", 0.20, "wood", "quack", "ACTIVE");
        String id = getDuckId(runner);

        // do
        getSwim(runner, id);

        // check
        validateResponse(runner, HttpStatus.NOT_FOUND, "{\"message\": \"Paws are not found ((((\"}");

        // repair
        deleteDuck(runner, id);
    }

    @Test(description = "Уточка с несуществующим id плывет")
    @CitrusTest
    public void swimWithNonExistingIdTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        createDuck(runner, "black", 0.20, "wood", "quack", "ACTIVE");
        String id = getDuckId(runner);
        deleteDuck(runner, id);
        validateResponse(runner, HttpStatus.OK, "{\"message\": \"Duck is deleted\"}");

        // do
        getSwim(runner, id);

        // check
        validateResponse(runner, HttpStatus.NOT_FOUND, "{\"message\": \"Paws are not found ((((\"}");
    }
}