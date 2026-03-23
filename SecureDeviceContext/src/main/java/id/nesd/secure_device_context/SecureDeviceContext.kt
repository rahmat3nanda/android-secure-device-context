package id.nesd.secure_device_context

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.Debug
import android.provider.Settings
import id.nesd.secure_device_context.enums.RiskLevel
import id.nesd.secure_device_context.enums.SecureContext
import id.nesd.secure_device_context.extensions.fromContexts
import java.io.File

/**
 * A utility class representing the device security context.
 * This includes checks for Rooted devices, Emulators, attached Debuggers, and Developer Mode.
 */
class SecureDeviceContext(private val context: Context) {
    /**
     * Retrieves a list of active secure contexts (e.g., `JAILBREAK`, `EMULATOR`, `DEV_MODE`, etc.).
     * @return A list of detected `SecureContext` cases.
     */
    val contexts: List<SecureContext>
        get() {
            val contexts = mutableListOf<SecureContext>()
            if (isRooted()) contexts.add(SecureContext.JAILBREAK)
            if (isEmulator()) contexts.add(SecureContext.EMULATOR)
            if (isDebugMode()) contexts.add(SecureContext.DEBUG_MODE)
            if (isDevMode()) contexts.add(SecureContext.DEV_MODE)
            return contexts
        }

    /**
     * Retrieves a map representation of the device security contexts.
     * This is useful for passing security parameters in API headers or payloads.
     * @return A map containing security keys and boolean values indicating their status.
     */
    val status: Map<String, Any>
        get() = mapOf(
            SecureContext.JAILBREAK.value to isRooted(),
            SecureContext.EMULATOR.value to isEmulator(),
            SecureContext.DEBUG_MODE.value to isDebugMode(),
            SecureContext.DEV_MODE.value to isDevMode(),
            "risk_level" to riskLevel.value
        )

    /**
     * Evaluates the overall security risk level based on the current context array.
     * @return The calculated `RiskLevel` for the device environment.
     */
    val riskLevel: RiskLevel
        get() = RiskLevel.fromContexts(
            root = isRooted(),
            emulator = isEmulator(),
            debug = isDebugMode(),
            devMode = isDevMode()
        )

    /**
     * Determines whether the device environment is operating as a rooted system.
     * Performs multiple heuristic checks including presence of known root binaries and test keys.
     * @return `true` if any indicator of root is present, `false` otherwise.
     */
    fun isRooted(): Boolean {
        val buildTags = Build.TAGS
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true
        }

        val suspiciousPaths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su"
        )
        for (path in suspiciousPaths) {
            if (File(path).exists()) return true
        }

        try {
            val process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            val inReader = java.io.BufferedReader(java.io.InputStreamReader(process.inputStream))
            return inReader.readLine() != null
        } catch (_: Throwable) {
            return false
        }
    }

    /**
     * Determines whether the code is being run inside an Android simulator/emulator instead of a real device.
     * @return `true` if the environment is a simulated one, `false` on physical hardware.
     */
    fun isEmulator(): Boolean {
        return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator")
    }

    /**
     * Checks if a debugger is actively attached to the current process.
     * @return `true` if a debugger is attached, `false` otherwise.
     */
    fun isDebugMode(): Boolean {
        return Debug.isDebuggerConnected() || (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0)
    }

    /**
     * Checks if Developer Options are enabled on the device.
     * @return `true` if Developer Options are enabled, `false` otherwise.
     */
    fun isDevMode(): Boolean {
        return Settings.Global.getInt(
            context.contentResolver,
            Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0
        ) != 0
    }


}
