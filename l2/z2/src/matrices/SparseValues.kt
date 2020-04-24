package matrices

class SparseValues {
    companion object {
        public val available = arrayOf(0, 32, 64, 128, 160, 192, 223, 255).map { it.toShort() }
    }
}