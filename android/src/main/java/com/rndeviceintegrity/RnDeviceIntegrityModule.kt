package com.rndeviceintegrity

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.google.android.gms.tasks.Task
import com.google.android.play.core.integrity.IntegrityManagerFactory
import com.google.android.play.core.integrity.IntegrityTokenRequest
import com.google.android.play.core.integrity.IntegrityTokenResponse

class RnDeviceIntegrityModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  override fun getName(): String {
    return NAME
  }

  private fun generateNonce(): String? {
    val length = 50
    var nonce = ""
    val allowed = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    for (i in 0 until length) {
      nonce += allowed[Math.floor(Math.random() * allowed.length).toInt()].toString()
    }
    return nonce
  }

  @ReactMethod
  fun getDeviceToken(promise: Promise) {
    val nonce = generateNonce()

    // Create an instance of a manager.
    val integrityManager = IntegrityManagerFactory.create(reactApplicationContext)

    // Request the integrity token by providing a nonce.
    val integrityTokenResponse: Task<IntegrityTokenResponse> =
      integrityManager.requestIntegrityToken(
        IntegrityTokenRequest.builder()
          .setNonce(nonce)
          .build(),
      )

    integrityTokenResponse.addOnSuccessListener { response ->
      val integrityToken: String = response.token()
      promise.resolve(integrityToken)
    }

    // failed
    integrityTokenResponse.addOnFailureListener(promise::reject)
  }

  companion object {
    const val NAME = "RnDeviceIntegrity"
  }
}
