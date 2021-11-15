package gb.android.nasapi.domain.exceptions

enum class NoInternetConnectionReason {
    FLY_MODE,
    NO_CONNECTION
}

class NoInternetConnectionException (
    val code: NoInternetConnectionReason
): Exception()