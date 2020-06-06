package map.enums

enum class FieldType(val value: Int) {
    INVALID(-1),
    VALID(0),
    WALL(1),
    TUNNEL_UD(2),
    TUNNEL_LR(3),
    AGENT(5),
    EXIT(8)
}