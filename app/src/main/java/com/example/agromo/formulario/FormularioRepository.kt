package com.example.agromo.formulario

import com.example.agromo.data.FormularioDao
import com.example.agromo.data.FormularioEntity

class FormularioRepository(private val dao: FormularioDao) {
    suspend fun saveFormulario(formulario: FormularioEntity) {
        dao.insertFormulario(formulario)
    }

    suspend fun getAllFormularios(): List<FormularioEntity> {
        return dao.getAllFormularios()
    }
}
