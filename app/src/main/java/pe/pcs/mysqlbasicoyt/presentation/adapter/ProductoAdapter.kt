package pe.pcs.mysqlbasicoyt.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pe.pcs.libpcs.UtilsCommon
import pe.pcs.mysqlbasicoyt.data.model.ProductoModel
import pe.pcs.mysqlbasicoyt.databinding.ItemsProductoBinding

class ProductoAdapter(
    private val listener: IOnClickListener
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    private var lista = emptyList<ProductoModel>()

    interface IOnClickListener {
        fun clickEditar(producto: ProductoModel)
        fun clickEliminar(producto: ProductoModel)
    }

    inner class ProductoViewHolder(private val binding: ItemsProductoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun enlazar(producto: ProductoModel) {
            binding.tvTitulo.text = producto.descripcion
            binding.tvCodigoBarra.text = producto.codigobarra
            binding.tvPrecio.text = UtilsCommon.formatFromDoubleToString(producto.precio)

            binding.ibEditar.setOnClickListener { listener.clickEditar(producto) }
            binding.ibEliminar.setOnClickListener { listener.clickEliminar(producto) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        return ProductoViewHolder(
            ItemsProductoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        holder.enlazar(lista[position])
    }

    fun setData(listaProducto: List<ProductoModel>) {
        this.lista = listaProducto
        notifyDataSetChanged()
    }

}