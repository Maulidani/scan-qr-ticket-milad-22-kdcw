package domain.skripsi.scantiketmilad22kdcw.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton
import domain.skripsi.scantiketmilad22kdcw.R

class DetailTicketActivity : AppCompatActivity() {
    private val imgBack:ImageView by lazy { findViewById(R.id.imgBack) }
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

        when (intent.getStringExtra("istatus")) {
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
        ticketNumber.text = "#"+intent.getStringExtra("id")
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
        ticketLink.text = intent.getStringExtra("url")

    }

}