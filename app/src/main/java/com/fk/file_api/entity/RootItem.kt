package com.fk.file_api.entity

data class RootItem(
    val id: String,
    val isDir: Boolean,
    val modificationDate: String,
    val name: String
)
