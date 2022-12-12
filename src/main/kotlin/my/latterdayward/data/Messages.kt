package my.latterdayward.data

class Messages {
    val successes = mutableListOf<Message>()
    val infos = mutableListOf<Message>()
    val warnings = mutableListOf<Message>()
    val errors = mutableListOf<Message>()

    val isEmpty get() =
        successes.isEmpty() && infos.isEmpty() && warnings.isEmpty() && errors.isEmpty()

    fun success(text: String): Messages {
        successes.add(Message(text = text))
        return this
    }

    fun info(text: String): Messages {
        infos.add(Message(text = text))
        return this
    }

    fun warning(text: String): Messages {
        warnings.add(Message(text = text))
        return this
    }

    fun error(text: String): Messages {
        errors.add(Message(text = text))
        return this
    }

    fun success(label: String, text: String): Messages {
        successes.add(Message(label, text))
        return this
    }

    fun info(label: String, text: String): Messages {
        infos.add(Message(label, text))
        return this
    }

    fun warning(label: String, text: String): Messages {
        warnings.add(Message(label, text))
        return this
    }

    fun error(label: String, text: String): Messages {
        errors.add(Message(label, text))
        return this
    }

    open class Message(val label: String? = null, val text: String)
}