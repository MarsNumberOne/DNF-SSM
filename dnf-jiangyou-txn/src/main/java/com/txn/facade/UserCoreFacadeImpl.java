package com.txn.facade;

import com.api.txn.apicore.UserCoreFacade;
import com.api.txn.dto.request.SelectUserByNameRequest;
import com.api.txn.dto.response.SelectlUserByNameResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * DESCRIPTION:
 * Create on:2018/3/8.
 *
 * @author MACHUNHUI
 */
@Service
@Slf4j
public class UserCoreFacadeImpl implements UserCoreFacade{

    @Override
    public SelectlUserByNameResponse selectUserByName(SelectUserByNameRequest selectUserByNameRequest) {

        return null;
    }

    @Override
    public void selectAllUser() {

    }
}
