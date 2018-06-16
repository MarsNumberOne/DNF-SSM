package com.api.txn.enums;

import lombok.Getter;

/**
 * <b>DESCRIPTION:</b>账户状态<br/>
 */
public enum AccountStatus {

    NORMAL("NORMAL", "正常"),
    FREEZE("FREEZE", "冻结"),
    CLOSE("CLOSE", "销户"),
    NOENABLE("NOENABLE", "未激活");

    @Getter
    String key;
    @Getter
    String value;

    private AccountStatus(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getValue(String key) {
        for(AccountStatus typeEnum : AccountStatus.values()){
            if (typeEnum.getKey().equals(key)) {
                return typeEnum.getValue();
            }
        }
        return null;
    }
}