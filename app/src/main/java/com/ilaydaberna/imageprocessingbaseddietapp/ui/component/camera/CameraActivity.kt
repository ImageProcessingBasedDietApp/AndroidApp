
package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.camera

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseUser
import com.ilaydaberna.imageprocessingbaseddietapp.R
import com.ilaydaberna.imageprocessingbaseddietapp.ml.FoodClassificationModel
import com.ilaydaberna.imageprocessingbaseddietapp.ml.PlateModel
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.FirebaseSource
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.FirestoreSource
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.Food
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.FoodSingleton
import com.ilaydaberna.imageprocessingbaseddietapp.util.YuvToRgbConverter
import com.ilaydaberna.imageprocessingbaseddietapp.util.isAmountValid
import com.ilaydaberna.imageprocessingbaseddietapp.util.startHomeActivity
import kotlinx.android.synthetic.main.dialog_add_food_amount.view.*
import kotlinx.android.synthetic.main.dialog_add_food_amount.view.btn_dialog_amount_save
import kotlinx.android.synthetic.main.dialog_add_meal_type.view.*
import org.tensorflow.lite.support.image.TensorImage
import java.util.concurrent.Executors
import java.util.stream.Collectors

// Constants
private const val MAX_RESULT_DISPLAY = 3 // Maximum number of results displayed
private const val TAG = "TFL Classify" // Name for logging
private const val REQUEST_CODE_PERMISSIONS = 999 // Return code after asking for permission
 val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA) // permission needed

// Listener for the result of the ImageAnalyzer
typealias RecognitionListener = (recognition: List<Recognition>) -> Unit


class CameraActivity : AppCompatActivity() {

    private lateinit var preview: Preview // Preview use case, fast, responsive view of the camera
    private lateinit var imageAnalyzer: ImageAnalysis // Analysis use case, for running ML code
    private lateinit var camera: Camera
    private val cameraExecutor = Executors.newSingleThreadExecutor()
    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraProvider: ProcessCameraProvider
    private var imageBitmap: Bitmap? = null
    private var mealType: String = ""
    private var foodArray = ArrayList<String>()
    private val currentUser: FirebaseUser? = FirebaseSource().getAuth().currentUser


    // Views attachment
   /* private val resultRecyclerView by lazy {
        findViewById<RecyclerView>(R.id.recognitionResults) // Display the result of analysis
    }*/

    private  val layoutCamera by lazy {
        findViewById<LinearLayout>(R.id.layoutCamera)
    }

    private  val foodImage by lazy {
        findViewById<ImageView>(R.id.foodImage)
    }

    private val viewFinder by lazy {
        findViewById<PreviewView>(R.id.viewFinder) // Display the preview image from Camera
    }

    private val recognatedFoodList by lazy {
        findViewById<ConstraintLayout>(R.id.linearLayoutFoodButtons)
    }

    private val btnRecognatedFood1 by lazy {
        findViewById<Button>(R.id.btnRecognatedFood1)
    }

    private val btnRecognatedFood2 by lazy {
        findViewById<Button>(R.id.btnRecognatedFood2)
    }

    private val btnRecognatedFood3 by lazy {
        findViewById<Button>(R.id.btnRecognatedFood3)
    }

    val btnBackToCamera by lazy {
        findViewById<Button>(R.id.btnBackToCamera)
    }

    // Contains the recognition result. Since  it is a viewModel, it will survive screen rotations
    private val recogViewModel: RecognitionListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        //ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)


        // Request camera permissions
        if (allPermissionsGranted()) {
            layoutCamera.visibility = View.VISIBLE
            //viewFinder.visibility = View.VISIBLE
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        // Initialising the resultRecyclerView and its linked viewAdaptor
        val viewAdapter = RecognitionAdapter(this)
        //resultRecyclerView.adapter = viewAdapter

        // Disable recycler view animation to reduce flickering, otherwise items can move, fade in
        // and out as the list change
        //resultRecyclerView.itemAnimator = null

        // Attach an observer on the LiveData field of recognitionList
        // This will notify the recycler view to update every time when a new list is set on the
        // LiveData field of recognitionList.
        recogViewModel.recognitionList.observe(this,
                Observer {
                    viewAdapter.submitList(it)
                }
        )

        btnBackToCamera.setOnClickListener {
            Log.i("camera", "a")
            if (allPermissionsGranted()) {
                layoutCamera.visibility = View.VISIBLE
                //viewFinder.visibility = View.VISIBLE
                //resultRecyclerView.visibility = View.VISIBLE

                foodImage.visibility = View.GONE
                recognatedFoodList.visibility = View.GONE
                btnBackToCamera.visibility = View.GONE
                startCamera()
            } else {
                ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
                )
            }
        }

        btnRecognatedFood1.setOnClickListener {
            val selectedFood = getFoodByName(foodArray[0])
            updateUserMeal(selectedFood)
        }

        btnRecognatedFood2.setOnClickListener {
            val selectedFood = getFoodByName(foodArray[1])
            updateUserMeal(selectedFood)
        }

