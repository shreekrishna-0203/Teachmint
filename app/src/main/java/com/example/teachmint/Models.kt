package com.example.teachmint


data class RepoSearchResponse(
    val items: List<Repository>
)

data class Repository(
    val id: Long,
    val name: String,
    val full_name: String,
    val description: String?,
    val stargazers_count: Int,
    val forks_count: Int,
    val owner: Owner
)
data class Owner(
    val login: String,
    val avatar_url: String
)
data class RepositoryDetails(
    val name: String,
    val full_name: String,
    val description: String?,
    val stargazers_count: Int,
    val forks_count: Int,
    val open_issues_count: Int,
    val owner: Owner
)
data class Contributor(
    val login: String,
    val avatar_url: String,
    val contributions: Int
)
