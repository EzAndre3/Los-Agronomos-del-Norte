package com.example.agromo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FormularioDao {

    // CAMBIO: Ahora devuelve un Flow para que la UI se actualice automáticamente.
    @Query("SELECT * FROM formulario ORDER BY fecha_registro DESC")
    fun getAllFormularios(): Flow<List<FormularioEntity>>

    @Query("SELECT * FROM formulario WHERE id = :id")
    suspend fun getFormularioById(id: String): FormularioEntity?

    @Query("SELECT * FROM formulario WHERE estado = 1")
    suspend fun getPendingFormularios(): List<FormularioEntity>

    @Query("UPDATE formulario SET estado = 0 WHERE id = :formId")
    suspend fun markFormularioAsSynced(formId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFormulario(formulario: FormularioEntity)

    @Update
    suspend fun updateFormulario(formulario: FormularioEntity)
    @Delete
    suspend fun deleteFormulario(formulario: FormularioEntity)
}