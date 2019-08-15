package ru.tdi.mismessenger.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan( basePackages = {"ru.tdi.mismessenger.entity"} )
@EnableCaching
public class AppConfig {
}
