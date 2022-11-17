package domain.skripsi.scantiketmilad22kdcw.util

class Constant {
    companion object {
        const val BASE_URL = "http://192.168.45.5:8000"

        var statusTicket: ArrayList<Array<String>> =
            arrayListOf(
                arrayOf("1", "pending", "Belum melakukan pembayaran"),
                arrayOf("2", "paid", "Sudah melakukan pembayaran"),
                arrayOf("3", "attend", "menghadiri acara")
            )
        var typeTicket: ArrayList<Array<String>> =
            arrayListOf(
                arrayOf("1", "Silver", "165000",""),
                arrayOf("2", "Gold", "200000",""),
                arrayOf("3", "Platinum", "400000","")
            )
    }
}