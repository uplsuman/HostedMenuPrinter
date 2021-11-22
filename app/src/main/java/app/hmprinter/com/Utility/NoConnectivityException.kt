package app.hmprinter.com.Utility

import app.hmprinter.com.Constants.AppConstant
import java.io.IOException


class NoConnectivityException : IOException() {

    override val message: String
        get() = AppConstant.NO_INTERNET_CONNECTIVITY

}