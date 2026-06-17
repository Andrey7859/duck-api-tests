package autotests.clients;

import com.consol.citrus.TestCaseRunner;
import io.qameta.allure.Step;

public class QuackClient extends DuckClient {
    @Step("Утка крякает")
    public void getQuack(TestCaseRunner runner, String id, String repetitionCount, String soundCount) {
        String path = "/api/duck/action/quack?id=" + id + "&repetitionCount=" + repetitionCount + "&soundCount=" + soundCount;

        prepareGetRequest(runner, path);
    }
}