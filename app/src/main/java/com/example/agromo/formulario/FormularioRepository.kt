package com.example.agromo.formulario

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.agromo.formulario.data.FormularioEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import android.util.Log


private val Context.dataStore by preferencesDataStore(name = "formulario_prefs")

class FormularioRepository(private val context: Context) {

    private val FORMULARIOS_KEY = stringPreferencesKey("formularios")
    private val gson = Gson()

    suspend fun saveFormulario(formulario: FormularioEntity) {
        val currentList = getFormulariosList().toMutableList()
        currentList.add(formulario)
        val json = gson.toJson(currentList)
        context.dataStore.edit { prefs ->
            prefs[FORMULARIOS_KEY] = json
        }

        // Imprimir en Logcat
        Log.d("FORMULARIO", "Formulario guardado: $formulario")
        Log.d("FORMULARIO", "Lista completa de formularios: $currentList")
    }

    fun getFormularios(): Flow<List<FormularioEntity>> =
        context.dataStore.data.map { prefs ->
            val json = prefs[FORMULARIOS_KEY] ?: "[]"
            val type = object : TypeToken<List<FormularioEntity>>() {}.type
            gson.fromJson(json, type)
        }

    private fun getFormulariosList(): List<FormularioEntity> {
        val json = runCatching {
            runBlocking {
                context.dataStore.data.map { it[FORMULARIOS_KEY] ?: "[]" }.first()
            }
        }.getOrDefault("[]")
        val type = object : TypeToken<List<FormularioEntity>>() {}.type
        return gson.fromJson(json, type)
    }
}
