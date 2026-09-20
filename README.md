# 🚨 BEACON – Emergency Safety & Offline Location System

**Emergency Location • SOS • Offline GPS • SMS • Trusted Circle • Security**

BEACON is an Android-based emergency safety and location application designed to provide quick assistance during emergency situations.

The application allows users to activate emergency SOS alerts, obtain GPS/GNSS coordinates, communicate location through cellular SMS, manage trusted contacts, protect sensitive location information, monitor system health, and maintain emergency activity logs.

---

## 📱 Project Overview

During an emergency, users may not always have access to mobile data or enough time to manually contact multiple people.

**BEACON** provides multiple emergency and safety features in a single Android application.

A major focus of the application is its **offline-oriented architecture**.

The application can obtain GPS/GNSS coordinates without mobile-data internet and can use the cellular network for SMS-based emergency communication when SMS coverage is available.

BEACON is developed using **Kotlin, XML, Android Studio, Android SDK, Google Play Services, Android SMS APIs, BroadcastReceiver, MediaPlayer, Vibrator API, BatteryManager, SharedPreferences, and Android Intents.**

---

# ✨ Features

### 🚨 1. Emergency SOS

BEACON provides a quick emergency response mechanism.

To prevent accidental activation, the user must **press and hold the SOS button for 3 seconds**.

When SOS is activated:

1. The SOS countdown begins.
2. Emergency mode is activated.
3. Siren starts playing.
4. Device vibration is activated.
5. GPS coordinates can be obtained.
6. Trusted contacts can be alerted.
7. The emergency activity is recorded.

---

### 🔊 2. Siren & Vibration

When emergency mode is activated:

* 🔊 Emergency siren starts
* 📳 Device vibration is activated
* 🛑 User can cancel the emergency mode when required

These alerts help attract nearby attention during an emergency.

---

### 📍 3. GPS / GNSS Location

BEACON provides detailed location information using Android location services.

The application displays:

* Latitude
* Longitude
* Estimated accuracy
* Altitude
* Location provider
* Last GPS update
* Copy coordinates option
* Open location using map services

GPS coordinates can be obtained without requiring mobile-data internet.

---

### 📩 4. SMS LOCATE System

BEACON supports location-based emergency communication through cellular SMS.

A trusted contact can send the keyword:

`LOCATE`

The application verifies the sender before processing the request.

If the sender is trusted:

1. The LOCATE request is detected.
2. Sender identity is verified.
3. GPS location is obtained.
4. Location information is generated.
5. A map link can be created.
6. Location information is sent through SMS.
7. The activity is stored in the audit log.

---

### 👥 5. Trusted Circle

BEACON provides a **Trusted Circle Whitelist** for protecting sensitive location information.

Users can:

* Add trusted contacts
* Maintain verified contacts
* Select a primary contact
* Send LOCATE requests
* Verify incoming requests
* Block unknown senders

Only trusted contacts are allowed to request sensitive location information.

---

### 🔐 6. Security & Privacy

BEACON includes several security mechanisms to protect user location and emergency information.

Security features include:

* 🛡️ Trusted sender whitelist
* 🚫 Unknown sender blocking
* 🔑 Exact `LOCATE` keyword verification
* ⏱️ Anti-spam protection
* 🕵️ Stealth controls
* 📋 Security activity auditing

---

### 📋 7. Beacon Activity Log

BEACON maintains an activity log of important emergency and security events.

Activities may include:

* 🚨 SOS activation
* 📍 GPS/location activities
* ✅ Successful trusted requests
* 🚫 Blocked unknown requests
* 📨 SMS location responses
* ❤️ Diagnostic activities

This provides a record of previous BEACON activities.

---

### ❤️ 8. Health & Telemetry

The diagnostic module monitors important BEACON components.

The application can display information related to:

* 🛰️ GPS subsystem
* 📩 SMS availability
* 🔋 Battery status
* 📡 Cellular system
* ❤️ Overall BEACON health

---

### 📶 9. Offline-Oriented Operation

BEACON separates location, SMS communication, and internet connectivity.

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

This means that **mobile-data internet is not required to obtain GPS coordinates**.

SMS communication still requires cellular SMS network coverage.

---

# 📲 BEACON Modules

| No. | Module                   | Purpose                                 |
| --- | ------------------------ | --------------------------------------- |
| 1   | 🏠 Home Dashboard        | System status and SOS access            |
| 2   | 🚨 Emergency SOS         | Siren, vibration and emergency dispatch |
| 3   | ⚙️ Beacon Control        | LOCATE keyword and SMS monitoring       |
| 4   | 👥 Trusted Circle        | Verified emergency contacts             |
| 5   | 📶 Offline Architecture  | GPS + cellular SMS operation            |
| 6   | 📍 GPS Tactical Location | Live GNSS coordinates and accuracy      |
| 7   | 📨 SMS Request Parser    | Validates incoming LOCATE requests      |
| 8   | 📤 Location Response     | Generates location SMS response         |
| 9   | 📋 Beacon Activity Log   | Emergency and security audit            |
| 10  | 🔐 Security & Privacy    | Whitelist and privacy controls          |
| 11  | ❤️ Health Diagnostics    | GPS, SMS and battery diagnostics        |
| 12  | 🔄 Complete Beacon Flow  | End-to-end BEACON architecture          |

