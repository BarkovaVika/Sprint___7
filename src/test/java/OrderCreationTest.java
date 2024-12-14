import orders.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

@RunWith(Parameterized.class)
public class OrderCreationTest {

    private final List<String> colorScooter;

    int track;
    OrdersClient orderClient;

    public OrderCreationTest(List<String> colorScooter) {
        this.colorScooter = colorScooter;

    }

    @Parameterized.Parameters(name = "{index}: Test with colors: {0}")

    public static Object[][] getOrderCreation() {
        return new Object[][]{
                {List.of()},
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("BLACK", "GREY")},
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        orderClient = new OrdersClient();
    }

    @Test
    @DisplayName("Создаём заказ с разными цветами самоката")
    public void orderCreateByScooterColor() {

        createOrderWithColorStep(colorScooter);

        Order order = Order.createOrderWithColor(colorScooter);
        Response response = orderClient.sendPostCreateToOrders(order);
        orderClient.compareResponseCodeAndBodyAboutOrderCreation(response);
        track = response.then().extract().path("track");
        Response responseGet = orderClient.sendGetToTrackOrder(track);
        orderClient.compareResponse200(responseGet);
    }

    @io.qameta.allure.Step("Создаём заказ с цветом самоката: {colorScooter}")
    private void createOrderWithColorStep(List<String> colorScooter) {
        if (colorScooter.isEmpty()) {
            System.out.println("Создаём заказ не выбрав цвет самоката");
        } else {
            String colorString = String.join(", ", colorScooter);
            System.out.printf("Создаём заказ выбрав цвет(а): %s%n", colorString);
        }
    }
}
