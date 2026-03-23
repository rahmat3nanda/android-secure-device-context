package id.nesd.secure_device_context.extensions

import id.nesd.secure_device_context.enums.RiskLevel

/**
 * Evaluates the risk level dynamically based on multiple security factors.
 *
 * @param root Boolean indicating if the device is rooted (equivalent to jailbreak).
 * @param emulator Boolean indicating if the app is running in an emulator.
 * @param debug Boolean indicating if the app has a debugger attached.
 * @param devMode Boolean indicating if developer options are enabled.
 * @return The derived `RiskLevel`.
 */
fun RiskLevel.Companion.fromContexts(
    root: Boolean,
    emulator: Boolean,
    debug: Boolean,
    devMode: Boolean
): RiskLevel {
    if (root) return RiskLevel.CRITICAL
    if (emulator || debug || devMode) return RiskLevel.WARNING
    return RiskLevel.SECURE
}
