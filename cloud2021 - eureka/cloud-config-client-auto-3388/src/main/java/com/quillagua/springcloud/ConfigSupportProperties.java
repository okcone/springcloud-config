package com.quillagua.springcloud;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@ConfigurationProperties(prefix = ConfigSupportProperties.CONFIG_PREFIX)
public class ConfigSupportProperties {

    /**
     * 加载的配置文件前缀
     */
    public static final String CONFIG_PREFIX = "spring.cloud.config.backup";

    /**
     * 默认文件名
     */
    private final String DEFAULT_FILE_NAME = "fallback.properties";

    /**
     * 是否启用
     */
    private boolean enabled = false;

    /**
     * 本地文件地址
     */
    private String fallbackLocation;

    public String getFallbackLocation() {
        return fallbackLocation;
    }

    public void setFallbackLocation(String fallbackLocation) {
        if (!fallbackLocation.contains(".")) {
            // 如果只指定了文件路径，自动拼接文件名
            fallbackLocation = fallbackLocation.endsWith(File.separator) ? fallbackLocation : fallbackLocation + File.separator;
            fallbackLocation += DEFAULT_FILE_NAME;
        }
        this.fallbackLocation = fallbackLocation;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

