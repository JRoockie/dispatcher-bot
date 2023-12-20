package org.voetsky.dispatcherBot.configuration;

import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

public class MvcConfig {
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
    }

}
