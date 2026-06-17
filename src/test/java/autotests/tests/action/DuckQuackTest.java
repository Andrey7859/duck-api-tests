package autotests.tests.action;

import autotests.clients.QuackClient;
import autotests.payloads.response.QuackResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckQuackTest extends QuackClient {
    /*
    Нужно предварительно создать уточек в ручную.
    Значения которые использовал при создание и проверке уточек.
    INSERT INTO duck VALUES
    (1, 'black', 0.2, 'rubber', 'quack', 'ACTIVE'),
    (2, 'black', 0.2, 'wood', 'quack', 'ACTIVE');
    */
    @Test(description = "Утка крякает нечётный id, корректный звук")
    @CitrusTest
    public void quackWithEvenIdTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        String id = "1";
        String repetitionCount = "2";
        String soundCount = "3";
        QuackResponse expected = new QuackResponse()
                .sound("quack-quack, quack-quack, quack-quack");

        // do
        getQuack(runner, id, repetitionCount, soundCount);

        // check
        validateResponsePayload(runner, HttpStatus.OK, expected);

        // repair
        deleteDuck(runner, id);
    }

    //TODO (ОР: "quack-quack, quack-quack", quack-quack. ФР: "moo-moo, moo-moo, moo-moo")
    @Test(description = "Утка крякает чётный id, корректный звук")
    @CitrusTest
    public void quackWithOddIdTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        String id = "2";
        String repetitionCount = "2";
        String soundCount = "3";
        QuackResponse expected = new QuackResponse()
                .sound("moo-moo, moo-moo, moo-moo");

        // do
        getQuack(runner, id, repetitionCount, soundCount);

        // check
        validateResponsePayload(runner, HttpStatus.OK, expected);

        // repair
        deleteDuck(runner, id);
    }
}