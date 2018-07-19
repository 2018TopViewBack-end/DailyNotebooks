import com.zhangmiaoxin.www.dao.FoodMapper;
import com.zhangmiaoxin.www.dao.OrderMapper;
import com.zhangmiaoxin.www.dao.StoreMapper;
import com.zhangmiaoxin.www.dao.UserMapper;

import com.zhangmiaoxin.www.po.Food;

import com.zhangmiaoxin.www.po.Order;
import com.zhangmiaoxin.www.po.Receiver;
import com.zhangmiaoxin.www.util.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class Test1 {
    @Test
    public void testRole() {
        SqlSession session = MybatisUtil.getSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        System.out.println(userMapper.getRole(1));
        session.close();
    }

    @Test
    public void testUpdateUser() {
        SqlSession session = MybatisUtil.getSession();
        StoreMapper storeMapper = session.getMapper(StoreMapper.class);
        Assert.assertEquals(1,storeMapper.updateSales(7,10));
        session.close();
    }

    @Test
    public void test() {
        SqlSession session = MybatisUtil.getSession();
        OrderMapper mapper = session.getMapper(OrderMapper.class);
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(1);
        list.add(2);
        list.add(2);
        list.add(3);
    }

}
