package ka.masato.openshift;

import java.net.URI;
import java.net.URISyntaxException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import net.sf.log4jdbc.Log4jdbcProxyDataSource;

@Configuration
public class AppConfig {

	@Autowired
	DataSourceProperties properties;
	DataSource dataSource;
	

	@Bean(destroyMethod = "close")
	DataSource realDataSource() throws URISyntaxException {
		String url;
		String username;
		String password;
		
		String herokuPsqlUrl = System.getenv("POSTGRES_URL");
		
		String openshiftPsqlUrl = System.getenv("POSTGRESQL_94_CENTOS7_PORT_5432_TCP");
		if (herokuPsqlUrl != null) {
			//heroku
			URI dbUri = new URI(herokuPsqlUrl);
			url = "jdbc:postgresql://" + dbUri.getHost() + ":" + dbUri.getPort() + dbUri.getPath();
			username = dbUri.getUserInfo().split(":")[0];
			password = dbUri.getUserInfo().split(":")[1];
		}else if(openshiftPsqlUrl != null) {		
			//openshift
			username = System.getenv("POSTGRESQL_USER");
			password = System.getenv("POSTGRESQL_PASSWORD");
			String database = System.getenv("POSTGRESQL_DATABASE");
			URI dbUri = new URI(openshiftPsqlUrl);
			url = "jdbc:postgresql://" + dbUri.getHost() + ":" + dbUri.getPort() + "/" + database;
		}else{
			//local
			url = this.properties.getUrl();
			username = this.properties.getUsername();
			password = this.properties.getPassword();
		}
		
		DataSourceBuilder factory = DataSourceBuilder
				.create(this.properties.getClassLoader())
				.url(url)
				.username(username)
				.password(password);
		this.dataSource = factory.build();
		return this.dataSource;
		
	}
	
}
