package autotests.action;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckProperties extends TestNGCitrusSpringSupport {
    private static final String URL = "http://localhost:2222";

    public void getProperties(TestCaseRunner runner, String id) {
        runner.$(
                http()
                        .client(URL)
                        .send()
                        .get("/api/duck/action/properties")
                        .queryParam("id", id));
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

    /*
    Нужно предварительно создать уточек в ручную.
    Значения которые использовал при создание и проверке уточек.
    INSERT INTO duck VALUES
    (1, 'black', 0.2, 'rubber', 'quack', 'ACTIVE');
    (2, 'black', 0.2, 'wood', 'quack', 'ACTIVE'),
    */
    @Test(description = "Получения уточки с четным ID и material равен wood")
    @CitrusTest
    public void getWoodDucksWithEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        //(2, 'black', 0.2, 'wood', 'quack', 'ACTIVE'),
        getProperties(runner, "2");
        validateResponse(runner, "{}");
    }

    @Test(description = "Получения уточки с нечетным ID и material равен rubber")
    @CitrusTest
    public void getRubberDucksWithOddId(@Optional @CitrusResource TestCaseRunner runner) {
        //(1, 'black', 0.2, 'rubber', 'quack', 'ACTIVE');
        // Расхождения в значениях при запросе в body "height": 20.0. Значение в БД 0.2.
        getProperties(runner, "1");
        validateResponse(runner, "{" +
                "\"color\":\"black\"," +
                "\"height\": 0.2," +
                "\"material\":\"rubber\"," +
                "\"sound\":\"quack\"," +
                "\"wingsState\":\"ACTIVE\"" +
                "}");
    }
}
