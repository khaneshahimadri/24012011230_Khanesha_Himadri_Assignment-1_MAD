# 🚨 BEACON – Emergency Safety & Offline Location System

**Emergency Location • SOS • Offline GPS • SMS • Trusted Circle • Security**

BEACON is an Android-based **Emergency Safety and Location System** developed using **Kotlin and XML**.

The application provides quick assistance during emergency situations using SOS alerts, GPS/GNSS location, cellular SMS, trusted contacts, emergency siren, vibration, security controls, activity logs, and device health monitoring.

---

## 📌 Project Overview

BEACON is developed as part of the **Mobile Application Development (MAD)** course.

During an emergency, users may not have access to mobile-data internet or enough time to manually contact multiple people.

BEACON provides multiple emergency and safety features in one Android application.

A major focus of BEACON is its **offline-oriented architecture**. GPS coordinates can be obtained without mobile-data internet, while cellular SMS can be used for emergency communication when SMS network coverage is available.

---

# 🎯 Objectives

* 🚨 Provide quick emergency assistance through SOS
* 📍 Obtain accurate GPS/GNSS coordinates
* 📩 Share emergency location through cellular SMS
* 👥 Maintain a secure Trusted Circle
* 📶 Reduce dependency on mobile-data internet
* 🔐 Protect location information from unauthorized users
* 📋 Maintain emergency and security activity logs
* ❤️ Monitor important BEACON system components

---

# ✨ Key Features

### 🚨 3-Second SOS Activation

Press and hold the **SOS button for 3 seconds** to activate emergency mode and prevent accidental activation.

When activated:

* 🚨 Emergency mode starts
* 🔊 Siren starts
* 📳 Device vibration is activated
* 📍 GPS coordinates are obtained
* 👥 Trusted contacts can be alerted
* 📋 Emergency activity is recorded

---

### 🔊 Siren & Vibration

Emergency mode activates:

* 🔊 Emergency siren
* 📳 Device vibration
* 🛑 SOS cancellation option

These features help attract nearby attention during an emergency.

---

### 📍 Live GPS / GNSS

BEACON provides detailed location information including:

* Latitude
* Longitude
* Estimated accuracy
* Altitude
* Location provider
* Last GPS update
* Copy coordinates
* Open location using map services

GPS coordinates can be obtained without mobile-data internet.

---

### 📩 SMS LOCATE System

A trusted contact can send:

`LOCATE`

BEACON verifies the sender before sharing location information.

```text
Incoming "LOCATE" SMS
          ↓
     Verify Sender
          ↓
   Trusted Circle
      ↙       ↘
 Trusted     Unknown
    ↓           ↓
 Get GPS      Block
    ↓
Generate Location
    ↓
Create Maps Link
    ↓
Send SMS Response
    ↓
Save Activity Log
```

---

### 👥 Trusted Circle

BEACON uses a **Trusted Circle Whitelist** to protect sensitive location information.

Users can:

* Add trusted contacts
* Set a primary contact
* Maintain verified contacts
* Send LOCATE requests
* Verify incoming requests
* Block unknown senders

Only authorized contacts can request sensitive location information.

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

### 📋 Beacon Activity Log

The application records important events such
