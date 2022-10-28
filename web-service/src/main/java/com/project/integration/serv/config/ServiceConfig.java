package com.project.integration.serv.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableAutoConfiguration
@EnableJpaRepositories("com.project.integration.dao")
@EntityScan("com.project.integration.dao")
@ComponentScan("com.project.integration.serv")
public class ServiceConfig {
  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }
}
