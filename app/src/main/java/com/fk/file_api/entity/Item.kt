package com.fk.file_api.entity

data class Item(
    val contentType: String,
    val id: String,
    val isDir: Boolean,
    val modificationDate: String,
    val name: String,
    val parentId: String,
    val size: Long? = null
)
