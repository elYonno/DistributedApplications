package be.kuleuven.foodrestservice.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum MealType {

    @JsonProperty("vegan")
    VEGAN("vegan"),
    @JsonProperty("veggie")
    VEGGIE("veggie"),
    @JsonProperty("meat")
    MEAT("meat"),
    @JsonProperty("fish")
    FISH("fish");
    private final String value;

    MealType(String v) {
        value = v;
    }

    @Override
    public String toString() {
        return value;
    }

    public static MealType fromValue(String v) {
        for (MealType c: MealType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    @JsonCreator
    public static MealType create(String value) {
        return fromValue(value);
    }
}
