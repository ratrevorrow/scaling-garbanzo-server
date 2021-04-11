package com.rtrevorrow.user.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        value = {
                "com.rtrevorrow.user.repository"
        },
        repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class
)
@EntityScan(basePackages={
        "com.rtrevorrow.user.model",
})
@ComponentScan(
        basePackages={
                "com.rtrevorrow.user",
                "com.rtrevorrow.user.authentication",
                "com.rtrevorrow.user.authorization",
                "com.rtrevorrow.user.config",
                "com.rtrevorrow.user.model",
                "com.rtrevorrow.user.repository",
                "com.rtrevorrow.user.service"
        }
)
//@EnableAutoConfiguration
public class UserServiceConfig {


}
