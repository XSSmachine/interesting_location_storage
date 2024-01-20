package org.unizd.rma.kovacevic.common


/**
 * Sealed class which is used to define closed set of subclasses
 * which in this case represents different states of application UI
 */
sealed class ScreenViewState<out T> {
    object Loading:ScreenViewState<Nothing>()
    data class Success<T>(val data:T):ScreenViewState<T>()
    data class Error(val message:String?):ScreenViewState<Nothing>()

}