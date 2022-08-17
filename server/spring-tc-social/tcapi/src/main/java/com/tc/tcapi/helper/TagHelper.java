package com.tc.tcapi.helper;

import com.tc.core.request.BaseParamRequest;
import com.tc.core.response.BaseResponse;
import com.tc.core.response.BaseTagReponse;
import com.tc.core.utilities.ValidationUtil;
import com.tc.tcapi.service.TagService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TagHelper {

    private final TagService service;
    private final ModelMapper modelMapper;

    public ResponseEntity<?> getTagsByName(String name) {
        Set<BaseTagReponse> data = service
                .getTagsByName(name).stream()
                .map(t -> modelMapper.map(t, BaseTagReponse.class))
                .collect(Collectors.toSet());
        return BaseResponse.success(data, "Get tags by name: " + name + " success");
    }

    public ResponseEntity<?> getTags(Map<String, String> param) {
        BaseParamRequest baseParamRequest = new BaseParamRequest(param);
        String name = null;
        String paramName = param.get("name");
        if(!ValidationUtil.isNullOrBlank(paramName)){
            name ="%"+ paramName +"%";
        }
        Set<BaseTagReponse> data = service
                .getTags(name,baseParamRequest.toPageRequest())
                .stream()
                .map(t -> modelMapper.map(t, BaseTagReponse.class))
                .collect(Collectors.toSet());
        return BaseResponse.success(data, "Get tags success");
    }

}
