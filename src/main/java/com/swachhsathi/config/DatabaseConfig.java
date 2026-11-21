package com.swachhsathi.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
public class DatabaseConfig {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConfig.class);
    private static final String DEFAULT_H2_URL = "jdbc:h2:mem:swachhsathidb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";

    @Value("${spring.datasource.url:}")
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
        String url = (datasourceUrl != null) ? datasourceUrl.trim() : "";

        // If the URL is invalid or literal text like "H2 in-memory", fall back to valid H2 JDBC URL
        if (!url.startsWith("jdbc:") && !url.startsWith("postgres://") && !url.startsWith("postgresql://")) {
            log.warn("Provided datasource URL '{}' is not a valid JDBC/PostgreSQL URL. Falling back to default in-memory H2: {}", url, DEFAULT_H2_URL);
            url = DEFAULT_H2_URL;
        }

        // Handle cloud Postgres URI (e.g. postgres://user:pass@host:port/db)
        if (url.startsWith("postgres://") || (url.startsWith("postgresql://") && !url.startsWith("jdbc:"))) {
            try {
                URI uri = new URI(url);
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
                log.warn("Could not parse cloud PostgreSQL URI ({}), falling back to default H2: {}", e.getMessage(), DEFAULT_H2_URL);
                config.setJdbcUrl(DEFAULT_H2_URL);
                config.setUsername("sa");
                config.setPassword("");
                config.setDriverClassName("org.h2.Driver");
            }
        } else {
            // Standard JDBC URL (e.g. H2 or properly formatted jdbc:postgresql://)
            config.setJdbcUrl(url);
            config.setUsername(url.contains(":h2:") ? "sa" : defaultUsername);
            config.setPassword(url.contains(":h2:") ? "" : defaultPassword);
            config.setDriverClassName(url.contains(":h2:") ? "org.h2.Driver" : driverClassName);
        }

        return new HikariDataSource(config);
    }
}
