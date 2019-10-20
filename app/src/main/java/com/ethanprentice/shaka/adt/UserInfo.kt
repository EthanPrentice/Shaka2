package com.ethanprentice.shaka.adt

import kotlinx.serialization.Serializable

/**
 * User specific information like Spotify display name, profile picture urls, etc.
 *
 * @author Ethan Prentice
 */
@Serializable
data class UserInfo(
        var displayName: String,
        var imageUrl: String?
)