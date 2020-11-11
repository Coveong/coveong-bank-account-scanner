package com.coveong.bankacountscanner.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Request @JsonCreator constructor(
    @JsonProperty("image") var image: ImageInfo? = null,
    @JsonProperty("features") var features: Feature? = null
)
