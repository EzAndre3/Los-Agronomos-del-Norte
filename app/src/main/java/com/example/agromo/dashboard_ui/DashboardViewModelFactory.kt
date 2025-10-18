package com.example.agromo.dashboard_ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.agromo.data.FormularioDao

class DashboardViewModelFactory(
    private val application: Application,
    private val formularioDao: FormularioDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(application, formularioDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
