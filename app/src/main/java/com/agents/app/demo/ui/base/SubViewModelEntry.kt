package com.agents.app.demo.ui.base

data class SubViewModelEntry<STATE, SvmState>(
    val subViewModel: BaseSubViewModel<SvmState, *, *>,
    val getSvmState: ((STATE) -> SvmState)? = null,
)
