package org.zerock.etl_items.sample;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCTests {

    @Test
    public void testConnection() throws Exception{

        //DB Driver Class
        Class.forName("org.mariadb.jdbc.Driver");

        //URL,username,password
        Connection connection = DriverManager.getConnection(
                "jdbc:mariadb://localhost:3306/etl_db",
                "etldbuser",
                "etldbuser");

        System.out.println(connection);//확인하기

        connection.close();//닫기

    }
    @Test
    public void testConnection2() throws Exception {

        // DB Driver Class (PostgreSQL)
        Class.forName("org.postgresql.Driver");

        // URL, username, password
        Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres", // PostgreSQL 연결 URL
                "postgres", // 사용자 이름
                "postgre"  // 비밀번호
        );

        System.out.println(connection); // 확인하기

        connection.close(); // 닫기
    }


}
