package app.hmprinter.com.Helpers

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import app.hmprinter.com.Constants.AppConstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class DataStoreManager(private val context: Context) {

    suspend fun updateDataToDataStore(isLoggedIn: Boolean, restaurantData: String) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = isLoggedIn
            preferences[RESTAURANT_DATA] = restaurantData
        }
    }

    val isLoggedIn: Flow<Boolean>
        get() = context.dataStore.data.map { preferences ->
            preferences[IS_LOGGED_IN] ?: false
        }


    val restaurantData: Flow<String?>
        get() = context.dataStore.data.map { preferences ->
            preferences[RESTAURANT_DATA]
        }

    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey(AppConstant.IS_LOGGED_IN)
        val RESTAURANT_DATA = stringPreferencesKey(AppConstant.RESTAURANT_DATA)
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = AppConstant.DATASTORE_NAME)
    }
}