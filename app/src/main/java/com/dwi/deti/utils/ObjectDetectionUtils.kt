package com.dwi.deti.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock
import android.util.Log
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.Rot90Op
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.detector.Detection
import org.tensorflow.lite.task.vision.detector.ObjectDetector
import java.lang.IllegalStateException

class ObjectDetectionUtils(
    var threshold: Float = 0.5f,
    var numThreads: Int = 2,
    var maxResults: Int = 1,
    val context: Context,
    val objectDetectorListener: DetectorListener?
) {
    private var objectDetector: ObjectDetector? = null

    init {
        setUpObjectDetector()
    }

    fun clearObjectDetector() {
        objectDetector = null
    }

    private fun setUpObjectDetector() {
        val optionsBuilder = ObjectDetector.ObjectDetectorOptions.builder()
            .setScoreThreshold(threshold)
            .setMaxResults(maxResults)
        val baseOptionBuilder = BaseOptions.builder().setNumThreads(numThreads)


        optionsBuilder.setBaseOptions(baseOptionBuilder.build())

        try {
            objectDetector = ObjectDetector.createFromFileAndOptions(context, "detiModel.tflite", optionsBuilder.build())
        } catch (e: IllegalStateException) {
            objectDetectorListener?.onError(e.message.toString())
            Log.d("ObjectDetectionUtils", e.message.toString())
        }
    }

    fun detect(image: Bitmap, imageRotation: Int) {
        if (objectDetector == null) {
            var inferenceTime = SystemClock.uptimeMillis()

            val imageProcessor = ImageProcessor.Builder()
                .add(Rot90Op(-imageRotation / 90))
                .build()

            val tensorImage = imageProcessor.process(TensorImage.fromBitmap(image))

            val results = objectDetector?.detect(tensorImage)
            inferenceTime = SystemClock.uptimeMillis() - inferenceTime

            objectDetectorListener?.onResults(
                results,
                inferenceTime,
                tensorImage.height,
                tensorImage.width
            )
        }
    }

    interface DetectorListener {
        fun onError(msg: String)
        fun onResults(
            results: MutableList<Detection>?,
            inferenceTime: Long,
            imageHeight: Int,
            imageWidth: Int,
        )
    }

}