package me.dingtou.constant;

/**
 * 扩展属性定义
 */
public enum FundGroupExtraEnum {

    MAX_PRICE("maxPrice", Integer.class, "基金组合最大金额（分）");

    private String key;
    private Class<?> clazz;
    private String desc;

    FundGroupExtraEnum(String key, Class<?> clazz, String desc) {
        this.key = key;
        this.clazz = clazz;
        this.desc = desc;
    }

    public String getKey() {
        return key;
    }

    public <T> Class<T> getClazz() {
        return (Class<T>) clazz;
    }

    public String getDesc() {
        return desc;
    }
}
