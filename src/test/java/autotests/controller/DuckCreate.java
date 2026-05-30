package autotests.controller;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckCreate extends TestNGCitrusSpringSupport {
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


    public void validateResponse(TestCaseRunner runner, String valueForValidate) {
        runner.$(
                http()
                        .client(URL)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .body(valueForValidate));
    }

    @Test(description = "Создание утки с material = rubber")
    @CitrusTest
    public void createDuckRubberTest(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "black", 0.20, "rubber", "quack", "ACTIVE");
        validateResponse(runner, "{" +
                "\"id\": \"@ignore@\"," +
                "\"color\":\"black\"," +
                "\"height\": 0.2," +
                "\"material\":\"rubber\"," +
                "\"sound\":\"quack\"," +
                "\"wingsState\":\"ACTIVE\"" +
                "}");
    }

    @Test(description = "Создание утки с material = wood")
    @CitrusTest
    public void createDuckWoodTest(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "black", 0.20, "wood", "quack", "ACTIVE");
        validateResponse(runner, "{" +
                "\"id\": \"@ignore@\"," +
                "\"color\":\"black\"," +
                "\"height\": 0.2," +
                "\"material\":\"wood\"," +
                "\"sound\":\"quack\"," +
                "\"wingsState\":\"ACTIVE\"" +
                "}");
    }
}
