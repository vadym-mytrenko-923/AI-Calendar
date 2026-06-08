package com.ai.calendar.demo.agent.model

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

private const val MODELS_DIR = "models"
private const val MODEL_ASSET = "models/gemma3-1b-it-int4.litertlm"

@Singleton
class LocalModelManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val _modelState = MutableStateFlow<ModelState>(ModelState.NotReady)
    val modelState: StateFlow<ModelState> = _modelState

    fun getModelPath(): String? {
        val modelsDir = File(context.filesDir, MODELS_DIR)
        val modelFile = modelsDir.listFiles()?.firstOrNull {
            it.extension in listOf("litertlm", "bin", "tflite")
        }
        return modelFile?.absolutePath
    }

    suspend fun prepareModel() {
        if (_modelState.value is ModelState.Ready) return
        _modelState.value = ModelState.Loading
        withContext(Dispatchers.IO) {
            Timber.d("Preparing model...")
            val path = getModelPath() ?: copyAssetModel()
            _modelState.value = if (path != null) {
                Timber.d("Model ready at: $path")
                ModelState.Ready(path)
            } else {
                ModelState.Error("Failed to prepare model")
            }
        }
    }

    private fun copyAssetModel(): String? {
        val fileName = MODEL_ASSET.substringAfterLast('/')
        val outDir = File(context.filesDir, MODELS_DIR)
        val outFile = File(outDir, fileName)

        if (outFile.exists() && outFile.length() > 0L) return outFile.absolutePath

        return try {
            outDir.mkdirs()
            context.assets.open(MODEL_ASSET).use { input ->
                outFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            Timber.d("Model copied from assets to: ${outFile.absolutePath}")
            outFile.absolutePath
        } catch (e: Exception) {
            Timber.e(e, "Failed to copy model from assets")
            null
        }
    }

    sealed interface ModelState {
        data object NotReady : ModelState
        data object Loading : ModelState
        data class Ready(val path: String) : ModelState
        data class Error(val message: String) : ModelState
    }
}
