package com.sky.Task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 每分钟清理一次已经超时15分钟未支付的订单
     */
    @Scheduled(cron = "0 * * * * *")
    public void dealOutTimeOrder(){
        LocalDateTime theOutTime = LocalDateTime.now().plusMinutes(-15);

        List<Orders> outTimeOrders = orderMapper.getByStatusAndOrderTime(Orders.PENDING_PAYMENT,theOutTime);
        if(!outTimeOrders.isEmpty()){
            for(Orders order : outTimeOrders){
                order.setStatus(Orders.CANCELLED);
                order.setCancelTime(LocalDateTime.now());
                order.setCancelReason("超时未支付");
                orderMapper.update(order);
            }
        }
    }

    /**
     * 每天0点将已派送1h的订单变为已完成
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void dealOutTimeDeliveryOrder(){
        LocalDateTime theOutTime = LocalDateTime.now().plusHours(-1);
        List<Orders> outTimeOrders = orderMapper.getByStatusAndOrderTime(Orders.DELIVERY_IN_PROGRESS,theOutTime);
        if(!outTimeOrders.isEmpty()){
            for(Orders order : outTimeOrders){
                order.setStatus(Orders.COMPLETED);
                orderMapper.update(order);
            }
        }
    }


}
