package com.common.redis;

/**
 * <b>锁</b><br/>
 * <b>Create on:</b>2018/2/27 16:07<br/>
 *
 * @author : liud<br/>
 */
public class Lock {
    private String name;
    private String value;


    public Lock(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
