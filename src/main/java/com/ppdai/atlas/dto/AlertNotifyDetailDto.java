package com.ppdai.atlas.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class AlertNotifyDetailDto {
    /** 自增ID **/
    private Integer id;

    /** 通知名 **/
    private String name;

    /** 通知类型 **/
    private String type;

    private String appid;

    /** 接收组名称 **/
    private String receiver;

    private Integer ischange;

    /** 值班时长 **/
    private Integer dutyTime;

    /** 下次换班时间 **/
    private Timestamp nextTime;

    /** 当前值班组 **/
    private String currentGroup;

    /** 静默时长 **/
    private Integer silenceTime;

    private String description;

    /** 新建时间 **/
    private Timestamp inserttime;

    /** 更新时间 **/
    private Timestamp updatetime;

    /** 逻辑删除 **/
    private Integer isactive;
}
