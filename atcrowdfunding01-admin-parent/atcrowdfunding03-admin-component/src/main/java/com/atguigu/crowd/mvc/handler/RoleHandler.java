package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.RoleService;
import com.atguigu.crowd.util.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
// 可以用@RestController 代替 @Controller，@ResponseBody
@RestController
// @Controller
// @ResponseBody
public class RoleHandler {
    @Autowired
    private RoleService roleService;

    /**
     * 删除角色
     * @param roleIdList
     * @return
     */
    // @ResponseBody
    @RequestMapping("/role/remove/by/role/id/array.json")
    public ResultEntity<String> remove(@RequestBody List<Integer> roleIdList){
        roleService.remove(roleIdList);
        return ResultEntity.successWithoutData();
    }

    /**
     * 更新角色
     * @param role
     * @return
     */
    // @ResponseBody
    @RequestMapping("/role/update.json")
    public ResultEntity<String> update(Role role){
        roleService.update(role);
        return ResultEntity.successWithoutData();
    }

    /**
     * 添加角色
     * @param role
     * @return
     */
    // @ResponseBody
    @RequestMapping("/role/save.json")
    public ResultEntity<String> save(Role role){
        roleService.save(role);
        return ResultEntity.successWithoutData();
    }

    /**
     * 分页查询
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    // 要求：访问Role的分页功能时具备“部长”角色或“role:get”权限，“加锁”的代码（适用于ajax请求）
    @PreAuthorize("hasRole('部长') or hasAuthority('role:get')")
    /*
    其它注解（了解）：
        @PostAuthorize：先执行方法然后根据方法返回值判断是否具备权限。
        @PreFilter：在方法执行前对传入的参数进行过滤。只能对集合类型的数据进行过滤。
        @PostFilter：在方法执行后对方法返回值进行过滤。只能对集合类型的数据进行过滤。
     */
    // @ResponseBody
    @RequestMapping("/role/get/page.json")
    public ResultEntity<PageInfo<Role>> getPageInfo(@RequestParam(value = "keyword",defaultValue = "") String keyword,
                                                    @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                                    @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize){
        PageInfo<Role> pageInfo = roleService.getPageInfo(keyword,pageNum,pageSize);
        // 如果上面的操作抛出异常交给异常映射机制处理
        return ResultEntity.successWithData(pageInfo);
    }
}
