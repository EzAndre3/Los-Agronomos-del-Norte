package com.example.agromo.formulario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.agromo.formulario.data.FormularioEntity
import kotlinx.coroutines.launch

class FormularioListViewModel(private val repository: FormularioRepository) : ViewModel() {

    private val _formularios = mutableListOf<FormularioEntity>()
    val formularios: List<FormularioEntity> get() = _formularios

    init {
        viewModelScope.launch {
            repository.getFormularios().collect { list ->
                _formularios.clear()
                _formularios.addAll(list)
            }
        }
    }

    fun addFormulario(formulario: FormularioEntity) {
        _formularios.add(formulario)
        viewModelScope.launch {
            repository.saveFormulario(formulario)
        }
    }

    class Factory(private val repository: FormularioRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return FormularioListViewModel(repository) as T
        }
    }
}
