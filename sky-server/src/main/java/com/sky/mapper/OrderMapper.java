package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {


    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    List<Orders> getHistoryOrder(OrdersPageQueryDTO ordersPageQueryDTO);

    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    List<Orders> getByOrdersPageQueryDTO(OrdersPageQueryDTO ordersPageQueryDTO);

    @Select("select count(*) from orders where status=#{i}")
    Integer getByStatus(int i);

    @Select("select * from orders where status=#{status} and order_time<#{theOutTime}")
    List<Orders> getByStatusAndOrderTime(Integer status, LocalDateTime theOutTime);
}
