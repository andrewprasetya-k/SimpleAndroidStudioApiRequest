package com.example.simpleapirequest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.Exception

class MyViewModel : ViewModel() {
    private val _users = MutableLiveData<List<User>>()
    val users = _users

    private val _currentUser = MutableLiveData<User>()
    val currentUser = _currentUser

    private val _produk = MutableLiveData<List<Product>>()
    val produk = _produk

    private val _averagePrice = MutableLiveData<Double>()
    val averagePrice = _averagePrice

    private val _error = MutableLiveData<String>()
    val error = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    private var currentUserId = 1
    private val maxUserId = 100

    init {
        fetchSingleUser(currentUserId)
    }

    fun fetchUsers() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val userList = RetrofitInstance.apiService.getUsers()
                _users.value = userList.users
            } catch (e: Exception) {
                _error.value = "Failed to fetch users: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchSingleUser(userId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val user = RetrofitInstance.apiService.getUser(userId)
                _currentUser.value = user
                currentUserId = userId
            } catch (e: Exception) {
                _error.value = "Failed to fetch user: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun goToNextUser() {
        if (currentUserId < maxUserId) {
            fetchSingleUser(currentUserId + 1)
        }
    }

    fun goToPreviousUser() {
        if (currentUserId > 1) {
            fetchSingleUser(currentUserId - 1)
        }
    }

    fun canGoNext(): Boolean = currentUserId < maxUserId
    fun canGoPrevious(): Boolean = currentUserId > 1
    fun getCurrentUserId(): Int = currentUserId

    fun getProduk() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val produkList = RetrofitInstance.apiService.getProducts()
                _produk.value = produkList.products

                val prices = produkList.products.map { it.price }
                val average = if (prices.isNotEmpty()) prices.average() else 0.0
                _averagePrice.value = average

            } catch (e: Exception) {
                _error.value = "Failed to fetch produk: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

}