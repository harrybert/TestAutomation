package com.adjust.automation.env_config;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:env_config/env.properties"})
public interface EnvConfig extends Config {

    String url();
}
