<p align="center">
  <img align="middle" src="https://raw.githubusercontent.com/zingat/rateme-android/master/art/ratemelogo.png">
</p>

<p align="center">
  <a href="https://bintray.com/zingatmobil/Rateme/rateme/1.2.0">
    <img src="https://api.bintray.com/packages/zingatmobil/Rateme/rateme/images/download.svg">
  </a>
  <a target="_blank" href="https://android-arsenal.com/api?level=15">
    <img src="https://img.shields.io/badge/API-15%2B-orange.svg">
  </a>
</p>

RateMe is a powerful system to get rates from users in Android applications.

RateMe takes cares to show pretty dialogs to collect rates from users so you don't have to.
One thing you do is to define some rules and send events to RateMe library. 
RateMe watch the events you define and show the a pretty dialog to users on correct time.

# Table of Contents

1. [Gradle Dependency](https://github.com/zingat/RateMe-Android/#gradle-dependency)
2. [Quick Start](https://github.com/zingat/RateMe-Android/#quick-start)
    1. [Initialize RateMe](https://github.com/zingat/RateMe-Android/#initialize-rateme-in-your-application-class)
    2. [How events are sent to RateMe](https://github.com/zingat/RateMe-Android/#how-touch_me_event-is-sent-to-rateme)
3. [How button works](https://github.com/zingat/RateMe-Android/#how-buttons-works)
    1. [Rate Us](https://github.com/zingat/RateMe-Android/#rate-us)
    2. [Remind Me Later](https://github.com/zingat/RateMe-Android/#remind-me-later)
    3. [Don't Ask Again](https://github.com/zingat/RateMe-Android/#dont-ask-again)
4. [Arranging delay time](https://github.com/zingat/RateMe-Android/#arranging-delay-time)
5. [Callbacks](https://github.com/zingat/RateMe-Android/#callbacks)
6. [Custom Views](https://github.com/zingat/RateMe-Android/#custom-views)
7. [Custom Buttons](https://github.com/zingat/RateMe-Android/#custom-buttons)
8. [Changing colors and texts](https://github.com/zingat/RateMe-Android/#changing-colors-and-texts)
    1. [colors.xml](https://github.com/zingat/RateMe-Android/#colorsxml)
    2. [strings.xml](https://github.com/zingat/RateMe-Android/#strings.xml)
9. [Adding Multiple Conditions](https://github.com/zingat/RateMe-Android/#adding-multiple-conditions)   
    1. [Adding one by one](https://github.com/zingat/RateMe-Android/#adding-one-by-one)   
    2. [Adding all as Collection](https://github.com/zingat/RateMe-Android/#adding-all-as-collection)   
    3. [How multiple conditions work](https://github.com/zingat/RateMe-Android/#how-multiple-conditions-work)
    
------

# GRADLE DEPENDENCY
The minimum API level supported by this library is API 15.
Add the dependency to your `build.gradle`:

```Gradle
dependencies {
    implementation 'com.zingat:rateme:1.3.0'
}
```

# Quick start

### Initialize `RateMe` in your `Application` class
```kotlin
   class App : Application() {
   
       override fun onCreate() {
           super.onCreate()
   
           Rateme.getInstance(this@App)
                   // `addCondition(conditionName,repeatTime)`
                   .addCondition("touch_me_event", 3)
                   .reminderDuration(3)
       }
   
   }
```

Now we are telling to RateMe that you can show a dialog when 3 times `touch_me_event` is sent.

### How `touch_me_event` is sent to RateMe.
```kotlin
    Rateme.getInstance(this)
                .addEvent("touch_me_event")
```

The event can be sent for each statement. For example when a user opens the app 4 times you can send `app_opened` event or user can like 
a product in your app 2 times you can send `product_liked` event. For all statements you can send event seperately.

The default appearance is like picture below.

<p align="center">
  <img align="middle" src="https://raw.githubusercontent.com/zingat/rateme-android/master/art/defaultRatemeDialogWindow.png">
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
                       .delay(1000)
```
 
# Callbacks

To know when the user selects an action button, you set callbacks:

```kotlin
    Rateme.getInstance(this@App)
                 .addCondition("touch_me_event", 3)
                 .delay(1000)
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
 The count value is defined by developer in `addCondition()` method.
 * `which (Int)` parameter tells which action is happening. Each number indicates different state.
    * STARTED = -1
    * POSITIVE = 0
    * NEUTRAL = 1
    * NEGATIVE = 2
   
# Custom Views

Custom views are very easy to implement.

```kotlin
    Rateme.getInstance(this@App)
                    .addCondition("touch_me_event", 3)
                    .reminderDuration(3)
                    .delay(1000)
                    .custom(R.layout.layout_dialog)
```

After custom view is added appearance is like picture below. 

<p align="center">
  <img align="middle" src="https://raw.githubusercontent.com/zingat/rateme-android/master/art/customImageDialogWindow.png">
</p>

When `custom()` method is used, default title and content disappears. Only the you layout file will display on screen.

It is recommended not to use buttons and different type views in custom layout.
Because you can not provide callbacks for buttons. 

# Custom Buttons

You can use `customButton()` and `customButtonReverse()` methods to provide pretty much colored buttons.

```kotlin
    Rateme.getInstance(this@App)
                 .addCondition("touch_me_event", 3)
                 .delay(1000)
                 .custom(R.layout.layout_dialog)
                 .customButton()
  ```

`customButton()` and `customButtonReverse()` methods shouldn't be used together. 
They have different type interfaces.
   
After `custombutton()` is added appearance is like picture below. 

<p align="center">
  <img align="middle" src="https://raw.githubusercontent.com/zingat/rateme-android/master/art/customButtonDialogWindow.png">
</p>

You can use the `customButtonReverse()` method with same way.

# Changing colors, and texts

You can change all values by creating new color items with same name in your applications.

### colors.xml

````xml
<resources>
    
    <!--Default background color for all buttons. It is active when customButton() is used.-->
    <color name="rm_defaultButtonBackground">#fff</color>
    
    <!--Default text color for buttons. It is become active when customButtonReverse() is used.-->
    <color name="rm_defaultTextColor">#fff</color>
    

    <!--Text, Border and Background color for Rate Us Button-->
    <color name="rm_BtnRateTextColor">#02a8fe</color>
    

    <!--Text, Border and Background color for Remind Me Later Button-->
    <color name="rm_BtnLaterTextColor">#4bca5e</color>
    
    <!--Text, Border and Background color for Don't Ask Again Button-->
    <color name="rm_BtnNeverTextColor">#ff6175</color>
    
</resources>
````

### strings.xml

````xml
<resources>
    <!--Default text for Rate Us button-->
    <string name="rateme_btn_rate_text">Rate us</string>
    

    <!--Default text for Remind Me Later button-->
    <string name="rateme_btn_later_text">Remind Me Later</string>
    

    <!--Default text for Don't Ask again button-->
    <string name="rateme_btn_never_text">Don\'t ask again</string>
    
    <!--Default dialog title. It is deactive when custom() method is used!-->
    <string name="rateme_dialog_title">How was your experience?</string>
    
    <!--Default dialog context. It is deactive when custom() method is used!-->
    <string name="rateme_dialog_message">Recommend us to others by rating us on Play Store</string>
    

</resources>
````

# Adding Multiple Conditions

There are two different easy way to add multiple conditions to RateMe library.

### Adding one by one

You can add on by one all condition. You can add event how much you want with this way. 

````kotlin
Rateme.getInstance(this@App)
                   // `addCondition(conditionName,repeatTime)`
                   .addCondition("touch_me_event", 3)
                   .addCondition("slide_me_event", 2)
                   .addCondition("rotate_me_event", 4)
                   .reminderDuration(3)
````

### Adding all as Collection

Every condition represents with `Condition` object. Condition object has two variables.
`count` and `type`.

You can create own `Condition` object and you can add this Condition object in a ArrayList. 
Finally you can use `setConditionList( conditionList : ArrayList<Condition> )` method.

```kotlin
    package com.zingat.rateme.model

    val conditionList = ArrayList<Condition>
    conditionList.add( Condition( 3, "touch_me_event" ) )
    conditionList.add( Condition( 2, "slide_me_event" ) )
    conditionList.add( Condition( 4, "rotate_me_event" ) )
    
    Rateme.getInstance(this@App)
                       .setConditionList(conditionList)
                       .reminderDuration(3)
``` 

### How multiple conditions work

When multiple events are added, RateMe shows dialog when one of conditions are completed and even other events 
completed, RateMe won't show a new dialog to user.

For exameple; if `slide_me_event` condition is completed , RateMe will show a new dialog and if user click `Rate Us` or `Don't show again`
buttons, even user complete the `touch_me_event` new dialog won't show. RateMe waits always the first completed condition rules.
In this way user won't be disturbed multiple times.

<p align="right">
    <img 
        align="right" 
        src="https://raw.githubusercontent.com/zingat/rateme-android/master/art/zingatLogo.png" width="320">
</p>
