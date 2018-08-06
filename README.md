<p align="center">
  <img align="middle" src="https://raw.githubusercontent.com/zingat/rateme-android/readmebranch/art/ratemelogo.png">
</p>

<p align="center">
  <a href="https://bintray.com/zingatmobil/Rateme/rateme/1.2.0">
    <img src="https://api.bintray.com/packages/zingatmobil/Rateme/rateme/images/download.svg">
  </a>
  <a target="_blank" href="https://android-arsenal.com/api?level=15">
    <img src="https://img.shields.io/badge/API-15%2B-orange.svg">
  </a>
</p>

RateMe is a powerful system for getting rates from users in Android applications.

RateMe takes cares to show garish dialogs to collect rates from users so you don't have to.
One thing you do is to define some rules and send events to RateMe library.

# GRADLE DEPENDENCY
The minimum API level supported by this library is API 15.
Add the dependency to your `build.gradle`:

```Gradle
dependencies {
    implementation 'com.zingat:rateme:1.2.0'
}
```

#Quick start

### 1. Initialize `RateMe` in your `Application` class
```kotlin
   class App : Application() {
   
       override fun onCreate() {
           super.onCreate()
   
           Rateme.getInstance(this@App)
                   .addCondition("touch_me_event", 3)
                   .reminderDuration(3)
                   .delay(2500)
       }
   
   }
```

Now we are telling RateMe that you can show a dialog when 3 times `touch_me_event` is sent.

### 2. How `touch_me_event` is sent to RateMe.
```kotlin
    Rateme.getInstance(this)
                .addEvent("touch_me_event")
```

The event can be sent for each statement. For example when a user opens the app 4 times you can send `app_opened` event or user can like 
a product in your app 2 times you can send `product_liked` event. For all statements you can send event seperately.

The default appearance is on App is like picture below.

<p align="center">
  <img align="middle" src="https://raw.githubusercontent.com/zingat/rateme-android/readmebranch/art/defaultRatemeDialogWindow.png">
</p>


