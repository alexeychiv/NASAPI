package gb.android.nasapi.data.repository

import com.google.gson.annotations.SerializedName

class DonkiData {
    @field:SerializedName("messageType")
    val messageType: String? = ""
    @field:SerializedName("messageID")
    val messageID: String? = ""
    @field:SerializedName("messageURL")
    val messageURL: String? = ""
    @field:SerializedName("messageIssueTime")
    val messageIssueTime: String? = ""
    @field:SerializedName("messageBody")
    val messageBody: String? = ""
}