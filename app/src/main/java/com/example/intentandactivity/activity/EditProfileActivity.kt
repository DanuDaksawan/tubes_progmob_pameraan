package com.example.intentandactivity.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.intentandactivity.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class EditProfileActivity : AppCompatActivity() {

    private lateinit var btnback: ImageView
    private lateinit var profileImage: ImageView
    private lateinit var changePhoto: Button
    private lateinit var editName: EditText
    private lateinit var editUsername: EditText
    private lateinit var editBio: EditText
    private lateinit var editAddress: EditText
    private lateinit var saveButton: Button
    private lateinit var logoutButton: Button

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storageRef: StorageReference
    private lateinit var userId: String
    private var imageUri: Uri? = null

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        btnback = findViewById(R.id.btnback)
        profileImage = findViewById(R.id.profile_image)
        changePhoto = findViewById(R.id.change_photo_button)
        editName = findViewById(R.id.edit_name)
        editUsername = findViewById(R.id.edit_username)
        editBio = findViewById(R.id.edit_bio)
        editAddress = findViewById(R.id.edit_location)
        saveButton = findViewById(R.id.save_button)
        logoutButton = findViewById(R.id.logout_button)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storageRef = FirebaseStorage.getInstance().reference
        userId = firebaseAuth.currentUser!!.uid

        // Set default hints if Firestore fields are empty
        setDefaultHints()

        // Load profile data from Firestore into EditText
        loadProfileData()

        // Load profile image from Firebase Storage or set default
        loadProfileImage()

        // Change photo button click listener
        changePhoto.setOnClickListener {
            openImagePicker()
        }

        // Save button click listener
        saveButton.setOnClickListener {
            saveProfileChanges()
        }

        // Logout button click listener
        logoutButton.setOnClickListener {
            logoutUser()
        }

        // Back button click listener
        btnback.setOnClickListener {
            finish()
        }
    }

    private fun setDefaultHints() {
        editName.hint = "Name"
        editUsername.hint = "Username"
        editBio.hint = "Biography"
        editAddress.hint = "Address"
    }

    private fun loadProfileData() {
        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val name = document.getString("name")
                    val username = document.getString("username")
                    val bio = document.getString("biography")
                    val address = document.getString("address")

                    // Set EditText values
                    editName.setText(name)
                    editUsername.setText(username)
                    editBio.setText(bio)
                    editAddress.setText(address)
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to load profile data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadProfileImage() {
        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val profileImageUrl = document.getString("profile")
                    if (!profileImageUrl.isNullOrEmpty()) {
                        Glide.with(this)
                            .load(profileImageUrl)
                            .placeholder(R.drawable.default_profil)
                            .error(R.drawable.default_profil)
                            .into(profileImage)
                    } else {
                        profileImage.setImageResource(R.drawable.default_profil)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to load profile image", Toast.LENGTH_SHORT).show()
            }
    }

    private fun openImagePicker() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data

            // Upload image to Firebase Storage
            uploadImageToFirebaseStorage()
        }
    }

    private fun uploadImageToFirebaseStorage() {
        imageUri?.let { uri ->
            val profileImageRef = storageRef.child("profile_images/${UUID.randomUUID()}")
            val uploadTask = profileImageRef.putFile(uri)

            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                profileImageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    updateProfileImageUrl(downloadUri.toString())
                } else {
                    Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateProfileImageUrl(imageUrl: String) {
        val profileUpdates = hashMapOf(
            "profile" to imageUrl
        )

        firestore.collection("users").document(userId)
            .set(profileUpdates, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(this, "Profile image updated", Toast.LENGTH_SHORT).show()
                loadProfileImage()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to update profile image", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveProfileChanges() {
        val newName = editName.text.toString().trim()
        val newUsername = editUsername.text.toString().trim()
        val newBio = editBio.text.toString().trim()
        val newAddress = editAddress.text.toString().trim()

        // Check if username is not empty
        if (newUsername.isNotEmpty()) {
            // Validate if username is unique
            validateUsername(newUsername) { isUnique ->
                if (isUnique) {
                    updateProfile(newName, newUsername, newBio, newAddress)
                } else {
                    // Check if the current username belongs to the current user
                    firestore.collection("users")
                        .document(userId)
                        .get()
                        .addOnSuccessListener { documentSnapshot ->
                            if (documentSnapshot.exists()) {
                                val currentUserUsername = documentSnapshot.getString("username")
                                if (currentUserUsername == newUsername) {
                                    updateProfile(newName, newUsername, newBio, newAddress)
                                } else {
                                    editUsername.error = "Username already exists, please choose another."
                                }
                            }
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "Failed to validate username", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        } else {
            updateProfile(newName, newUsername, newBio, newAddress)
        }
    }

    private fun updateProfile(name: String, username: String, bio: String, address: String) {
        val profileUpdates = hashMapOf(
            "name" to name,
            "username" to username,
            "biography" to bio,
            "address" to address
        )

        firestore.collection("users").document(userId)
            .set(profileUpdates, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
            }
    }

    private fun validateUsername(username: String, callback: (Boolean) -> Unit) {
        firestore.collection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { documents ->
                callback(documents.isEmpty)
            }
            .addOnFailureListener { exception ->
                callback(false) // Assume it's not unique on failure
            }
    }

    private fun logoutUser() {
        firebaseAuth.signOut()
        startActivity(Intent(this, SplashScreenActivity::class.java))
        finish()
    }
}
