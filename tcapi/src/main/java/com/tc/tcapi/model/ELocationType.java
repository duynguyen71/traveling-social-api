package com.tc.tcapi.model;

import java.util.stream.Stream;

public enum ELocationType {

    PERSONAL_ADDRESS(1), POPULAR_PLACE(2);

    int i;

    ELocationType(int i) {
        this.i = i;
    }

    public int getI() {
        return this.i;
    }

    public static ELocationType of(int i) {
        return Stream.of(ELocationType.values())
                .filter(p -> p.getI() == i)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
