package autotests.action;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.JsonPathSupport.jsonPath;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckQuack extends TestNGCitrusSpringSupport {
    private static final String URL = "http://localhost:2222";

    public void getQuack(TestCaseRunner runner, String id, String repetitionCount, String soundCount) {
        runner.$(
                http()
                        .client(URL)
                        .send()
                        .get("/api/duck/action/quack")
                        .queryParam("id", id)
                        .queryParam("repetitionCount", repetitionCount)
                        .queryParam("soundCount", soundCount));
    }

    public void validateResponse(TestCaseRunner runner, String valueForValidate) {
        runner.$(
                http()
                        .client(URL)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .validate(jsonPath().expression("$.sound", valueForValidate)));
    }

    /*
    Нужно предварительно создать уточек в ручную.
    Значения которые использовал при создание и проверке уточек.
    INSERT INTO duck VALUES
    (1, 'black', 0.2, 'rubber', 'quack', 'ACTIVE');
    (2, 'black', 0.2, 'wood', 'quack', 'ACTIVE'),
    */

    @Test(description = "Утка крякает нечётный id, корректный звук")
    @CitrusTest
    public void quackWithEvenIdTest(@Optional @CitrusResource TestCaseRunner runner) {
        getQuack(runner, "1", "2", "3");
        validateResponse(runner, "quack-quack, quack-quack, quack-quack");
    }

    //TODO (ОР: "quack-quack, quack-quack", quack-quack. ФР: "moo-moo, moo-moo, moo-moo")
    @Test(description = "Утка крякает чётный id, корректный звук")
    @CitrusTest
    public void quackWithOddIdTest(@Optional @CitrusResource TestCaseRunner runner) {
        getQuack(runner, "2", "2", "3");
        validateResponse(runner, "moo-moo, moo-moo, moo-moo");
    }
}
