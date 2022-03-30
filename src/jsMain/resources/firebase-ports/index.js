import {FIREBASE_CONFIG} from "./constants";
import { initializeApp } from "firebase/app";
import { getAuth, signInWithPopup, GoogleAuthProvider, signOut } from "firebase/auth";

const firebaseApp = initializeApp(FIREBASE_CONFIG);
const provider = new GoogleAuthProvider();
const auth = getAuth();

export async function logIn(){

    console.log("Logging in!");

    const userCred = await signInWithPopup(auth, provider);

    return {
        accessToken: userCred.user.accessToken,
        email: userCred.user.email,
        uid: userCred.user.uid,
    }
}

export async function logOut(){
    console.log("Logging out!");
    await signOut(auth);
}