package com.why.codelab.theming.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.preferredHeightIn
import androidx.compose.material.AmbientContentAlpha
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codelab.theming.R
import com.why.codelab.theming.data.Post
import com.why.codelab.theming.data.PostGatewayImp
import com.why.codelab.theming.theme.JetnewsTheme
import java.util.*

@Composable
fun PostMetadata(
    post: Post,
    modifier: Modifier = Modifier
) {
    val divider = "  •  "
    val tagDivider = "  "
    val text = buildAnnotatedString {
        append(post.metadata.date)
        append(divider)
        append(
            stringResource(R.string.read_time, post.metadata.readTimeMinutes))
        append(divider)
        post.tags.forEachIndexed { index, tag ->
            if (index != 0) append(tagDivider)
            append(" ${tag.toUpperCase(Locale.getDefault())} ")
        }
    }
    Providers(AmbientContentAlpha provides ContentAlpha.medium) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            modifier = modifier
        )
    }
}

@Composable
fun PostItem(
    post: Post,
    modifier: Modifier = Modifier
) {
    ListItem(
        modifier = modifier
            .clickable { /* todo */ }
            .padding(vertical = 8.dp),
        icon = {
            Image(
                bitmap = imageResource(post.imageThumbId),
            )
        },
        text = {
            Text(text = post.title)
        },
        secondaryText = {
            PostMetadata(post)
        }
    )
}

@Composable
fun FeaturedPost(
    post: Post,
    modifier: Modifier = Modifier
) {
    Card(modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* onClick */ }
        ) {
            Image(
                bitmap = imageResource(post.imageId),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .preferredHeightIn(min = 180.dp)
                    .fillMaxWidth()
            )
            Spacer(Modifier.preferredHeight(16.dp))

            val padding = Modifier.padding(horizontal = 16.dp)
            Text(
                text = post.title,
                modifier = padding
            )
            Text(
                text = post.metadata.author.name,
                modifier = padding
            )
            PostMetadata(post, padding)
            Spacer(Modifier.preferredHeight(16.dp))
        }
    }
}

@Composable
fun Header(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
        contentColor = MaterialTheme.colors.primary,
        modifier = modifier
    ) {
        Text(
            text = text,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun AppBar() {
    TopAppBar(
        navigationIcon = {
            Icon(Icons.Rounded.Palette, Modifier.padding(horizontal = 12.dp))
        },
        title = {
            Text(text = stringResource(R.string.app_title))
        },
        backgroundColor = MaterialTheme.colors.primarySurface
    )
}

@Composable
fun Home(isDarkTheme: Boolean = false) {
    val featured = remember { PostGatewayImp.getFeaturedPost() }
    val posts = remember { PostGatewayImp.getPosts() }

    JetnewsTheme(isDarkTheme = isDarkTheme) {
        Scaffold(
            topBar = { AppBar() }
        ) { innerPadding ->
            ScrollableColumn(contentPadding = innerPadding) {
                Header(stringResource(R.string.top))
                FeaturedPost(
                    post = featured,
                    modifier = Modifier.padding(16.dp)
                )
                Header(stringResource(R.string.popular))
                posts.forEach { post ->
                    PostItem(post = post)
                    Divider(startIndent = 72.dp)
                }
            }
        }
    }
}

@Preview("Post Item")
@Composable
private fun PostItemPreview() {
    val post = remember { PostGatewayImp.getFeaturedPost() }
    JetnewsTheme {
        Surface {
            PostItem(post = post)
        }
    }
}

@Preview("Featured Post")
@Composable
private fun FeaturedPostPreview() {
    val post = remember { PostGatewayImp.getFeaturedPost() }
    JetnewsTheme {
        FeaturedPost(post = post)
    }
}

@Preview("Home - Light Theme")
@Composable
private fun HomePreview() {
    JetnewsTheme {
        Home()
    }
}

@Preview("Home - Dark Theme")
@Composable
private fun HomeDarkPreview() {
    Home(isDarkTheme = true)
}
