package cn.lifehub.config.conf;

import cn.lifehub.config.dbenv.JdbcEnvironmentRepository;
import cn.lifehub.config.service.IConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;

@Configuration
@Profile("jdbc")
public class JdbcRepositoryConfiguration {
	@Autowired
	private ConfigurableEnvironment environment;
	// @Autowired
	// private ConfigServiceback configService;
	@Autowired
	private IConfigService configService;

	@Bean
	public JdbcEnvironmentRepository jdbcEnvironmentRepository() {
		return new JdbcEnvironmentRepository(environment, configService);
	}
}
