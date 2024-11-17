# Secure Data Manager

**Secure Data Manager** is a mobile application designed to securely store and manage personal data, including passwords, secure notes, and encrypted documents. The app ensures data privacy by leveraging encryption and provides an intuitive interface for efficient data management.

---

## Features

### Authentication
**PIN-based Authentication**:
- Users must set a PIN during the initial app setup.
- This PIN is required to access the app on subsequent launches.
- PINs are stored securely using `EncryptedSharedPreferences`.

**Future Enhancements**:
- Biometric authentication for added convenience.
- PIN reset functionality.

### Password Manager
- Add and securely store **username** and **password** pairs.
- View stored passwords in a secure environment.
- Delete passwords when no longer needed.

### Secure Notes
- Create **secure notes** to store sensitive information.
- Edit existing notes securely.
- Delete notes when they are no longer needed.

### Document Storage
- **Upload Documents**:
  - Encrypt and securely store files uploaded from your device.
- **View Document List**:
  - See a list of all uploaded documents with metadata such as file name and timestamp.
- **Download and Decrypt**:
  - Select a desired location to save and decrypt the uploaded document.

---

## Technology Stack

- **Programming Language**: Kotlin
- **Framework**: Jetpack Compose
- **Database**: Room Database
- **Encryption**: AES-based encryption for secure data storage
- **Android Version**: Supports Android 8.0 (API Level 26) and above

---

## Installation and Usage

### Prerequisites
- Android Studio with Kotlin support.
- Android device or emulator running Android 8.0 (API Level 26) or above.

### Steps to Run
1. Clone the repository:
   ```bash
   git clone git@github.com:aditya577/SecureDataManager.git
   cd SecureDataManager
2. Open the project in Android Studio.
3. Sync the Gradle files.
4. Run the app on an emulator or connected device.
