package id.nesd.secure_device_context.enums

/**
 * Identifies specific security vulnerabilities or contexts detected on the device.
 */
enum class SecureContext(val value: String) {
    /** Indicates the device is rooted (equivalent to jailbreak). */
    JAILBREAK("jailbreak"),

    /** Indicates the app is running in an Android simulator/emulator environment. */
    EMULATOR("emulator"),

    /** Indicates the app is running with a debugger attached. */
    DEBUG_MODE("debug_mode"),

    /** Indicates the device has Developer Options enabled. */
    DEV_MODE("dev_mode")
}
