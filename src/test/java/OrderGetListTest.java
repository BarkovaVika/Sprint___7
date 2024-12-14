import orders.OrdersClient;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class OrderGetListTest {
    OrdersClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrdersClient();
    }

    @Test
    @DisplayName("Проверяем, что в теле ответа возвращается список заказов")
    public void checkListOfOrdersContainedInResponse() {

        Response response = sendGetToOrdersStep();


        int statusCode = response.getStatusCode();


        compareResponse200Step(statusCode);


        isResponseBodyHaveOrdersListStep(response);
    }

    @Step("Отправляем GET-запрос на получение списка заказов")
    private Response sendGetToOrdersStep() {
        return orderClient.sendGetToOrders();
    }

    @Step("Проверяем, что статус ответа - {statusCode}")
    private void compareResponse200Step(int statusCode) {
        if (statusCode != 200) {
            throw new AssertionError("Expected status code 200, but got " + statusCode);
        }
    }

    @Step("Проверяем, что в теле ответа есть список заказов")
    private void isResponseBodyHaveOrdersListStep(Response response) {
        response.then().assertThat()
                .body("orders", not(empty()))
                .body("orders.id", everyItem(notNullValue()));
    }
}
