package autotests.action;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.JsonPathSupport.jsonPath;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckFly extends TestNGCitrusSpringSupport {
    private static final String URL = "http://localhost:2222";

    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        runner.$(
                http()
                        .client(URL)
                        .send()
                        .post("/api/duck/create")
                        .message().contentType(MediaType.APPLICATION_JSON_VALUE).body("{\n" +
                                "\"color\": \"" + color + "\",\n" +
                                "\"height\": " + height + ",\n" +
                                "\"material\": \"" + material + "\",\n" +
                                "\"sound\": \"" + sound + "\",\n" + "\"wingsState\": \"" + wingsState + "\"\n" + "}"));
    }

    public void getFly(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client(URL)
                        .send()
                        .get("/api/duck/action/fly")
                        .queryParam("id", id));
    }

    public void saveDuckId(TestCaseRunner runner) {
        runner.$(
                http()
                        .client(URL)
                        .receive()
                        .response()
                        .message().extract(fromBody().expression("$.id", "duckId")));
    }

    public void validateResponse(TestCaseRunner runner, String valueForValidate) {
        runner.$(
                http()
                        .client(URL)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .validate(jsonPath().expression("$.message", valueForValidate)));
    }

    //TODO (ОР:  { “message”: “I’m flying”} ФР: { "message": "I am flying :)")
    @Test(description = "Существующий id с активными крыльями")
    @CitrusTest
    public void wingsStateActiveTest(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "black", 0.20, "wood", "quack", "ACTIVE");
        saveDuckId(runner);
        getFly(runner, "${duckId}");
        validateResponse(runner, "I am flying :)");
    }

    //TODO (ОР:  { “message”: “I can’t fly”} ФР: { "message": "I can not fly :C")
    @Test(description = "Существующий id со связанными крыльями")
    @CitrusTest
    public void wingsStateFixedTest(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "black", 0.20, "wood", "quack", "FIXED");
        saveDuckId(runner);
        getFly(runner, "${duckId}");
        validateResponse(runner, "I can not fly :C");
    }

    @Test(description = "Существующий id с крыльями в неопределенном состоянии")
    @CitrusTest
    public void wingsStateUndefinedTest(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "black", 0.20, "wood", "quack", "UNDEFINED");
        saveDuckId(runner);
        getFly(runner, "${duckId}");
        validateResponse(runner, "Wings are not detected :(");
    }

}
