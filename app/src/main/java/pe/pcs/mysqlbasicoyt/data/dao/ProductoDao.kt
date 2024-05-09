package pe.pcs.mysqlbasicoyt.data.dao

import pe.pcs.mysqlbasicoyt.data.model.ProductoModel

object ProductoDao {

    fun listar(dato: String): List<ProductoModel> {
        var lista = mutableListOf<ProductoModel>()

        val ps = MySqlConexion.getConexion().prepareStatement(
            "SELECT id, descripcion, codigobarra, precio FROM producto WHERE descripcion LIKE concat('%',?,'%');"
        )

        ps.setString(1, dato)

        val rs = ps.executeQuery()

        while (rs.next()) {
            lista.add(
                ProductoModel(
                    rs.getInt("id"),
                    rs.getString("descripcion"),
                    rs.getString("codigobarra"),
                    rs.getDouble("precio")
                )
            )
        }

        rs.close()
        ps.close()

        return lista

    }

    private fun registrar(producto: ProductoModel) {
        val ps = MySqlConexion.getConexion().prepareStatement(
            "INSERT INTO producto (descripcion, codigobarra, precio) VALUES (?, ?, ?);"
        )

        ps.setString(1, producto.descripcion)
        ps.setString(2, producto.codigobarra)
        ps.setDouble(3, producto.precio)

        ps.executeUpdate()

        ps.close()
    }

    private fun actualizar(producto: ProductoModel) {
        val ps = MySqlConexion.getConexion().prepareStatement(
            "UPDATE producto SET descripcion=?, codigobarra=?, precio=? WHERE id=?"
        )

        ps.setString(1, producto.descripcion)
        ps.setString(2, producto.codigobarra)
        ps.setDouble(3, producto.precio)
        ps.setInt(4, producto.id)

        ps.executeUpdate()

        ps.close()
    }

    fun eliminar(producto: ProductoModel) {
        val ps = MySqlConexion.getConexion().prepareStatement(
            "DELETE FROM producto WHERE id=?"
        )

        ps.setInt(1, producto.id)

        ps.executeUpdate()

        ps.close()
    }

    fun grabar(producto: ProductoModel) {
        if (producto.id == 0) {
            registrar(producto)
        } else {
            actualizar(producto)
        }
    }

}