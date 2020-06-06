package solver.enums

// haha so smart, turning right is +1 mod 4, left -1 mod 4
enum class Moves(val value: Int) {
    U(0),
    R(1),
    D(2),
    L(3)
}