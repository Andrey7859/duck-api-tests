package autotests.tests.action;

import autotests.clients.PropertiesClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.CitrusParameters;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.springframework.http.HttpStatus;
import org.testng.annotations.DataProvider;
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
        validateResponse(runner, HttpStatus.OK, "{}");

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
                "}");

        // repair
        executeDatabase(runner, "DELETE FROM Duck WHERE id =" + id);
    }

    @Test(dataProvider = "duckList", description = "Получение характеристик подмножества уток")
    @CitrusTest
    @CitrusParameters({"id", "color", "height", "material", "sound", "wingsState", "status", "resourcePath", "runner"})
    public void ducksPropertiesTest(String id, String color, Double height, String material, String sound, String wingsState, HttpStatus status, String resourcePath, @Optional @CitrusResource TestCaseRunner runner) {
        // prepare
        String insert = String.format(
                "INSERT INTO Duck (id, color, height, material, sound, wings_state) " +
                        "VALUES (%s, '%s', %s, '%s', '%s', '%s')",
                id, color, height, material, sound, wingsState
        );
        executeDatabase(runner, insert);

        // do
        getProperties(runner, id);

        // check
        validateResponseResources(runner, status, resourcePath, false);

        // repair
        executeDatabase(runner, "DELETE FROM Duck WHERE id =" + id);
    }


    @DataProvider(name = "duckList")
    public Object[][] DuckProvider() {
        return new Object[][]{
                {"1", "black", 0.2, "rubber", "quack", "ACTIVE", HttpStatus.OK, "response/duckPropertiesTest/RubberDucksWithOddId.json", null},
                {"2", "black", 0.2, "wood", "quack", "ACTIVE", HttpStatus.OK, "response/duckPropertiesTest/ucksWithEvenIdTest.json", null},
                {"3", "red", 0.2, "rubber", "quack", "ACTIVE", HttpStatus.OK, "response/duckPropertiesTest/RedDucksWithOddId.json", null},
                {"4", "red", 0.2, "wood", "quack", "ACTIVE", HttpStatus.OK, "response/duckPropertiesTest/ucksWithEvenIdTest.json", null},
                {"5", "black", 0.2, "rubber", "moo", "ACTIVE", HttpStatus.OK, "response/duckPropertiesTest/RubberDucksWithOddIdSoundMoo.json", null},
        };
    }
}