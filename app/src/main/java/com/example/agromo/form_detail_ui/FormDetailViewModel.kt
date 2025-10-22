package com.example.agromo.form_detail_ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agromo.data.FormularioDao
import com.example.agromo.data.FormularioEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FormDetailViewModel(
    private val formularioDao: FormularioDao,
    private val formId: String
) : ViewModel() {

    private val _formState = MutableStateFlow<FormularioEntity?>(null)
    val formState: StateFlow<FormularioEntity?> = _formState.asStateFlow()

    init {
        viewModelScope.launch {
            _formState.value = formularioDao.getFormularioById(formId)
        }
    }

    fun updateForm(formulario: FormularioEntity) {
        viewModelScope.launch {
            formularioDao.updateFormulario(formulario)
        }
    }

    fun deleteForm(formulario: FormularioEntity) {
        viewModelScope.launch {
            formularioDao.deleteFormulario(formulario)
        }
    }
}
