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

### Initialize `RateMe` in your `Application` class
```kotlin
   class App : Application() {
   
       override fun onCreate() {
           super.onCreate()
   
           Rateme.getInstance(this@App)
                   .addCondition("touch_me_event", 3)
                   .reminderDuration(3)
       }
   
   }
```

Now we are telling RateMe that you can show a dialog when 3 times `touch_me_event` is sent.

### How `touch_me_event` is sent to RateMe.
```kotlin
    Rateme.getInstance(this)
                .addEvent("touch_me_event")
```

The event can be sent for each statement. For example when a user opens the app 4 times you can send `app_opened` event or user can like 
a product in your app 2 times you can send `product_liked` event. For all statements you can send event seperately.

The default appearance is on App like picture below.

<p align="center">
  <img align="middle" src="https://raw.githubusercontent.com/zingat/rateme-android/readmebranch/art/defaultRatemeDialogWindow.png">
</p>

`When dialog shown on screen, user have to choose one of these options to close the window. 
To touch background and back button don't close the window.`

# How Buttons works

### Rate Us

RateMe library detects your applicationId(for example com.zingat.rateme) and
 When user clicks the `Rate Us` button, your app's Google Play page opens automaticly 
 and user can rate your app easily.
 
### Remind Me Later

When user clicks the `Remind Me Later` button, the dialog disappears until the days finished 
you defined in initialization code. `reminderDuration(day:Int)` is used define the necessary time.
The parameter is given in days.

### Don't Ask Again

When user clicks the `Don't Ask Again` button, the dialog disappears and never appear again.

# Arranging delay time

RateMe supports to arrange the delay time
`delay(time : Long)` indicates the time to display dialog after all events completed.
Default value is 0.


```kotlin
    Rateme.getInstance(this@App)
                       .addCondition("touch_me_event", 3)
                       .reminderDuration(3)
                       .delay(2500)
```
 
# Callbacks

To know when the user selects an action button, you set callbacks:

```kotlin
    Rateme.getInstance(this@App)
                 .addCondition("touch_me_event", 3)
                 .reminderDuration(3)
                 .delay(2500)
                 .onRateCallback( object : RMEventCallback{
                     override fun onEvent() {
                         // TODO
                     }
                 })
                 .onDontAskCallback(object : RMEventCallback{
                     override fun onEvent() {
                         // TODO
                     }
                 })
                 .onRemindLaterCallback(object : RMEventCallback{
                     override fun onEvent() {
                         // TODO
                     }
                 })
                 .onShowCallback(object : RMEventCallback{
                     override fun onEvent() {
                         // TODO
                     }
                 })
                 .onRMCallback(object : RMCallback{
                     override fun onEvent(eventName: String, count: Int, which: Int) {
                         // TODO
                     }
                 })
  ```
If you are listening for all three action buttons, you could just use `onRMCallback()`.
 
 * `eventName (String)` parameter tells completed event name. In our case this is `touch_me_event`
 * `count (Int)` parameter tells completed event count value. In our case this is `3`. 
 The count value is defined by developer when you in `addCondition()` method.
 * `which (Int)` parameter tells which action is happening. Each number indicates different state.
    * STARTED = -1
    * POSITIVE = 0
    * NEUTRAL = 1
    * NEGATIVE = 2
   
           




