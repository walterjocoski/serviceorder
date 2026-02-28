package com.rfpiscinas.serviceorder.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rfpiscinas.serviceorder.data.model.ServiceOrder
import com.rfpiscinas.serviceorder.data.repository.ServiceOrderRepository
import com.rfpiscinas.serviceorder.util.PdfReportGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val serviceOrderRepository: ServiceOrderRepository
) : ViewModel() {

    private val _orders = MutableStateFlow<List<ServiceOrder>>(emptyList())
    val orders: StateFlow<List<ServiceOrder>> = _orders.asStateFlow()

    private val _pdfUri = MutableStateFlow<Uri?>(null)
    val pdfUri: StateFlow<Uri?> = _pdfUri.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        viewModelScope.launch {
            serviceOrderRepository.getAllOrders().collect { _orders.value = it }
        }
    }

    fun generatePdfForOrder(context: Context, order: ServiceOrder) {
        viewModelScope.launch {
            _isGenerating.value = true
            _errorMessage.value = null
            try {
                val uri = PdfReportGenerator.generateOrderReport(context, order)
                _pdfUri.value = uri
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao gerar relatório: ${e.message}"
            } finally {
                _isGenerating.value = false
            }
        }
    }

    fun generatePdfForPeriod(context: Context, orders: List<ServiceOrder>, startDate: String, endDate: String) {
        viewModelScope.launch {
            _isGenerating.value = true
            _errorMessage.value = null
            try {
                val uri = PdfReportGenerator.generatePeriodReport(context, orders, startDate, endDate)
                _pdfUri.value = uri
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao gerar relatório: ${e.message}"
            } finally {
                _isGenerating.value = false
            }
        }
    }

    fun clearPdfUri() {
        _pdfUri.value = null
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
