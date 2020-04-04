package com.gzeinnumer.chatappkt.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.gzeinnumer.chatappkt.R
import com.gzeinnumer.chatappkt.databinding.FragmentProfileBinding
import com.gzeinnumer.chatappkt.model.User
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
//todo 65 part 10
class ProfileFragment : Fragment() {

    //todo 67
    lateinit var binding: FragmentProfileBinding
    lateinit var reference: DatabaseReference
    var firebaseUser: FirebaseUser? = null

    //todo 72
    var storageReference: StorageReference? = null
    private val IMAGE_REQUEST = 1
    private var imageUri: Uri? = null
    private var uploadTask: StorageTask<*>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //todo 68
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    //todo 69
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Users_chat_app").child(firebaseUser!!.uid)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user: User = dataSnapshot.getValue(User::class.java)!!
                binding.username.text = user.username
                if (user.imageURL.equals("default")) {
                    binding.profileImage.setImageResource(R.mipmap.ic_launcher)
                } else {
                    Glide.with(view.context).load(user.imageURL)
                        .into(binding.profileImage)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        //todo 73
        storageReference = FirebaseStorage.getInstance().getReference("uploads_chat_app")

        binding.profileImage.setOnClickListener { openImage() }
    }


    //todo 74
    private fun openImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, IMAGE_REQUEST)
    }

    //todo 75
    private fun getFileExtention(uri: Uri): String? {
        val contentResolver = context!!.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    //todo 76
    private fun uploadImage() {
        val pd = ProgressDialog(context)
        pd.setMessage("Uploading")
        pd.show()
        if (imageUri != null) {
            Toast.makeText(context, getFileExtention(imageUri!!), Toast.LENGTH_SHORT).show()
            val fileReference = storageReference!!.child(
                System.currentTimeMillis().toString() + "-" + getFileExtention(imageUri!!)
            )
            uploadTask = fileReference.putFile(imageUri!!)
            (uploadTask as UploadTask).continueWithTask(Continuation<UploadTask.TaskSnapshot?, Task<Uri?>?> { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!
                }
                fileReference.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val mUri = downloadUri.toString()
                    reference = FirebaseDatabase.getInstance().getReference("Users_chat_app")
                        .child(firebaseUser!!.uid)
                    val map =
                        HashMap<String, Any>()
                    map["imageURL"] = mUri
                    reference.updateChildren(map)
                    pd.dismiss()
                } else {
                    Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show()
                    pd.dismiss()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                pd.dismiss()
            }
        } else {
            Toast.makeText(context, "Belum ada gambar yang dipilih!", Toast.LENGTH_SHORT).show()
        }
    }

    //todo 77
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            Toast.makeText(context, getFileExtention(imageUri!!), Toast.LENGTH_SHORT).show()
            if (uploadTask != null && uploadTask!!.isInProgress) {
                Toast.makeText(context, "Upload in progress", Toast.LENGTH_SHORT).show()
            } else {
                uploadImage()
            }
        }
        //todo 78 ubah setingan storage di firebase
        //rules_version = '2';
        //service firebase.storage {
        //  match /b/{bucket}/o {
        //    match /{allPaths=**} {
        //      allow read, write: if true;
        //    }
        //  }
        //}
    }

}
