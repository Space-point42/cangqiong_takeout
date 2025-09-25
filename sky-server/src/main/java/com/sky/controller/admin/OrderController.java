package com.sky.controller.admin;


import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("AdminOrderController")
@RequestMapping("/admin/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/conditionSearch")
    public Result<PageResult> searchOrder(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("要查询的订单信息为{}", ordersPageQueryDTO);
        PageResult pageResult = orderService.adminSearchOrder(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> getOrderStatistics() {
        log.info("获取各个状态的订单数量");
        OrderStatisticsVO orderStatisticsVO = orderService.getOrderStatistics();
        return Result.success(orderStatisticsVO);
    }

    @GetMapping("/details/{id}")
    public Result<OrderVO> searchOrderDetail(@PathVariable Long id){
        log.info("要查询订单详情的订单的id为{}",id);
        OrderVO orderVO = orderService.searchOrder(id);
        return Result.success(orderVO);

    }

    @PutMapping({"/confirm"})
    public Result confirmOrder(@RequestBody Orders orders){
        log.info("需要接单的订单id为{}", orders.getId());
        orderService.confirmOrder(orders);
        return Result.success();
    }

    @PutMapping("/rejection")
    public Result rejectOrder(@RequestBody Orders orders){
        log.info("要拒绝的订单id为{}", orders.getId());
        orderService.rejectOrder(orders);
        return Result.success();
    }

    @PutMapping("/cancel")
    public Result adminCancelOrder(@RequestBody Orders orders){
        log.info("要取消的订单id{}", orders.getId());
        orderService.adminCancelOrder(orders);
        return Result.success();
    }

    @PutMapping("/delivery/{id}")
    public Result deliveryOrder(@PathVariable Long id){
        log.info("要派送的订单的id为{}",id);
        orderService.deliveryOrder(id);
        return Result.success();
    }

    @PutMapping("/complete/{id}")
    public Result completeOrder(@PathVariable Long id){
        log.info("已完成的订单的id为{}", id);
        orderService.completeOrder(id);
        return Result.success();
    }

}
