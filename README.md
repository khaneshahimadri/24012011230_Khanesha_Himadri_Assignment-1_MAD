# 🚨 BEACON – Emergency Location System

BEACON is an Android-based emergency safety application developed as part of my **Mobile Application Development (MAD) Assignment-1**.

The application is designed to provide quick access to emergency features such as **SOS alerts, GPS location, trusted contacts, SMS-based location requests, activity logs, and device health monitoring**.

---

## 🎯 Objectives

- Develop a functional Android emergency safety application.
- Implement an emergency SOS system.
- Access and display GPS location.
- Manage trusted emergency contacts.
- Implement SMS-based location request functionality.
- Generate Google Maps location links.
- Understand Android runtime permissions.
- Store application data locally using SharedPreferences.
- Apply Android concepts learned during MAD.

---

## ✨ Features

### 🚨 Emergency SOS
- Hold SOS button for 3 seconds to activate.
- Emergency siren using MediaPlayer.
- Device vibration during emergency mode.
- SOS can be stopped from the application.

### 📍 GPS Location
- Fetches available GPS location.
- Displays latitude and longitude.
- Shows location accuracy and provider information.
- Generates a Google Maps link.
- Supports copying/opening coordinates.

### 👥 Trusted Circle
- Add trusted emergency contacts.
- Select contacts from the device.
- Store contact details locally.
- Remove trusted contacts.
- Trusted contacts are used for location-request functionality.

### 📩 SMS Location Request
BEACON recognizes the emergency keyword:

`LOCATE`

The basic workflow is:

`LOCATE → Verify Trusted Contact → Get GPS → Generate Maps Link → Location Response`

### 🗺️ Location Response
- Uses available GPS coordinates.
- Generates a Google Maps location link.
- Prepares location information for trusted contacts.

### 📋 Activity Logs
- Stores important BEACON activities.
- Displays application events.
- Supports clearing stored logs.

### 🔒 Security & Privacy
- Trusted-contact whitelist concept.
- Unknown contacts can be excluded from the trusted workflow.
- Important settings are stored locally.

### 📡 Offline Support
BEACON is designed around GPS and cellular SMS functionality.

- GPS coordinates can be obtained without mobile internet when a GPS fix is available.
- SMS does not require internet access.
- SMS functionality requires cellular network/SMS service.
- Online Maps display may require an internet connection.

### ❤️ Beacon Health
Provides information about important services such as:

- GPS availability
- SMS/Telephony availability
- Battery information
- Network status
- BEACON configuration

### 🔄 Complete Beacon Flow
The application contains a one-device demonstration of the complete emergency workflow:

1. Trusted Person
2. SMS `"LOCATE"`
3. BEACON Receives Request
4. Verify Trusted Sender
5. Get GPS Location
6. Generate Google Maps Link
7. Prepare SMS Reply
8. Location Response Ready

The Complete Flow screen demonstrates the SMS exchange locally on one device while using saved contact and location information where available.

---

## 📱 Main Screens

The application contains the following screens:

1. Home Dashboard
2. Emergency SOS
3. Beacon Control
4. Trusted Circle
5. Offline Architecture
6. GPS Tactical Location
7. SMS Request Parser
8. Location Response
9. Beacon Activity Logs
10. Security & Privacy
11. Beacon Health Diagnostics
12. Complete Beacon Flow

---

## 🛠️ Technologies Used

- **Kotlin** – Application logic
- **XML** – User interface
- **Android Studio** – Development environment
- **ConstraintLayout** – UI layouts
- **CardView** – UI components
- **SharedPreferences** – Local data storage
- **Android Location Services** – GPS functionality
- **SMS APIs** – SMS functionality
- **BroadcastReceiver** – SMS/system event handling
- **MediaPlayer** – SOS siren
- **Vibrator** – Emergency vibration
- **Intent** – Activity navigation

---

## 🔐 Permissions Used

The application uses Android permissions such as:

- `ACCESS_FINE_LOCATION`
- `ACCESS_COARSE_LOCATION`
- `READ_CONTACTS`
- `SEND_SMS`
- `RECEIVE_SMS`
- `VIBRATE`
- `ACCESS_NETWORK_STATE`

Runtime permissions are requested where required.

---

## 🔄 Working Flow

```text
Open BEACON
     ↓
Configure Trusted Contact
     ↓
Emergency / LOCATE Request
     ↓
Verify Trusted Contact
     ↓
Get Available GPS Location
     ↓
Generate Google Maps Link
     ↓
Prepare Location Response
```

---

## ⚠️ Challenges Faced

During the development of BEACON, I faced several challenges:

### 1. GPS Permission Handling
Managing Android runtime location permissions and retrieving GPS coordinates correctly.

### 2. SOS Siren
Handling MediaPlayer so that the emergency siren starts and stops properly.

### 3. SMS Handling
Understanding SMS permissions and BroadcastReceiver functionality.

### 4. Trusted Contact Management
Selecting, storing, retrieving, and removing trusted contacts.

### 5. Multiple Screen Navigation
Managing navigation between multiple Android Activities using Intents.

### 6. UI Consistency
Maintaining the same design, colors, cards, and navigation across different screens.

### 7. Complete Flow
Creating a simple one-device demonstration of the complete emergency workflow.

---

## 📚 Concepts Learned

Through this project, I learned and implemented:

- Activities
- Intents
- XML Layouts
- ConstraintLayout
- CardView
- Event Listeners
- Runtime Permissions
- SharedPreferences
- GPS / Location Services
- SMS Handling
- BroadcastReceiver
- MediaPlayer
- Vibrator
- Multi-screen Navigation

---

## ▶️ How to Run

1. Clone or download this repository.
2. Open the project in **Android Studio**.
3. Wait for Gradle Sync to complete.
4. Connect an Android device.
5. Build and run the application.
6. Grant the required permissions.
7. Add a trusted contact.
8. Enable GPS/Location.
9. Test the BEACON features.

> A physical Android device is recommended for testing GPS, SMS, vibration, contacts, and other device-dependent functionality.

---

## 🚧 Future Improvements

- Improved background location support
- More trusted contacts
- Emergency notifications
- Improved activity history
- Cloud backup support
- Additional emergency customization
- Improved location tracking

---

## 👩‍💻 Developed By

**Himadri Khanesha**  
**Enrollment No.: 24012011230**  
**Class:** CE-I  
**Batch:** I-2  
**Course:** B.Tech Computer Engineering  
**University:** Ganpat University  
**College:** U.V. Patel College of Engineering (UVPCE)

---

## 📚 Academic Information

**Subject:** Mobile Application Development (MAD)  
**Assignment:** Assignment-1  
**Platform:** Android  
**Programming Language:** Kotlin  

---



### 🚨 BEACON
**Emergency Location System – Stay Connected. Stay Safe.**
