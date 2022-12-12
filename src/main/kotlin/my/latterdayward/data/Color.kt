package my.latterdayward.data

enum class Color(val css: String) {
    PRIMARY("primary"),
    SECONDARY("secondary"),
    INFO("info"),
    SUCCESS("success"),
    WARN("warning"),
    DARK("dark"),
    DANGER("danger");
}

class Colors {
    fun nextColor(i: Int): String {
        val colors = Color.values()
        var ordinal = i + 1
        ordinal = ++ordinal % colors.size
        return colors[ordinal].css
    }
}