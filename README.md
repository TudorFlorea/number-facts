# NumberFacts - Capstone project for the Udacity Android Nanodegree Program

## Project Overview
Numberfacts is an app that displays curiosities about numbers / dates / years using the http://numbersapi.com/ api.
It’s a educational and fun app that feeds your curiosity with interesting facts about numbers.

The facts are divided into 4 main categories:

* Trivia facts
* Math facts
* Date facts
* Year facts

You can get random facts for each of these categories or get a fact for a specific number / year / date that you provide.
The facts can be shared or saved to be viewed locally.

## Intended User

The intended users for this app are curious persons willing to increase their knowledge or persons that like to share interesting stuff over social media.
The main uses will be:

* In an education environment such as a professor trying to make teaching numbers interesting or a student to write an essay about numbers
* In a social media environment were the user will share interesting facts for popularity. 

## Features

Main app features:
* Display a random trivia fact
* Display a trivia fact for a given number
* Display a random math fact
* Display a math fact for a given number 
* Display a random date fact
* Display a date fact for a given date
* Display a random year fact
* Display a year fact for a given year
* Share any of above mentioned facts
* Save a fact and view it locally
* Change the color scheme of the app in a settings activity
* Receive notifications with random facts
* Displays ads
* Add a widget that displays a random fact

## Video

<img src="./images/numberfacts.gif" width="500">


## Common Project Requirements
- [x] App is written solely in the Java Programming Language.
- [x] App conforms to common standards found in the Android Nanodegree General Project Guidelines.

## Core Platform Development
- [x] App integrates a third-party library. (Okhttp and Butterknife)
- [x] App validates all input from servers and users. If data does not exist or is in the wrong format, the app logs this fact and does not crash.
- [x] App includes support for accessibility. That includes content descriptions, navigation using a D-pad, and, if applicable, non-audio versions of audio cues.
- [x] App keeps all strings in a strings.xml file and enables RTL layout switching on all layouts.
- [x] App provides a widget to provide relevant information to the user on the home screen.

## Google Play Services
- [x] App integrates two or more Google services. Google service integrations can be a part of Google Play Services or Firebase.
- [x] Each service imported in the build.gradle is used in the app.
- [x] If Admob is used, the app displays test ads. If Admob was not used, student meets specifications.
- [x] If Analytics is used, the app creates only one analytics instance. If Analytics was not used, student meets specifications.

## Material Design
- [x] App theme extends AppCompat.

- [x] App uses an app bar and associated toolbars.

- [x] App uses standard and simple transitions between activities.

## Data Persistence
- [x] App stores data locally either by implementing a ContentProvider OR using Firebase Realtime Database OR using Room. No third party frameworks nor Persistence Libraries may be used.

- [x] If Content provider is used, the app uses a Loader to move its data to its views.

## Building
- [x] App builds from a clean repository checkout with no additional configuration. (Except for API keys)

- [x] App builds and deploys using the installRelease Gradle task.

- [x] App is equipped with a signing configuration, and the keystore and passwords are included in the repository. Keystore is referred to by a relative path.

- [x] All app dependencies are managed by Gradle.


