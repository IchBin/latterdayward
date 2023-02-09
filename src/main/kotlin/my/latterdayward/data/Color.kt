package my.latterdayward.data

enum class Color(val css: String) {
    PRIMARY("blue"),
    SECONDARY("orange"),
    INFO("blue"),
    SUCCESS("green"),
    WARN("amber"),
    DARK("slate"),
    DANGER("red");
}

class Colors {
    fun nextColor(i: Int): String {
        val colors = Color.values()
        var ordinal = i + 1
        ordinal = ++ordinal % colors.size
        return colors[ordinal].css
    }
}