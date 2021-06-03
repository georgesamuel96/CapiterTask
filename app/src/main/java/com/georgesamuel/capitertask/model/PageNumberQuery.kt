package com.georgesamuel.capitertask.model

class PageNumberQuery(
    var page: Int = 1
) {
    override fun toString(): String {
        return "{\"page\":$page}"
    }
}