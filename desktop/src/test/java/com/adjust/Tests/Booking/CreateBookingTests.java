package com.adjust.Tests.Booking;

import com.adjust.Pages.BookingPage;
import com.adjust.Tests.BaseTests;
import com.adjust.automation.model.BookingDates;
import com.adjust.automation.model.CustomerInfo;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.hamcrest.core.Is;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;


public class CreateBookingTests extends BaseTests {

    Integer bookingId;
    Integer roomId;

    @DataProvider(name = "canCreateANewBookingCases")
    public Object[][] canCreateANewBookingCases() {
        return new Object[][]{
                {
                        CustomerInfo.builder()
                                .bookingid(0)
                                .depositpaid(true)
                                .email("createbooking@doublemail.de")
                                .firstname("Mario")
                                .lastname("Benjamin")
                                .phone("017212345678")
                                .roomid(51)
                                .build(),
                        BookingDates.builder()
                                .checkin("2021-01-01")
                                .checkout("2021-01-03")
                                .build()
                },
        };

    }

    @Test(dataProvider = "canCreateANewBookingCases", priority = 1)
    public void canCreateANewBooking(CustomerInfo user,
                                     BookingDates bookingDates) throws Exception {

        BookingPage bookingPage = new BookingPage();
        Response response;

        //check if the checkout date is greater then checkin date
        bookingPage.isCheckoutDateGreaterThenCheckin(bookingDates);

        //Check if the Booking exist for the given date
        response = given()
                .contentType("application/json")
                .get(bookingPage.url() + bookingPage.getBookingUrlByRoomId + user.getRoomid())
                .then()
                .extract()
                .response();

        Integer size = response.jsonPath().getList("bookings").size();
        for (int i = 0; i < size; i++) {
            if (response.jsonPath().get("bookings.bookingdates[" + i + "].checkin").toString()
                    .contentEquals(bookingDates.getCheckin())) {
                throw new Exception("Booking already exist for the given date");
            }
        }

        CustomerInfo data = new CustomerInfo(bookingDates,
                user.getBookingid(),
                user.isDepositpaid(),
                user.getEmail(),
                user.getFirstname(),
                user.getLastname(),
                user.getPhone(),
                user.getRoomid());
        //create a booking
        response = given()
                .contentType(ContentType.JSON)
                .body(data)
                .post(bookingPage.url());

        response.then()
                .statusCode(201)
                .body("booking.firstname", Is.is(user.getFirstname()),
                        "booking.lastname", Is.is(user.getLastname()),
                        "booking.roomid", Is.is(user.getRoomid()),
                        "booking.bookingdates.checkin", Is.is(bookingDates.getCheckin()),
                        "booking.bookingdates.checkout", Is.is(bookingDates.getCheckout()));
        bookingId = response.jsonPath().get("booking.bookingid");
        roomId = response.jsonPath().get("booking.roomid");
    }


    @Test(priority = 2)
    public void canFetchTheCreatedBookingByBookingId() {
        BookingPage bookingPage = new BookingPage();
        given()
                .contentType("application/json")
                .get(bookingPage.url() + bookingId)
                .then()
                .body("bookingid", equalTo(bookingId))
                .statusCode(200);
    }

    @DataProvider(name = "canReturnAtleastOneBookingCase")
    public Object[][] canReturnAtleastOneBookingCase() {
        return new Object[][]{
                {
                        roomId
                },
        };
    }

    @Test(dataProvider = "canReturnAtleastOneBookingCase", priority = 3)
    public void canReturnAtleastOneBooking(int roomId) {
        BookingPage bookingPage = new BookingPage();

        Integer bookingsSize = given()
                .contentType("application/json")
                .get(bookingPage.url() + bookingPage.getBookingUrlByRoomId + roomId)
                .then()
                .extract()
                .jsonPath()
                .getList("bookings")
                .size();

        Assert.assertTrue(bookingsSize != 0);
    }

    @DataProvider(name = "canUpdateTheBookingCase")
    public Object[][] canUpdateTheBookingCase() {
        return new Object[][]{
                {
                        CustomerInfo.builder()
                                .bookingid(bookingId)
                                .depositpaid(true)
                                .email("updateCustomerEmail@doublemail.de")
                                .firstname("updatedFirstName")
                                .lastname("updatedLastName")
                                .phone("01511234567")
                                .roomid(roomId)
                                .build(),
                        BookingDates.builder()
                                .checkin("2021-02-01")
                                .checkout("2021-02-04")
                                .build()
                },
        };
    }

    @Test(dataProvider = "canUpdateTheBookingCase", priority = 4)
    public void canUpdateTheBooking(CustomerInfo user,
                                    BookingDates bookingDates) {
        BookingPage bookingPage = new BookingPage();
        String token = bookingPage.canLoginAndReturnToken("admin", "password");
        CustomerInfo data = new CustomerInfo(bookingDates,
                user.getBookingid(),
                user.isDepositpaid(),
                user.getEmail(),
                user.getFirstname(),
                user.getLastname(),
                user.getPhone(),
                user.getRoomid());

        Response response = given()
                .contentType("application/json")
                .cookie("token", token)
                .body(data)
                .put(bookingPage.url() + user.getBookingid())
                .then()
                .statusCode(200)
                .extract()
                .response();

        ResponseBody responseBody = response.getBody();
        Assert.assertEquals(responseBody.jsonPath().get("bookingid"), user.getBookingid());
        Assert.assertEquals(responseBody.jsonPath().get("booking.roomid"), user.getRoomid());
        Assert.assertEquals(responseBody.jsonPath().get("booking.firstname"), user.getFirstname());
        Assert.assertEquals(responseBody.jsonPath().get("booking.lastname"), user.getLastname());
        Assert.assertEquals(responseBody.jsonPath().get("booking.bookingdates.checkin"), bookingDates.getCheckin());
        Assert.assertEquals(responseBody.jsonPath().get("booking.bookingdates.checkout"), bookingDates.getCheckout());
    }

    @Test(priority = 5)
    public void canDeleteTheBooking() {
        BookingPage bookingPage = new BookingPage();
        String token = bookingPage.canLoginAndReturnToken("admin", "password");

        Response response = given()
                .contentType("application/json")
                .cookie("token", token)
                .delete(bookingPage.url() + bookingId)
                .then()
                .extract()
                .response();

        Assert.assertEquals(response.statusCode(), 202);
    }

}
