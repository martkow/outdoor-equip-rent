package com.kodilla.outdoor.equiprent.external.api.nbp.pl.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ApiNbpPlConfig {
    @Value("${apinbppl.api.endpoint}")
    private String apiEndpoint;
}
