package gb.android.nasapi.data.repository

import com.google.gson.annotations.SerializedName

class PicData {
    @field:SerializedName("img_src")
    val img_src: String = ""
    @field:SerializedName("earth_date")
    val earth_date: String = ""
}

class MarsData {
    @field:SerializedName("photos")
    val photos: List<PicData> = listOf()
}