        btnRecognatedFood3.setOnClickListener {
            val selectedFood = getFoodByName(foodArray[2])
            updateUserMeal(selectedFood)
        }
    }

    private fun updateUserMeal(selectedFood: List<Food>) {
        var amount = ""
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_meal_type, null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        val values = arrayOf("Kahvaltı", "Öğle Yemeği", "Akşam Yemeği", "Ara Öğün")
        mDialogView.np_meal_type.setMinValue(0)
        mDialogView.np_meal_type.setMaxValue(values.size - 1)
        mDialogView.np_meal_type.setWrapSelectorWheel(false)
        mDialogView.np_meal_type.setDisplayedValues(values)

        mDialogView.btn_dialog_amount_save.setOnClickListener {
            var mealType = ""
            when (mDialogView.np_meal_type.value) { // 0 - 1 - 2 - 3
                0 -> mealType = "Breakfast"
                1 -> mealType = "Lunch"
                2 -> mealType = "Dinner"
                3 -> mealType = "Snacks"
            }
            mAlertDialog.dismiss()
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_food_amount, null)
            val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
            val mAlertDialog = mBuilder.show()

            mDialogView.tv_serving_type.text = selectedFood[0].servingType

            mDialogView.btn_dialog_amount_save.setOnClickListener {
                if (mDialogView.et_amount.text.isEmpty()) {
                    mDialogView.et_amount.setError("Bu alan boş bırakılamaz")
                } else if (mDialogView.et_amount.text.toString().isAmountValid()) {
                    amount = mDialogView.et_amount.text.toString()
                    FirestoreSource.getUserMeal(currentUser, mealType, successHandler = {
                        val thisMeal = it
                        val amountDouble = amount.toDouble()

                        thisMeal.contents?.add(mapOf("amount" to amount.toDouble().toString(), "foodID" to selectedFood[0].id.toString()))
                        thisMeal.totalCalorie = thisMeal.totalCalorie?.plus((selectedFood[0].calorie * amountDouble).toInt())
                        thisMeal.totalCarbohydrate = thisMeal.totalCarbohydrate?.plus((selectedFood[0].carbohydrate * amountDouble).toInt())
                        thisMeal.totalFat = thisMeal.totalFat?.plus((selectedFood[0].fat * amountDouble).toInt())
                        thisMeal.totalProtein = thisMeal.totalProtein?.plus((selectedFood[0].protein * amountDouble).toInt())

                        FirestoreSource.updateUserMealForToday(currentUser = currentUser,
                                userMeal = thisMeal,
                                mealType = mealType,
                                successHandler = {
                                    startHomeActivity()
                                },
                                failHandler = {

                                }
                        )
                    }, failHandler = {})
                } else {
                    mDialogView.et_amount.setError("Geçerli bir değer giriniz")
                }
                mAlertDialog.dismiss()
            }
        }
    }

    private fun getFoodByName(foodName: String): List<Food> {
        return FoodSingleton.food.get()?.stream()
                ?.filter { food ->
                    try {
                        foodName == food.fileName
                    } catch (e: java.lang.Exception) {
                        Log.i("", "")
                        false
                    }
                }
                ?.collect(Collectors.toList())?: listOf()
    }

    /**
     * Check all permissions are granted - use for Camera permission in this example.
     */
    private fun allPermissionsGranted(): Boolean = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
                baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * This gets called after the Camera permission pop up is shown.
     */
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                // Exit the app if permission is not granted
                // Best practice is to explain and offer a chance to re-request but this is out of
                // scope in this sample. More details:
                // https://developer.android.com/training/permissions/usage-notes
                Toast.makeText(
                        this,
                        getString(R.string.permission_deny_text),
                        Toast.LENGTH_SHORT
                ).show()
               // finish()
            }
        }
    }

    /**
     * Start the Camera which involves:
     *
     * 1. Initialising the preview use case
     * 2. Initialising the image analyser use case
     * 3. Attach both to the lifecycle of this activity
     * 4. Pipe the output of the preview object to the PreviewView on the screen
     */
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
        

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            cameraProvider = cameraProviderFuture.get()

            preview = Preview.Builder()
                    .build()

            imageAnalyzer = ImageAnalysis.Builder()
                    // This sets the ideal size for the image to be analyse, CameraX will choose the
                    // the most suitable resolution which may not be exactly the same or hold the same
                    // aspect ratio
                    .setTargetResolution(Size(224, 224))
                    // How the Image Analyser should pipe in input, 1. every frame but drop no frame, or
                    // 2. go to the latest frame and may drop some frame. The default is 2.
                    // STRATEGY_KEEP_ONLY_LATEST. The following line is optional, kept here for clarity
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also { analysisUseCase: ImageAnalysis ->
                        analysisUseCase.setAnalyzer(cameraExecutor, ImageAnalyzer(this) { items ->
                            // updating the list of recognised objects
                            recogViewModel.updateData(items)
                        })
                    }

            // Select camera, back is the default. If it is not available, choose front camera
            val cameraSelector =
                    if (cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA))
                        CameraSelector.DEFAULT_BACK_CAMERA else CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera - try to bind everything at once and CameraX will find
                // the best combination.
                camera = cameraProvider.bindToLifecycle(
                        this, cameraSelector, preview, imageAnalyzer
                )

                // Attach the preview to preview view, aka View Finder
                preview.setSurfaceProvider(viewFinder.surfaceProvider)
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private inner class ImageAnalyzer(ctx: Context, private val listener: RecognitionListener) :
        ImageAnalysis.Analyzer {

        // TODO 6. Optional GPU acceleration


        // TODO 1: Add class variable TensorFlow Lite Model

        private val plateModel = PlateModel.newInstance(ctx)
        private val foodModel = FoodClassificationModel.newInstance(ctx)

        override fun analyze(imageProxy: ImageProxy) {

            val items = mutableListOf<Recognition>()
            val foods = mutableListOf<Recognition>()

            // TODO 2: Convert Image to Bitmap then to TensorImage

            //If you want to image as an input, then use only bitmap here.
            val tfImage = TensorImage.fromBitmap(toBitmap(imageProxy))
            //var x = TensorImage.createFrom(tfImage, DataType.UINT8)
            imageBitmap = toBitmap(imageProxy)

            // TODO 3: Process the image using the trained model, sort and pick out the top results
            val outputs = plateModel.process(tfImage)
                .probabilityAsCategoryList.apply {
                    sortByDescending { it.score }
                }.take(MAX_RESULT_DISPLAY)

            Log.i("OUTPUTS: ", outputs.toString())

            //foodModel.close()

            //You can change the count of results from MAx_RESULT_DISPLAY

            // TODO 4: Converting the top probability items into a list of recognitions
            for(output in outputs){
                items.add(Recognition(output.label, output.score))
                if(output.label == "resized-plate" && output.score < 0.70){
                    runOnUiThread {
                        layoutCamera.setBackgroundColor(resources.getColor(R.color.colorError))
                    }
                }
                if(output.label == "resized-plate" && output.score > 0.70){
                    runOnUiThread {
                        layoutCamera.setBackgroundColor(resources.getColor(R.color.yellow))
                    }
                }
                if(output.label == "resized-plate" && output.score > 0.90){
                    runOnUiThread {
                        layoutCamera.setBackgroundColor(resources.getColor(R.color.green_200))
                    }
                }
                if(output.label == "resized-plate" && output.score > 0.95) {
                    runOnUiThread {
                        cameraProvider.unbindAll()
                        foodImage.setImageBitmap(imageBitmap)
                        //viewFinder.visibility = View.GONE
                        layoutCamera.visibility = View.GONE
                        //resultRecyclerView.visibility = View.GONE

                        foodImage.visibility = View.VISIBLE
                        recognatedFoodList.visibility = View.VISIBLE
                        btnBackToCamera.visibility = View.VISIBLE

                        val tfImage2 = TensorImage.fromBitmap(imageBitmap)
                        val foodOutputs = foodModel.process(tfImage2)
                                .probabilityAsCategoryList.apply{
                                sortByDescending { it.score }
                            }.take(MAX_RESULT_DISPLAY)
                        foodArray.clear()
                        for(foodOutput in foodOutputs){
                            foods.add(Recognition(foodOutput.label, foodOutput.score))
                            foodArray.add(foodOutput.label)
                        }
                        btnRecognatedFood1.setText(foods[0].toString())
                        btnRecognatedFood2.setText(foods[1].toString())
                        btnRecognatedFood3.setText(foods[2].toString())
                    }
                }
            }

            listener(items.toList())

            // Close the image,this tells CameraX to feed the next image to the analyzer
            imageProxy.close()
        }

        /**
         * Convert Image Proxy to Bitmap
         */
        private val yuvToRgbConverter = YuvToRgbConverter(ctx)
        private lateinit var bitmapBuffer: Bitmap
        private lateinit var rotationMatrix: Matrix

        @SuppressLint("UnsafeExperimentalUsageError")
        private fun toBitmap(imageProxy: ImageProxy): Bitmap? {

            val image = imageProxy.image ?: return null

            // Initialise Buffer
            if (!::bitmapBuffer.isInitialized) {
                // The image rotation and RGB image buffer are initialized only once
                Log.d(TAG, "Initalise toBitmap()")
                rotationMatrix = Matrix()
                rotationMatrix.postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())
                bitmapBuffer = Bitmap.createBitmap(
                        imageProxy.width, imageProxy.height, Bitmap.Config.ARGB_8888
                )
            }

            // Pass image to an image analyser
            yuvToRgbConverter.yuvToRgb(image, bitmapBuffer)

            // Create the Bitmap in the correct orientation
            return Bitmap.createBitmap(
                    bitmapBuffer,
                    0,
                    0,
                    bitmapBuffer.width,
                    bitmapBuffer.height,
                    rotationMatrix,
                    false
            )
        }
    }

    override fun onBackPressed() {
        this.startHomeActivity()
    }
}