package me.dingtou.constant;

/**
 * @author qiyan
 * @date 2017/08/27
 */
public enum GroupTypeEnum {

    VALUE(1, "价值平均"),

    PRICE(2, "价格平均"),;

    private final String desc;
    private final Integer type;

    GroupTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getType() {
        return type;
    }
}
