# SecureDeviceContext

`SecureDeviceContext` is a robust Android library designed to evaluate the physical device environment for critical security risks and vulnerabilities, providing clear assessments to ensure data integrity and application safety.

## Features

- **Root/Jailbreak Detection**: Actively scans for common indicators of a compromised Android environment (e.g., suspicious paths like `/system/app/Superuser.apk` or test-keys).
- **Emulator Checks**: Identifies if the application is being run in a simulated Android environment (AVD, Genymotion, etc.) rather than on physical hardware.
- **Debugger Detection**: Determines whether an active debugger process is attached to your app, which could indicate a potential reverse-engineering attempt.
- **Developer Mode Detection**: Verifies if Developer Options are currently enabled on the user's Android device.
- **Standardized Risk Levels**: Aggregates contexts into clean, easy-to-digest risk levels (`SECURE`, `WARNING`, `CRITICAL`) allowing your app to seamlessly make critical security decisions.

## Requirements

- Minimum SDK: 24 (Android 7.0+)
- Compile SDK: 36
- Kotlin 2.0+

## Installation

### Standard Gradle / JitPack

You can easily integrate `SecureDeviceContext` into your projects via [JitPack](https://jitpack.io).

**Step 1.** Add the JitPack repository to your root `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
```

**Step 2.** Add the dependency to your app module's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.rahmat3nanda:android-secure-device-context:1.0.0")
}
```

## Usage

Here’s a quick guide on how to evaluate the device security context within your Android app:

### Initialize and Evaluate

```kotlin
import id.nesd.secure_device_context.SecureDeviceContext
import id.nesd.secure_device_context.enums.RiskLevel
import id.nesd.secure_device_context.enums.SecureContext

// Initialize the context using an active Android Context (e.g., inside an Activity)
val deviceContext = SecureDeviceContext(this)

// 1. Get a comprehensive list of active security risks
val activeRisks: List<SecureContext> = deviceContext.contexts 
if (activeRisks.contains(SecureContext.JAILBREAK)) {
    println("Warning: Device is rooted/jailbroken!")
}

// 2. Get the overall risk level for decision making
val riskLevel: RiskLevel = deviceContext.riskLevel
when (riskLevel) {
    RiskLevel.SECURE -> println("Device is secure. Proceed as normal.")
    RiskLevel.WARNING -> println("Risk Level: Warning. Non-critical risks detected (e.g., Emulator, Dev Mode).")
    RiskLevel.CRITICAL -> println("Risk Level: CRITICAL! Root or severe bypass detected.")
}

// 3. Serialize as a map (Useful for API headers, analytics, or payloads)
val deviceStatus: Map<String, Any> = deviceContext.status
println(deviceStatus)
/* Expected Output:
  {
    jailbreak=false,
    emulator=false,
    debug_mode=false,
    dev_mode=false,
    risk_level=secure
  }
*/
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
