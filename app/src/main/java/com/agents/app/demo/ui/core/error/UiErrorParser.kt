package com.agents.app.demo.ui.core.error

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.agents.app.demo.R
import com.agents.app.demo.data.common.toObject
import com.agents.app.demo.ui.core.error.model.UiError
import com.agents.app.demo.ui.theme.AppIcons
import com.agents.app.demo.utils.NonTranslatableStringResource
import com.agents.app.demo.utils.StringResource
import com.agents.app.demo.utils.notNullOrEmpty
import retrofit2.HttpException
import timber.log.Timber
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class UiErrorParser @Inject constructor() {
    fun parse(error: Throwable?): UiError = when {
        error != null -> parseThrowable(error)
        else -> UiError(StringResource(R.string.errorGeneric))
    }

    private fun parseThrowable(error: Throwable?): UiError {
        val description = when (error) {
            is UnknownHostException, is ConnectException, is SocketException -> {
                return UiError(
                    title = StringResource(R.string.errorNetworkConnectionTitle),
                    message = StringResource(R.string.errorNetworkConnectionMessage),
                    icon = AppIcons.NoInternet
                )
            }

            is SocketTimeoutException -> StringResource(R.string.errorTimeout)
            is HttpException -> getHttpExceptionErrorMessage(error)
            is JsonSyntaxException -> StringResource(R.string.errorRequestGeneral)
            else -> NonTranslatableStringResource(error?.message ?: "Error")
        }.also {
            Timber.e("Error: $error")
        }

        return UiError(
            message = description,
            icon = R.drawable.ic_warning
        )
    }

    private fun getHttpExceptionErrorMessage(exception: HttpException): StringResource {
        val errorMessageByCode = parseNetworkErrorCause(exception.message(), exception.code())
        val errorMsg = exception.response()?.errorBody()?.string()?.toObject<ErrorBody>(Gson())?.message.notNullOrEmpty()?.let {
            NonTranslatableStringResource(it)
        } ?: errorMessageByCode
        return errorMsg
    }

    private fun parseNetworkErrorCause(errorMessage: String, errorCode: Int): StringResource = when (errorCode) {
        HttpURLConnection.HTTP_UNAUTHORIZED -> StringResource(R.string.errorUnauthorized)
        HttpURLConnection.HTTP_BAD_REQUEST, HttpURLConnection.HTTP_FORBIDDEN -> StringResource(
            R.string.errorServerCommunication
        )

        HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> StringResource(R.string.errorTimeout)
        HttpURLConnection.HTTP_INTERNAL_ERROR,
        HttpURLConnection.HTTP_BAD_GATEWAY,
        HttpURLConnection.HTTP_UNAVAILABLE -> StringResource(R.string.errorServerCommunication)

        else -> NonTranslatableStringResource(errorMessage)
    }
}

// TODO: change with the BE error body object structure and define the error codes where it should be parsed except HTTP_BAD_REQUEST in the getHttpExceptionErrorMessage method
internal data class ErrorBody(val code: Int?, val message: String?)

fun Throwable.isNetworkError(): Boolean = this is HttpException

fun Throwable.isNetworkConnectivityError(): Boolean = this is UnknownHostException || this is ConnectException || this is SocketException

fun Throwable.asNetworkError(): HttpException? = this as? HttpException
