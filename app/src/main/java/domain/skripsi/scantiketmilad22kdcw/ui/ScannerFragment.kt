package domain.skripsi.scantiketmilad22kdcw.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import domain.skripsi.scantiketmilad22kdcw.R


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
//                scanner(result.text)
                Toast.makeText(requireContext().applicationContext, result.text, Toast.LENGTH_LONG)
                    .show()

                if (isAdded) showDialog("Success")

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

    private fun showDialog(status: String) {
        pbLoading.visibility = View.VISIBLE
        pbLoading.visibility = View.INVISIBLE

        if (status == "Success") {
            val pDialog =
                SweetAlertDialog(requireView().context, SweetAlertDialog.SUCCESS_TYPE)
            pDialog.titleText = status
            pDialog.contentText = "Berhasil Scan"
            pDialog.setCancelable(false)
            pDialog.show()
        } else {
            val pDialog =
                SweetAlertDialog(requireView().context, SweetAlertDialog.ERROR_TYPE)
            pDialog.titleText = status
            pDialog.setCancelable(false)
            pDialog.show()
        }
    }

}