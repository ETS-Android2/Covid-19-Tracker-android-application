# CovidTracker

### How it works
After you sign in, you get an OTP token generated using Firebase Phone Auth. After you login, the application starts a background service that constantly publishes and receives the Firestore Database UIDs, by using the Nearby Messages API from Google. When two devices are in close proximity (approximately 4-5m for Bluetooth + Sonar), their meetup is registered in Firestore.
From the picker in the logged in screen, you can choose your current health status and press the button. This updates your health status in the database. Using Firestore Cloud Messages, there is a JavaScript function that triggers when this update happens and sends a push notification to the users that you have interacted with.

### Screenshots
<img src="/Screenshots/screenshot.jpg" height=500 width=100%>

### Backend
- https://firebase.google.com/docs/firestore/quickstart
- https://firebase.google.com/docs/functions/get-started
- https://developers.google.com/nearby/messages/overview
- https://cloud.google.com/maps-platform/

In order to use ***Backend Services***, You have to create ***API KEY*** from ***Google Cloud console*** and enable ***Maps SDK for Android*** and ***Nearby Message API***. 

### Backend Configuration (Firestore / Functions / Authentication)
Follow the installation guide for Firestore \
https://firebase.google.com/docs/firestore/quickstart

Set up Firebase Functions \
https://firebase.google.com/docs/functions/get-started 


### JSON schema for Firebase
After creating the firebase project you have to create Realtime Database Schemas \
\
2 tabels:
- users
    ```
    "users": {
        "user_id1" : {
            "phone" :
        }
        "user_id2" : {
            "phone" :
        }
    }
    ```

- users meetings
    ```
    "users_meetings" : {
        "user_id1" : {
            "meetings" : {
                "user_id_met1" : {
                    "found_timestamp" : ...
                    "lost_timestamp" : ...
                    "status" : ...
                }
                "user_id_met2" : {
                    "found_timestamp" : ...
                    "lost_timestamp" : ...
                    "status" : ...
                }
            }
        }
        "user_id2" : {
            "meetings" : {
                "user_id_met1" : {
                    "found_timestamp" : ...
                    "lost_timestamp" : ...
                    "status" : ...
                }
                "user_id_met2" : {
                    "found_timestamp" : ...
                    "lost_timestamp" : ...
                    "status" : ...
                }
            }
        }
    }
    ```

Once you complete the Firebase Functions setup, do the following steps:
- Goto this repository and look for firestore directory.
- Execute the following command
```
cd firestore/functions
npm install
```
- Then navigate back to previous directory by  ``` cd ../ ```
- Then run ``` firebase deploy --only functions ``` command to deploy push notification in cloud.

***Make sure you have installed Firebase CLI reference***\
If not installed then Follow the instructions guide for \
https://firebase.google.com/docs/cli

***Also You have to download your ```google-services.json``` file and move it into the project folder.***

***The Sample APK file is located [here](https://github.com/Aniket-Wali/CovidTracker/blob/master/debug/app-debug.apk)***

---

If any problem occur in this project, feel free to contact me : <aniket.wali007@gmail.com>

