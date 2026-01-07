# Nexus Radio v2.1

Nexus Radio is a high-performance, AI-assisted communication node designed to transform Android devices into dedicated Wi-Fi Radio consoles. Built on the open-source VDO.Ninja core, it establishes a primary system environment that prioritizes low-latency audio, hardware telemetry, and intelligent transmission.

## 🚀 Core Features

- **Nexus OS UI**: A custom, high-contrast "Nexus Green" console interface optimized for embedded OLED/LCD hardware.
- **NexusBridge v2.0**: A native bi-directional bridge allowing the Web UI to control Android Bluetooth, Battery, and System Settings.
- **AI Voice Trigger (VAD)**: Integrated Voice Activity Detection with 1500ms hysteresis. Automatically toggles transmission based on human speech.
- **Hardware Telemetry**: Real-time monitoring of battery percentage, system uptime, and hardware status.
- **Bluetooth Radio Hub**: Native pairing and syncing with external Bluetooth speakers and HID devices.
- **Enterprise Ready**: Includes a portable telemetry component for integration into master dashboards.

## 🛠 Setup & Installation

### 1. The Host Server
The Nexus Radio UI requires a local host.
1. Clone the modified VDO.Ninja server files.
2. Run a local web server (e.g., `python -m http.server 8080`).
3. Ensure the `LOCAL_SERVER_IP` in `MainActivity.java` matches your PC's IP.

### 2. The Android System
1. Open the `/app` project in Android Studio or build via CLI.
2. Build the Debug APK: `./gradlew assembleDebug`.
3. Install to device: `adb install nexus-radio.apk`.
4. Set as **Home Launcher** to establish as the primary system.

## 📡 Radio Workflow
- **Transmission**: AI monitors the floor. Transmit glow (Green Aura) activates upon speech.
- **Monitoring**: Top-pinned status bar provides mission-critical system data.
- **Admin**: Access secondary Android settings via the "OS SETTINGS" trigger.

---
*Developed by the Nexus UI/UX Architect Engine.*
