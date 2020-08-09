package me.dingtou.domain;

import java.util.Date;

public class UserFundGroup {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_fund_group.fund_group_id
     *
     * @mbg.generated Sun Aug 09 23:40:03 CST 2020
     */
    private Integer fundGroupId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_fund_group.user_id
     *
     * @mbg.generated Sun Aug 09 23:40:03 CST 2020
     */
    private Long userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_fund_group.group_name
     *
     * @mbg.generated Sun Aug 09 23:40:03 CST 2020
     */
    private String groupName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_fund_group.group_type
     *
     * @mbg.generated Sun Aug 09 23:40:03 CST 2020
     */
    private Integer groupType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_fund_group.start_date
     *
     * @mbg.generated Sun Aug 09 23:40:03 CST 2020
     */
    private Date startDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_fund_group.increment
     *
     * @mbg.generated Sun Aug 09 23:40:03 CST 2020
     */
    private Integer increment;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_fund_group.cron
     *
     * @mbg.generated Sun Aug 09 23:40:03 CST 2020
     */
    private String cron;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_fund_group.price_offset
     *
     * @mbg.generated Sun Aug 09 23:40:03 CST 2020
     */
    private Integer priceOffset;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_fund_group.status
     *
     * @mbg.generated Sun Aug 09 23:40:03 CST 2020
     */
    private Integer status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_fund_group.extra
     *
     * @mbg.generated Sun Aug 09 23:40:03 CST 2020
     */
    private String extra;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_fund_group.fund_group_id
     *
     * @return the value of user_fund_group.fund_group_id
     *
     * @mbg.generated Sun Aug 09 23:40:03 CST 2020
     */
    public Integer getFundGroupId() {
        return fundGroupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_fund_group.fund_group_id
     *
     * @param fundGroupId the value for user_fund_group.fund_group_id
     *
     * @mbg.generated Sun Aug 09 23:40:03 CST 2020
     */
    public void setFundGroupId(Integer fundGroupId) {
        this.fundGroupId = fundGroupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_fund_group.user_id
     *
     * @return the value of user_fund_group.user_id
     *
     * @mbg.generated Sun Aug 09 23:40:03 CST 2020
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_fund_group.user_id
     *
     * @param userId the value for user_fund_group.user_id
     *
     * @mbg.generated Sun Aug 09 23:40:03 CST 2020
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_fund_group.group_name
     *
     * @return the value of user_fund_group.group_name
     *
     * @mbg.generated Sun Aug 09 23:40:03 CST 2020
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_fund_group.group_name
     *
     * @param groupName the value for user_fund_group.group_name
     *
     * @mbg.generated Sun Aug 09 23:40:03 CST 2020
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_fund_group.group_type
     *
     * @return the value of user_fund_group.group_type
     *
     * @mbg.generated Sun Aug 09 23:40:03 CST 2020
     */
    public Integer getGroupType() {
        return groupType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_fund_group.group_type
     *
     * @param groupType the value for user_fund_group.group_type
     *
     * @mbg.generated Sun Aug 09 23:40:03 CST 2020
     */
    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_fund_group.start_date
     *
     * @return the value of user_fund_group.start_date
     *
     * @mbg.generated Sun Aug 09 23:40:03 CST 2020
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_fund_group.start_date
     *
     * @param startDate the value for user_fund_group.start_date
     *
     * @mbg.generated Sun Aug 09 23:40:03 CST 2020
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_fund_group.increment
     *
     * @return the value of user_fund_group.increment
     *
     * @mbg.generated Sun Aug 09 23:40:03 CST 2020
     */
    public Integer getIncrement() {
        return increment;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_fund_group.increment
     *
     * @param increment the value for user_fund_group.increment
     *
     * @mbg.generated Sun Aug 09 23:40:03 CST 2020
     */
    public void setIncrement(Integer increment) {
        this.increment = increment;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_fund_group.cron
     *
     * @return the value of user_fund_group.cron
     *
     * @mbg.generated Sun Aug 09 23:40:03 CST 2020
     */
    public String getCron() {
        return cron;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_fund_group.cron
     *
     * @param cron the value for user_fund_group.cron
     *
     * @mbg.generated Sun Aug 09 23:40:03 CST 2020
     */
    public void setCron(String cron) {
        this.cron = cron == null ? null : cron.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_fund_group.price_offset
     *
     * @return the value of user_fund_group.price_offset
     *
     * @mbg.generated Sun Aug 09 23:40:03 CST 2020
     */
    public Integer getPriceOffset() {
        return priceOffset;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_fund_group.price_offset
     *
     * @param priceOffset the value for user_fund_group.price_offset
     *
     * @mbg.generated Sun Aug 09 23:40:03 CST 2020
     */
    public void setPriceOffset(Integer priceOffset) {
        this.priceOffset = priceOffset;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_fund_group.status
     *
     * @return the value of user_fund_group.status
     *
     * @mbg.generated Sun Aug 09 23:40:03 CST 2020
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_fund_group.status
     *
     * @param status the value for user_fund_group.status
     *
     * @mbg.generated Sun Aug 09 23:40:03 CST 2020
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_fund_group.extra
     *
     * @return the value of user_fund_group.extra
     *
     * @mbg.generated Sun Aug 09 23:40:03 CST 2020
     */
    public String getExtra() {
        return extra;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_fund_group.extra
     *
     * @param extra the value for user_fund_group.extra
     *
     * @mbg.generated Sun Aug 09 23:40:03 CST 2020
     */
    public void setExtra(String extra) {
        this.extra = extra == null ? null : extra.trim();
    }
}