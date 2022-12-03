package domain.skripsi.scantiketmilad22kdcw.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import domain.skripsi.scantiketmilad22kdcw.R
import domain.skripsi.scantiketmilad22kdcw.adapter.TicketAdapter
import domain.skripsi.scantiketmilad22kdcw.model.Model
import domain.skripsi.scantiketmilad22kdcw.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListTicketActivity : AppCompatActivity(), TicketAdapter.IUserRecycler {
    private val imgBack: ImageView by lazy { findViewById(R.id.imgBack) }
    private val tvHead: TextView by lazy { findViewById(R.id.tvHead) }
    private val tvDataEmpty: TextView by lazy { findViewById(R.id.tvDataisEmpty) }
    private val inputSearch: TextInputEditText by lazy { findViewById(R.id.inputSearch) }
    private val inputFilter: AutoCompleteTextView by lazy { findViewById(R.id.inputFilter) }
    private val rvTicket: RecyclerView by lazy { findViewById(R.id.rvSaleTicket) }
    private val dataFilter = arrayListOf("pending", "paid", "attend", "all")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_ticket)

        getTicket("", "")

        val arrayDateAdapter = ArrayAdapter(
            this,
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
            dataFilter
        )
        inputFilter.setAdapter(arrayDateAdapter)
        inputFilter.setOnItemClickListener { _, _, position, _ ->
            getTicket("", dataFilter[position])
        }

        imgBack.setOnClickListener { finish() }

        inputSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                getTicket(s.toString(), "")
                if (s.isEmpty()) getTicket(s.toString(), "")
            }
        })

    }

    override fun onResume() {
        super.onResume()
        //
    }

    private fun getTicket(search: String, status: String) {

        ApiClient.instances.getTickets(search, status)
            .enqueue(object : Callback<Model.ResponseModel> {
                override fun onResponse(
                    call: Call<Model.ResponseModel>,
                    response: Response<Model.ResponseModel>
                ) {
                    val responseBody = response.body()
                    val message = responseBody?.message
                    val data = responseBody?.data
                    val total = responseBody?.total
                    val totalPaid = responseBody?.total_paid

                    if (response.isSuccessful && message == "Success") {
                        Log.e(applicationContext.toString(), "onResponse: $response")

                        tvDataEmpty.visibility = View.INVISIBLE
                        rvTicket.visibility = View.VISIBLE

                        val adapterNewProduct =
                            data?.let { TicketAdapter("-", it, this@ListTicketActivity) }
                        rvTicket.layoutManager = LinearLayoutManager(this@ListTicketActivity)
                        rvTicket.adapter = adapterNewProduct

                    } else {
                        Log.e(applicationContext.toString(), "onResponse: $response")

                        tvDataEmpty.visibility = View.VISIBLE
                        rvTicket.visibility = View.INVISIBLE
                    }

                }

                override fun onFailure(call: Call<Model.ResponseModel>, t: Throwable) {
                    Log.e(applicationContext.toString(), "onFailure: ${t.message}")

                    Toast.makeText(applicationContext, "Terjadi kesalahan", Toast.LENGTH_SHORT)
                        .show()

                    tvDataEmpty.visibility = View.VISIBLE
                    rvTicket.visibility = View.INVISIBLE

                }

            })
    }

    override fun refreshView(onUpdate: Boolean) {
        if (onUpdate) {
            getTicket("", "")
        }
    }
}
