package com.example.apiflags

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apiflags.data.Country
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var rvCountries: RecyclerView
    private lateinit var etSearch: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var tvMessage: TextView
    private lateinit var adapter: CountryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvCountries = findViewById(R.id.rvCountries)
        etSearch = findViewById(R.id.etSearch)
        progressBar = findViewById(R.id.progressBar)
        tvMessage = findViewById(R.id.tvMessage)

        setupRecyclerView()
        setupSearch()
        loadCountries()
    }

    private fun setupRecyclerView() {
        val imageLoader = FlagImageLoader.create(this)

        adapter = CountryAdapter(
            imageLoader
        ) { country ->
            openDetail(country)
        }

        rvCountries.layoutManager =
            LinearLayoutManager(this)

        rvCountries.setHasFixedSize(true)

        rvCountries.adapter = adapter
    }

    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter(s?.toString().orEmpty())
                tvMessage.visibility =
                    if (adapter.isEmptyResult()) View.VISIBLE else View.GONE
                tvMessage.text = getString(R.string.empty_result)
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun loadCountries() {
        showLoading(true)
        lifecycleScope.launch {
            try {
                val response = APIclient.service.getCountryFlags()
                showLoading(false)
                if (!response.error) {
                    adapter.submitList(response.data)
                    tvMessage.visibility = View.GONE
                } else {
                    showError(response.msg)
                }
            } catch (e: Exception) {
                showLoading(false)
                showError(getString(R.string.error_load, e.localizedMessage ?: "-"))
            }
        }
    }

    private fun showLoading(loading: Boolean) {
        progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        rvCountries.visibility = if (loading) View.GONE else View.VISIBLE
        if (loading) tvMessage.visibility = View.GONE
    }

    private fun showError(message: String) {
        tvMessage.visibility = View.VISIBLE
        tvMessage.text = message
    }

    private fun openDetail(country: Country) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_COUNTRY, country)
        startActivity(intent)
    }
}