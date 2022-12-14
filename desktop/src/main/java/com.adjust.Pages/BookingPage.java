package com.adjust.Pages;

import com.adjust.automation.model.BookingDates;
import io.restassured.response.Response;
import org.testng.Assert;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;

public class BookingPage extends BasePage {

    String bookingUrl = "/booking/";
    public String getBookingUrlByRoomId = "?roomid=";

    public String url() {
        return super.url(bookingUrl);
    }

    public void open() {
        super.open(bookingUrl);
    }

    public LocalDate parseDate(String date) {
        return LocalDate.parse(date);
    }

    public Response canloginUserAndReturnToken(String userName, String password) {

        /**
         * Login and return token
         * username: admin
         * password: token
         */

        Response response = given()
                .contentType("application/json")
                .body("{\"username\":\"" + userName + "\", \"password\":\"" + password + "\"}")
                .post("/auth/login")
                .then()
                .extract()
                .response();

        Assert.assertEquals(response.getStatusCode(), 200);
        return response;

    }

    public String getToken(String userName, String password) {
        Response response = canloginUserAndReturnToken(userName, password);
        String token = response.path("token");
        return token;
    }

    public String canLoginAndReturnToken(String username, String password) {
        BookingPage bookingPage = new BookingPage();
        String token = getToken(username, password);
        //Validate authentication
        given()
                .contentType("application/json")
                .body("{ \"token\": \"" + token + "\"}")
                .post(bookingPage.baseUrl() + "/auth/validate")
                .then()
                .statusCode(200);
        return token;
    }

    public void isCheckoutDateGreaterThenCheckin(BookingDates bookingDates) throws Exception {
        if (parseDate(bookingDates.getCheckin()).
                isAfter(parseDate(bookingDates.getCheckout()))) {
            throw new Exception("check-out date must be greater than the check-in date");
        }
    }

}

