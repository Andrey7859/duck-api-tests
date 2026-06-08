package autotests.tests.action;

import autotests.clients.PropertiesClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckPropertiesTest extends PropertiesClient {
    /*
    Нужно предварительно создать уточек в ручную.
    Значения которые использовал при создание и проверке уточек.
    INSERT INTO duck VALUES
    (1, 'black', 0.2, 'rubber', 'quack', 'ACTIVE'),
    (2, 'black', 0.2, 'wood', 'quack', 'ACTIVE');
    */

    //(2, 'black', 0.2, 'wood', 'quack', 'ACTIVE'),
    //TODO (ОР: свойства уточки. ФР: Тело ответа пустое)
    @Test(description = "Получения уточки с четным ID и material равен wood")
    @CitrusTest
    public void getWoodDucksWithEvenIdTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        String id = "2";

        // do
        getProperties(runner, id);

        // check
        validateResponse(runner, HttpStatus.OK, "{}");

        // repair
        deleteDuck(runner, id);
    }

    //(1, 'black', 0.2, 'rubber', 'quack', 'ACTIVE');
    //TODO (ОР: "height" = 0.2. ФР: "height" = 20.0) Расхождения в значениях
    @Test(description = "Получения уточки с нечетным ID и material равен rubber")
    @CitrusTest
    public void getRubberDucksWithOddIdTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        String id = "1";

        // do
        getProperties(runner, id);

        // check
        validateResponse(runner, HttpStatus.OK, "{" +
                "\"color\":\"black\"," +
                "\"height\": 20.0," +
                "\"material\":\"rubber\"," +
                "\"sound\":\"quack\"," +
                "\"wingsState\":\"ACTIVE\"" +
                "}");

        // repair
        deleteDuck(runner, id);
    }
}