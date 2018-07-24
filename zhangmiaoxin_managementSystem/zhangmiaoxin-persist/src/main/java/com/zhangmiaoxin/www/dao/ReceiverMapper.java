package com.zhangmiaoxin.www.dao;

import com.zhangmiaoxin.www.po.Receiver;

import java.util.List;

public interface ReceiverMapper {

    /**
     * 显示用户的所有收货信息
     * @param userId
     * @return 用户的所有收货信息list
     */
    List<Receiver> listReceiver(int userId);

    /**
     * 编辑收货信息
     * @param receiver
     * @return
     */
    Integer updateReceiver(Receiver receiver);

    /**
     * 新增收货信息
     * @param receiver
     * @return
     */
    Integer addReceiver(Receiver receiver);
}
