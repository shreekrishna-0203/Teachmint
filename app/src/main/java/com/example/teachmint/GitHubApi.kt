package com.example.teachmint

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {

    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String
    ): RepoSearchResponse

    @GET("repos/{owner}/{repo}")
    suspend fun getRepositoryDetails(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): RepositoryDetails

    @GET("repos/{owner}/{repo}/contributors")
    suspend fun getContributors(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): List<Contributor>

    @GET("users/{username}/repos")
    suspend fun getUserRepositories(
        @Path("username") username: String
    ): List<Repository>
}
