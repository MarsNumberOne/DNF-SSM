package com.api.txn.apicore;

import com.api.txn.dto.request.SelectUserByNameRequest;
import com.api.txn.dto.response.SelectlUserByNameResponse;

/**
 * DESCRIPTION:
 * Create on:2018/3/8.
 *
 * @author MACHUNHUI
 */
public interface UserCoreFacade {

    public SelectlUserByNameResponse selectUserByName(SelectUserByNameRequest selectUserByNameRequest);

    public void selectAllUser();
}
