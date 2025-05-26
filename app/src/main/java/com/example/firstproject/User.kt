package com.example.firstproject

data class User(val name: String, val username: String, val password: String)

val users = listOf(
    User("Алия Касенова", "aliya", "1234"),
    User("Нурбол Еркебулан", "nurbek", "abcd"),
    User("Жанат Серик", "janat", "pass123")
)
