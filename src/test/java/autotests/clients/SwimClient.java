package autotests.clients;

import com.consol.citrus.TestCaseRunner;
import io.qameta.allure.Step;

public class SwimClient extends DuckClient {
    @Step("Утка плывет")
    public void getSwim(TestCaseRunner runner, String id) {
        String path = "/api/duck/action/swim?id=" + id;

        prepareGetRequest(runner, path);
    }
}