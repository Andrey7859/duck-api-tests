package autotests.tests.action;

import autotests.clients.FlyClient;
import autotests.payloads.request.PropertiesRequest;
import autotests.payloads.response.UniversalMessageResponse;
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
        PropertiesRequest properties = new PropertiesRequest()
                .color("black")
                .height(0.20)
                .material("wood")
                .sound("quack")
                .wingsState("ACTIVE");
        createDuck(runner, properties);
        String id = getDuckId(runner);
        UniversalMessageResponse expected = new UniversalMessageResponse()
                .message("I am flying :)");

        // do
        getFly(runner, id);

        // check
        validateResponsePayload(runner, HttpStatus.OK, expected);

        // repair
        deleteDuck(runner, id);
    }

    @Test(description = "Существующий id со связанными крыльями")
    @CitrusTest
    public void wingsStateFixedTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        PropertiesRequest properties = new PropertiesRequest()
                .color("black")
                .height(0.20)
                .material("wood")
                .sound("quack")
                .wingsState("FIXED");
        createDuck(runner, properties);
        String id = getDuckId(runner);

        // do
        getFly(runner, id);

        // check
        validateResponseResources(runner, HttpStatus.OK, "response/duckFlyTest/wingsStateFixed.json");

        // repair
        deleteDuck(runner, id);
    }

    @Test(description = "Существующий id с крыльями в неопределенном состоянии")
    @CitrusTest
    public void wingsStateUndefinedTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        PropertiesRequest properties = new PropertiesRequest()
                .color("black")
                .height(0.20)
                .material("wood")
                .sound("quack")
                .wingsState("UNDEFINED");
        createDuck(runner, properties);
        String id = getDuckId(runner);

        // do
        getFly(runner, id);

        // check
        validateResponse(runner, HttpStatus.OK, "{\"message\": \"Wings are not detected :(\"}");

        // repair
        deleteDuck(runner, id);
    }

}