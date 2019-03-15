package com.gpetuhov.android.samplemlkitlabelimages

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.pawegio.kandroid.toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        labelButton.setOnClickListener { labelImage() }
        clearButton.setOnClickListener { resultTextView.text = "" }
    }

    private fun labelImage() {
        // Image for labeling is taken from:
        // https://blog.usejournal.com/flutter-for-android-developers-how-to-design-activity-ui-in-flutter-4bf7b0de1e48
        val bitmap = getBitmapFromAsset("photo.jpeg")

        // Since the image in this example is already correctly oriented,
        // we don't have to compensate camera rotation.

        if (bitmap != null) {
            val image = FirebaseVisionImage.fromBitmap(bitmap)

            // Use on-device image labeling
            val labeler = FirebaseVision.getInstance().onDeviceImageLabeler

            // We can set minimum confidence threshold like this
//            val options = FirebaseVisionOnDeviceImageLabelerOptions.Builder()
//                .setConfidenceThreshold(0.7f)
//                .build()
//            val labeler = FirebaseVision.getInstance().getOnDeviceImageLabeler(options)

            labeler.processImage(image)
                .addOnSuccessListener { labels ->
                    var text = "Labels: "

                    // Concatenate texts from all labels into one
                    for (label in labels) {
                        text += label.text + ", "
                    }

                    resultTextView.text = text
                }
                .addOnFailureListener { e ->
                    toast("Error labeling image")
                }
        }
    }

    private fun getBitmapFromAsset(filePath: String): Bitmap? {
        val inputStream: InputStream
        var bitmap: Bitmap? = null

        try {
            inputStream = assets.open(filePath)
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            toast("Error opening image file")
        }

        return bitmap
    }
}
