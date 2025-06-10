package tech.ologn.hardwaretest

import android.content.Context

object TestResultStore {
    private const val PREF_NAME = "test_results"

    fun saveResult(context: Context, featureId: Int, message: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString("test_result_$featureId", if (message.contains("Support")) "Not supported" else message ).apply()
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
