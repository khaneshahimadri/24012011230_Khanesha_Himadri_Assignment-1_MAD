
# 🚨 BEACON – Emergency Safety & Offline Location System

<p align="center">
  <b>Emergency Location • SOS • Offline GPS • SMS • Trusted Circle • Security</b>
</p>

BEACON is an **Android-based emergency safety and location application** developed using **Kotlin and XML**.

The application provides quick assistance during emergency situations using SOS alerts, GPS/GNSS location, cellular SMS, trusted contacts, emergency siren, vibration, security controls, activity logs, and system health monitoring.

---

## 📱 Project Overview

During an emergency, users may not always have access to mobile-data internet or enough time to manually contact multiple people.

**BEACON** combines multiple emergency and safety features into a single Android application.

A major focus of BEACON is its **offline-oriented architecture**. GPS coordinates can be obtained without mobile-data internet, while cellular SMS can be used for emergency communication when SMS network coverage is available.

---

# ✨ Key Features

### 🚨 Emergency SOS

The user must **press and hold the SOS button for 3 seconds** to activate emergency mode.

When activated:

* 🚨 Emergency mode starts
* 🔊 Siren starts playing
* 📳 Device vibration is activated
* 📍 GPS coordinates are obtained
* 👥 Trusted contacts can be alerted
* 📋 Emergency activity is recorded

---

### 📍 GPS / GNSS Location

BEACON provides:

* Latitude
* Longitude
* Estimated accuracy
* Altitude
* Location provider
* Last GPS update
* Copy coordinates
* Open location in map services

GPS coordinates can be obtained without mobile-data internet.

---

### 📩 SMS LOCATE System

A trusted contact can send:

`LOCATE`

BEACON verifies the sender before sharing the user's location.

```text
Incoming "LOCATE" SMS
          │
          ▼
     Verify Sender
          │
          ▼
   Trusted Circle
       /     \
      /       \
 Trusted     Unknown
    │            │
    ▼            ▼
 Get GPS        Block
    │
    ▼
Generate Location
    │
    ▼
Create Maps Link
    │
    ▼
Send SMS Response
    │
    ▼
Save Activity Log
```

---

### 👥 Trusted Circle

Users can:

* Add trusted contacts
* Maintain verified contacts
* Select a primary contact
* Verify incoming LOCATE requests
* Block unknown senders
* Share emergency location securely

---

### 🔐 Security & Privacy

BEACON includes:

* 🛡️ Trusted sender whitelist
* 🚫 Unknown sender blocking
* 🔑 Exact `LOCATE` keyword verification
* ⏱️ Anti-spam protection
* 🕵️ Stealth controls
* 📋 Security activity auditing

---

### 📋 Activity Log

BEACON records important events such as:

* 🚨 SOS activation
* 📍 GPS activities
* ✅ Successful trusted requests
* 🚫 Blocked unknown requests
* 📨 SMS location responses
* ❤️ Diagnostic activities

---

### ❤️ Health & Telemetry

BEACON monitors:

* 🛰️ GPS subsystem
* 📩 SMS availability
* 🔋 Battery status
* 📡 Cellular system
* ❤️ Overall system health

---

# 📶 Offline Architecture

```text
🛰️ GPS / GNSS
      │
      ▼
Location Coordinates

📡 Cellular Network
      │
      ▼
SMS Communication

🌐 Internet
      │
      ▼
Online Map Services
```

> **Mobile-data internet is not required to obtain GPS coordinates.**

SMS communication requires cellular SMS network coverage.

---

# 📲 BEACON Modules

| No. | Module                   | Purpose                                 |
| --- | ------------------------ | --------------------------------------- |
| 1   | 🏠 Home Dashboard        | System status and SOS access            |
| 2   | 🚨 Emergency SOS         | Siren, vibration and emergency dispatch |
| 3   | ⚙️ Beacon Control        | LOCATE keyword and SMS monitoring       |
| 4   | 👥 Trusted Circle        | Verified emergency contacts             |
| 5   | 📶 Offline Architecture  | GPS + cellular SMS operation            |
| 6   | 📍 GPS Tactical Location | Live GNSS coordinates                   |
| 7   | 📨 SMS Request Parser    | Validates LOCATE requests               |
| 8   | 📤 Location Response     | Generates location response             |
| 9   | 📋 Activity Log          | Emergency and security audit            |
| 10  | 🔐 Security & Privacy    | Whitelist and privacy controls          |
| 11  | ❤️ Health Diagnostics    | GPS, SMS and battery diagnostics        |
| 12  | 🔄 Complete Beacon Flow  | End-to-end BEACON architecture          |

---

# 🚨 SOS Workflow

```text
User Holds SOS Button
        │
        ▼
3-Second Countdown
        │
        ▼
   SOS Activated
        │
        ▼
Siren + Vibration
        │
        ▼
Get GPS Coordinates
        │
        ▼
Alert Trusted Contacts
        │
        ▼
Save Activity Log
```

---

# 🛠️ Technology Stack

| Technology              | Purpose                         |
| ----------------------- | ------------------------------- |
| Kotlin                  | Android application logic       |
| XML                     | User interface design           |
| Android Studio          | Development environment         |
| Android SDK             | Android functionality           |
| Google Play Services    | Location services               |
| Fused Location Provider | GPS/GNSS location               |
| BroadcastReceiver       | SMS and event handling          |
| Android SMS APIs        | SMS communication               |
| MediaPlayer             | Emergency siren                 |
| Vibrator API            | Emergency vibration             |
| BatteryManager          | Battery monitoring              |
| SharedPreferences       | Local application data          |
| Intent                  | Navigation and external actions |

---

