package autotests.tests.action;

import autotests.clients.FlyClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckFlyTest extends FlyClient {
    @Test(description = "Существующий id с активными крыльями")
    @CitrusTest
    public void wingsStateActiveTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        createDuck(runner, "black", 0.20, "wood", "quack", "ACTIVE");
        String id = getDuckId(runner);

        // do
        getFly(runner, id);

        // check
        validateResponse(runner, HttpStatus.OK, "{\"message\": \"I am flying :)\"}");

        // repair
        deleteDuck(runner, id);
    }

    @Test(description = "Существующий id со связанными крыльями")
    @CitrusTest
    public void wingsStateFixedTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        createDuck(runner, "black", 0.20, "wood", "quack", "FIXED");
        String id = getDuckId(runner);

        // do
        getFly(runner, id);

        // check
        validateResponse(runner, HttpStatus.OK, "{\"message\": \"I can not fly :C\"}");

        // repair
        deleteDuck(runner, id);
    }

    @Test(description = "Существующий id с крыльями в неопределенном состоянии")
    @CitrusTest
    public void wingsStateUndefinedTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        createDuck(runner, "black", 0.20, "wood", "quack", "UNDEFINED");
        String id = getDuckId(runner);

        // do
        getFly(runner, id);

        // check
        validateResponse(runner, HttpStatus.OK, "{\"message\": \"Wings are not detected :(\"}");

        // repair
        deleteDuck(runner, id);
    }

}