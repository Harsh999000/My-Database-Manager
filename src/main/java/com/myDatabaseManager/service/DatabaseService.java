package com.myDatabaseManager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Create a new database.
     * 
     * @param dbName The name of the new database.
     */
    public void createDatabase(String dbName) {
        String sql = "CREATE DATABASE " + dbName;
        jdbcTemplate.execute(sql);
    }

    /**
     * Drop an existing database.
     * 
     * @param dbName The name of the database to drop.
     */
    public void dropDatabase(String dbName) {
        String sql = "DROP DATABASE " + dbName;
        jdbcTemplate.execute(sql);
    }

    /**
     * List all databases on the server.
     * 
     * @return A list of database names.
     */
    public List<String> listDatabases() {
        String sql = "SHOW DATABASES";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    /**
     * Use a specific database.
     * 
     * @param dbName The name of the database to use.
     */
    public void useDatabase(String dbName) {
        String sql = "USE " + dbName;
        jdbcTemplate.execute(sql);
    }

    /**
     * Execute a custom SQL query.
     * 
     * @param sqlQuery The SQL query to run.
     */
    public void executeCustomQuery(String sqlQuery) {
        jdbcTemplate.execute(sqlQuery);
    }
}
