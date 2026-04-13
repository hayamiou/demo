package com.example.demo

fun cypher(char: Char, key: Int): Char {
    require(key >= 0) { "key must be >= 0" }
    val effectiveKey = key % 26
    val shifted = (char - 'A' + effectiveKey) % 26
    return 'A' + shifted
}