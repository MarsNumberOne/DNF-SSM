package com.api.txn.dto.response;

import lombok.Data;

/**
 * DESCRIPTION:
 * Create on:2018/3/8.
 *
 * @author MACHUNHUI
 */
@Data
public class SelectlUserByNameResponse {
    private Integer id;
    private String username;
    private String password;
}
