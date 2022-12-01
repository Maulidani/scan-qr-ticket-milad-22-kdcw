package domain.skripsi.scantiketmilad22kdcw.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import domain.skripsi.scantiketmilad22kdcw.R
import domain.skripsi.scantiketmilad22kdcw.adapter.TicketAdapter
import domain.skripsi.scantiketmilad22kdcw.model.Model
import domain.skripsi.scantiketmilad22kdcw.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ScannerFragment : Fragment() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var pbLoading: ProgressBar
    private lateinit var cardAttendManual: CardView
    private lateinit var btnAttendManual: MaterialButton
    private lateinit var fabCardAttendManual: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scanner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scannerView: CodeScannerView = requireActivity().findViewById(R.id.scanner)
        pbLoading = requireActivity().findViewById(R.id.pbLoading)
        fabCardAttendManual = requireActivity().findViewById(R.id.fabCardAttendManual)
        cardAttendManual = requireActivity().findViewById(R.id.cardAttendManual)
        btnAttendManual = requireActivity().findViewById(R.id.btnAttendManual)

        cardAttendManual.visibility = View.GONE

        codeScanner = CodeScanner(requireContext().applicationContext, scannerView)

        codeScanner.decodeCallback = DecodeCallback { result ->

            requireActivity().runOnUiThread(Runnable {
                scanAttend(result.text)
            })

        }

        scannerView.setOnClickListener { codeScanner.startPreview() }

        fabCardAttendManual.setOnClickListener {
            if (cardAttendManual.isVisible) {
                fabCardAttendManual.setImageResource(R.drawable.ic_arrow_up)
                cardAttendManual.visibility = View.GONE
            } else {
                fabCardAttendManual.setImageResource(R.drawable.ic_arrow_down)
                cardAttendManual.visibility = View.VISIBLE
            }
        }

    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun showDialog(status: String, customerName: String) {
        if (status == "Success") {
            val pDialog =
                SweetAlertDialog(requireView().context, SweetAlertDialog.SUCCESS_TYPE)
            pDialog.titleText = "Sukses scan hadir"
            pDialog.contentText = customerName
            pDialog.setCancelable(false)
            pDialog.show()

        } else {
            val pDialog =
                SweetAlertDialog(requireView().context, SweetAlertDialog.ERROR_TYPE)
            pDialog.titleText = "Gagal"
            pDialog.contentText = "Tiket tidak terdaftar"
            pDialog.setCancelable(false)
            pDialog.show()
        }
    }

    private fun scanAttend(nraCampus: String) {
        if (isAdded) {
            pbLoading.visibility = View.VISIBLE

            ApiClient.instances.scanTicketAttend(nraCampus, "3")
                .enqueue(object : Callback<Model.ResponseModel> {
                    override fun onResponse(
                        call: Call<Model.ResponseModel>,
                        response: Response<Model.ResponseModel>
                    ) {
                        val responseBody = response.body()
                        val message = responseBody?.message
                        val ticket = responseBody?.ticket

                        if (response.isSuccessful && message == "Success" && isAdded) {
                            Log.e(requireContext().toString(), "onResponse: $response")

                            ticket?.customer_name?.let { showDialog(message, it) }

                        } else if (message == "Attend") {
                            Log.e(requireContext().toString(), "onResponse: $response")

                            Toast.makeText(requireContext(), "Sudah hadir", Toast.LENGTH_SHORT)
                                .show()

                        } else {
                            Log.e(requireContext().toString(), "onResponse: $response")

                            showDialog("Failed", "")
                        }

                        pbLoading.visibility = View.INVISIBLE

                    }

                    override fun onFailure(call: Call<Model.ResponseModel>, t: Throwable) {
                        Log.e(requireContext().toString(), "onFailure: ${t.message}")

                        Toast.makeText(requireContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT)
                            .show()

                        pbLoading.visibility = View.INVISIBLE

                    }

                })
        }
    }

}