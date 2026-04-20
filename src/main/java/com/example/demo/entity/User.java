package com.example.demo.entity;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String username;
    private String email;
    // Getter 和 Setter (或者使用 Lombok)
}