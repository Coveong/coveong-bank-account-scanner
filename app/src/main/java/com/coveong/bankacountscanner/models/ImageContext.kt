package com.coveong.bankacountscanner.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ImageContext @JsonCreator constructor(
    @JsonProperty("languageHints") var languageHints: List<String>? = null
)
