package cn.lifehub.config.conf;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "cn.lifehub.config.repository")
@EntityScan(basePackages = "cn.lifehub.config.models.tables")
public class JpaConfig {

}
