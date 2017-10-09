package de.eternalwings.awww.http

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

interface ImgurApi {
    @GET("gallery/r/{subreddit}/top/week")
    fun getTopImagesFromSubreddit(@Path("subreddit") subreddit: String): Call<SubredditApiResponse>

    @GET("gallery/r/{subreddit}/top/week/{page}")
    fun getTopImagesFromSubreddit(@Path("subreddit") subreddit: String, @Path("page")
    page: Int): Call<SubredditApiResponse>

    @GET("gallery/r/{subreddit}/top/{time}/{page}")
    fun getTopImagesFromSubreddit(@Path("subreddit") subreddit: String, @Path("page")
    page: Int, @Path("time") time: String): Call<SubredditApiResponse>

    companion object {
        val BASE_URL = "https://api.imgur.com/3/"

        fun create(retrofit: Retrofit): ImgurApi {
            return retrofit.create(ImgurApi::class.java)
        }
    }
}

data class SubredditApiResponse(var data: Collection<SubredditImageData>)

data class SubredditImageData(var link: String, var type: String, var id: String, var is_album: Boolean,
                              var mp4: String, var gifv: String, var size: Int, var nsfw: Boolean,
                              var animated: Boolean) {

    fun getWorkingLink(): String {
        return if (animated) gifv else link
    }

    fun isAcceptable(): Boolean {
        return !this.is_album && !this.nsfw && this.size > 0
    }
}
