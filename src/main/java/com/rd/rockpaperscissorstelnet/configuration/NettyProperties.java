package com.rd.rockpaperscissorstelnet.configuration;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "telnet")
public class NettyProperties {

    @NotNull
    @Size(min=100, max = 65535)
    private Integer port;

    @NotNull
    @Min(1)
    private Integer bossGroupSize;

    @NotNull
    @Min(2)
    private Integer workerGroupSize;

    @NotNull
    private Boolean keepAlive;

    @NotNull
    private Integer backlog;
}
