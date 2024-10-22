package jp.android.pokemon.util

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

class AppInfoProvider(private val context: Context) {
    fun getAppVersion(): String {
        return try {
            val packageInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName ?: "不明なバージョン"
        } catch (e: PackageManager.NameNotFoundException) {
            "不明なバージョン"
        }
    }

    fun getBuildNumber(): String {
        return try {
            val packageInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.longVersionCode.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            "0"
        }
    }
}