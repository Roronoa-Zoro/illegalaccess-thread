package com.illegalaccess.thread.sdk.alarm;

/**
 * Created by xiao on 2019/12/28.
 */
public enum ThreadPoolAlarmTypeEnum {

    Reject_Strategy(1, "按拒绝策略报警"),
    Task_Cost_Strategy(2, "按任务执行时间报警");


    ThreadPoolAlarmTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private int type;
    private String desc;

    public static ThreadPoolAlarmTypeEnum getEnumByCode(int type) {
        for (ThreadPoolAlarmTypeEnum enums : ThreadPoolAlarmTypeEnum.values()) {
            if (enums.type == type) {
                return enums;
            }
        }
        return null;
    }

}
