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
	

//	@ConfigurationProperties(prefix = DataSourceProperties.PREFIX)
	@Bean(destroyMethod = "close")
	DataSource realDataSource() throws URISyntaxException {
		String url;
		String username;
		String password;
		//Add POSTGRES_URL to enviroment variable like heroku.
		//for example on Openshift
		//oc new-app openshift-sample 
		//	-e POSTGRES_URL=postgres://newuser123:hogehoge123@xxx.xx.xx.xxx/database_name
		//	-e SPRING_PROFILES_ACTIVE=production
		String databaseUrl = System.getenv("POSTGRES_URL");
		
		if (databaseUrl != null) {
			URI dbUri = new URI(databaseUrl);
			url = "jdbc:postgresql://" + dbUri.getHost() + ":" + dbUri.getPort() + dbUri.getPath();
			username = dbUri.getUserInfo().split(":")[0];
			password = dbUri.getUserInfo().split(":")[1];
		}else {
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
