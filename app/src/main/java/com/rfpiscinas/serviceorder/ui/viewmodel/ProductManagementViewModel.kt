package com.rfpiscinas.serviceorder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rfpiscinas.serviceorder.data.model.Product
import com.rfpiscinas.serviceorder.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductManagementViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    init {
        viewModelScope.launch {
            productRepository.getAll().collect { _products.value = it }
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            productRepository.insert(product)
            _message.value = "Produto cadastrado com sucesso!"
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            productRepository.update(product)
            _message.value = "Produto atualizado!"
        }
    }

    fun toggleActive(product: Product) {
        viewModelScope.launch {
            productRepository.update(product.copy(active = !product.active))
            val status = if (!product.active) "ativado" else "inativado"
            _message.value = "Produto $status!"
        }
    }

    fun clearMessage() { _message.value = null }
}
