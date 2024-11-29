package com.example.teachmint

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onRepositorySelected: (String, String) -> Unit // Callback for navigating to Repo Details
) {
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var searchResults by remember { mutableStateOf<List<Repository>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    // Track current page for pagination
    var currentPage by remember { mutableStateOf(1) }
    val resultsPerPage = 10

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Search Bar
        BasicTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(56.dp)
                .background(Color.Gray.copy(alpha = 0.1f), shape = MaterialTheme.shapes.medium)
                .padding(16.dp),
            textStyle = MaterialTheme.typography.bodySmall.copy(color = Color.Black)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (searchQuery.text.isNotEmpty()) {
                    scope.launch {
                        isLoading = true
                        searchResults = try {
                            val response = RetrofitInstance.api.searchRepositories(searchQuery.text)
                            response.items.take(resultsPerPage)
                        } catch (e: Exception) {
                            emptyList()
                        }
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Results Section
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(searchResults.size) { index ->
                    val repo = searchResults[index]
                    RepoItem(repo = repo) {
                        onRepositorySelected(repo.owner.login, repo.name)
                    }
                }
            }
        }
    }
}

@Composable
fun RepoItem(
    repo: Repository,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = repo.name, style = MaterialTheme.typography.headlineMedium)
            Text(text = repo.description ?: "No description", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "⭐ ${repo.stargazers_count} | 🍴 ${repo.forks_count}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
