import {FIREBASE_CONFIG} from "./constants";
import { initializeApp } from "firebase/app";
import { getAuth, signInWithPopup, GoogleAuthProvider, signOut } from "firebase/auth";
import { collection, addDoc, getFirestore, getDocs, onSnapshot, query}  from "firebase/firestore";

const firebaseApp = initializeApp(FIREBASE_CONFIG);
const provider = new GoogleAuthProvider();
const auth = getAuth();
const firestore = getFirestore(firebaseApp);

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

export async function saveMessage(uid, message){
    console.log("Saving message : " + message + " for user " + uid + "!");

    await addDoc(collection(firestore, `users/${uid}/messages`), {
        content : message
    });
}

export async function getMessages(uid){
    console.log("Retrieving existing messages");
    let messages = [];

    const querySnapshot = await getDocs(collection(firestore, `users/${uid}/messages`));
    querySnapshot.forEach((doc) => {
        messages.push({
            id: doc.id,
            content: doc.data().content
        })
    });

    return messages;
}

export async function syncMessages(uid, callback){
    console.log("syncMessages activated!");

    const q = query(collection(firestore, `users/${uid}/messages`));
    onSnapshot(q, (querySnapshot) => {
        const messages = [];
        querySnapshot.forEach((doc) => {
            messages.push({
                id: doc.id,
                content: doc.data().content
            })
        });

        callback(messages);
    });
}