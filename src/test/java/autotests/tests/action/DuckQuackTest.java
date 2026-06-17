package autotests.tests.action;

import autotests.clients.QuackClient;
import autotests.payloads.response.QuackResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

@Epic("Тесты на duck-action-controller")
@Feature("Утка крякает")
@Story("Эндпоинт /api/duck/action/quack")
public class DuckQuackTest extends QuackClient {
    @Test(description = "Утка крякает нечётный id, корректный звук")
    @CitrusTest
    public void quackWithEvenIdTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        String id = "1";
        String repetitionCount = "2";
        String soundCount = "3";
        executeDatabase(runner, "INSERT INTO Duck (id, color, height, material, sound, wings_state) \n" +
                "VALUES (" + id + ",'black', 0.2, 'rubber', 'quack', 'ACTIVE');");
        QuackResponse expected = new QuackResponse()
                .sound("quack-quack, quack-quack, quack-quack");

        // do
        getQuack(runner, id, repetitionCount, soundCount);

        // check
        validateResponsePayload(runner, HttpStatus.OK, expected, false);

        // repair
        executeDatabase(runner, "DELETE FROM Duck WHERE id =" + id);
    }

    //TODO (ОР: "quack-quack, quack-quack", quack-quack. ФР: "moo-moo, moo-moo, moo-moo")
    @Test(description = "Утка крякает чётный id, корректный звук")
    @CitrusTest
    public void quackWithOddIdTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        String id = "2";
        String repetitionCount = "2";
        String soundCount = "3";
        executeDatabase(runner, "INSERT INTO Duck (id, color, height, material, sound, wings_state) \n" +
                "VALUES (" + id + ",'black', 0.2, 'wood', 'quack', 'ACTIVE');");
        QuackResponse expected = new QuackResponse()
                .sound("moo-moo, moo-moo, moo-moo");

        // do
        getQuack(runner, id, repetitionCount, soundCount);

        // check
        validateResponsePayload(runner, HttpStatus.OK, expected, false);

        // repair
        executeDatabase(runner, "DELETE FROM Duck WHERE id =" + id);
    }
}