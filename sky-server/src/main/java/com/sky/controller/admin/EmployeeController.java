package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.service.impl.EmployeeServiceImpl;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private EmployeeServiceImpl employeeServiceImpl;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }


    /**
     * 加入新员工
     * @param employeeDTO
     * @return
     */
    @PostMapping
    public Result<String> saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("加入员工{}", employeeDTO);
        employeeService.saveEmployee(employeeDTO);
        return Result.success();
    }


    /**
     * 分页查询员工数据
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("分页查询的数据为{}", employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);

    }

    /**
     * 禁用或启用员工账号
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable("status") Integer status, Long id){
        log.info("启用禁用账号信息{},{}", status, id);
        employeeService.startOrStop(status,id);
        return Result.success();
    }


    /**
     * 根据id查询员工，进行修改前的回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Employee> getEmployeeById(@PathVariable("id") Long id) {
        log.info("要查询的员工id为{}",id);
        Employee employee = employeeService.getEmployeeById(id);
        return Result.success(employee);
    }


    @PutMapping
    public Result updateEmployee(@RequestBody Employee employee) {
        log.info("要修改的信息为{}", employee);
        employeeService.updateEmployee(employee);
        return Result.success();
    }








}
