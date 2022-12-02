package domain.skripsi.scantiketmilad22kdcw.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton
import domain.skripsi.scantiketmilad22kdcw.R
import domain.skripsi.scantiketmilad22kdcw.util.Constant.Companion.BASE_URL

class DetailTicketActivity : AppCompatActivity() {
    private val imgBack: ImageView by lazy { findViewById(R.id.imgBack) }
    private val imgCopy: ImageView by lazy { findViewById(R.id.imgCopy) }
    private val ticketNumber: TextView by lazy { findViewById(R.id.tvNumberTicket) }
    private val ticketType: TextView by lazy { findViewById(R.id.tvTicketCategory) }
    private val ticketStatus: TextView by lazy { findViewById(R.id.tvStatusTicket) }
    private val fullName: TextView by lazy { findViewById(R.id.tvName) }
    private val nra: TextView by lazy { findViewById(R.id.tvNra) }
    private val campus: TextView by lazy { findViewById(R.id.tvCampus) }
    private val phone: TextView by lazy { findViewById(R.id.tvPhone) }
    private val email: TextView by lazy { findViewById(R.id.tvEmail) }
    private val address: TextView by lazy { findViewById(R.id.tvAddress) }
    private val tshirt: TextView by lazy { findViewById(R.id.tvTshirt) }
    private val tshirtType: TextView by lazy { findViewById(R.id.tvTshirtType) }
    private val tshirtSize: TextView by lazy { findViewById(R.id.tvTshirtSize) }
    private val ticketLink: TextView by lazy { findViewById(R.id.tvTicketLink) }
    private val btnChat: MaterialButton by lazy { findViewById(R.id.btnChat) }
    private val item: CardView by lazy { findViewById(R.id.cardTicket) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_ticket)

        when (intent.getStringExtra("status")) {
            "attend" -> {
                ticketStatus.text = "Hadir"
                ticketStatus.setTextColor(Color.GREEN)
                ticketLink.text = BASE_URL + "/" + intent.getStringExtra("url")
            }
            "paid" -> {
                ticketStatus.text = "Sudah bayar"
                ticketStatus.setTextColor(Color.GREEN)
                ticketLink.text = BASE_URL + "/" + intent.getStringExtra("url")
            }
            else -> {
                ticketLink.text = "-"

                ticketStatus.text = "Belum bayar"
                ticketStatus.setTextColor(Color.RED)
            }
        }
        ticketNumber.text = "#" + intent.getStringExtra("id")
        ticketType.text = intent.getStringExtra("ticket")
        fullName.text = intent.getStringExtra("customer_name")
        nra.text = intent.getStringExtra("customer_nra")
        campus.text = intent.getStringExtra("customer_campus")
        phone.text = intent.getStringExtra("customer_phone")
        email.text = intent.getStringExtra("customer_email")
        address.text = intent.getStringExtra("customer_address")

        tshirt.text = intent.getStringExtra("merchandise")
        tshirtType.visibility = View.GONE
        tshirtSize.visibility = View.GONE

        imgBack.setOnClickListener {
            finish()
        }

        imgCopy.setOnClickListener {
            val textToCopy = ticketLink.text.toString()
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("Link ticket", textToCopy)
            clipboardManager.setPrimaryClip(clipData)

            Toast.makeText(applicationContext, "Text copied to clipboard", Toast.LENGTH_SHORT)
                .show()

        }

    }

}