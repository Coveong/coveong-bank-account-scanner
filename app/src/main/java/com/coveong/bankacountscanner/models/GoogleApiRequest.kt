package com.coveong.bankacountscanner.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class GoogleApiRequest @JsonCreator constructor(
    @JsonProperty("requests") var requests: List<Request>? = null
)
