package domain.skripsi.scantiketmilad22kdcw.model


class Model {
    data class ResponseModel(
        val message: String,
        val errors: Boolean,
        val data: ArrayList<DataModel>,
        val ticket: DataModel,
        val status: ArrayList<StatusModel>,
        val ticket_category: ArrayList<TicketCategory>,
    )
    data class DataModel(
        val url: String,
        val id: String,
        val ticket: String,
        val quantity: String,
        val customer_name: String,
        val customer_nra: String,
        val customer_phone: String,
        val customer_email: String,
        val customer_address: String,
        val customer_campus: String,
        val merchandise: String,
        val status: String,
        val created_at: String,
        val updated_at: String,
    )
    data class StatusModel(
        val id: String,
        val name: String,
        val description: String,
    )
    data class TicketCategory(
        val id: String,
        val name: String,
        val price: String,
        val description: String,
    )

}