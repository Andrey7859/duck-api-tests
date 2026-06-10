package autotests.clients;

import com.consol.citrus.TestCaseRunner;
import io.qameta.allure.Step;

public class UpdateClient extends DuckClient {
    @Step("Обновить характеристики утки")
    public void updateDuck(TestCaseRunner runner, String color, double height, String id, String material, String sound, String wingsState) {
        String path = "/api/duck/update" +
                "?color=" + color +
                "&height=" + height +
                "&id=" + id +
                "&material=" + material +
                "&sound=" + sound +
                "&wingsState=" + wingsState;

        preparePutRequest(runner, path);
    }
}