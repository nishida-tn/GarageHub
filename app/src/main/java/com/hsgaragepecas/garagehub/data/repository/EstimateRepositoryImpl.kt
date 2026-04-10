package com.hsgaragepecas.garagehub.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.hsgaragepecas.garagehub.data.model.CreateDemandRequest
import com.hsgaragepecas.garagehub.data.model.CreateEstimateResponse
import com.hsgaragepecas.garagehub.data.model.EstimateDetailResponse
import com.hsgaragepecas.garagehub.data.model.EstimateListResponse
import com.hsgaragepecas.garagehub.data.model.EstimateUpdateRequest
import com.hsgaragepecas.garagehub.data.model.FotosIn
import com.hsgaragepecas.garagehub.data.model.TimeSuggestionResponse
import com.hsgaragepecas.garagehub.data.remote.EstimateService
import com.hsgaragepecas.garagehub.domain.repository.EstimateRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

/**
 * The implementation of the [EstimateRepository] interface.
 *
 * @param estimateService The service for the estimate API.
 */
class EstimateRepositoryImpl @Inject constructor(
    private val estimateService: EstimateService,
    private val json: Json,
    @param:ApplicationContext private val context: Context
) : EstimateRepository {

    override suspend fun getEstimates(
        status: String?,
        query: String?,
        page: Int,
        pageSize: Int
    ): EstimateListResponse {
        return estimateService.getEstimates(status, query, page, pageSize)
    }

    override suspend fun getEstimateDetail(estimateId: Int): EstimateDetailResponse {
        return estimateService.getEstimateDetail(estimateId)
    }

    override suspend fun createEstimate(
        request: EstimateUpdateRequest,
        photoUris: List<Uri>
    ): CreateEstimateResponse {
        val uploadedUrls = mutableListOf<String>()

        // 1. Upload photos first to get URLs
        if (photoUris.isNotEmpty()) {
            photoUris.forEach { uri ->
                val photoPart = compressAndSaveImage(uri)
                if (photoPart != null) {
                    try {
                        val uploadResponse = estimateService.uploadPhoto(photoPart)
                        uploadedUrls.add(uploadResponse.url)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        // 2. Prepare request with the first photo as mainPhoto if available
        val finalRequest = if (uploadedUrls.isNotEmpty()) {
            request.copy(mainPhoto = uploadedUrls.first())
        } else {
            request
        }

        // 3. Create the estimate with metadata (including mainPhoto)
        val response = estimateService.createEstimate(finalRequest)
        
        if (response.ok && response.id != null && uploadedUrls.isNotEmpty()) {
            val estimateId = response.id

            // 4. Link all uploaded URLs to the estimate
            try {
                estimateService.addEstimatePhotos(estimateId, FotosIn(uploadedUrls))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        return response
    }

    private fun compressAndSaveImage(uri: Uri): MultipartBody.Part? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val originalBitmap = BitmapFactory.decodeStream(inputStream) ?: return null
                
                // Define max dimensions for 1080p
                val maxWidth = 1920
                val maxHeight = 1080
                val width = originalBitmap.width
                val height = originalBitmap.height

                val ratio = width.toFloat() / height.toFloat()
                var newWidth = width
                var newHeight = height

                if (width > maxWidth || height > maxHeight) {
                    if (ratio > 1) {
                        newWidth = maxWidth
                        newHeight = (newWidth / ratio).toInt()
                    } else {
                        newHeight = maxHeight
                        newWidth = (newHeight * ratio).toInt()
                    }
                }

                val resizedBitmap = if (newWidth != width || newHeight != height) {
                    Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true)
                } else {
                    originalBitmap
                }

                val file = File(context.cacheDir, "img_${System.currentTimeMillis()}.jpg")
                FileOutputStream(file).use { out ->
                    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
                }

                if (resizedBitmap != originalBitmap) {
                    resizedBitmap.recycle()
                }
                originalBitmap.recycle()

                val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                // Use "file" as the part name for the uploadPhoto endpoint
                MultipartBody.Part.createFormData("file", file.name, requestFile)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun updateEstimate(
        estimateId: Int,
        request: EstimateUpdateRequest
    ): EstimateDetailResponse {
        return estimateService.updateEstimate(estimateId, request)
    }

    override suspend fun deleteEstimate(estimateId: Int): Boolean {
        return try {
            val response = estimateService.deleteEstimate(estimateId)
            response["ok"] == true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getTimeSuggestion(partName: String): TimeSuggestionResponse {
        return estimateService.getTimeSuggestion(partName)
    }

    override suspend fun generateOrders(estimateId: Int): Map<String, Any> {
        return estimateService.generateOrders(estimateId)
    }

    override suspend fun checkItemDeletion(estimateId: Int, itemId: Int): Map<String, Any> {
        return estimateService.checkItemDeletion(estimateId, itemId)
    }

    override suspend fun createDemand(request: CreateDemandRequest): Map<String, Any> {
        return estimateService.createDemand(request)
    }
}
