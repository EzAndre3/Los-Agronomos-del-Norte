package com.example.agromo.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RegistroFormularioViewModelFactory(
    private val repository: FormularioRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegistroFormularioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegistroFormularioViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
