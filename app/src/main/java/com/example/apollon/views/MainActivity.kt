package com.example.apollon.views

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.apollon.R
import com.example.apollon.adapters.ShortenedLinksAdapter
import com.example.apollon.data.DatabaseBuilder
import com.example.apollon.data.DatabaseHelperImpl
import com.example.apollon.data.Result
import com.example.apollon.databinding.ActivityMainBinding
import com.example.apollon.network.ApiHelper
import com.example.apollon.network.RetrofitBuilder
import com.example.apollon.network.Status
import com.example.apollon.viewmodels.MainViewModel
import com.example.apollon.viewmodels.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), ShortenedLinksAdapter.ItemInterface {
    private lateinit var adapter: ShortenedLinksAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupViewModel()
        initView()
        retrieveListOfShortenedLinks()
    }

    private fun initView() {
        binding.shortenButton.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(it.windowToken, 0)
            if (binding.linkEditText.text.toString() == "") {
                binding.linkEditText.setHintTextColor(Color.RED)
                binding.linkEditText.hint = getString(R.string.add_link_hint)
            } else {
                getShortenedLink()
            }
        }
        adapter = ShortenedLinksAdapter(this)
        binding.linksList.adapter = adapter
    }

    private fun setupViewModel() {
        viewModel =
            ViewModelProvider(
                this,
                ViewModelFactory(
                    ApiHelper(RetrofitBuilder.apiService),
                    DatabaseHelperImpl(DatabaseBuilder.getInstance(this.applicationContext))
                )
            ).get(
                MainViewModel::class.java
            )
    }

    private fun getShortenedLink() {
        viewModel.getShortenedLink(binding.linkEditText.text.toString()).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.linksList.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        resource.data?.let { response -> viewModel.insertShortenedLink(response.result) }
                    }
                    Status.ERROR -> {
                        binding.linksList.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun retrieveListOfShortenedLinks() {
        viewModel.getListOfShortenedLink().observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.emptyStateView.visibility = View.GONE
                        binding.linksList.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        resource.data?.let { results ->
                            adapter.submitList(results.asReversed())
                            GlobalScope.launch(Dispatchers.Main) {
                                delay(1000)
                                if (results.size > 1) {
                                    binding.linksList.layoutManager?.scrollToPosition(0)
                                }
                            }
                        }
                    }
                    Status.ERROR -> {
                        binding.linksList.visibility = View.GONE
                        binding.emptyStateView.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        binding.emptyStateView.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        })
    }

    override fun onDelete(result: Result) {
        viewModel.deleteShortenedLink(result)
    }

    override fun onCopy(link: String?) {
        val clipboard: ClipboardManager =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(getString(R.string.shortened_link), link)
        clipboard.setPrimaryClip(clip)
    }
}
