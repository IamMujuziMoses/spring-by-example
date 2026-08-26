package com.springbyexample.configurationproperties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Mujuzi Moses
 */
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String name;
    private String description;
    private String version;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
