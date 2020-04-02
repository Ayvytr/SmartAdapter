package androidx.ayvytr.adapter

/**
 * @author EDZ
 */
object Internals {
    const val NO_GETTER: String = "Property does not have a getter"

    fun noGetter(): Nothing = throw Exception("Property does not have a getter")
}
