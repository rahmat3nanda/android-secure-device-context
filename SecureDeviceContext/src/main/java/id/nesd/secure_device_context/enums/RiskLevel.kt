package id.nesd.secure_device_context.enums

/**
 * Represents the overall security risk level of the device.
 */
enum class RiskLevel(val value: String) {
    /** The device appears to be in a secure state with no risks detected. */
    SECURE("secure"),

    /** The device has moderate risks, such as running on an emulator, having dev mode enabled, or an attached debugger. */
    WARNING("warning"),

    /** The device is in a highly insecure state, such as being rooted/jailbroken. */
    CRITICAL("critical");

    companion object
}
