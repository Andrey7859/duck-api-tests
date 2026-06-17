package autotests.clients;

import com.consol.citrus.TestCaseRunner;
import io.qameta.allure.Step;

public class PropertiesClient extends DuckClient {
    @Step("Получение характеристик утки")
    public void getProperties(TestCaseRunner runner, String id) {
        String path = "/api/duck/action/properties?id=" + id;

        prepareGetRequest(runner, path);
    }
}