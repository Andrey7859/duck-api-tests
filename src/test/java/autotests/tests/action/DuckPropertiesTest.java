package autotests.tests.action;

import autotests.clients.PropertiesClient;
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
@Feature("Показать характеристики утки")
@Story("Эндпоинт /api/duck/action/properties")
public class DuckPropertiesTest extends PropertiesClient {
    //TODO (ОР: свойства уточки. ФР: Тело ответа пустое)
    @Test(description = "Получения уточки с четным ID и material равен wood")
    @CitrusTest
    public void getWoodDucksWithEvenIdTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        String id = "2";
        executeDatabase(runner, "INSERT INTO Duck (id, color, height, material, sound, wings_state) \n" +
                "VALUES (" + id + ",'black', 0.2, 'wood', 'quack', 'ACTIVE');");

        // do
        getProperties(runner, id);

        // check
        validateResponse(runner, HttpStatus.OK, "{}", false);

        // repair
        executeDatabase(runner, "DELETE FROM Duck WHERE id =" + id);
    }

    //TODO (ОР: "height" = 0.2. ФР: "height" = 20.0) Расхождения в значениях
    @Test(description = "Получения уточки с нечетным ID и material равен rubber")
    @CitrusTest
    public void getRubberDucksWithOddIdTest(@Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        String id = "1";
        executeDatabase(runner, "INSERT INTO Duck (id, color, height, material, sound, wings_state) \n" +
                "VALUES (" + id + ",'black', 0.2, 'rubber', 'quack', 'ACTIVE');");

        // do
        getProperties(runner, id);

        // check
        validateResponse(runner, HttpStatus.OK, "{" +
                "\"color\":\"black\"," +
                "\"height\": 20.0," +
                "\"material\":\"rubber\"," +
                "\"sound\":\"quack\"," +
                "\"wingsState\":\"ACTIVE\"" +
                "}", false);

        // repair
        executeDatabase(runner, "DELETE FROM Duck WHERE id =" + id);
    }
}