package domain.skripsi.scantiketmilad22kdcw.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
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
import com.google.android.material.textfield.TextInputEditText
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
    private lateinit var inputNra: TextInputEditText
    private lateinit var inputCampus: AutoCompleteTextView
    private lateinit var btnAttendManual: MaterialButton
    private lateinit var fabCardAttendManual: FloatingActionButton
    private val dataCampus = arrayListOf("dipa", "umi")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scanner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isAdded) {
            val scannerView: CodeScannerView = requireView().findViewById(R.id.scanner)
            pbLoading = requireView().findViewById(R.id.pbLoading)
            fabCardAttendManual = requireView().findViewById(R.id.fabCardAttendManual)
            cardAttendManual = requireView().findViewById(R.id.cardAttendManual)
            cardAttendManual = requireView().findViewById(R.id.cardAttendManual)
            inputNra = requireView().findViewById(R.id.inputNra)
            inputCampus = requireView().findViewById(R.id.inputCampus)
            btnAttendManual = requireView().findViewById(R.id.btnAttendManual)

            cardAttendManual.visibility = View.GONE

            codeScanner = CodeScanner(requireContext().applicationContext, scannerView)

            codeScanner.decodeCallback = DecodeCallback { result ->
                if (isAdded) {
                    requireActivity().runOnUiThread(Runnable {
                        scanAttend(result.text)
                    })
                }
            }

            val arrayDateAdapter = ArrayAdapter(
                requireContext(),
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                dataCampus
            )
            inputCampus.setAdapter(arrayDateAdapter)

            scannerView.setOnClickListener { codeScanner.startPreview() }

            fabCardAttendManual.setOnClickListener {
                if (isAdded) {
                    if (cardAttendManual.isVisible) {
                        fabCardAttendManual.setImageResource(R.drawable.ic_arrow_up)
                        cardAttendManual.visibility = View.GONE
                    } else {
                        fabCardAttendManual.setImageResource(R.drawable.ic_arrow_down)
                        cardAttendManual.visibility = View.VISIBLE
                    }
                }
            }

            btnAttendManual.setOnClickListener {
                if (isAdded) {
                    if (inputNra.text.toString().isNotEmpty() && inputCampus.text.toString()
                            .isNotEmpty()
                    ) {
                        val ticketData =
                            inputNra.text.toString() + "," + inputCampus.text.toString()
                        scanAttend(ticketData)
                    } else {
                        Toast.makeText(requireContext(), "Lengkapi data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        if (isAdded) {
            codeScanner.startPreview()
        }
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
                            Log.e(requireView().toString(), "onResponse: $response")

                            ticket?.customer_name?.let { showDialog(message, it) }

                        } else if (message == "Attend") {
                            Log.e(requireView().toString(), "onResponse: $response")

                            Toast.makeText(requireContext(), "Sudah hadir", Toast.LENGTH_SHORT)
                                .show()

                        } else {
                            Log.e(requireView().toString(), "onResponse: $response")

                            showDialog("Failed", "")
                        }

                        pbLoading.visibility = View.INVISIBLE
                        codeScanner.startPreview()

                    }

                    override fun onFailure(call: Call<Model.ResponseModel>, t: Throwable) {
                        Log.e(requireView().toString(), "onFailure: ${t.message}")

                        Toast.makeText(requireContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT)
                            .show()

                        pbLoading.visibility = View.INVISIBLE
                        codeScanner.startPreview()

                    }

                })
        }
    }

}