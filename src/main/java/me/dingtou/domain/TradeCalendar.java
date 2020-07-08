package me.dingtou.domain;

import java.util.Date;

public class TradeCalendar {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade_calendar.id
     *
     * @mbg.generated Sat Jun 24 01:21:40 CST 2017
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade_calendar.fund_group_id
     *
     * @mbg.generated Sat Jun 24 01:21:40 CST 2017
     */
    private Integer fundGroupId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade_calendar.trade_date
     *
     * @mbg.generated Sat Jun 24 01:21:40 CST 2017
     */
    private Date tradeDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade_calendar.target_price
     *
     * @mbg.generated Sat Jun 24 01:21:40 CST 2017
     */
    private Integer targetPrice;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column trade_calendar.trade_status
     *
     * @mbg.generated Sat Jun 24 01:21:40 CST 2017
     */
    private Integer tradeStatus;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column trade_calendar.id
     *
     * @return the value of trade_calendar.id
     *
     * @mbg.generated Sat Jun 24 01:21:40 CST 2017
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column trade_calendar.id
     *
     * @param id the value for trade_calendar.id
     *
     * @mbg.generated Sat Jun 24 01:21:40 CST 2017
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column trade_calendar.fund_group_id
     *
     * @return the value of trade_calendar.fund_group_id
     *
     * @mbg.generated Sat Jun 24 01:21:40 CST 2017
     */
    public Integer getFundGroupId() {
        return fundGroupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column trade_calendar.fund_group_id
     *
     * @param fundGroupId the value for trade_calendar.fund_group_id
     *
     * @mbg.generated Sat Jun 24 01:21:40 CST 2017
     */
    public void setFundGroupId(Integer fundGroupId) {
        this.fundGroupId = fundGroupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column trade_calendar.trade_date
     *
     * @return the value of trade_calendar.trade_date
     *
     * @mbg.generated Sat Jun 24 01:21:40 CST 2017
     */
    public Date getTradeDate() {
        return tradeDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column trade_calendar.trade_date
     *
     * @param tradeDate the value for trade_calendar.trade_date
     *
     * @mbg.generated Sat Jun 24 01:21:40 CST 2017
     */
    public void setTradeDate(Date tradeDate) {
        this.tradeDate = tradeDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column trade_calendar.target_price
     *
     * @return the value of trade_calendar.target_price
     *
     * @mbg.generated Sat Jun 24 01:21:40 CST 2017
     */
    public Integer getTargetPrice() {
        return targetPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column trade_calendar.target_price
     *
     * @param targetPrice the value for trade_calendar.target_price
     *
     * @mbg.generated Sat Jun 24 01:21:40 CST 2017
     */
    public void setTargetPrice(Integer targetPrice) {
        this.targetPrice = targetPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column trade_calendar.trade_status
     *
     * @return the value of trade_calendar.trade_status
     *
     * @mbg.generated Sat Jun 24 01:21:40 CST 2017
     */
    public Integer getTradeStatus() {
        return tradeStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column trade_calendar.trade_status
     *
     * @param tradeStatus the value for trade_calendar.trade_status
     *
     * @mbg.generated Sat Jun 24 01:21:40 CST 2017
     */
    public void setTradeStatus(Integer tradeStatus) {
        this.tradeStatus = tradeStatus;
    }
}