package com.example.android.metercollectionapp.data.storage

import android.content.Context
import com.example.android.metercollectionapp.MeterCollectionApplication
import java.io.IOException

private const val SHARED_PREFERENCES_FILE_NAME = "SHARED_PREFERENCES_FILE_NAME"

class SharedPreferencesStorage(private val context: MeterCollectionApplication) : Storage {

    /**
     * @return возвращает либо список Id параметров, которые привязаны к данному устройству либо пустой список
     */
    override suspend fun getDeviceParamsIdsAssociatedFrom(guid: Long): List<Long> {
        val pref = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
        val stringGuid = guid.toString()
        var result = listOf<Long>()
        try {
            val setOfIds = pref.getStringSet(stringGuid, null)
            if (setOfIds != null) { result = setOfIds.toList().map { it.toLong() } }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return result
    }

    override suspend fun setDeviceParamsIdsAssociatedTo(guid: Long, paramsIds: List<Long>) {
        val stringGuid = guid.toString()
        val setOfIds = paramsIds.map { it.toString() }.toSet()
        val pref = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
        try {
            val editor = pref.edit()
            editor.remove(stringGuid)
            if (setOfIds.isNotEmpty()) {
                editor.putStringSet(stringGuid, setOfIds)
            }
            editor.apply()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

fun getSharedPreferencesStorage(context: MeterCollectionApplication) = SharedPreferencesStorage(context)

// за счет того, что в DataModule соответствующий метод @Provides обозначен как @Singleton
// нет необходимости дублировать ссылку на объект-синглтон здесь
// по логам, каждый раз при обращении к этому полю возвращается один и тот же HashCode
//    This method is thread-safe.
//    context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE)
