package com.tc.core.enumm;

public enum EMessageType {

    RAW_TEXT_MESSAGE(0),ATTACHMENT_MESSAGE(1),REPLY_MESSAGE(2);

    int i;

    EMessageType(int i){
        this.i = i;
    }


}
