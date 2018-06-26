package com.weweb.core.web;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ErrorDetails {
    private int code;
    private String message;
    private Map<String, List<String>> details;
}
