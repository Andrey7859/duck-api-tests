package autotests.clients;

import com.consol.citrus.TestCaseRunner;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class QuackClient extends DuckClient {
    public void getQuack(TestCaseRunner runner, String id, String repetitionCount, String soundCount) {
        String path = "/api/duck/action/quack";

        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .get(path)
                        .queryParam("id", id)
                        .queryParam("repetitionCount", repetitionCount)
                        .queryParam("soundCount", soundCount));
    }
}