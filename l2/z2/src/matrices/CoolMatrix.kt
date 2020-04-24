package matrices

// this was supposed to be CoolMatrix<T> but we need doubles
// to calculate the distance :(
interface CoolMatrix {
    fun distance(other: CoolMatrix): Double
    fun getValue(row: Int, column: Int): Short
}