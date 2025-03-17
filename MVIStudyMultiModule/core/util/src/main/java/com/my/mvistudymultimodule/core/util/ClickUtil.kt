package com.my.mvistudymultimodule.core.util

import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

object ClickUtil {

    /**
     * 단일 클릭 보장
     *
     * @param onClicked
     * @param debounceTime 해당 시간 동안 재클릭 불가
     * @return
     */
    fun View.onSingleClickWithDebounce(
        onClicked: (() -> Unit)? = null,
        debounceTime: Long
    ): Flow<Unit> = callbackFlow {
        var isClicked = false

        setOnClickListener { v ->
            if (isClicked.not()) {
                isClicked = true
                onClicked?.invoke()
                trySend(Unit)

                v.postDelayed({
                    isClicked = false
                }, debounceTime)
            }
        }

        awaitClose {
            setOnClickListener(null)
        }
    }

    /**
     * 재클릭 시 이전 이벤트 취소
     *
     * @param onClicked
     * @param scope
     * @return
     */
    fun View.onSingleClickWithCancel(
        onClicked: (suspend () -> Unit)? = null,
        scope: CoroutineScope
    ): Flow<Unit> = callbackFlow {
        var clickJob: Job? = null

        setOnClickListener { v ->
            clickJob?.cancel()
            clickJob = scope.launch(Dispatchers.Main) {
                onClicked?.invoke()
            }

            trySend(Unit)
        }

        awaitClose {
            setOnClickListener(null)
            clickJob?.cancel()
        }
    }
}