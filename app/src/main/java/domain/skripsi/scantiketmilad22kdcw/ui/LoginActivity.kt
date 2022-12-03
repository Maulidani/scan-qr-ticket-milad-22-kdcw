package domain.skripsi.scantiketmilad22kdcw.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import domain.skripsi.scantiketmilad22kdcw.R
import domain.skripsi.scantiketmilad22kdcw.adapter.TicketAdapter
import domain.skripsi.scantiketmilad22kdcw.model.Model
import domain.skripsi.scantiketmilad22kdcw.network.ApiClient
import domain.skripsi.scantiketmilad22kdcw.util.Constant
import domain.skripsi.scantiketmilad22kdcw.util.Constant.Companion.setShowProgress
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private var btnLogin:MaterialButton? = null
    private var dialog:BottomSheetDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dialog = BottomSheetDialog(this)
        val view =
            layoutInflater.inflate(R.layout.dialog_login, null)
        btnLogin = view.findViewById(R.id.btnLogin)
        val inputPassword = view.findViewById<TextInputEditText>(R.id.inputPassword)

        dialog?.setCancelable(false)
        dialog?.setContentView(view)
        dialog?.show()

        btnLogin?.setOnClickListener {
            if (inputPassword.text.toString().isNotEmpty()) {
                login(inputPassword.text.toString())

            } else {
                Toast.makeText(applicationContext, "Masukkan password", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun login(password: String) {
        btnLogin?.setShowProgress(true)

        ApiClient.instances.login(password)
            .enqueue(object : Callback<Model.ResponseModel> {
                override fun onResponse(
                    call: Call<Model.ResponseModel>,
                    response: Response<Model.ResponseModel>
                ) {
                    val responseBody = response.body()
                    val message = responseBody?.message

                    if (response.isSuccessful && message == "Success") {
                        Log.e(applicationContext.toString(), "onResponse: $response")

                        Toast.makeText(applicationContext, "Berhasil masuk", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(Intent(Intent(this@LoginActivity, HomeActivity::class.java)))
                        dialog?.dismiss()
                        finish()

                    } else {
                        Toast.makeText(applicationContext, "gagal", Toast.LENGTH_SHORT)
                            .show()
                    }
                    btnLogin?.setShowProgress(false)

                }

                override fun onFailure(call: Call<Model.ResponseModel>, t: Throwable) {
                    Log.e(applicationContext.toString(), "onFailure: ${t.message}")

                    Toast.makeText(applicationContext, "Terjadi kesalahan", Toast.LENGTH_SHORT)
                        .show()

                    btnLogin?.setShowProgress(false)
                }

            })
    }
}