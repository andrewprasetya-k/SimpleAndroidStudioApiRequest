package com.example.simpleapirequest

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MyViewModel

    private lateinit var txtDetailsData: TextView
    private lateinit var btnStartRequest: Button
    private lateinit var btnPrev: Button
    private lateinit var btnNext: Button
    private lateinit var btnProduct: Button
    private lateinit var txtProduct: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupViews()
        setupViewModel()
        setupClickListeners()
        observeData()
    }

    private fun setupViews() {
        txtDetailsData = findViewById(R.id.txtDetailsData)
        btnStartRequest = findViewById(R.id.btnStartRequest)
        btnPrev = findViewById(R.id.btnPrev)
        btnNext = findViewById(R.id.btnNext)
        btnProduct = findViewById(R.id.btnProduct)
        txtProduct = findViewById(R.id.txtProduct)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[MyViewModel::class.java]
    }

    private fun setupClickListeners() {
        btnStartRequest.setOnClickListener {
            viewModel.fetchSingleUser(viewModel.getCurrentUserId())
        }

        btnPrev.setOnClickListener {
            viewModel.goToPreviousUser()
        }

        btnNext.setOnClickListener {
            viewModel.goToNextUser()
        }

        btnProduct.setOnClickListener {
            viewModel.getProduk()
        }
    }

    private fun observeData() {
        viewModel.currentUser.observe(this) { user ->
            displayUser(user)
            updateNavigationButtons()
        }

        viewModel.isLoading.observe(this) { isLoading ->
            btnStartRequest.isEnabled = !isLoading
        }

        viewModel.error.observe(this) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.produk.observe(this) { product ->
            viewModel.produk.observe(this) { products ->
                val firstProduct = products.firstOrNull()
                if (firstProduct != null) {
                    displayProduct(firstProduct)
                } else {
                    txtProduct.text = "Produk kosong"
                }
            }
        }

        viewModel.averagePrice.observe(this) { average ->
            val formatted = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(average)
            txtProduct.text = "Rata-rata harga: $formatted"
        }

    }
    private fun displayUser(user: User) {
        txtDetailsData.text = "${user.firstName} \n${user.lastName}\n ${user.university ?: "N/A"}"
    }

    private fun displayProduct(products: Product) {
        txtProduct.text = "Rp ${products.price}"
    }

    private fun updateNavigationButtons() {
        btnPrev.isEnabled = viewModel.canGoPrevious()
        btnNext.isEnabled = viewModel.canGoNext()
    }
}
