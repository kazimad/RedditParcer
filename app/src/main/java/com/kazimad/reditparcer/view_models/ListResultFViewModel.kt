package com.kazimad.reditparcer.view_models

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.text.TextUtils
import com.kazimad.reditparcer.extentions.getApiProvider
import com.kazimad.reditparcer.extentions.getPrefs
import com.kazimad.reditparcer.models.error.InnerError
import com.kazimad.reditparcer.models.response.ChildrenItem
import com.kazimad.reditparcer.models.response.TopResponse
import com.kazimad.reditparcer.remote.ApiHelper
import com.kazimad.reditparcer.remote.ApiProvider
import com.kazimad.reditparcer.tools.Logger
import com.kazimad.reditparcer.tools.PREFS_DEVICE_ID
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.*
import kotlin.collections.ArrayList


class ListResultFViewModel : ViewModel() {
    var topLiveData: MutableLiveData<ArrayList<ChildrenItem>?> = MutableLiveData()
    var errorLiveData: MutableLiveData<InnerError> = MutableLiveData()
    var loadedChildrenItems = ArrayList<ChildrenItem>()
    var lastPosition: Int = 0
    //TODO clean
//    fun callAuthorization() {
//        var disposable = getApiProvider().create(ApiProvider.baseUrl).authorize(Utils.getResString(R.string.reddit_app_id),
//                "token",
//                Utils.generateRandomString(),
//                Utils.getResString(R.string.reddit_app_redirect_uri),
//                "read")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ result -> Log.d("myLog", "result token is ${result.body()?.accessToken}") },
//                        { error ->
//                            errorLiveData.value = InnerError(error.localizedMessage)
//                            error.printStackTrace()
//                        })
//    }

    fun callListResults(after: String? = null, lastVisilePosition: Int = 0, limit: Int = 10) {
        lastPosition = lastVisilePosition
        var disposable = getApiProvider().create(ApiProvider.baseUrl).getList(after, limit)
                .filter(ApiHelper.baseApiFilterPredicate(TopResponse::class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    topLiveData.value = result.body()!!.data.children as ArrayList<ChildrenItem>
                }, { error ->
                    error.printStackTrace()
                    val innerError = InnerError()
                    innerError.errorMessage = error?.localizedMessage
                    innerError.throwable = error?.cause
                    errorLiveData.value = innerError
                    Logger.log("ListResultFViewModel errorLiveData.value ${innerError.cause}")
                })
    }

    private fun createRequestBodyForAuth(): RequestBody {
        return MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("device_id", workWithDeviceId())
                .addFormDataPart("grant_type", "https://oauth.reddit.com/grants/installed_client")
                .build()
    }

    private fun workWithDeviceId(): String {
        if (TextUtils.isEmpty(getPrefs().getString(PREFS_DEVICE_ID))) {
            val uuid = UUID.randomUUID().toString()
            getPrefs().save(PREFS_DEVICE_ID, uuid)
            return uuid
        }
        return getPrefs().getString(PREFS_DEVICE_ID)!!
    }
}