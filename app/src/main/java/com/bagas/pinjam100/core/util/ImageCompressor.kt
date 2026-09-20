package com.bagas.pinjam100.core.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object ImageCompressor {

    /**
     * Kompresi file gambar dari URI atau File secara asinkron.
     *
     * @param context Context aplikasi
     * @param imageFile File gambar asli yang ingin dikompresi
     * @param maxDimension Dimensi maksimum (lebar/tinggi) dalam piksel. Default: 1280px
     * @param maxFileSizeKb Batas target ukuran file dalam KB. Default: 500 KB
     * @return File baru hasil kompresi di folder cache
     */
    suspend fun compress(
        context: Context,
        imageFile: File,
        maxDimension: Int = 1280,
        maxFileSizeKb: Int = 500
    ): File = withContext(Dispatchers.IO) {
        // 1. Dapatkan rotasi gambar dari metadata EXIF (mencegah gambar miring saat dikompresi)
        val rotation = getExifRotation(imageFile)

        // 2. Decode Bitmap dengan opsi pembatasan memori
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeFile(imageFile.absolutePath, options)

        // Calculate sample size
        options.inSampleSize = calculateInSampleSize(options, maxDimension, maxDimension)
        options.inJustDecodeBounds = false

        val decodedBitmap = BitmapFactory.decodeFile(imageFile.absolutePath, options)
            ?: return@withContext imageFile

        // 3. Putar bitmap jika ada rotasi EXIF
        val rotatedBitmap = if (rotation != 0) {
            rotateBitmap(decodedBitmap, rotation)
        } else {
            decodedBitmap
        }

        // 4. Resize skala resolusi
        val scaledBitmap = scaleBitmap(rotatedBitmap, maxDimension)

        // 5. Kompresi Kualitas JPEG
        var quality = 85
        val outputStream = ByteArrayOutputStream()

        do {
            outputStream.reset()
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            quality -= 5
        } while (outputStream.toByteArray().size / 1024 > maxFileSizeKb && quality >= 20)

        // 6. Simpan ke File Cache Sementara
        val compressedFile = File(context.cacheDir, "compressed_${System.currentTimeMillis()}.jpg")
        FileOutputStream(compressedFile).use { fos ->
            fos.write(outputStream.toByteArray())
            fos.flush()
        }

        // Clean up memory
        if (scaledBitmap != rotatedBitmap) rotatedBitmap.recycle()
        scaledBitmap.recycle()

        return@withContext compressedFile
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val (height: Int, width: Int) = options.outHeight to options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    private fun scaleBitmap(bitmap: Bitmap, maxDimension: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        if (width <= maxDimension && height <= maxDimension) return bitmap

        val ratio = width.toFloat() / height.toFloat()
        val newWidth: Int
        val newHeight: Int

        if (width > height) {
            newWidth = maxDimension
            newHeight = (maxDimension / ratio).toInt()
        } else {
            newHeight = maxDimension
            newWidth = (maxDimension * ratio).toInt()
        }

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    private fun getExifRotation(file: File): Int {
        return try {
            val exif = ExifInterface(file.absolutePath)
            when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
        } catch (e: Exception) {
            0
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees.toFloat()) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}