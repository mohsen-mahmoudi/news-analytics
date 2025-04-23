package com.demo.microservice_2021;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

@EnableDiscoveryClient
@SpringBootApplication
public class ElasticQueryServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElasticQueryServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner checkDatabase(DataSource dataSource) {
        return args -> {
            try (Connection conn = dataSource.getConnection()) {
                DatabaseMetaData meta = conn.getMetaData();
                ResultSet tables = meta.getTables(null, "public", "%", new String[]{"TABLE"});
                System.out.println(conn.toString());
                while (tables.next()) {
                    System.out.println("Found table: " + tables.getString("TABLE_NAME"));
                }
            }
        };
    }
}
