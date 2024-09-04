package com.unq.crypto_exchange.api.controller.handler;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
public record ErrorBody(@JsonProperty("status") Integer status, @JsonProperty("message") String message) { }
