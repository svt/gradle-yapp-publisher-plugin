package se.svt.oss.gradle.yapp.publishtarget

import java.net.URI

data class RepositoryConfiguration(val uri: URI, val name: String, val credential: RepositoryCredential)
data class RepositoryCredential(val name: String, val value: String)
