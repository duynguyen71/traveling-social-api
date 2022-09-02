package com.tc.core.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ReactionRequest {

    @NotNull(message = "numericField: positive number value is required")
    @Min(value = 0, message = "numericField: positive number, min 0 is required")
    private Long postId;

    private Long reactionId;
}
