# udacity-capstone-graduation-app

MyLoLHelper - Fifth and final project from the Android Developer Nanodegree program
Documentation: https://drive.google.com/open?id=1nyGsglOzRSS9CfHc2P6tNV4k6--J1lBH

# Notes
- On some emulators the "log in" will not work because of googles services
- Project suffered some restructure because Riot API has a Request Rate Limit until you receive an App KEY... 
As a developer you have to generate the API Key every 24 hours at https://developer.riotgames.com/ and for example I put some images in assets in the app.
Also, there are only 10 request for Details Champion every one hour... if it's the same champion is no problem because it's cached... but if there are 11 different champions it will show blank for the new ones. :(

- Build on the MVVM architecture from Android Architecture components
- Used  https://github.com/pwittchen/ReactiveNetwork
- Tested on Android 8.0.0 - HTC 10
- Used Firebase Auth, Firebase Database, Picasso, Retrofit, RxJava, Butter Knife
