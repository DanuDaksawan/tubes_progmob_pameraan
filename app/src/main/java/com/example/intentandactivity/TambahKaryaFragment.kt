package com.example.intentandactivity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class TambahKaryaFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var uploadButton: Button
    private lateinit var selectImageButton: Button
    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var priceEditText: EditText
    private var imageUri: Uri? = null

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tambah_karya, container, false)

        imageView = view.findViewById(R.id.tambahgambar)
        uploadButton = view.findViewById(R.id.btnposting)
        selectImageButton = view.findViewById(R.id.selectImageButton)
        titleEditText = view.findViewById(R.id.judulkarya)
        descriptionEditText = view.findViewById(R.id.deskripsi)
        priceEditText = view.findViewById(R.id.harga)

        selectImageButton.setOnClickListener {
            selectImage()
        }

        uploadButton.setOnClickListener {
            uploadImageToFirebase()
        }

        return view
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }
    }

    private fun uploadImageToFirebase() {
        if (imageUri == null) {
            Toast.makeText(requireContext(), "Tolong masukkan gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }

        val title = titleEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val priceText = priceEditText.text.toString()
        val price = priceText.toLongOrNull()

        if (title.isEmpty() || description.isEmpty() || price == null) {
            Toast.makeText(requireContext(), "Tolong lengkapi data", Toast.LENGTH_SHORT).show()
            return
        }

        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val fileName = UUID.randomUUID().toString() + ".jpg"
        val storageRef = storage.reference.child("products/$fileName")

        storageRef.putFile(imageUri!!)
            .addOnSuccessListener { task ->
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    saveImageUrlToFirestore(imageUrl, currentUser.uid, title, description, price)
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Gagal upload gambar", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveImageUrlToFirestore(imageUrl: String, userId: String, title: String, description: String, price: Long) {
        val image = hashMapOf(
            "url" to imageUrl,
            "userId" to userId,
            "title" to title,
            "description" to description,
            "price" to price
        )

        db.collection("products")
            .add(image)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Karya berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), SuccessActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Karya gagal ditambahkan", Toast.LENGTH_SHORT).show()
            }
    }
}
