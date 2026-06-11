package com.ai.calendar.demo.agent.model

data class GenerationConfig(
    val contextLength: Int = DEFAULT_CONTEXT_LENGTH,
    val maxTokens: Int = DEFAULT_MAX_TOKENS,
    val numThreads: Int = DEFAULT_NUM_THREADS,
    val temperature: Float = DEFAULT_TEMPERATURE,
    val topP: Float = DEFAULT_TOP_P,
    val topK: Int = DEFAULT_TOP_K,
    val repeatPenalty: Float = DEFAULT_REPEAT_PENALTY,
    val batchSize: Int = DEFAULT_BATCH_SIZE,
    val gpuLayers: Int = 0,
    val useMmap: Boolean = true,
    val flashAttention: Boolean = false,
) {
    companion object {
        private const val DEFAULT_CONTEXT_LENGTH = 4096
        private const val DEFAULT_MAX_TOKENS = 128
        private const val DEFAULT_NUM_THREADS = 4
        private const val DEFAULT_TEMPERATURE = 0.1f
        private const val DEFAULT_TOP_P = 0.9f
        private const val DEFAULT_TOP_K = 40
        private const val DEFAULT_REPEAT_PENALTY = 1.3f
        private const val DEFAULT_BATCH_SIZE = 512
    }
}
