package com.example.apiflags

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import coil3.load
import com.example.apiflags.data.Country

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_COUNTRY = "extra_country"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val country = intent.getSerializableExtra(EXTRA_COUNTRY) as? Country

        val imgFlag: ImageView = findViewById(R.id.imgFlagDetail)
        val tvName: TextView = findViewById(R.id.tvCountryNameDetail)
        val tvIso2: TextView = findViewById(R.id.tvValueIso2)
        val tvIso3: TextView = findViewById(R.id.tvValueIso3)
        val tvInitial: TextView = findViewById(R.id.tvValueInitial)

        country?.let {
            tvName.text = it.name
            tvIso2.text = it.iso2
            tvIso3.text = it.iso3
            tvInitial.text = it.name.first().uppercase()

            imgFlag.load(it.flag)
        }

        findViewById<android.view.View>(R.id.btnBack).setOnClickListener {
            finish()
        }

        findViewById<TextView>(R.id.tvToolbarTitle).setOnClickListener {
            finish()
        }
    }
}