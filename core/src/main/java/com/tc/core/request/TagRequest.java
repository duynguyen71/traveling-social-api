package com.tc.core.request;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TagRequest implements Serializable {

    private Long id;

    private String name;

    private int status;
}
