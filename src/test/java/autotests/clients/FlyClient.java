package autotests.clients;

import com.consol.citrus.TestCaseRunner;
import io.qameta.allure.Step;

public class FlyClient extends DuckClient {
    @Step("Утка полетела")
    public void getFly(TestCaseRunner runner, String id) {
        String path = "/api/duck/action/fly?id=" + id;

        prepareGetRequest(runner, path);
    }
}