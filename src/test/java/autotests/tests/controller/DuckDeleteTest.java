package autotests.tests.controller;

import autotests.clients.DuckClient;
import autotests.payloads.request.PropertiesRequest;
import autotests.payloads.response.UniversalMessageResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckDeleteTest extends DuckClient {

    @Test(description = "Удаление утки")
    @CitrusTest
    public void deleteDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        PropertiesRequest properties = new PropertiesRequest()
                .color("black")
                .height(0.20)
                .material("rubber")
                .sound("quack")
                .wingsState("ACTIVE");
        createDuck(runner, properties);
        String id = getDuckId(runner);

        UniversalMessageResponse expected = new UniversalMessageResponse()
                .message("Duck is deleted");

        // do
        deleteDuck(runner, id);

        // check
        validateResponsePayload(runner, HttpStatus.OK, expected);
    }
}