# 🔑 Android Permissions

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.SEND_SMS" />
<uses-permission android:name="android.permission.RECEIVE_SMS" />
<uses-permission android:name="android.permission.VIBRATE" />
```

---

# 📸 Application Screenshots

## 🏠 Home Dashboard

<p align="center">
  <img src="./screenshots/home.jpg" width="280" alt="BEACON Home Dashboard"/>
</p>

---

## 🚨 Emergency SOS

<p align="center">
  <img src="./screenshots/sos.jpg" width="280" alt="Emergency SOS"/>
</p>

---

## ⚙️ Beacon Control

<p align="center">
  <img src="./screenshots/beacon-control.jpg" width="280" alt="Beacon Control"/>
</p>

---

## 👥 Trusted Circle

<p align="center">
  <img src="./screenshots/trusted-circle.jpg" width="280" alt="Trusted Circle"/>
</p>

---

## 📶 Offline Architecture

<p align="center">
  <img src="./screenshots/offline-architecture.jpg" width="280" alt="Offline Architecture"/>
</p>

---

## 📍 GPS Tactical Location

<p align="center">
  <img src="./screenshots/gps-location.jpg" width="280" alt="GPS Tactical Location"/>
</p>

---

## 📨 SMS Request Parser

<p align="center">
  <img src="./screenshots/sms-parser.jpg" width="280" alt="SMS Request Parser"/>
</p>

---

## 📤 Location Response

<p align="center">
  <img src="./screenshots/location-response.jpg" width="280" alt="Location Response"/>
</p>

---

## 📋 Beacon Activity Log

<p align="center">
  <img src="./screenshots/activity-log.jpg" width="280" alt="Beacon Activity Log"/>
</p>

---

## 🔐 Security & Privacy

<p align="center">
  <img src="./screenshots/security.jpg" width="280" alt="Security and Privacy"/>
</p>

---

## ❤️ Health Diagnostics

<p align="center">
  <img src="./screenshots/diagnostics.jpg" width="280" alt="Health Diagnostics"/>
</p>

---

## 🔄 Complete BEACON Flow

<p align="center">
  <img src="./screenshots/beacon-flow.jpg" width="280" alt="Complete BEACON Flow"/>
</p>

---

# 🖼️ Screenshot Gallery

<p align="center">
  <img src="./screenshots/home.jpg" width="230" alt="Home"/>
  <img src="./screenshots/sos.jpg" width="230" alt="SOS"/>
  <img src="./screenshots/beacon-control.jpg" width="230" alt="Beacon Control"/>
</p>

<p align="center">
  <img src="./screenshots/trusted-circle.jpg" width="230" alt="Trusted Circle"/>
  <img src="./screenshots/offline-architecture.jpg" width="230" alt="Offline Architecture"/>
  <img src="./screenshots/gps-location.jpg" width="230" alt="GPS Location"/>
</p>

<p align="center">
  <img src="./screenshots/sms-parser.jpg" width="230" alt="SMS Parser"/>
  <img src="./screenshots/location-response.jpg" width="230" alt="Location Response"/>
  <img src="./screenshots/activity-log.jpg" width="230" alt="Activity Log"/>
</p>

<p align="center">
  <img src="./screenshots/security.jpg" width="230" alt="Security"/>
  <img src="./screenshots/diagnostics.jpg" width="230" alt="Diagnostics"/>
  <img src="./screenshots/beacon-flow.jpg" width="230" alt="Beacon Flow"/>
</p>

---

# 🌟 Advantages

* 🚨 Quick SOS activation
* 📍 GPS/GNSS location support
* 📩 SMS-based emergency communication
* 📶 Offline-oriented functionality
* 👥 Trusted Circle security
* 🚫 Unknown sender blocking
* 🔊 Emergency siren
* 📳 Emergency vibration
* 📋 Activity auditing
* 🔋 Battery monitoring
* 🔐 Privacy-focused location sharing

---

# ⚠️ Limitations

* SMS requires cellular network availability.
* GPS accuracy depends on the device and environment.
* Required Android permissions must be granted.
* Online map visualization may require internet connectivity.
* Background functionality can be affected by Android battery optimization.

---

# 🚀 Future Enhancements

* 🎙️ Voice-activated SOS
* 📳 Shake-to-activate SOS
* 📍 Real-time location tracking
* 🧠 AI-based emergency detection
* 🚶 Fall detection
* ⌚ Smartwatch integration
* 👨‍👩‍👧 Family safety monitoring
* 🗺️ Location history
* 🔔 Advanced emergency notifications
* 📞 Emergency service integration
* ☁️ Optional cloud synchronization

---

# ⚙️ Installation

### 1. Clone the Repository

```bash
git clone https://github.com/khaneshahimadri/24012011230_Khanesha_Himadri_Assignment-1_MAD.git
```

### 2. Open in Android Studio

Open the project using **Android Studio**.

### 3. Sync Gradle

Allow Android Studio to download and synchronize all required dependencies.

### 4. Grant Permissions

Grant required permissions for:

* 📍 Location
* 📩 SMS
* 📳 Vibration
* 👥 Contacts where applicable

### 5. Run the Application

Connect an Android device and run the application.

> A physical Android device is recommended for testing GPS, SMS, siren, vibration and cellular functionality.

---

# 🧪 Testing Checklist

* [ ] Application launch
* [ ] 3-second SOS activation
* [ ] SOS cancellation
* [ ] Emergency siren
* [ ] Emergency vibration
* [ ] GPS coordinate retrieval
* [ ] GPS accuracy
* [ ] Trusted contact management
* [ ] Primary contact selection
* [ ] LOCATE keyword detection
* [ ] Trusted sender verification
* [ ] Unknown sender blo
