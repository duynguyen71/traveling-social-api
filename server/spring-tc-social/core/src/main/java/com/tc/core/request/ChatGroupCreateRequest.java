package com.tc.core.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatGroupCreateRequest  {

    private String name;

    //list member ids in group
    //not required current user id
    private List<Long> userIds = new LinkedList<>();

    private List<String> names = new LinkedList<>();
}
