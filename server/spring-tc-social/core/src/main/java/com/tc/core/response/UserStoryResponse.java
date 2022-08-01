package com.tc.core.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStoryResponse {

    private UserInfoResponse userInfo;

    private List<StoryInfoResponse> stories;

}
