package org.example.ada.Post

data class PostItem(
    val UserId: String,
    var imageUrl: String,
    val Article_head: String,
    val Article: String,
    val LikeNumber: String,
    val UserImage: String
    )