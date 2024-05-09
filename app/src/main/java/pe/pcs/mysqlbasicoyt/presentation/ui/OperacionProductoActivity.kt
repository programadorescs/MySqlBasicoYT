package pe.pcs.mysqlbasicoyt.presentation.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pe.pcs.libpcs.UtilsCommon
import pe.pcs.libpcs.UtilsMessage
import pe.pcs.mysqlbasicoyt.R
import pe.pcs.mysqlbasicoyt.data.dao.ProductoDao
import pe.pcs.mysqlbasicoyt.data.model.ProductoModel
import pe.pcs.mysqlbasicoyt.databinding.ActivityOperacionProductoBinding

class OperacionProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOperacionProductoBinding
    private var _id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOperacionProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.extras != null)
            obtenerProducto()

        initListener()
    }

    private fun obtenerProducto() {
        _id = intent.extras?.getInt("id", 0) ?: 0
        binding.etDescripcion.setText(intent.extras?.getString("descripcion", ""))
        binding.etCodigoBarra.setText(intent.extras?.getString("codigobarra", ""))
        binding.etPrecio.setText(intent.extras?.getDouble("precio", 0.0).toString())
    }

    private fun initListener() {

        binding.toolbar.toolbar.apply {
            subtitle = "Operación Producto"
            navigationIcon = AppCompatResources.getDrawable(
                this@OperacionProductoActivity,
                R.drawable.ic_back
            )

            setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }

        binding.toolbar.ibAccion.setImageResource(R.drawable.ic_done)

        binding.toolbar.ibAccion.setOnClickListener {
            if (binding.etDescripcion.text.toString().trim().isEmpty() ||
                binding.etCodigoBarra.text.toString().trim().isEmpty() ||
                binding.etPrecio.text.toString().trim().isEmpty()
            ) {
                UtilsMessage.showAlertOk(
                    "ADVERTENCIA", "Todos los campos son obligatorios", this
                )
                return@setOnClickListener
            }

            grabar(
                ProductoModel(
                    _id,
                    binding.etDescripcion.text.toString(),
                    binding.etCodigoBarra.text.toString(),
                    binding.etPrecio.text.toString().toDouble()
                )
            )
        }
    }

    private fun grabar(producto: ProductoModel) {
        var msgError = ""

        lifecycleScope.launch {
            binding.progressBar.isVisible = true

            withContext(Dispatchers.IO) {
                try {
                    ProductoDao.grabar(producto)
                } catch (ex: Exception) {
                    msgError = ex.message.toString()
                }
            }

            binding.progressBar.isVisible = false

            if (msgError.isNotEmpty()) {
                UtilsMessage.showAlertOk(
                    "ERROR", msgError, this@OperacionProductoActivity
                )
                return@launch
            }

            UtilsMessage.showToast(
                this@OperacionProductoActivity, "Registro grabado correctamente"
            )
            UtilsCommon.cleanEditText(binding.root.rootView)
            _id = 0
            binding.etDescripcion.requestFocus()
            UtilsCommon.hideKeyboard(
                this@OperacionProductoActivity,
                binding.root.rootView
            )

            MainActivity.existeCambio = true
        }
    }
}