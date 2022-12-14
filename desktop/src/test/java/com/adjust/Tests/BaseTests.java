package com.adjust.Tests;

import com.adjust.automation.env_config.EnvConfig;
import com.codeborne.selenide.WebDriverRunner;
import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTests {

    public EnvConfig envConfig = ConfigFactory.create(EnvConfig.class, System.getProperties());

    @BeforeMethod
    public void setUp() {
        RestAssured.baseURI = envConfig.url();
    }

    @AfterMethod
    public void tearDown() {
        if (WebDriverRunner.hasWebDriverStarted()) {
            WebDriverRunner.closeWebDriver();
        }
    }
}
