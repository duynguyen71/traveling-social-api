package com.tc.core.request;

import com.tc.core.utilities.ValidationUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class UserSearchParam {

    private String username = null;

    private String fullName = null;

    private String phone = null;

    private Integer page = 0;

    private Integer pageSize = 15;

    private String sortBy = "create_date";

    private String direction = "DESC";

    public UserSearchParam(Map<String, String> map) {
        String pUsername = map.get("username");
        if (!ValidationUtil.isNullOrBlank(pUsername))
            this.username = "%" + pUsername.trim() + "%";

        String pFullName = map.get("fullName");
        if (!ValidationUtil.isNullOrBlank(pFullName))
            this.fullName = "%" + pFullName.trim() + "%";

        String phone = map.get("phone");
        if (!ValidationUtil.isNullOrBlank(phone))
            this.phone = "%" + phone.trim() + "%";

        String pPage = map.get("page");
        if (ValidationUtil.isNumeric(pPage))
            this.page = Integer.parseInt(pPage);

        String pPageSize = map.get("pageSize");
        if (ValidationUtil.isNumeric(pPageSize))
            this.pageSize = Integer.parseInt(pPageSize);

        String sortBy = map.get("sortBy");
        if (ValidationUtil.isNumeric(sortBy))
            this.sortBy = sortBy;

        String direction = map.get("direction");
        if (ValidationUtil.isNumeric(direction))
            this.direction = direction;

    }
}
