package com.example.carlo.findrepo

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApi {

    @GET("search/repositories?")
    fun getRepo(@Query("q") q: String) : Call<SearchRepo>

}

class SearchRepo(val total_count : Int, val items : List<RepoItems>)

class RepoItems(val full_name : String, val homepage : String, val owner : RepoOwner, val html_url : String)

class RepoOwner(val avatar_url : String)

class RepoRetriever {
    val service : GitHubApi

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        service = retrofit.create(GitHubApi::class.java)
    }

    fun getRepo(callback: Callback<SearchRepo>, searchTerm : String) {
        val q = searchTerm
        val call = service.getRepo(q)
        call.enqueue(callback)
    }

}