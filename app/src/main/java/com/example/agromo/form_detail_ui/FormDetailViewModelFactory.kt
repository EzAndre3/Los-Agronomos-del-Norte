package com.example.agromo.form_detail_ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.agromo.data.FormularioDao

class FormDetailViewModelFactory(private val formularioDao: FormularioDao, private val formId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FormDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FormDetailViewModel(formularioDao, formId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
