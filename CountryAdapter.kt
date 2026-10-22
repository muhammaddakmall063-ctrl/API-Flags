package com.example.apiflags

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil3.ImageLoader
import coil3.load
import coil3.request.error
import coil3.request.placeholder
import com.example.apiflags.data.Country
import java.util.Locale

class CountryAdapter(
    private val imageLoader: ImageLoader,
    private val onItemClick: (Country) -> Unit
) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    private val fullList = mutableListOf<Country>()

    private val shownList = mutableListOf<Country>()

    fun submitList(countries: List<Country>) {

        fullList.clear()

        fullList.addAll(
            countries.sortedBy {
                it.name
            }
        )

        shownList.clear()
        shownList.addAll(fullList)

        notifyDataSetChanged()
    }

    fun filter(keyword: String) {

        val query = keyword
            .trim()
            .lowercase(Locale.getDefault())

        shownList.clear()

        if (query.isEmpty()) {

            shownList.addAll(fullList)

        } else {

            shownList.addAll(
                fullList.filter { country ->

                    country.name
                        .lowercase(Locale.getDefault())
                        .contains(query) ||

                            country.iso2
                                .lowercase(Locale.getDefault())
                                .contains(query) ||

                            country.iso3
                                .lowercase(Locale.getDefault())
                                .contains(query)
                }
            )
        }

        notifyDataSetChanged()
    }

    fun isEmptyResult(): Boolean {
        return shownList.isEmpty()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CountryViewHolder {

        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.item_country,
                parent,
                false
            )

        return CountryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return shownList.size
    }

    override fun onBindViewHolder(
        holder: CountryViewHolder,
        position: Int
    ) {
        holder.bind(shownList[position])
    }

    inner class CountryViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val imgFlag: ImageView =
            itemView.findViewById(R.id.imgFlag)

        private val tvName: TextView =
            itemView.findViewById(R.id.tvCountryName)

        private val tvIso2: TextView =
            itemView.findViewById(R.id.tvIso2)

        private val tvIso3: TextView =
            itemView.findViewById(R.id.tvIso3)

        fun bind(country: Country) {

            // Nama negara
            tvName.text = country.name

            // ISO 2
            tvIso2.text = itemView.context.getString(
                R.string.format_iso2,
                country.iso2
            )

            // ISO 3
            tvIso3.text = itemView.context.getString(
                R.string.format_iso3,
                country.iso3
            )

            // Cek URL bendera
            Log.d(
                "FLAG_URL",
                "${country.name} -> ${country.flag}"
            )

            // Memuat gambar bendera
            imgFlag.load(
                country.flag,
                imageLoader = imageLoader
            ) {

                // Gambar sementara ketika loading
                placeholder(
                    R.drawable.bg_flag_placeholder
                )

                // Gambar jika gagal
                error(
                    R.drawable.bg_flag_placeholder
                )

                listener(

                    // Jika berhasil
                    onSuccess = { _, _ ->

                        Log.d(
                            "FLAG_SUCCESS",
                            "Berhasil: ${country.name}"
                        )
                    },

                    // Jika gagal
                    onError = { _, result ->

                        Log.e(
                            "FLAG_ERROR",
                            "URL bendera: ${country.flag}",
                            result.throwable
                        )
                    }
                )
            }

            // Ketika item negara diklik
            itemView.setOnClickListener {

                onItemClick(country)
            }
        }
    }
}