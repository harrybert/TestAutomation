package com.adjust.Pages;

import com.adjust.automation.env_config.EnvConfig;
import com.codeborne.selenide.Selenide;
import org.aeonbits.owner.ConfigFactory;

public class BasePage {

    private EnvConfig envConfig = ConfigFactory.create(EnvConfig.class, System.getProperties());

    public BasePage open(String url) {
        Selenide.open(envConfig.url() + url);
        return this;
    }

    public String baseUrl() {
        return envConfig.url();
    }

    public String url(String url) {
        return envConfig.url() + url;
    }
}
