package com.ai.calendar.demo.agent.model

import android.content.Context
import com.ai.calendar.demo.domain.base.logger.Logger
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "ModelManager"
private const val MODELS_DIR = "models"
private const val DOWNLOAD_BUFFER_SIZE = 8192
private const val PROGRESS_LOG_INTERVAL = 10

@Singleton
class LocalModelManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val logger: Logger,
) {
    private val _modelState = MutableStateFlow<ModelState>(ModelState.NotReady)
    val modelState: StateFlow<ModelState> = _modelState

    var activeConfig: LlmModelConfig = LlmModelConfig.HAMMER_2_1_1_5B
        private set

    fun getModelPath(): String? {
        val modelsDir = File(context.filesDir, MODELS_DIR)
        return modelsDir.listFiles()?.firstOrNull {
            it.extension in listOf("gguf", "bin")
        }?.absolutePath
    }

    suspend fun prepareModel() {
        if (_modelState.value is ModelState.Ready) return
        _modelState.value = ModelState.Loading
        withContext(Dispatchers.IO) {
            val path = getModelPath() ?: downloadModel(activeConfig)
            _modelState.value = if (path != null) {
                logger.log("$TAG: model ready at $path")
                ModelState.Ready(path)
            } else {
                ModelState.Error("Failed to prepare model")
            }
        }
    }

    @Suppress("TooGenericExceptionCaught", "NestedBlockDepth")
    private fun downloadModel(config: LlmModelConfig): String? {
        val outDir = File(context.filesDir, MODELS_DIR)
        outDir.mkdirs()
        val outFile = File(outDir, config.fileName)
        val tempFile = File(outDir, "${config.fileName}.tmp")

        if (outFile.exists() && outFile.length() > 0L) return outFile.absolutePath

        logger.log("$TAG: downloading ${config.name}...")
        _modelState.value = ModelState.Downloading(0)

        return try {
            val connection = URL(config.downloadUrl).openConnection() as HttpURLConnection
            connection.connectTimeout = 30_000
            connection.readTimeout = 30_000
            connection.connect()

            val totalBytes = connection.contentLength.toLong()
            var downloadedBytes = 0L
            var lastLoggedPercent = -1

            connection.inputStream.buffered().use { input ->
                tempFile.outputStream().buffered().use { output ->
                    val buffer = ByteArray(DOWNLOAD_BUFFER_SIZE)
                    var bytesRead: Int
                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        output.write(buffer, 0, bytesRead)
                        downloadedBytes += bytesRead
                        if (totalBytes > 0) {
                            val percent = (downloadedBytes * 100 / totalBytes).toInt()
                            if (percent / PROGRESS_LOG_INTERVAL > lastLoggedPercent / PROGRESS_LOG_INTERVAL) {
                                lastLoggedPercent = percent
                                _modelState.value = ModelState.Downloading(percent)
                            }
                        }
                    }
                }
            }

            tempFile.renameTo(outFile)
            logger.log("$TAG: download complete (%.1f MB)".format(outFile.length() / 1_048_576.0))
            outFile.absolutePath
        } catch (e: Exception) {
            logger.logException(e)
            tempFile.delete()
            null
        }
    }

    sealed interface ModelState {
        data object NotReady : ModelState
        data object Loading : ModelState
        data class Downloading(val progressPercent: Int) : ModelState
        data class Ready(val path: String) : ModelState
        data class Error(val message: String) : ModelState
    }
}
