package com.ysfcyln.common

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
sealed class Resource<out T> {
    class Success<T>(val data: T) : Resource<T>()
    class Error(val exception : Exception) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
    object Empty : Resource<Nothing>()
}