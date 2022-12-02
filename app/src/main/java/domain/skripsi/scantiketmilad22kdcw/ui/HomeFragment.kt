package domain.skripsi.scantiketmilad22kdcw.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import domain.skripsi.scantiketmilad22kdcw.R
import domain.skripsi.scantiketmilad22kdcw.adapter.TicketAdapter
import domain.skripsi.scantiketmilad22kdcw.network.ApiClient
import domain.skripsi.scantiketmilad22kdcw.model.Model
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(), TicketAdapter.IUserRecycler {
    private lateinit var rvTicket: RecyclerView
    private lateinit var cardTicketSale: CardView
    private lateinit var cardTicketPaid: CardView
    private lateinit var tvSaleCount: TextView
    private lateinit var tvPaidCount: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        getTicket("")
    }

    private fun init(){
        if (isAdded) {
            rvTicket = requireView().findViewById(R.id.tvTicketNew)
            tvSaleCount = requireView().findViewById(R.id.tvSale)
            tvPaidCount = requireView().findViewById(R.id.tvPaid)
            cardTicketSale = requireView().findViewById(R.id.cardTicketSale)
            cardTicketPaid = requireView().findViewById(R.id.cardTicketPaid)

            cardTicketSale.setOnClickListener {
                startActivity(Intent(requireContext(), ListTicketActivity::class.java)
                    .putExtra("status", "sale"))
            }
            cardTicketPaid.setOnClickListener {
                startActivity(Intent(requireContext(), ListTicketActivity::class.java)
                    .putExtra("status", "paid"))
            }
        }
    }

    private fun getTicket(search: String) {
        if (isAdded) {

            ApiClient.instances.getTickets(search)
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

                        if (response.isSuccessful && message == "Success" && isAdded) {
                            Log.e(requireContext().toString(), "onResponse: $response")

                            tvSaleCount.text = total.toString()
                            tvPaidCount.text = totalPaid.toString()

                            val adapterNewProduct =
                                data?.let { TicketAdapter("home", it, this@HomeFragment) }
                            rvTicket.layoutManager = LinearLayoutManager(requireActivity())
                            rvTicket.adapter = adapterNewProduct

                        } else {
                            Log.e(requireContext().toString(), "onResponse: $response")

                            Toast.makeText(requireContext(), "Gagal", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }

                    override fun onFailure(call: Call<Model.ResponseModel>, t: Throwable) {
                        Log.e(requireContext().toString(), "onFailure: ${t.message}")

                        Toast.makeText(requireContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT)
                            .show()

                    }

                })
        }
    }

    override fun refreshView(onUpdate: Boolean) {
        if (isAdded && onUpdate) {
            getTicket("")
        }
    }

}