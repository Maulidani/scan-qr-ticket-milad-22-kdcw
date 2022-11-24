package domain.skripsi.scantiketmilad22kdcw.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import domain.skripsi.scantiketmilad22kdcw.R
import domain.skripsi.scantiketmilad22kdcw.model.Model
import domain.skripsi.scantiketmilad22kdcw.network.ApiClient
import domain.skripsi.scantiketmilad22kdcw.util.PreferencesHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var sharedPref: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        sharedPref = PreferencesHelper(applicationContext)

        CoroutineScope(Dispatchers.Main).launch {
            getDatas()
            delay(2000)

        }
    }

    private fun getDatas() {

        ApiClient.instances.getData()
            .enqueue(object : Callback<Model.ResponseModel> {
                override fun onResponse(
                    call: Call<Model.ResponseModel>,
                    response: Response<Model.ResponseModel>
                ) {
                    val responseBody = response.body()
                    val message = responseBody?.message
                    val statusTicket = responseBody?.status
                    val categoryTicket = responseBody?.ticket_category

                    if (response.isSuccessful && message == "Success") {
                        Log.e(ContentValues.TAG, "onResponse: $responseBody")

                        val setStatusTicket: MutableSet<String> = HashSet()
                        val setTicketCategory: MutableSet<String> = HashSet()

                        if (statusTicket != null) {
                            for (i in statusTicket) {
                                setStatusTicket.add("${i.id}.${i.name}")
                            }
                        } else {
                            Toast.makeText(applicationContext, "Tidak ada data status ticket", Toast.LENGTH_SHORT).show()
                            sharedPref.logout()
                            finish()
                        }

                        if (categoryTicket != null) {
                            for (i in categoryTicket) {
                                setTicketCategory.add("${i.id}.${i.name}")
                            }
                        } else {
                            Toast.makeText(applicationContext, "Tidak ada data kategori ticket", Toast.LENGTH_SHORT).show()
                            sharedPref.logout()
                            finish()
                        }

                        sharedPref.logout()
                        sharedPref.put(PreferencesHelper.PREF_STATUS_TICKET, setStatusTicket)
                        sharedPref.put(PreferencesHelper.PREF_CATEGORY_TICKET, setTicketCategory)

                        Log.e(ContentValues.TAG, "status ticket pref: "+sharedPref.getStringSet(PreferencesHelper.PREF_STATUS_TICKET))
                        Log.e(ContentValues.TAG, "category ticket pref: "+sharedPref.getStringSet(PreferencesHelper.PREF_CATEGORY_TICKET))

//                        for (i in sharedPref.getStringSet(PreferencesHelper.PREF_STATUS_TICKET)!!){
//                            Log.e(ContentValues.TAG, i.split(",").toTypedArray().joinToString())
//                        }
//                        for (i in sharedPref.getStringSet(PreferencesHelper.PREF_CATEGORY_TICKET)!!){
//                            Log.e(ContentValues.TAG, i.split(",").toTypedArray().joinToString())
//                        }

                        startActivity(Intent(this@SplashScreenActivity, HomeActivity::class.java))
                        finish()

                    } else {
                        Log.e(ContentValues.TAG, "onResponse: $response")
                        Toast.makeText(applicationContext, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()

                    }

                }

                override fun onFailure(call: Call<Model.ResponseModel>, t: Throwable) {
                    Log.e(ContentValues.TAG, "onResponse: ${t.message}")

                    Toast.makeText(applicationContext, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()

                }

            })
    }
}