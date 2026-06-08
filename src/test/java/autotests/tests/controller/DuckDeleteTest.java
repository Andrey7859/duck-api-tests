package autotests.tests.controller;

import autotests.clients.DeleteClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckDeleteTest extends DeleteClient {

    @Test(description = "Удаление утки")
    @CitrusTest
    public void deleteDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        createDuck(runner, "black", 0.20, "rubber", "quack", "ACTIVE");
        String id = getDuckId(runner);

        // do
        deleteDuck(runner, id);

        // check
        validateResponse(runner, HttpStatus.OK, "{\"message\": \"Duck is deleted\"}");
    }
}