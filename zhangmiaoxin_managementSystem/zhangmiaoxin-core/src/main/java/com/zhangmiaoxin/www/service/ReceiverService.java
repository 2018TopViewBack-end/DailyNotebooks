package com.zhangmiaoxin.www.service;

import com.zhangmiaoxin.www.dao.ReceiverMapper;
import com.zhangmiaoxin.www.po.Receiver;
import com.zhangmiaoxin.www.util.MybatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class ReceiverService {

    /**
     * 获取用户的所有收货地址服务
     * @param userId
     * @return
     */
    public List<Receiver> listReceiverService(int userId){
        SqlSession session = MybatisUtil.getSession();
        ReceiverMapper mapper = session.getMapper(ReceiverMapper.class);
        List<Receiver> receiverList = mapper.listReceiver(userId);
        session.close();

        return receiverList;
    }

    /**
     * 更改收货信息
     * @param receiver
     * @return
     */
    public Integer updateReceiverService(Receiver receiver){
        SqlSession session = MybatisUtil.getSession();
        ReceiverMapper mapper = session.getMapper(ReceiverMapper.class);
        Integer result = mapper.updateReceiver(receiver);
        session.close();

        return result;
    }

    /**
     * 新增收货信息
     * @param receiver
     * @return
     */
    public Integer addReceiverService(Receiver receiver){
        SqlSession session = MybatisUtil.getSession();
        ReceiverMapper mapper = session.getMapper(ReceiverMapper.class);
        Integer result = mapper.addReceiver(receiver);
        session.close();

        return result;
    }
}
