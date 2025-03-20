package com.my.mvistudymultimodule.core.util

import android.annotation.SuppressLint
import android.view.View
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
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

    // 일반 뷰에서 사용
    fun View.onSingleClick(
        interval: Long = 350L,
        action: (v: View) -> Unit
    ) {
        var isClicked = false

        setOnClickListener { v ->
            if (isClicked.not()) {
                isClicked = true
                v?.run {
                    postDelayed({
                        isClicked = false
                    }, interval)
                    action(v)
                }
            }
        }
    }

    // 컴포즈에서 사용
    @SuppressLint("ModifierFactoryUnreferencedReceiver")
    fun Modifier.onSingleClick(
        enabled: Boolean = true,
        onClickLabel: String? = null,
        role: Role? = null,
        onClick: () -> Unit
    ) = composed(
        inspectorInfo = debugInspectorInfo {
            name = "clickable"
            properties["enabled"] = enabled
            properties["onClickLabel"] = onClickLabel
            properties["role"] = role
            properties["onClick"] = onClick
        }
    ) {
        val multipleEventsCutter = remember { MultipleEventsCutter.get() }
        Modifier.clickable(
            enabled = enabled,
            onClickLabel = onClickLabel,
            onClick = { multipleEventsCutter.processEvent { onClick() } },
            role = role,
            indication = LocalIndication.current,
            interactionSource = remember { MutableInteractionSource() }
        )
    }

    /**
     *  컴포즈 전용 인터페이스
     *  [코드출처] : https://al-e-shevelev.medium.com/how-to-prevent-multiple-clicks-in-android-jetpack-compose-8e62224c9c5e
     **/
    internal interface MultipleEventsCutter {
        fun processEvent(event: () -> Unit)

        companion object
    }

    internal fun MultipleEventsCutter.Companion.get(): MultipleEventsCutter = MultipleEventsCutterImpl()

    private class MultipleEventsCutterImpl : MultipleEventsCutter {
        private val now: Long
            get() = System.currentTimeMillis()

        private var lastEventTimeMs: Long = 0

        override fun processEvent(event: () -> Unit) {
            if (now - lastEventTimeMs >= 350L) {
                event.invoke()
            }
            lastEventTimeMs = now
        }
    }
}