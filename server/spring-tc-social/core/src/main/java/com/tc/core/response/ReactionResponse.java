package com.tc.core.response;

import com.tc.core.enumm.EReactionType;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class ReactionResponse {

    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private EReactionType type;

    private String name;
}
