package com.example.agromo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FormularioDao {

    // CAMBIO: Ahora devuelve un Flow para que la UI se actualice automáticamente.
    @Query("SELECT * FROM formulario ORDER BY fechaRegistro DESC")
    fun getAllFormularios(): Flow<List<FormularioEntity>>

    @Query("SELECT * FROM formulario WHERE id = :id")
    suspend fun getFormularioById(id: String): FormularioEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFormulario(formulario: FormularioEntity)

    @Update
    suspend fun updateFormulario(formulario: FormularioEntity)
}