package map.enums

enum class FieldType(val value: Int) {
    INVALID(-1),
    VALID(0),
    WALL(1),
    AGENT(5),
    EXIT(8)
}