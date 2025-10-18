package com.example.agromo.data

class FormularioRepository(private val dao: FormularioDao) {
    suspend fun saveFormulario(formulario: FormularioEntity) {
        dao.insertFormulario(formulario)
    }

    suspend fun getAllFormularios(): List<FormularioEntity> {
        return dao.getAllFormularios()
    }
}
