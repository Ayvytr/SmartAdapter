package androidx.ayvytr.rados.recyclerviewlibrary

data class Item(var value: String, val type: Int) {
    override fun toString(): String {
        return "Item(value='$value', type=$type)"
    }
}