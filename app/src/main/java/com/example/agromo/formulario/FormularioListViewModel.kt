package com.example.agromo.formulario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.agromo.formulario.data.FormularioEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FormularioListViewModel(private val repository: FormularioRepository) : ViewModel() {

    private val _formularios = MutableStateFlow<List<FormularioEntity>>(emptyList())
    val formularios: StateFlow<List<FormularioEntity>> = _formularios.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getFormularios().collect { list ->
                _formularios.value = list
            }
        }
    }

    fun addFormulario(formulario: FormularioEntity) {
        viewModelScope.launch {
            repository.saveFormulario(formulario)
        }
    }

    fun getFormularioById(id: String): FormularioEntity? {
        return _formularios.value.find { it.id == id }
    }

    class Factory(private val repository: FormularioRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return FormularioListViewModel(repository) as T
        }
    }
}
