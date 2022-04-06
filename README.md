# Kotlin Ports Demo

[![Netlify Status](https://api.netlify.com/api/v1/badges/a5e6c09a-f21f-4d3a-834c-78e5f9dfff15/deploy-status)](https://app.netlify.com/sites/kotlin-ports-demo/deploys)

This repository is a demo of how the [Elm ports] method can be used to quickly introduce a Javascript library into a Kotlin/JS project without too much work with external declarations.

You can demo the app **[here](https://kotlin-ports-demo.netlify.app/)**.

It is a support to **[this blog post](https://lengrand.fr/using-elm-knowledge-to-dive-into-kotlin-js)**

This repository contains a short [Kotlin/Js](https://kotlinlang.org/docs/js-interop.html) project with a [Firebase integration](https://firebase.google.com/).


## Getting started

To run this app locally, you will need to :

- Set up your [Firebase Cloud Firestore](https://firebase.google.com/docs/firestore) to handle and receive users and messages.
- Be able to run this repository

### Setting up the Database and Auth

- Go to the [Firebase console](https://console.firebase.google.com/?pli=1) and create a new project
- Setup Google Signin in the Authentication part.

![How to setup Google Sign-In](images/auth-setup.png)

- Create a new Cloud Firestore in locked mode
- Change the Firestore rules so that only logged requests can be written / read.

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
		match /users/{userId}/messages/{messageId} {
      allow create, read, update, delete: if request.auth.uid == userId;
    }
  }
}
```

Using those rules, each user will get its own space, with a messages store inside it. So this doubles down as database setup, and security.

That should be it! If needed, test your rules using the [Simulator](https://firebase.google.com/docs/firestore/security/get-started#testing_rules).

### Running the code

To run the code, only a few steps are needed :).

- Clone the repo : `$ git clone git@github.com:jlengrand/kotlin-ports-demo.git`
- Move in the repo : `$ cd kotlin-ports-demo`
- You will need to modify the `src/jsMain/kotlin/resources/firebase-ports/constants.js` file to let the app know about your Firestore project. The file looks like this :

```
export const FIREBASE_CONFIG = {
    apiKey: "AIzaSyAbDxaYepNPHJSSnUtWAH9VmYQXWPVm3gI",
    authDomain: "kotlin-js-ports-demo.firebaseapp.com",
    projectId: "kotlin-js-ports-demo",
    storageBucket: "kotlin-js-ports-demo.appspot.com",
    messagingSenderId: "246888979761",
    appId: "1:246888979761:web:e4dd65801c34ed5abfa410",
    measurementId: "G-BG8D2JGLQD"
};
```

The required information is the same as described in the [Firebase config object](https://firebase.google.com/docs/web/setup#config-object).
The easiest way is to go to the settings page of your project, and scroll down until you see the javascript snippet. It will contain all the information needed.

_Note : All the information in the .env file is not secret, and can be shared. In fact, it will be accessible to anyone using your app via the console. [This is not a problem](https://stackoverflow.com/questions/37482366/is-it-safe-to-expose-firebase-apikey-to-the-public)!_

![Settings page of Firebase project](images/settings.png)

- Run the app : `$ ./gradlew clean build jsBrowserDevelopmentRun`. You will be able to test the app at `http://localhost:8080`!

### Deploying

If for some reason you want to deploy the code somewhere, you can run `$  ./gradlew jsBrowserDistribution`. Now you have to serve the content of the `build/distributions` folder, and you're done!

## Contributing

Contributions are more than welcome ! You can [pick one of the issues of the list](https://github.com/jlengrand/kotlin-ports-demo/issues) or simply create your own :).

## Authors

* [Julien Lengrand-Lambert](https://twitter.com/jlengrand)

Happy hacking!
