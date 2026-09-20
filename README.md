# 🚨 BEACON – Emergency Safety & Offline Location System

<p align="center">
  <b>Emergency Location • SOS • Offline GPS • SMS • Trusted Circle</b>
</p>

<p align="center">
  An Android-based emergency safety application built using Kotlin and XML.
</p>

---

## 📌 Project Overview

**BEACON** is an Android-based Emergency Safety and Location System developed as part of the **Mobile Application Development (MAD)** course.

The application is designed to provide quick assistance during emergency situations using **SOS alerts, GPS/GNSS location, cellular SMS, trusted contacts, emergency siren, vibration, security controls, activity logs, and device health monitoring**.

A major focus of BEACON is its **offline-oriented architecture**. GPS coordinates can be obtained without mobile-data internet, while cellular SMS can be used for emergency communication when SMS network coverage is available.

---

## 📱 Application Preview

<p align="center">
  <img src="screenshots/home.jpg" width="30%" />
  <img src="screenshots/sos_active.jpg" width="30%" />
  <img src="screenshots/gps.jpg" width="30%" />
</p>

---

## 🎯 Objectives

- 🚨 Provide quick emergency assistance through SOS
- 📍 Obtain accurate GPS/GNSS coordinates
- 📩 Share emergency location through cellular SMS
- 👥 Maintain a secure Trusted Circle
- 📶 Reduce dependency on mobile internet
- 🔐 Protect location information from unauthorized users
- 📋 Maintain emergency and security activity logs
- ❤️ Monitor important BEACON system components

---

# ✨ Key Features

### 🚨 3-Second SOS Activation
Press and hold the SOS button for **3 seconds** to activate emergency mode and reduce accidental triggers.

### 🔊 Siren & Vibration
Emergency mode activates an audible siren and vibration alert.

### 📍 Live GPS/GNSS
Displays latitude, longitude, accuracy, altitude and latest GPS update.

### 📩 SMS LOCATE System
Trusted contacts can use the keyword **`LOCATE`** for location-based emergency communication.

### 👥 Trusted Circle
Only authorized/whitelisted contacts are permitted to request sensitive location information.

### 🛡️ Security & Privacy
Unknown sender blocking, trusted sender verification, stealth controls and anti-spam protection.

### 📋 Activity Audit
Maintains records of successful requests, blocked requests and SOS events.

### 🔋 Health & Telemetry
Displays GPS, SMS, cellular and battery subsystem status.

### 📶 Offline-Oriented Operation
Core GPS and SMS features are designed without dependency on Wi-Fi or mobile-data internet.

---

# 📲 12 BEACON Modules

BEACON contains **12 major functional modules**:

| No. | Module | Purpose |
|---|---|---|
| 1 | 🏠 Home Dashboard | System status and SOS access |
| 2 | 🚨 Emergency SOS | Siren, vibration and emergency dispatch |
| 3 | ⚙️ Beacon Control | LOCATE keyword and SMS monitoring |
| 4 | 👥 Trusted Circle | Verified emergency contacts |
| 5 | 📶 Offline Architecture | GPS + cellular SMS operation |
| 6 | 📍 GPS Tactical Location | Live GNSS coordinates and accuracy |
| 7 | 📨 SMS Request Parser | Validates incoming LOCATE requests |
| 8 | 📤 Location Response | Generates location SMS response |
| 9 | 📋 Beacon Activity Log | Emergency and security audit |
| 10 | 🔐 Security & Privacy | Whitelist and privacy controls |
| 11 | ❤️ Health Diagnostics | GPS, SMS and battery diagnostics |
| 12 | 🔄 Complete Beacon Flow | End-to-end BEACON architecture |

<p align="center">
  <img src="screenshots/screens1.jpg" width="42%" />
  <img src="screenshots/screens2.jpg" width="42%" />
</p>

---

# 🚨 Emergency SOS System

The user must hold the SOS button for **3 seconds**.

```text
Hold SOS for 3 Seconds
          ↓
     SOS Activated
          ↓
   Siren + Vibration
          ↓
  Get GPS Coordinates
          ↓
 Alert Trusted Contacts
          ↓
  Store Activity Log
```

<p align="center">
  <img src="screenshots/sos.jpg" width="38%" />
  <img src="screenshots/sos_active.jpg" width="38%" />
</p>

---

# 📍 GPS Tactical Telemetry

BEACON provides detailed GPS information including:

- Latitude
- Longitude
- Estimated accuracy
- Altitude
- Location provider
- Last GPS update
- Copy coordinates
- Open location on web map

<p align="center">
  <img src="screenshots/gps.jpg" width="38%" />
</p>

---

# 👥 Trusted Circle

The **Trusted Circle Whitelist** protects the user's location from unauthorized requests.

Users can:

- Add trusted contacts
- Set a primary contact
- Maintain verified contacts
- Send LOCATE requests
- Block unknown senders

<p align="center">
  <img src="screenshots/circle.jpg" width="38%" />
</p>

---

# 📩 SMS LOCATE Workflow

One of the main BEACON workflows is location communication through cellular SMS.

