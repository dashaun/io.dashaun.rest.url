package io.dashaun.rest.url.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class Target implements Serializable {
    private String longUrl;
}