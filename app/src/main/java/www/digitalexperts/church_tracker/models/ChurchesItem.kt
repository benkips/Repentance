package www.digitalexperts.church_tracker.models

data class ChurchesItem(
    val id: String,
    val latitude: String,
    val location: String,
    val longitude: String,
    val name: String,
    val photo: String,
    val status:String
)