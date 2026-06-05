package autotests.tests.action;

import autotests.clients.SwimClient;
import autotests.payloads.request.PropertiesRequest;
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
        PropertiesRequest properties = new PropertiesRequest()
                .color("black")
                .height(0.20)
                .material("wood")
                .sound("quack")
                .wingsState("ACTIVE");
        createDuck(runner, properties);
        String id = getDuckId(runner);

        // do
        getSwim(runner, id);

        // check
        validateResponseResources(runner, HttpStatus.NOT_FOUND, "response/duckSwimTest/swimWithExistId.json");

        // repair
        deleteDuck(runner, id);
    }

    @Test(description = "Уточка с несуществующим id плывет")
    @CitrusTest
    public void swimWithNonExistingIdTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        PropertiesRequest properties = new PropertiesRequest()
                .color("black")
                .height(0.20)
                .material("wood")
                .sound("quack")
                .wingsState("ACTIVE");
        createDuck(runner, properties);
        String id = getDuckId(runner);
        deleteDuck(runner, id);

        // do
        getSwim(runner, id);

        // check
        validateResponse(runner, HttpStatus.NOT_FOUND, "{\"message\": \"Paws are not found ((((\"}");
    }
}