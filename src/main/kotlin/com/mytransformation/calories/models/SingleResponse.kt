package com.mytransformation.calories.models

import com.fasterxml.jackson.annotation.JsonProperty

class SingleResponse {
    @JsonProperty("message")
    var message: String = ""
}
