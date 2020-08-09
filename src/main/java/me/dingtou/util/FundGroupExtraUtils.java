package me.dingtou.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import me.dingtou.constant.FundGroupExtraEnum;

/**
 * 扩展属性处理工具类
 */
public class FundGroupExtraUtils {

    /**
     * 最大金额处理工具
     *
     * @param extra
     * @return
     */
    public static int getMaxPrice(String extra) {
        return getObject(extra, FundGroupExtraEnum.MAX_PRICE.getKey(), FundGroupExtraEnum.MAX_PRICE.getClazz(), 0);
    }

    /**
     * 通用处理工具
     *
     * @param extra
     * @param key
     * @param clazz
     * @param defaultVal
     * @param <T>
     * @return
     */
    public static <T> T getObject(String extra, String key, Class<T> clazz, T defaultVal) {
        if (null != extra) {
            JSONObject jsonObject = JSON.parseObject(extra);
            T val = jsonObject.getObject(key, clazz);
            return null == val ? defaultVal : val;
        }
        return defaultVal;
    }

}
