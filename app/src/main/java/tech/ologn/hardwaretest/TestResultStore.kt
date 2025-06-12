package tech.ologn.hardwaretest

import android.content.Context

object TestResultStore {
    private const val PREF_NAME = "test_results"

    fun saveResult(context: Context, featureId: Int, message: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val timestamp = System.currentTimeMillis().toString()

        prefs.edit()
            .putString("test_result_$featureId", if (message.contains("Support")) "Not supported" else message)
            .putString("test_timestamp_$featureId", timestamp)
            .apply()
    }

    fun getTesterName(context: Context): String{
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString("name", "") ?: ""
    }

    fun saveTesterName(context: Context, name: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString("name", name).apply()
    }

    fun getTimestamp(context: Context, featureId: Int): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val time = prefs.getString("test_timestamp_$featureId", null)?.toLongOrNull()
        return if (time != null) {
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
            sdf.format(java.util.Date(time))
        } else {
            "N/A"
        }
    }

    fun getResult(context: Context, featureId: Int): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return if (prefs.contains("test_result_$featureId")) {
            prefs.getString("test_result_$featureId", "Not tested")
        } else {
            "Not tested"
        }
    }

    fun clearAll(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().clear().apply()
    }
}
