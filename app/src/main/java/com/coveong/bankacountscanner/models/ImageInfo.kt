package com.example.coveong.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ImageInfo @JsonCreator constructor(
    @JsonProperty("content") var content: String? = null
)
