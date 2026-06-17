package autotests.clients;

import com.consol.citrus.TestCaseRunner;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class PropertiesClient extends DuckClient {
    public void getProperties(TestCaseRunner runner, String id) {
        String path = "/api/duck/action/properties";

        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .get(path)
                        .queryParam("id", id));
    }
}