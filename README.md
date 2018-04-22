# Spoodle
Spoodle is a mobile app for people and restaurant owners to sell their leftover food to people at a discounted price, and donate the rest to homeless shelters, students, etc. Using a real-time marketplace for leftover food, restaurants can show what extra food they have and sell a discounted price in order to bring in extra cash rather than just throwing the food away. Spoodlers would have access to viewing all the discounted menu items throughout areas or a desired restaurant, which allows them to select cheaper food quicker and smarter than before. Additionally, by partnering with homeless shelters in the future, this would allow for food leftovers to be picked up by them to implement a zero-waste community. Basically, companies make money, students and homeless shelters save money, and the Earth gets cleaner.

# Video Demonstration
- https://youtu.be/hZMEX0xz21E

# Developers
- Pravat Bhusal (Java Client-side Developer)
- Rohith Rajashekarbabu (PHP & MySQL Server-side Developer)
- Michael Kasman (Team Coordinator)
- Mostafa Amir (Graphics Designer)
- Hrishikesh Rajashekarbabu (Android Studio Designer)

# Technologies
- Android Studio: Java (Client-side)
- XML (Views)
- PHP (Server-side)
- MySQL (Database)
- UI (Adobe Animate CC)
- APIs: Google Maps, PayPal

# Application Set-up
### 1. Server-side Connection
- Clone or download the .zip of this repository
- Inside the main/php folder, place all php files into your web-server
- Open the MainActivity.java file in app/src/main/java/com/shadowsych/spoodle
- Change the serverURL variable to your server's URL
- Compile and Build within Android Studio
- Finished!

### 2. Database Configuration
- Inside the app/src/main/php/db folder, export the database.sql file into your MySQL database
- Inside the app/src/main/php/db folder, open the dbconnection.php file and configure the variables to your database credentials

### 3. Paypal Configuration
- Open the PaypalControl.java file in the app/src/main/java/com/shadowsych/spoodle/assets folder
- Change PAYPAL_CLIENT_ID to your PayPal Sandbox or Production client in PayPalControl
- Change PayPalConfiguration.ENVIRONMENT_SANDBOX to PayPalConfiguration.ENVIRONMENT_PRODUCTION when in production
- Compile and Build within Android Studio
- Finished!

### 4. Google Maps Android API
- Install Google Repository and Google Play Services from Android Studio's SDK Manager
- Create API key and Enable Google Maps Android: https://console.cloud.google.com/
- Set your API key in the app/src/main/res/layout/strings.xml file
- Compile and Build within Android Studio
- Finished!

# Disclaimer
Spoodle is protected under Section 107 of the Copyright Act for fair use. The application’s in-app billing, assets, and sources were all developed for “non-profit educational purposes” at EarthHack 2018: a non-profit fair for “social good and technology.”