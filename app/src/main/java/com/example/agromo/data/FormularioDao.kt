package com.example.agromo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FormularioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFormulario(formulario: FormularioEntity)

    @Query("SELECT * FROM formulario")
    suspend fun getAllFormularios(): List<FormularioEntity>
}
