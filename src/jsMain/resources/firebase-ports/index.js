import {FIREBASE_CONFIG} from "./constants";
import { initializeApp } from "firebase/app";
import { getAuth, createUserWithEmailAndPassword, signInWithEmailAndPassword, signOut } from "firebase/auth";

const firebaseApp = initializeApp(FIREBASE_CONFIG);
const auth = getAuth();

export async function signUp(email, password){

    console.log("Signing Up");

    try {
        const userCred = await createUserWithEmailAndPassword(auth, email, password);

        return {
            accessToken: userCred.user.accessToken,
            email: userCred.user.email,
            uid: userCred.user.uid,
            lastLogin: userCred.user.metadata.lastLoginAt,
            createdAt : userCred.user.metadata.createdAt
        }
    }
    catch (e) {
        console.log(e);
    }
}

export async function logIn(email, password){

    console.log("Signing In");

    try {
        const userCred = await signInWithEmailAndPassword(auth, email, password);

        return {
            accessToken: userCred.user.accessToken,
            email: userCred.user.email,
            uid: userCred.user.uid,
            lastLogin: userCred.user.metadata.lastLoginAt,
            createdAt : userCred.user.metadata.createdAt
        }
    }
    catch (e) {
        console.log(e);
    }
}

export async function logOut(){

    console.log("Logging out!");

    try {
        await signOut(auth);
    }
    catch (e) {
        console.log(e);
    }
}