package pe.pcs.mysqlbasicoyt.data.dao

import java.sql.Connection
import java.sql.DriverManager

object MySqlConexion {
    fun getConexion(): Connection {
        Class.forName("com.mysql.jdbc.Driver")

        return DriverManager.getConnection(
            "jdbc:mysql://192.168.18.23:3306/dbdemo",
            "jackelprogramador",
            "1234"
        )
    }
}