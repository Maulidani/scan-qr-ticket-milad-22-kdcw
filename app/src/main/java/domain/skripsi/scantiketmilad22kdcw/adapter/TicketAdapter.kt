package domain.skripsi.scantiketmilad22kdcw.adapter

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import domain.skripsi.scantiketmilad22kdcw.R
import domain.skripsi.scantiketmilad22kdcw.model.Model
import domain.skripsi.scantiketmilad22kdcw.network.ApiClient
import domain.skripsi.scantiketmilad22kdcw.ui.DetailTicketActivity
import domain.skripsi.scantiketmilad22kdcw.util.PreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TicketAdapter(
    private val type: String,
    private val list: ArrayList<Model.DataModel>,
    private val mListener: IUserRecycler
) :
    RecyclerView.Adapter<TicketAdapter.ListViewHolder>() {

    val _type = type

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var sharedPref: PreferencesHelper

        private val ticketNumber: TextView by lazy { itemView.findViewById(R.id.tvNumberTicket) }
        private val ticketType: TextView by lazy { itemView.findViewById(R.id.tvTicketCategory) }
        private val ticketStatus: TextView by lazy { itemView.findViewById(R.id.tvStatusTicket) }
        private val fullName: TextView by lazy { itemView.findViewById(R.id.tvName) }
        private val nra: TextView by lazy { itemView.findViewById(R.id.tvNra) }
        private val campus: TextView by lazy { itemView.findViewById(R.id.tvCampus) }
        private val item: CardView by lazy { itemView.findViewById(R.id.cardTicket) }

        fun bindData(list: Model.DataModel) {

            ticketNumber.text = "#" + list.id
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
            fullName.text = "Nama : " + list.customer_name
            nra.text = "NRA : " + list.customer_nra
            campus.text = "KD : " + list.customer_campus

            item.setOnClickListener {
                optionAlert(list)
            }
        }

        private fun optionAlert(list: Model.DataModel) {
            sharedPref = PreferencesHelper(itemView.context)

            val builder: AlertDialog.Builder = AlertDialog.Builder(itemView.context)

            val options = arrayOf("Lihat detail", "Ganti status pesanan", "Hapus pesanan")
            builder.setItems(
                options
            ) { _, which ->
                when (which) {
                    0 -> {
                        ContextCompat.startActivity(
                            itemView.context,
                            Intent(itemView.context, DetailTicketActivity::class.java)
                                .putExtra("url", list.url)
                                .putExtra("id", list.id)
                                .putExtra("ticket", list.ticket)
                                .putExtra("customer_name", list.customer_name)
                                .putExtra("customer_nra", list.customer_nra)
                                .putExtra("customer_phone", list.customer_phone)
                                .putExtra("customer_email", list.customer_email)
                                .putExtra("customer_address", list.customer_address)
                                .putExtra("customer_campus", list.customer_campus)
                                .putExtra("merchandise", list.merchandise)
                                .putExtra("status", list.status), null
                        )
                    }
                    1 -> {
                        val builderEdit: AlertDialog.Builder = AlertDialog.Builder(itemView.context)
                        val optionsEdit =
                            sharedPref.getStringSet(PreferencesHelper.PREF_STATUS_TICKET)
                                ?.toTypedArray()
                        builderEdit.setItems(
                            optionsEdit
                        ) { _, whichEdit ->
                            for (i in 0..optionsEdit!!.size) {
                                when (whichEdit) {
                                    i -> {
                                        val idStatusTicket =
                                            optionsEdit[i].split(".").toTypedArray()
                                        editPesanan(list.id, idStatusTicket[0])
                                    }
                                }
                            }
                        }
                        val dialogEdit: AlertDialog = builderEdit.create()
                        dialogEdit.show()
                    }
                    2 -> {
                        val builder = AlertDialog.Builder(itemView.context)
                        builder.setTitle("Hapus")
                        builder.setMessage("Hapus tiket ${list.customer_name} ?")

                        builder.setPositiveButton("Ya") { _, _ ->
                            deletePesanan(list.id)
                        }

                        builder.setNegativeButton("Tidak") { _, _ ->
                            // cancel
                        }
                        builder.show()
                    }
                }
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()

        }

        private fun editPesanan(ticketId: String, statusId: String) {

            ApiClient.instances.editTicket(ticketId, statusId)
                .enqueue(object : Callback<Model.ResponseModel> {
                    override fun onResponse(
                        call: Call<Model.ResponseModel>,
                        response: Response<Model.ResponseModel>
                    ) {
                        val responseBody = response.body()
                        val message = responseBody?.message

                        if (response.isSuccessful && message == "Success") {
                            Log.e(itemView.context.toString(), "onResponse: $response")

                            Toast.makeText(
                                itemView.context,
                                "Berhasil ganti status pesanan",
                                Toast.LENGTH_SHORT
                            ).show()

                            mListener.refreshView(true)
                            notifyDataSetChanged()

                        } else {
                            Log.e(itemView.context.toString(), "onResponse: $response")

                            Toast.makeText(itemView.context, "Gagal", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<Model.ResponseModel>, t: Throwable) {
                        Log.e(itemView.context.toString(), "onFailure: ${t.message}")

                        Toast.makeText(itemView.context, "Terjadi Kesalahan", Toast.LENGTH_SHORT)
                            .show()
                    }

                })
        }

        private fun deletePesanan(ticketId: String) {

            ApiClient.instances.deleteTicket(ticketId)
                .enqueue(object : Callback<Model.ResponseModel> {
                    override fun onResponse(
                        call: Call<Model.ResponseModel>,
                        response: Response<Model.ResponseModel>
                    ) {
                        val responseBody = response.body()
                        val message = responseBody?.message

                        if (response.isSuccessful && message == "Success") {
                            Log.e(itemView.context.toString(), "onResponse: $response")

                            Toast.makeText(
                                itemView.context,
                                "Berhasil hapus pesanan",
                                Toast.LENGTH_SHORT
                            ).show()

                            mListener.refreshView(true)
                            notifyDataSetChanged()

                        } else {
                            Log.e(itemView.context.toString(), "onResponse: $response")

                            Toast.makeText(itemView.context, "Gagal", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<Model.ResponseModel>, t: Throwable) {
                        Log.e(itemView.context.toString(), "onFailure: ${t.message}")

                        Toast.makeText(itemView.context, "Terjadi Kesalahan", Toast.LENGTH_SHORT)
                            .show()
                    }

                })
        }
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
            if (list.size <= 3) {
                list.size
            } else {
                3
            }

        } else {
            list.size
        }
    }

    interface IUserRecycler {
        fun refreshView(onUpdate: Boolean)
    }
}