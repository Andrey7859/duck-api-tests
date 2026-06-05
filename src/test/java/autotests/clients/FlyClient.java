package autotests.clients;

import com.consol.citrus.TestCaseRunner;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class FlyClient extends DuckClient {
    public void getFly(TestCaseRunner runner, String id) {
        String path = "/api/duck/action/fly";

        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .get(path)
                        .queryParam("id", id));
    }
}