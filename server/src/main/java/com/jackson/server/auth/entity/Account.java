package com.jackson.server.auth.entity;

import lombok.Data;

@Data
public class Account {
    private Long userId;
    private String passwordHash;
}