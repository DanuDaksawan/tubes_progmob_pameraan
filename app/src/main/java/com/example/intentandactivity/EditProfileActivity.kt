package com.example.myapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class EditProfileActivity : AppCompatActivity() {

    private lateinit var profileImage: ImageView
    private lateinit var changePhotoButton: Button
    private lateinit var editName: EditText
    private lateinit var editUsername: EditText
    private lateinit var editBio: EditText
    private lateinit var editLocation: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        profileImage = findViewById(R.id.profile_image)
        changePhotoButton = findViewById(R.id.change_photo_button)
        editName = findViewById(R.id.edit_name)
        editUsername = findViewById(R.id.edit_username)
        editBio = findViewById(R.id.edit_bio)
        editLocation = findViewById(R.id.edit_location)
        saveButton = findViewById(R.id.save_button)

        // Load current profile data
        loadProfileData()

        changePhotoButton.setOnClickListener {
            // Handle change photo action
            changeProfilePhoto()
        }

        saveButton.setOnClickListener {
            saveProfileData()
        }
    }

    private fun loadProfileData() {
        // Load the current profile data from the intent or from a data source
        val intent = intent
        val name = intent.getStringExtra("name")
        val username = intent.getStringExtra("username")
        val bio = intent.getStringExtra("bio")
        val location = intent.getStringExtra("location")

        editName.setText(name)
        editUsername.setText(username)
        editBio.setText(bio)
        editLocation.setText(location)
    }

    private fun changeProfilePhoto() {
        // Implement logic to change the profile photo
        // This could involve launching a photo picker or capturing a new photo
    }

    private fun saveProfileData() {
        // Save the updated profile data
        val updatedName = editName.text.toString()
        val updatedUsername = editUsername.text.toString()
        val updatedBio = editBio.text.toString()
        val updatedLocation = editLocation.text.toString()

        // Return the updated data to the calling activity
        val resultIntent = Intent()
        resultIntent.putExtra("updated_name", updatedName)
        resultIntent.putExtra("updated_username", updatedUsername)
        resultIntent.putExtra("updated_bio", updatedBio)
        resultIntent.putExtra("updated_location", updatedLocation)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}
