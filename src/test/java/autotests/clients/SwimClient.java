package autotests.clients;

import autotests.BaseTest;
import com.consol.citrus.TestCaseRunner;
import org.springframework.http.MediaType;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class SwimClient extends BaseTest {
    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        String path = "/api/duck/create";
        String body = "{\n" +
                "\"color\": \"" + color + "\",\n" +
                "\"height\": " + height + ",\n" +
                "\"material\": \"" + material + "\",\n" +
                "\"sound\": \"" + sound + "\",\n" + "\"wingsState\": \"" + wingsState + "\"\n" + "}";

        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .post(path)
                        .message().contentType(MediaType.APPLICATION_JSON_VALUE).body(body));
    }

    public void getSwim(TestCaseRunner runner, String id) {
        String path = "/api/duck/action/swim";

        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .get(path)
                        .queryParam("id", id));
    }

    public void deleteDuck(TestCaseRunner runner, String duckId) {
        String path = "/api/duck/delete";

        runner.$(
                http()
                        .client(duckService)
                        .send()
                        .delete(path)
                        .queryParam("id", duckId));
    }
}