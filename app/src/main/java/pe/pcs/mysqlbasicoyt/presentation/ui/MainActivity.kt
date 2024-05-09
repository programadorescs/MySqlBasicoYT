package pe.pcs.mysqlbasicoyt.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pe.pcs.libpcs.UtilsCommon
import pe.pcs.libpcs.UtilsMessage
import pe.pcs.mysqlbasicoyt.R
import pe.pcs.mysqlbasicoyt.data.dao.ProductoDao
import pe.pcs.mysqlbasicoyt.data.model.ProductoModel
import pe.pcs.mysqlbasicoyt.databinding.ActivityMainBinding
import pe.pcs.mysqlbasicoyt.presentation.adapter.ProductoAdapter

class MainActivity : AppCompatActivity(), ProductoAdapter.IOnClickListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener()
        leerProducto("")
    }

    override fun onResume() {
        super.onResume()

        if(!existeCambio) return

        leerProducto(binding.etBuscar.text.toString().trim())
        existeCambio = false
    }

    private fun initListener() {
        binding.rvLista.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ProductoAdapter(this@MainActivity)
        }

        binding.tilBuscar.setEndIconOnClickListener {
            leerProducto(binding.etBuscar.text.toString().trim())
        }

        binding.etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if (binding.etBuscar.text.toString().trim().isEmpty()) {
                    leerProducto("")
                    UtilsCommon.hideKeyboard(
                        this@MainActivity, binding.root.rootView
                    )
                }
            }

        })

        binding.toolbar.ibAccion.setOnClickListener {
            startActivity(
                Intent(this, OperacionProductoActivity::class.java)
            )
        }
    }

    private fun leerProducto(dato: String) {
        var msgError = ""

        lifecycleScope.launch {
            binding.progressBar.isVisible = true

            val result = withContext(Dispatchers.IO) {
                try {
                    ProductoDao.listar(dato)
                } catch (e: Exception) {
                    msgError = e.message.toString()
                    emptyList<ProductoModel>()
                }
            }

            binding.progressBar.isVisible = false

            if (msgError.isNotEmpty())
                UtilsMessage.showAlertOk("ERROR", msgError, this@MainActivity)

            (binding.rvLista.adapter as ProductoAdapter).setData(result)
        }
    }

    private fun eliminar(producto: ProductoModel) {
        var msgError = ""

        lifecycleScope.launch {
            binding.progressBar.isVisible = true

            withContext(Dispatchers.IO) {
                try {
                    ProductoDao.eliminar(producto)
                } catch (e: Exception) {
                    msgError = e.message.toString()
                }
            }

            binding.progressBar.isVisible = false

            if (msgError.isNotEmpty()) {
                UtilsMessage.showAlertOk("ERROR", msgError, this@MainActivity)
                return@launch
            }

            UtilsMessage.showToast(this@MainActivity, "Producto eliminado")
            leerProducto(binding.etBuscar.text.toString().trim())
        }
    }

    override fun clickEditar(producto: ProductoModel) {
        startActivity(
            Intent(this, OperacionProductoActivity::class.java).apply {
                putExtra("id", producto.id)
                putExtra("descripcion", producto.descripcion)
                putExtra("codigobarra", producto.codigobarra)
                putExtra("precio", producto.precio)
            }
        )
    }

    override fun clickEliminar(producto: ProductoModel) {
        MaterialAlertDialogBuilder(this).apply {
            setCancelable(false)
            setTitle("ELIMINAR")
            setMessage("¿Desea eliminar el registro: ${producto.descripcion}?")

            setPositiveButton("SI") { dialog, _ ->
                eliminar(producto)
                dialog.dismiss()
            }

            setNegativeButton("NO") { dialog, _ ->
                dialog.cancel()
            }
        }.create().show()
    }

    companion object {
        var existeCambio = false
    }
}