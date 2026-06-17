package com.ktx.dormitory.presentation.features.access

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class FaceNetModel(context: Context) {
    private var interpreter: Interpreter? = null
    private val imgSize = 112

    init {
        try {
            val modelFile = loadModelFile(context, "mobile_face_net.tflite")
            val options = Interpreter.Options().apply {
                setNumThreads(4)
                setUseNNAPI(true) // Ưu tiên NPU nếu có
            }
            interpreter = Interpreter(modelFile, options)
        } catch (e: Exception) {
            android.util.Log.e("FaceNetModel", "Lỗi khởi tạo TFLite: ${e.message}")
            interpreter = null
        }
    }

    private fun loadModelFile(context: Context, modelPath: String): MappedByteBuffer {
        return try {
            val fileDescriptor = context.assets.openFd(modelPath)
            val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
            val fileChannel = inputStream.channel
            val startOffset = fileDescriptor.startOffset
            val declaredLength = fileDescriptor.declaredLength
            fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        } catch (e: Exception) {
            throw Exception("Không tìm thấy file model: $modelPath. Vui lòng kiểm tra thư mục assets.")
        }
    }

    fun getFaceEmbedding(faceBitmap: Bitmap): FloatArray {
        if (interpreter == null) {
            android.util.Log.e("FaceNetModel", "Interpreter chưa được khởi tạo!")
            return floatArrayOf()
        }

        return try {
            // BƯỚC 1: Tự resize ảnh bằng thư viện chuẩn của Android
            val resizedBitmap = Bitmap.createScaledBitmap(faceBitmap, imgSize, imgSize, true)

            // BƯỚC 2: Chuẩn hóa ảnh về khoảng [-1, 1]
            val imageProcessor = ImageProcessor.Builder()
                .add(NormalizeOp(127.5f, 127.5f))
                .build()

            var tensorImage = TensorImage(org.tensorflow.lite.DataType.FLOAT32)
            tensorImage.load(resizedBitmap)
            tensorImage = imageProcessor.process(tensorImage)

            // BƯỚC 3: Chạy Inference (Dự đoán)
            val output = Array(1) { FloatArray(192) }
            interpreter?.run(tensorImage.buffer, output)

            output[0]
        } catch (e: Exception) {
            android.util.Log.e("FaceNetModel", "Lỗi trong quá trình Inference: ${e.message}")
            floatArrayOf()
        }
    }
}
