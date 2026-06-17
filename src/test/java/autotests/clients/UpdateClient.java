package autotests.clients;

import com.consol.citrus.TestCaseRunner;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class UpdateClient extends DuckClient {
    public void updateDuck(TestCaseRunner runner, String color, double height, String id, String material, String sound, String wingsState) {
        String path = "/api/duck/update";

        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .put(path)
                        .queryParam("color", color)
                        .queryParam("height", String.valueOf(height))
                        .queryParam("id", id)
                        .queryParam("material", material)
                        .queryParam("sound", sound)
                        .queryParam("wingsState", wingsState));
    }
}