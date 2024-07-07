package com.example.intentandactivity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var namaprofil: TextView
    private lateinit var usernameprofil: TextView
    private lateinit var bioprofil: TextView
    private lateinit var alamatprofil: TextView
    private lateinit var buttonSetting: ImageView
    private lateinit var fotoprofil: ImageView
    private lateinit var recyclerViewKarya: RecyclerView
    private lateinit var karyaAdapter: KaryaAdapter
    private lateinit var belumAdaKaryaText: TextView // TextView untuk menampilkan pesan "Belum Ada Karya"

    private val karyaList: MutableList<Karya> = mutableListOf() // List untuk menyimpan data produk


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        namaprofil = view.findViewById(R.id.namaprofil)
        usernameprofil = view.findViewById(R.id.usernameprofil)
        bioprofil = view.findViewById(R.id.bioprofil)
        alamatprofil = view.findViewById(R.id.alamatprofil)
        buttonSetting = view.findViewById(R.id.buttonsetting)
        fotoprofil = view.findViewById(R.id.fotoprofil)
        recyclerViewKarya = view.findViewById(R.id.recyclerViewKarya1)
        belumAdaKaryaText = view.findViewById(R.id.belumAdaKaryaText)

        val numberOfColumns = 2
        val layoutManager = GridLayoutManager(requireContext(), numberOfColumns)
        recyclerViewKarya.layoutManager = layoutManager

        karyaAdapter = KaryaAdapter(karyaList) { karya ->
            val intent = Intent(requireContext(), DetailKaryaActivity::class.java)
            intent.putExtra("karyaId", karya.id)
            startActivity(intent)
        }
        recyclerViewKarya.adapter = karyaAdapter

        buttonSetting.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserData()
    }

    private fun getUserData() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let { currentUser ->
            val uid = currentUser.uid
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("users").document(uid)

            userRef.get().addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val name = document.getString("name")
                    val username = document.getString("username")
                    val bio = document.getString("biography") ?: ""
                    val address = document.getString("address") ?: ""
                    val profileUrl = document.getString("profile")

                    name?.let { namaprofil.text = it }
                    username?.let { usernameprofil.text = it }
                    bioprofil.text = bio
                    alamatprofil.text = address

                    profileUrl?.let {
                        Glide.with(requireContext())
                            .load(it)
                            .placeholder(R.drawable.default_profil)
                            .error(R.drawable.default_profil)
                            .into(fotoprofil)
                    } ?: run {
                        fotoprofil.setImageResource(R.drawable.default_profil)
                    }

                    fetchKarya()
                } else {
                    Toast.makeText(requireContext(), "Document does not exist", Toast.LENGTH_SHORT)
                        .show()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(
                    requireContext(),
                    "Failed to fetch user data: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchKarya() {
        val db = FirebaseFirestore.getInstance()
        val karyaRef = db.collection("products")
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            karyaRef.whereEqualTo("userId", userId).get().addOnSuccessListener { itemkarya ->
                val products = itemkarya.documents.mapNotNull { doc ->
                    val karya = doc.toObject(Karya::class.java)
                    karya?.id = doc.id
                    karya
                }

                karyaList.clear()
                karyaList.addAll(products)
                karyaAdapter.notifyDataSetChanged()

                checkForEmptyData()
            }.addOnFailureListener {
                checkForEmptyData()
            }
        } else {
            checkForEmptyData()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
//    private fun loadProducts(uid: String) {
//        val db = FirebaseFirestore.getInstance()
//        db.collection("products")
//            .whereEqualTo("userId", uid)
//            .get()
//            .addOnSuccessListener { querySnapshot ->
//                karyaList.clear()
//                if (querySnapshot.isEmpty) {
//                    recyclerViewKarya.visibility = View.GONE
//                    belumAdaKaryaText.visibility = View.VISIBLE
//                } else {
//                    recyclerViewKarya.visibility = View.VISIBLE
//                    belumAdaKaryaText.visibility = View.GONE
//
//                    for (document in querySnapshot.documents) {
//                        val productName = document.getString("title") ?: ""
//                        val productImageUrl = document.getString("url")
//
//                        karyaList.add(Karya(productName, url = ))
//                    }
//                    karyaAdapter.notifyDataSetChanged()
//                }
//            }
//            .addOnFailureListener { exception ->
//                Toast.makeText(requireContext(), "Failed to load products: ${exception.message}", Toast.LENGTH_SHORT).show()
//            }
//    }

    private fun checkForEmptyData() {
        if (karyaList.isEmpty()) {
            belumAdaKaryaText.visibility = View.VISIBLE
        } else {
            belumAdaKaryaText.visibility = View.GONE
        }
    }


//    companion object {
//        @JvmStatic
//        fun newInstance() = ProfileFragment()
//    }
}
