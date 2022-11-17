package domain.skripsi.scantiketmilad22kdcw.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        getTicket("")
    }


    private fun getTicket(search: String) {
        if (isAdded) {
            rvTicket = requireView().findViewById(R.id.tvTicketNew)

            ApiClient.instances.getTickets(search)
                .enqueue(object : Callback<Model.ResponseModel> {
                    override fun onResponse(
                        call: Call<Model.ResponseModel>,
                        response: Response<Model.ResponseModel>
                    ) {
                        val responseBody = response.body()
                        val message = responseBody?.message
                        val data = responseBody?.data

                        if (response.isSuccessful && message == "Success" && isAdded) {
                            Log.e(TAG, "onResponse: $response")

                            val adapterNewProduct =
                                data?.let { TicketAdapter("home", it, this@HomeFragment) }
                            rvTicket.layoutManager = LinearLayoutManager(requireActivity())
                            rvTicket.adapter = adapterNewProduct

                        } else {
                            Log.e(TAG, "onResponse: $response")

                        }

                    }

                    override fun onFailure(call: Call<Model.ResponseModel>, t: Throwable) {
                        Log.e(TAG, "onResponse: ${t.message}")

                    }

                })
        }
    }

    override fun refreshView(onUpdate: Boolean) {
        //
    }

}