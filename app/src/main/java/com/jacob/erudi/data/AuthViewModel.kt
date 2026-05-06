package com.jacob.erudi.data

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.jacob.erudi.models.User

class AuthViewModel : ViewModel() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // functions will go here
    fun signup(
        fullname: String,
        email: String,
        phone: String,
        password: String,
        confirmPass: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        // 🔹 Validation
        if (fullname.isBlank() || email.isBlank() || phone.isBlank() || password.isBlank() || confirmPass.isBlank()) {
            onError("All fields are required")
            return
        }

        if (password != confirmPass) {
            onError("Passwords do not match")
            return
        }

        if (password.length < 6) {
            onError("Password must be at least 6 characters")
            return
        }

        // 🔹 Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val uid = mAuth.currentUser?.uid
                    if (uid == null) {
                        onError("Something went wrong. Try again.")
                        return@addOnCompleteListener
                    }

                    // 🔹 Create user object (NO PASSWORD STORED)
                    val user = User(
                        fullname = fullname,
                        email = email,
                        phone = phone,
                        uid = uid,
                        role = "User"
                    )

                    // 🔹 Save to Firebase Database
                    val ref = FirebaseDatabase.getInstance()
                        .getReference("Users/$uid")

                    ref.setValue(user).addOnCompleteListener { dbTask ->
                        if (dbTask.isSuccessful) {
                            onSuccess()
                        } else {
                            onError(dbTask.exception?.message ?: "Failed to save user data")
                        }
                    }

                } else {
                    onError(task.exception?.message ?: "Registration failed")
                }
            }
    }

    fun signin(
        email: String,
        password: String,
        onSuccessAdmin: () -> Unit,
        onSuccessUser: () -> Unit,
        onError: (String) -> Unit
    ) {

        if (email.isBlank() || password.isBlank()) {
            onError("Email and password cannot be blank")
            return
        }

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (!task.isSuccessful) {
                    onError(task.exception?.message ?: "Login failed")
                    return@addOnCompleteListener
                }

                val uid = mAuth.currentUser?.uid
                if (uid == null) {
                    onError("User not found")
                    return@addOnCompleteListener
                }

                // 🔹 Fetch user data from database
                FirebaseDatabase.getInstance()
                    .getReference("Users/$uid")
                    .get()
                    .addOnSuccessListener { snapshot ->

                        val role = snapshot.child("role").value?.toString() ?: "User"

                        if (role == "Admin") {
                            onSuccessAdmin()
                        } else {
                            onSuccessUser()
                        }
                    }
                    .addOnFailureListener {
                        onError("Failed to fetch user data")
                    }
            }
    }

}