package edu.school21.sockets.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ComponentScan("edu.school21.sockets")
@PropertySource("classpath:db.properties")
public class SocketsApplicationConfig {
  @Value("${db.driver.name}")
  private String driverClassName;

  @Value("${db.url}")
  private String url;

  @Value("${db.user}")
  private String userName;

  @Value("${db.password}")
  private String password;

  @Bean
  public DriverManagerDataSource springDataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setUrl(url);
    dataSource.setUsername(userName);
    dataSource.setPassword(password);
    dataSource.setDriverClassName(driverClassName);
    return dataSource;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
