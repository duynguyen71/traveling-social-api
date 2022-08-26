package com.tc.tcapi.request;

import com.tc.core.utilities.ValidationUtil;
import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseParamRequest implements Serializable {

    private String sortBy = "create_date";

    private String direction = "DESC";

    private Integer page = 0;

    private Integer pageSize = 15;

    private Integer active = 1;

    private Integer status = 1;

    public BaseParamRequest(Map<String, String> paramsRequest) {
        String sortBy = paramsRequest.get("sortBy");
        String direction = paramsRequest.get("direction");
        String page = paramsRequest.get("page");
        String pageSize = paramsRequest.get("pageSize");
        String status = paramsRequest.get("status");
        String active = paramsRequest.get("active");
        if (!ValidationUtil.isNullOrBlank(sortBy))
            this.sortBy = sortBy.trim();
        if (!ValidationUtil.isNullOrBlank(direction))
            this.direction = direction.trim();
        if (ValidationUtil.isNumeric(page))
            this.page = Integer.valueOf(page);
        if (ValidationUtil.isNumeric(pageSize))
            this.pageSize = Integer.valueOf(pageSize);
        if (ValidationUtil.isNumeric(status))
            this.status = Integer.valueOf(status);
        if (ValidationUtil.isNumeric(active))
            this.active = Integer.valueOf(active);
    }

    public Pageable toPageRequest() {
        return PageRequest.of(this.page, this.pageSize, Sort.by(Sort.Direction.fromString(this.direction), this.sortBy));
    }

    public Pageable toNativePageRequest(String ...strings) {
        return PageRequest.of(this.page, this.pageSize, Sort.by(Sort.Direction.fromString("DESC"),strings));
    }

}
