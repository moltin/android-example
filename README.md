# Android Example

The android example application is an example of how the Moltin Android SDK can be leveraged to create an Android application.
It utilises most of the Moltin core functionality and can be used as a base project to create your own applications
against the Moltin API. The Android SDK is included in the Gradle build file.

## Requirements
Android SDK  
Java JDK 1.7 or higher  
Gradle  


## Getting Started
Clone the android project to your local machine and in your IDE such and Android Studio or Eclipse import the project
using Gradle.  The import process will depend on which IDE you are using.

### Collections
The application will automatically load collections from your store, please ensure you have product collections configured
in forge.

### Add your public key
The application will try and authorize once the CollectionsActivity Activity is called

`106: moltin.authenticate(getString(R.string.moltin_api_key), new Handler.Callback()`  

The above line shows the moltin API key is being requested from the string resource file so we need to place our key
in strings.xml which can be found in the resources folder:

`4: <string name="moltin_api_key">YOUR_API_KEY_HERE</string>`

### Launch The Application
Once you have entered your API key you can then launch the application in the Android Emulator or a physical device.  The
application will automatically pull in your collections.
