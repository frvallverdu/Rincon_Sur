package cl.duoc.rinconsur.data.local.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


/*
    creamos un archivo con las preferencias en el telefono
    llamado user_session_prefs el cual servira para guardar la información de
    inicio de sesion para que no tenga que iniciar sesion
    siempre al abrir la app
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_session_prefs")

class UserSessionPrefs(context: Context) {
    private val appContext = context.applicationContext
    /*
        definimos las claves para guardar y leer datos

     */
    companion object{
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_NAME = stringPreferencesKey("user_name")

    }
    /*
        flujo para leer si el usuario esta logeado boolean en true o false
     */
    val isLoggedIn: Flow<Boolean> = appContext.dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] ?: false
    }

    /*
        esta funcion se encarga de guardar la sesion una vez
        el usuario se registra correctamente
        usando suspend fun
     */
    suspend fun guardarSesion(email: String, nombre: String){
        appContext.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = true
            preferences[USER_EMAIL] = email
            preferences[USER_NAME] = nombre
        }
    }

    /*
        Funcion para cerrar sesion
     */
    suspend fun cerrarSesion(){
        appContext.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = false
            preferences[USER_EMAIL] = ""
            preferences[USER_NAME] = ""
        }
    }
}