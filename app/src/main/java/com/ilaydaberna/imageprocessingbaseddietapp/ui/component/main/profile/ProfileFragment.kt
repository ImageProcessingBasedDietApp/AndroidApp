package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.profile

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ilaydaberna.imageprocessingbaseddietapp.R
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*


class ProfileFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        view.iv_profile_photo.setOnClickListener {
            selectImage()
        }
        
        return view
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == RESULT_OK && data != null) {
                    val selectedImage = data.extras!!["data"] as Bitmap?
                    iv_profile_photo.setImageBitmap(selectedImage)
                    //TODO: firebase kaydet
                }
                1 -> if (resultCode == RESULT_OK && data != null) {
                    val selectedImage: Uri? = data.data
                    if (selectedImage != null) {
                        iv_profile_photo.setImageURI(selectedImage)
                        //TODO: firebase kaydet
                    }
                }
            }
        }
    }

    private fun selectImage() {
        val options = arrayOf<CharSequence>("Fotoğraf Çek", "Galeriden Seç")
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            if (options[item] == "Fotoğraf Çek") {
                val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePicture, 0)
            } else if (options[item] == "Galeriden Seç") {
                val pickPhoto =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickPhoto, 1)
            }
        })
        builder.show()
    }


}
