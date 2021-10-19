package gb.android.nasapi.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import gb.android.nasapi.R
import gb.android.nasapi.presentation.apod.ApodFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*Log.d("BLAH", ">>>>>>>>>>>>>>>>>>>>>>>>> MAIN ACTIVITY")

        val callback = object : retrofit2.Callback<ApodDTO> {
            override fun onResponse(call: Call<ApodDTO>, response: Response<ApodDTO>) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d("BLAH", "RETROFIT SUCCESS: + ${response.body().toString()}")
                }
            }

            override fun onFailure(call: Call<ApodDTO>, t: Throwable) {
                Log.d("BLAH", "RETROFIT FAILURE")
            }

        }

        val retrofit = Retrofit()
        retrofit.getRetrofitImpl().getApod(BuildConfig.NASA_API_KEY).enqueue(callback)*/

        if (savedInstanceState == null)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, ApodFragment.newInstance())
                .commit()
    }
}