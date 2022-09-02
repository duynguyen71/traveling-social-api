package com.tc.core.constant;


public enum NotificationEntityType {

    CREATION_POST(0),
    CREATION_REVIEW(1),
    ADD_COMMENT(2),
    REPLY_COMMENT(3),
    SENT_MESSAGE(4),
    REPLY_MESSAGE(5);

    private int num;

    NotificationEntityType(int num) {
        this.num = num;
    }

    public int getNum() {
        return this.num;
    }
}
