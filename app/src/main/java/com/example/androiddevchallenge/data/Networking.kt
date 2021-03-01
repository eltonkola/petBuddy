/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.data

import android.util.Log
import com.example.androiddevchallenge.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Authenticator
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.IOException

data class AuthResponse(val access_token: String, val token_type: String, val expires_in: Int)

class AuthorizationRepository() {

    private var lastToken: String? = null

    fun getToken(): String {
        return lastToken ?: fetchFreshAccessToken()
    }

    fun fetchFreshAccessToken(): String {
        lastToken = refreshToken()
        return lastToken!!
    }

    private fun refreshToken(): String {
        val client = OkHttpClient()

        val formBody = FormBody.Builder()
            .add("grant_type", "client_credentials")
            .add("client_id", BuildConfig.CLIENT_ID)
            .add("client_secret", BuildConfig.CLIENT_SECRET)
            .build()
        val request = Request.Builder()
            .url("https://api.petfinder.com/v2/oauth2/token")
            .post(formBody)
            .build()

        return try {
            val response = client.newCall(request).execute()
            val responseString = response.body?.string()
            val convertedObject = Gson().fromJson(responseString, AuthResponse::class.java)
            Log.d("DemoClass", "access_token ${convertedObject.access_token}")
            convertedObject.access_token
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }
}

private val okhttp3.Response.retryCount: Int
    get() {
        var currentResponse = priorResponse
        var result = 0
        while (currentResponse != null) {
            result++
            currentResponse = currentResponse.priorResponse
        }
        return result
    }

fun Request.signWithToken(accessToken: String) =
    newBuilder()
        .header("Authorization", "Bearer $accessToken")
        .build()

class TokenRefreshAuthenticator(
    private val authorizationRepository: AuthorizationRepository
) : Authenticator {

    override fun authenticate(route: Route?, response: okhttp3.Response): Request? = when {
        response.retryCount > 2 -> null
        else -> response.createSignedRequest()
    }

    private fun okhttp3.Response.createSignedRequest(): Request? = try {
        val accessToken = authorizationRepository.fetchFreshAccessToken()
        request.signWithToken(accessToken)
    } catch (error: Throwable) {
        Log.e("DemoClass", "Failed to re-sign request", error)
        null
    }
}

class OAuthInterceptor(private val authorizationRepository: AuthorizationRepository) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        val accessToken = authorizationRepository.getToken()
        request = request.signWithToken(accessToken)

        return chain.proceed(request)
    }
}

class Oauth2Authenticator<T> {

    private val authorizationRepository = AuthorizationRepository()

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.HEADERS) })
        .addInterceptor(OAuthInterceptor(authorizationRepository))
        .authenticator(TokenRefreshAuthenticator(authorizationRepository))
        .build()

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.petfinder.com/v2/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun create(service: Class<T>): T {
        return retrofit.create(service)
    }
}

// https://www.petfinder.com/developers/v2/docs

interface DemoRemoteService {
    @GET("animals")
    suspend fun getAnimalsPage(
        @Query("location") location: String,
        @Query("type") type: String,
        @Query("page") pageNumber: Int,
        @Query("limit") limit: Int
    ): AnimalsResponse
}
