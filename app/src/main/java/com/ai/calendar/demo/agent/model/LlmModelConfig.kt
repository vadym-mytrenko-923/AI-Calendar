package com.ai.calendar.demo.agent.model

enum class LlmModelConfig(
    val fileName: String,
    val downloadUrl: String,
) {
    HAMMER_2_1_0_5B(
        fileName = "hammer2.1-0.5b-q4_k_m.gguf",
        downloadUrl = "https://huggingface.co/mradermacher/Hammer2.1-0.5b-GGUF/resolve/main/Hammer2.1-0.5b.Q4_K_M.gguf",
    ),
    HAMMER_2_1_1_5B(
        fileName = "hammer2.1-1.5b-q4_k_m.gguf",
        downloadUrl = "https://huggingface.co/mradermacher/Hammer2.1-1.5b-GGUF/resolve/main/Hammer2.1-1.5b.Q4_K_M.gguf",
    ),
}
