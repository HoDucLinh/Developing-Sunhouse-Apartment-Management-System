// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getDatabase } from "firebase/database";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
const firebaseConfig = {
  apiKey: "AIzaSyCnCMyrG62rLul-2ZLM0d7_UrUijfCsCuE",
  authDomain: "sunhouse-b77af.firebaseapp.com",
  databaseURL: "https://sunhouse-b77af-default-rtdb.asia-southeast1.firebasedatabase.app",
  projectId: "sunhouse-b77af",
  storageBucket: "sunhouse-b77af.firebasestorage.app",
  messagingSenderId: "349732198159",
  appId: "1:349732198159:web:320aedea61661a72bbd07a"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);

export const db = getDatabase(app);