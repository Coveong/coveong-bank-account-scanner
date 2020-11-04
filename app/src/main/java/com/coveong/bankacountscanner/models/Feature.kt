package com.example.coveong.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Feature @JsonCreator constructor(
    @JsonProperty("type") var type: String? = null,
    @JsonProperty("maxResults") var maxResults: Int? = null
)
