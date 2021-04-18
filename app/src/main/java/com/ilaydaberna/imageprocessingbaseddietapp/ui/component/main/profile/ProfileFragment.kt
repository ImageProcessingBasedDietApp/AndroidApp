package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.profile

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.ilaydaberna.imageprocessingbaseddietapp.R
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.FirestoreSource
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.User
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.UserInfo
import com.ilaydaberna.imageprocessingbaseddietapp.util.alertDialog
import com.ilaydaberna.imageprocessingbaseddietapp.util.isEmpty
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.io.ByteArrayOutputStream
import java.util.*


class ProfileFragment : Fragment(){

    var isEditGoals = false
    var isEditUserInfo = false
    var isEditSettings = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        view.iv_profile_photo.setOnClickListener {
            selectImage()
        }

        view.tv_edit_goals.setOnClickListener {
            if(!isEditGoals){
                view.layout_goals.visibility = View.GONE
                view.layout_edit_goals.visibility = View.VISIBLE
                isEditGoals = true
            }
            else{
                view.layout_goals.visibility = View.VISIBLE
                view.layout_edit_goals.visibility = View.GONE
                isEditGoals = false
            }
        }

        view.tv_edit_user_info.setOnClickListener {
            if(!isEditUserInfo){
                view.layout_user_info.visibility = View.GONE
                view.layout_edit_user_info.visibility = View.VISIBLE
                isEditUserInfo = true
            }
            else{
                view.layout_user_info.visibility = View.VISIBLE
                view.layout_edit_user_info.visibility = View.GONE
                isEditUserInfo = false
            }
        }

        view.tv_edit_settings.setOnClickListener{
            if(!isEditSettings){
                view.layout_settings.visibility = View.GONE
                view.layout_edit_settings.visibility = View.VISIBLE
                isEditSettings = true
            }
            else{
                view.layout_settings.visibility = View.VISIBLE
                view.layout_edit_settings.visibility = View.GONE
                isEditSettings = false
            }
        }


        view.et_birthdate.setOnClickListener {
            //TODO: DATEPİCKER EKLE
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog =
                context?.let { it1 ->
                    DatePickerDialog(it1, DatePickerDialog.OnDateSetListener{ mView, mYear, mMonth, mDay ->
                        view.et_birthdate.setText(""+ mDay +"/" +( mMonth+1) + "/"+ mYear)
                    }, year, month, day)
                }
            datePickerDialog?.show()

        }

        view.et_gender.setOnClickListener {
            val options = arrayOf<CharSequence>( "Kadın", "Erkek", "Belirtmek İstemiyorum")
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
                view.et_gender.setText(options[item].toString())
            })
            builder.show()
        }

        view.button_save_goals.setOnClickListener {
            if(!view.et_goal_coffee.isEmpty() && !view.et_goal_weight.isEmpty() && !view.et_goal_water.isEmpty() && !view.et_goal_tea.isEmpty() && !view.et_goal_step.isEmpty()){
                //TODO: firebase kaydet
                view.tv_goal_weight.setText(view.et_goal_weight.text.toString() + " kg")
                view.tv_goal_water.setText(view.et_goal_water.text.toString() + " bardak su")
                view.tv_goal_coffee.setText(view.et_goal_coffee.text.toString() + " bardak Türk Kahvesi")
                view.tv_goal_tea.setText(view.et_goal_tea.text.toString() + " bardak çay")
                view.tv_goal_step.setText(view.et_goal_step.text.toString() + " adım")
                view.layout_edit_goals.visibility = View.GONE
                view.layout_goals.visibility = View.VISIBLE
                isEditGoals = false
            }
            else{
                context?.let { it1 -> this.alertDialog(it1, "Hedeflerimi Kaydet", "Lütfen tüm boşlukları doldurun", "Tamam", null, null) }
            }

        }

        view.button_save_user_info.setOnClickListener {
            if(!view.et_height.isEmpty() && !view.et_weight.isEmpty() && !view.et_birthdate.isEmpty() && !view.et_gender.isEmpty()){
                //TODO: firebase kaydet
                view.tv_height.setText(view.et_height.text.toString() + " cm")
                view.tv_weight.setText(view.et_weight.text.toString() + " kg")
                view.tv_birthdate.setText(view.et_birthdate.text.toString())
                view.tv_gender.setText(view.et_gender.text.toString())
                view.layout_user_info.visibility = View.VISIBLE
                view.layout_edit_user_info.visibility = View.GONE
                isEditUserInfo = false
            }
            else{
                context?.let { it1 -> this.alertDialog(it1, "Bilgilerimi Kaydet", "Lütfen tüm boşlukları doldurun", "Tamam", null, null) }
            }

        }

        view.button_save_settings.setOnClickListener {
            //TODO: firebase kaydet
            if(sw_notification.isChecked){
                view.tv_notification.setText("Bildirimler açık")
            }
            else{
                view.tv_notification.setText("Bildirimler kapalı")
            }
            view.layout_settings.visibility = View.VISIBLE
            view.layout_edit_settings.visibility = View.GONE
            isEditSettings = false
        }


        return view
    }

    fun initViews(view: View){
        //TODO: Firebase'den gelenler ile doldur
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == RESULT_OK && data != null) {
                    val selectedImage = data.extras!!["data"] as Bitmap?
                    iv_profile_photo.setImageBitmap(selectedImage)
                    val bitmap = (iv_profile_photo.drawable as BitmapDrawable).bitmap
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()
                    FirestoreSource().uploadPhotoToStorageBitmap(data)
                }
                1 -> if (resultCode == RESULT_OK && data != null) {
                    val selectedImage: Uri? = data.data
                    if (selectedImage != null) {
                        iv_profile_photo.setImageURI(selectedImage)
                        FirestoreSource().uploadPhotoToStorageUri(selectedImage)
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
