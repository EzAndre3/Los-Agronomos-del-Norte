package com.example.agromo.formulario

import com.example.agromo.data.FormularioDao
import com.example.agromo.data.FormularioEntity
import kotlinx.coroutines.flow.Flow

class FormularioRepository(private val dao: FormularioDao) {
    suspend fun saveFormulario(formulario: FormularioEntity) {
        dao.insertFormulario(formulario)
    }

    // CAMBIO: Ahora también devuelve un Flow y ya no es suspend.
    fun getAllFormularios(): Flow<List<FormularioEntity>> {
        return dao.getAllFormularios()
    }
}
