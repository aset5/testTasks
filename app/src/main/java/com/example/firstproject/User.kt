package com.example.firstproject

data class User(val name: String, val username: String, val password: String, val age: Int)

val users = listOf(
    User("Алия Касенова", "aliya", "1234", 8),
    User("Нурбол Еркебулан", "nurbek", "abcd", 8),
    User("Жанат Серик", "janat", "pass123", 8)
)
