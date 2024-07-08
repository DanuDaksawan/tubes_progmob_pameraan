package com.example.intentandactivity.model

data class Karya(
    var id: String? = null,
    val description: String = "",
    val price: Int = 0,
    val title: String = "",
    val url: String? = null,
    val userId: String = "",
) {
    override fun toString(): String {
        return "Karya(id='$id',url='$url', title='$title', description='$description', userId='$userId', price=$price)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as Karya

        if (id != other.id) return false
        if (url != other.url) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (userId != other.userId) return false
        if (price != other.price) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + userId.hashCode()
        result = 31 * result + price.hashCode()
        return result
    }

    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to (id ?: ""),
            "url" to (url?: ""),
            "title" to title,
            "description" to description,
            "userId" to userId,
            "price" to price
        )
    }
}

