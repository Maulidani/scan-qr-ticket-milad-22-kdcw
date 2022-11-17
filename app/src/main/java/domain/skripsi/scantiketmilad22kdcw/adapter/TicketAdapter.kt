package domain.skripsi.scantiketmilad22kdcw.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import domain.skripsi.scantiketmilad22kdcw.R
import domain.skripsi.scantiketmilad22kdcw.model.Model
import domain.skripsi.scantiketmilad22kdcw.ui.DetailTicketActivity

class TicketAdapter(
    private val type: String,
    private val list: ArrayList<Model.DataModel>,
    private val mListener: IUserRecycler
) :
    RecyclerView.Adapter<TicketAdapter.ListViewHolder>() {

    val _type = type

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ticketNumber: TextView by lazy { itemView.findViewById(R.id.tvNumberTicket) }
        private val ticketType: TextView by lazy { itemView.findViewById(R.id.tvTicketCategory) }
        private val ticketStatus: TextView by lazy { itemView.findViewById(R.id.tvStatusTicket) }
        private val fullName: TextView by lazy { itemView.findViewById(R.id.tvName) }
        private val nra: TextView by lazy { itemView.findViewById(R.id.tvNra) }
        private val campus: TextView by lazy { itemView.findViewById(R.id.tvCampus) }
        private val item: CardView by lazy { itemView.findViewById(R.id.cardTicket) }

        fun bindData(list: Model.DataModel) {

            ticketNumber.text = "#"+list.id
            ticketType.text = list.ticket
            when (list.status) {
                "attend" -> {
                    ticketStatus.text = "Hadir"
                    ticketStatus.setTextColor(Color.GREEN)
                }
                "paid" -> {
                    ticketStatus.text = "Sudah bayar"
                    ticketStatus.setTextColor(Color.GREEN)
                }
                else -> {
                    ticketStatus.text = "Belum bayar"
                    ticketStatus.setTextColor(Color.RED)
                }
            }
            fullName.text = list.customer_name
            nra.text = list.customer_nra
            campus.text = "KD : "+list.customer_campus

            item.setOnClickListener {
               optionAlert(list)

            }
        }

        private fun optionAlert(list: Model.DataModel) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(itemView.context)

            val options = arrayOf("Lihat detail", "Sudah bayar", "Batalkan pembayaran", "Hapus pesanan")
            builder.setItems(
                options
            ) { _, which ->
                when (which) {
                    0 -> {
                        ContextCompat.startActivity(
                            itemView.context,
                            Intent(itemView.context, DetailTicketActivity::class.java)
                                .putExtra("url",list.url)
                                .putExtra("id",list.id)
                                .putExtra("ticket",list.ticket)
                                .putExtra("customer_name",list.customer_name)
                                .putExtra("customer_nra",list.customer_nra)
                                .putExtra("customer_phone",list.customer_phone)
                                .putExtra("customer_email",list.customer_email)
                                .putExtra("customer_address",list.customer_address)
                                .putExtra("customer_campus",list.customer_campus)
                                .putExtra("merchandise",list.merchandise)
                                .putExtra("status",list.status)
                            , null
                        )
                    }
                    1 -> {
                        val builder = AlertDialog.Builder(itemView.context)
                        builder.setTitle("Sudah bayar")
                        builder.setMessage("pesanan tiket ${list.customer_name} ?")

                        builder.setPositiveButton("Ya") { _, _ ->
//                            paid(list.id)
                        }

                        builder.setNegativeButton("Tidak") { _, _ ->
                            // cancel
                        }
                        builder.show()
                    }
                    2 -> {
                        val builder = AlertDialog.Builder(itemView.context)
                        builder.setTitle("Batalkan pembayaran")
                        builder.setMessage("pesanan tiket ${list.customer_name} ?")

                        builder.setPositiveButton("Ya") { _, _ ->
//                            pending(list.id)
                        }

                        builder.setNegativeButton("Tidak") { _, _ ->
                            // cancel
                        }
                        builder.show()
                    }
                    3 -> {
//                        val builder = AlertDialog.Builder(itemView.context)
//                        builder.setTitle("Hapus")
//                        builder.setMessage("Hapus tiket ${list.customer_name} ?")
//
//                        builder.setPositiveButton("Ya") { _, _ ->
//                            delete(list.id)
//                        }
//
//                        builder.setNegativeButton("Tidak") { _, _ ->
//                            // cancel
//                        }
//                        builder.show()
                    }
                }
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

//        private fun delete(id: String) {
//
//            ApiClient.instances.deleteProduct(id, userId)
//                .enqueue(object : Callback<Model.ResponseModel> {
//                    override fun onResponse(
//                        call: Call<Model.ResponseModel>,
//                        response: Response<Model.ResponseModel>
//                    ) {
//                        val responseBody = response.body()
//                        val message = responseBody?.message
//
//                        if (response.isSuccessful && message == "Success") {
//                            Toast.makeText(itemView.context, "Berhasil hapus produk", Toast.LENGTH_SHORT).show()
//                            mListener.refreshView(true)
//                            notifyDataSetChanged()
//
//                        } else {
//                            Toast.makeText(itemView.context, "Gagal", Toast.LENGTH_SHORT)
//                                .show()
//                        }
//                    }
//
//                    override fun onFailure(call: Call<Model.ResponseModel>, t: Throwable) {
//                        Toast.makeText(
//                            itemView.context,
//                            t.message.toString(),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//
//                })
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_ticket_sale, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int {
        return if (_type == "home") {
            if (list.size <= 6) {
                list.size
            } else {
                6
            }

        } else {
            list.size
        }
    }

    interface IUserRecycler {
        fun refreshView(onUpdate: Boolean)
    }
}