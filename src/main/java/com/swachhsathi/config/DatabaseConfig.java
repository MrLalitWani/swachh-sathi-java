package com.swachhsathi.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
public class DatabaseConfig {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConfig.class);

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username:sa}")
    private String defaultUsername;

    @Value("${spring.datasource.password:}")
    private String defaultPassword;

    @Value("${spring.datasource.driverClassName:org.h2.Driver}")
    private String driverClassName;

    @Bean
    @Primary
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();

        // Check if the URL is a cloud Postgres URI (e.g. Render, Railway: postgres:// or postgresql://)
        if (datasourceUrl.startsWith("postgres://") || (datasourceUrl.startsWith("postgresql://") && !datasourceUrl.startsWith("jdbc:"))) {
            try {
                URI uri = new URI(datasourceUrl.replace("jdbc:", ""));
                String userInfo = uri.getUserInfo();
                String username = defaultUsername;
                String password = defaultPassword;

                if (userInfo != null && userInfo.contains(":")) {
                    String[] parts = userInfo.split(":", 2);
                    username = parts[0];
                    password = parts[1];
                }

                String host = uri.getHost();
                int port = uri.getPort() > 0 ? uri.getPort() : 5432;
                String path = uri.getPath(); // /databaseName

                String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + path;
                log.info("Normalized Cloud PostgreSQL URI to JDBC URL: {}", jdbcUrl);

                config.setJdbcUrl(jdbcUrl);
                config.setUsername(username);
                config.setPassword(password);
                config.setDriverClassName("org.postgresql.Driver");
            } catch (Exception e) {
                log.warn("Could not parse cloud PostgreSQL URI, falling back to raw URL: {}", e.getMessage());
                config.setJdbcUrl(datasourceUrl);
                config.setUsername(defaultUsername);
                config.setPassword(defaultPassword);
                config.setDriverClassName(driverClassName);
            }
        } else {
            // Standard JDBC URL (e.g. H2 or properly formatted jdbc:postgresql://)
            config.setJdbcUrl(datasourceUrl);
            config.setUsername(defaultUsername);
            config.setPassword(defaultPassword);
            config.setDriverClassName(driverClassName);
        }

        return new HikariDataSource(config);
    }
}
