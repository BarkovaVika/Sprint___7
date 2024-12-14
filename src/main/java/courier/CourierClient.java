package courier;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import rest.RestApi;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.apache.http.HttpStatus.*;
public class CourierClient extends RestApi {
    private final String ROOT = "/api/v1/courier";

    @Step("Проходим авторизацию")
    public Response postToCourierLogin(CourierCredentials courierLogin) {
        String LOGIN = ROOT + "/login";
        return reqSpec
                .and()
                .body(courierLogin)
                .when()
                .post(LOGIN);
    }
    @Step("Создаём курьера")
    public Response postCreateToCourier(Courier courier) {
        return reqSpec
                .and()
                .body(courier)
                .when()
                .post(ROOT);
    }

    @Step("Удаляем курьера")
    public Response deleteCourier(int courierId) {
        String json = "{\"id\": \"" + courierId + "\"}";
        String DELETE = ROOT + "/{courierId}";
        return reqSpec
                .pathParam("courierId", courierId)
                .and()
                .body(json)
                .when()
                .delete(DELETE);
    }
    @Step("Запрос успешный. ok: true")
    public void compareResponseCodeAndBodyAboutCreation(Response response) {
        response.then().assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("ok", is(true));
    }
    @Step("Запрос успешный. ok: true")
    public void compareDeleteResponseCodeAndBodyOk(Response response) {
        response.then().assertThat()
                .statusCode(SC_OK)
                .and()
                .body("ok", is(true));
    }
    @Step("Ошибка 409 Conflict. Этот логин уже используется")
    public void compareResponseCodeAndMessageWithError409(Response response) {
        response.then().assertThat()
                .statusCode(SC_CONFLICT)
                .and()
                .body("message", is("Этот логин уже используется"));
        //отличается текст startsWith
    }
    @Step("Ошибка 400 Bad Request. Недостаточно данных для создания учетной записи")
    public void compareCodeAndMessageWithError400(Response response) {
        response.then().assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }
    @Step("Проверяем, что id не 0")
    public void compareLoginResponseAndBodyIdNotNull(Response response) {
        response.then().assertThat()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue());
    }
    @Step("Ошибка 400 Bad Request. Недостаточно данных для входа")
    public void compareLoginResponseCodeAndBody400Message(Response response) {
        response.then().assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", is("Недостаточно данных для входа"));
    }
    @Step("Ошибка 404 Not Found. Учетная запись не найдена")
    public void compareLoginResponseCodeAndBody404Message(Response response) {
        response.then().assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", is("Учетная запись не найдена"));
    }
}