```text
Incoming "LOCATE" SMS
          ↓
     Verify Sender
          ↓
   Check Trusted Circle
       ↙       ↘
   Trusted   Unknown
      ↓          ↓
 Get GPS       Block
      ↓
Generate Location
      ↓
Create Maps Link
      ↓
 Send SMS Response
      ↓
 Save Activity Log
```

<p align="center">
  <img src="screenshots/sms_request.jpg" width="38%" />
  <img src="screenshots/sms_response.jpg" width="38%" />
</p>

---

# 📶 Offline Architecture

BEACON separates **GPS, cellular SMS and internet connectivity**.

```text
🛰️ GPS/GNSS
     ↓
Location Coordinates

📡 Cellular Network
     ↓
SMS Communication

🌐 Internet
     ↓
Online Map Services
```

This means mobile-data internet is not required to obtain GPS coordinates. SMS communication still requires cellular SMS coverage.

<p align="center">
  <img src="screenshots/offline.jpg" width="38%" />
</p>

---

# 🔐 Security & Privacy

BEACON includes several mechanisms designed to protect location information:

- 🛡️ Trusted sender whitelist
- 🚫 Unknown sender blocking
- 🔑 Exact LOCATE keyword verification
- ⏱️ Anti-spam cooldown
- 🕵️ Stealth mode
- 📋 Activity auditing

<p align="center">
  <img src="screenshots/security.jpg" width="38%" />
</p>

---

# 📋 Beacon Activity Audit

BEACON records important events such as:

- Emergency SOS activation
- Successful trusted requests
- Blocked unknown requests
- GPS/location responses
- Diagnostic activities

<p align="center">
  <img src="screenshots/logs.jpg" width="38%" />
</p>

---

# ❤️ Health & Telemetry

The diagnostic module monitors important BEACON subsystems.

It includes:

- 🛰️ GPS subsystem
- 📩 Cellular SMS availability
- 🔋 Battery and power status
- 📡 Cellular system status

<p align="center">
  <img src="screenshots/health.jpg" width="38%" />
</p>

---

# 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| **Kotlin** | Application logic |
| **XML** | UI design |
| **Android Studio** | Development environment |
| **Android SDK** | Android functionality |
| **Google Play Services** | Location services |
| **Fused Location Provider** | GPS/GNSS location |
| **BroadcastReceiver** | SMS/event handling |
| **Android SMS APIs** | SMS communication |
| **MediaPlayer** | Emergency siren |
| **Vibrator API** | Emergency vibration |
| **BatteryManager** | Battery monitoring |
| **SharedPreferences** | Local application data |
| **Intent** | Navigation and external actions |

---

# ⚠️ Challenges Faced

During development, some of the major challenges were:

- Implementing the **3-second SOS hold**
- Getting accurate and updated GPS coordinates
- Handling Android runtime permissions
- Working with GPS without mobile-data internet
- Receiving and processing SMS requests
- Verifying trusted contacts before sharing location
- Handling unknown/unauthorized senders
- Managing siren and vibration during SOS
- Handling different Android versions
- Maintaining activity logs
- Managing security and privacy controls
- Keeping the code student-friendly while implementing real Android functionality

---

# 🔐 Permissions Used

The application uses Android permissions for:

- 📍 Fine Location
- 📍 Coarse Location
- 📩 SMS
- 📳 Vibration
- 👥 Contacts where required

---

# 🧪 Testing

BEACON can be tested for:

- SOS activation after a 3-second hold
- SOS cancellation
- Siren and vibration
- GPS coordinate retrieval
- GPS accuracy
- Battery percentage
- Trusted contact selection
- LOCATE keyword handling
- Trusted sender verification
- Unknown sender blocking
- SMS location response
- Activity log generation
- Offline GPS operation

---

# 🚀 Future Scope

- Improved background location handling
- Multiple emergency profiles
- Enhanced contact management
- Location history
- Additional emergency triggers
- Improved battery optimization
- Emergency notification improvements
- Further integration with emergency services

---

# 📸 Screenshots

<p align="center">
  <img src="screenshots/home.jpg" width="24%" />
  <img src="screenshots/gps.jpg" width="24%" />
  <img src="screenshots/circle.jpg" width="24%" />
  <img src="screenshots/logs.jpg" width="24%" />
</p>

<p align="center">
  <img src="screenshots/offline.jpg" width="24%" />
  <img src="screenshots/security.jpg" width="24%" />
  <img src="screenshots/sms_request.jpg" width="24%" />
  <img src="screenshots/health.jpg" width="24%" />
</p>

---

# 👩‍💻 Developed By

### Himadri Khanesha

**Enrollment No:** `24012011230`  
**Class:** CE-I  
**Batch:** I-2  
**Subject:** Mobile Application Development (MAD)

---

## ⚠️ Disclaimer

BEACON is an **academic project developed for educational purposes**.

It demonstrates Android emergency communication, location and safety concepts and should **not be considered a replacement for official emergency services**.

---

<p align="center">
  <b>🚨 BEACON</b><br>
  <i>Emergency Assistance • Location • Communication • Safety</i>
</p>
