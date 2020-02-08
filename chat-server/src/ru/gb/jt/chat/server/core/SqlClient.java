package ru.gb.jt.chat.server.core;

import java.sql.*;

public class SqlClient {

    /**
     * База Sql
     */

    /**
     * Для подключения необходим обьект соединения
     */
    private static Connection connection;
    /**
     * Для отправки запросов и получения ответов из БД
     * необходим обьект Statement
     * формировщик запросов
     */
    private static Statement statement;

    /**
     * Подключается к серверу
     */
    synchronized static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:chat-server/chat.db");
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Отключается от сервера
     */
    synchronized static void disConnect() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            // connection close failed.
            throw new RuntimeException(e);
        }
    }

    /**
     * Возвращать никнейм по логину и парлю
     *
     * @param login
     * @param password
     * @return
     */
    synchronized static String getNickname(String login, String password) {
        String query = String.format("select nickname from users where login='%s' and password='%s'", login, password);
        try (ResultSet set = statement.executeQuery(query)) {
            if (set.next()) {
                return set.getString(1);// нумерация колонок в Sql начинается с 1.
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