---

# 🔄 Application Workflow

```text
                         BEACON
                            │
                            ▼
                     Home Dashboard
                            │
          ┌─────────────────┼─────────────────┐
          │                 │                 │
          ▼                 ▼                 ▼
    Emergency SOS      GPS Location      Trusted Circle
          │                 │                 │
          └─────────────────┼─────────────────┘
                            │
                            ▼
                   Emergency Services
                            │
          ┌─────────────────┼─────────────────┐
          │                 │                 │
          ▼                 ▼                 ▼
     SMS LOCATE       Activity Log       Diagnostics
          │
          ▼
   Security & Privacy
```

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

# 📩 SMS LOCATE Workflow

```text
Incoming "LOCATE" SMS
          │
          ▼
     Verify Sender
          │
          ▼
 Check Trusted Circle
       /       \
      /         \
 Trusted       Unknown
    │              │
    ▼              ▼
 Get GPS          Block
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

BEACON uses Android permissions required for its emergency functionality.

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.SEND_SMS" />
<uses-permission android:name="android.permission.RECEIVE_SMS" />
<uses-permission android:name="android.permission.VIBRATE" />
```

Required permissions should be requested at runtime where applicable.

---

# 🎯 Project Objectives

The main objectives of BEACON are:

1. 🚨 Provide quick emergency assistance using SOS.
2. 📍 Obtain accurate GPS/GNSS location.
3. 📩 Share emergency location using cellular SMS.
4. 👥 Maintain trusted emergency contacts.
5. 📶 Reduce dependency on mobile-data internet.
6. 🔐 Protect sensitive location information.
7. 📋 Maintain emergency and security activity logs.
8. ❤️ Monitor important application and device components.
9. 🛡️ Provide a simple and secure emergency safety application.

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
* 📱 Simple Android interface

---

# ⚠️ Limitations

* SMS functionality depends on cellular network availability.
* GPS accuracy depends on the device and surrounding environment.
* Required Android permissions must be granted.
* Online map visualization may require internet connectivity.
* Background functionality can be affected by Android battery optimization and system restrictions.
* Emergency features depend on the capabilities of the Android device.

---

# 🚀 Future Enhancements

Future versions of BEACON could include:

* 🎙️ Voice-activated SOS
* 📳 Shake-to-activate emergency mode
* 📍 Real-time location tracking
* 🧠 AI-based emergency detection
* 🚶 Fall detection
* ⌚ Smartwatch / wearable integration
* 🔋 Improved background monitoring
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

Open the cloned project using **Android Studio**.

### 3. Sync Gradle

Allow Android Studio to download and synchronize all required Gradle dependencies.

### 4. Grant Permissions

Grant the required permissions for:

* Location
* SMS
* Vibration
* Contacts, where applicable

### 5. Run the Application

Connect an Android device or use an Android emulator and run the application.

> For GPS, SMS, vibration and cellular-network features, testing on a physical Android device is recommended.

---

# 🧪 Testing Checklist

* [ ] Application launch
* [ ] 3-second SOS activation
* [ ] SOS cancellation
* [ ] Emergency siren
* [ ] Emergency vibration
* [ ] GPS coordinate retrieval
* [ ] GPS accuracy display
* [ ] Trusted contact management
* [ ] Primary contact selection
* [ ] LOCATE keyword detection
* [ ] Trusted sender verification
* [ ] Unknown sender blocking
* [ ] SMS location response
* [ ] Activity log generation
* [ ] Battery status
* [ ] Health diagnostics
* [ ] Offline GPS operation

---

# 📸 Screenshots

Add the BEACON application screenshots here to demonstrate:

* 🏠 Home Dashboard
* 🚨 Emergency SOS
* ⚙️ Beacon Control
* 👥 Trusted Circle
* 📶 Offline Architecture
* 📍 GPS Tactical Location
* 📨 SMS Request Parser
* 📤 Location Response
* 📋 Activity Log
* 🔐 Security & Privacy
* ❤️ Health Diagnostics
* 🔄 Complete BEACON Flow

---

# 🤖 AI-Assisted Development

AI tools may be used as development assistance for understanding concepts, debugging errors, improving code, UI implementation, Android permissions, and troubleshooting.

All generated suggestions should be reviewed, tested, and modified according to the requirements of the BEACON application.

---

![image alt](https://github.com/khaneshahimadri/24012011230_Khanesha_Himadri_Assignment-1_MAD/tree/589b3796f7643b9ca5f7509cfcd8c71a5e14e0cf/screenshots)

# 👩‍💻 Developer

### Himadri Khanesha

**Enrollment No:** `24012011230`
**Class:** CE-I
**Batch:** I-2
**Subject:** Mobile Application Development (MAD)

**GitHub:**
`https://github.com/khaneshahimadri`

**Project Repository:**
`https://github.com/khaneshahimadri/24012011230_Khanesha_Himadri_Assignment-1_MAD`

---

# ⚠️ Disclaimer

BEACON is an **academic project developed for educational purposes**.

The application demonstrates Android emergency communication, GPS/location, SMS, security, and safety concepts. It should **not be considered a replacement for official emergency services**.

---

# 🚨 BEACON

### Emergency Assistance • Location • Communication • Safety

**Built with Kotlin ❤️ for Mobile Application Development**
