package com.example.teachmint

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import coil.compose.rememberImagePainter


@Composable
fun RepoDetailsScreen(
    owner: String,
    repo: String
) {
    val scope = rememberCoroutineScope()
    var repoDetails by remember { mutableStateOf<RepositoryDetails?>(null) }
    var contributors by remember { mutableStateOf<List<Contributor>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                repoDetails = RetrofitInstance.api.getRepositoryDetails(owner, repo)
                contributors = RetrofitInstance.api.getContributors(owner, repo)
            } catch (e: Exception) {
                // Handle error
            }
            isLoading = false
        }
    }

    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else {
        Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
            repoDetails?.let { details ->
                Text(text = details.name, style = MaterialTheme.typography.bodyMedium)
                Text(text = details.description ?: "No description", style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Project Link:", style = MaterialTheme.typography.bodySmall)
                ClickableText(
                    text = AnnotatedString(details.owner.avatar_url),
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Blue),
                    onClick = {
                        val intent = Intent(context, WebViewActivity::class.java).apply {
                            putExtra("url", details.owner.avatar_url)
                        }
                        context.startActivity(intent)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Contributors:", style = MaterialTheme.typography.headlineMedium)
                LazyColumn {
                    items(contributors.size) { index ->
                        val contributor = contributors[index]
                        ContributorItem(contributor = contributor)
                    }
                }
            }
        }
    }
}

@Composable
fun ContributorItem(contributor: Contributor) {
    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        Image(
            painter = rememberImagePainter(contributor.avatar_url),
            contentDescription = "Contributor Avatar",
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = contributor.login, style = MaterialTheme.typography.bodyMedium)
    }
}
