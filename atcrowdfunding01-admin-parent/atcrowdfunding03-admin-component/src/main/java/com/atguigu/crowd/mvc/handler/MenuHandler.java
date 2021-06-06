package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Menu;
import com.atguigu.crowd.service.api.MenuService;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
@RestController
// @Controller
public class MenuHandler {
    @Autowired
    private MenuService menuService;

    /**
     * 删除节点
     * @param id
     * @return
     */
    //  @ResponseBody
    @RequestMapping("/menu/remove.json")
    public ResultEntity<String> remove(@RequestParam("id") Integer id){
        menuService.remove(id);
        return ResultEntity.successWithoutData();
    }
    /**
     * 更新节点
     * @param menu
     * @return
     */
    //  @ResponseBody
    @RequestMapping("/menu/update.json")
    public ResultEntity<String> update(Menu menu){
        menuService.update(menu);
        return ResultEntity.successWithoutData();
    }
    /**
     * 添加节点
     * @param menu
     * @return
     */
    //  @ResponseBody
    @RequestMapping("/menu/save.json")
    public ResultEntity<String> save(Menu menu){
        menuService.save(menu);
        return ResultEntity.successWithoutData();
    }

    /**
     * 获取整个组装好的树形结构
     * @return
     */
    //  @ResponseBody
    @RequestMapping("/menu/get/whole/tree.json")
    public ResultEntity<Menu> getWholeTreeNew(){
        // 查询全部的Menu对象
        List<Menu> menuList = menuService.getAll();
        // 声明一个变量存储找到的根节点
        Menu root = null;
        // 创建一个Map集合存储id和Menu对象的映射关系，以便查找父节点
        Map<Integer,Menu> menuMap = new HashMap<>();
        // 遍历menuList填充menuMap
        for (Menu menu:menuList){
            menuMap.put(menu.getId(),menu);
        }
        // 再次遍历menuList，查找根节点，组装父子节点
        for (Menu menu:menuList){
            // 获取当前menu的pid属性
            Integer pid = menu.getPid();
            // 如果pid为null，说明当前节点没有父节点，是根节点
            if (pid == null){
                // 把当前menu赋值给root
                root = menu;
                // 跳过本次循环，进入下次循环
                continue;
            }
            // 如果pid不为null，说明当前节点有父节点，可以根据pid到menuMap中查找对应的Menu对象，即父节点father
            Menu father = menuMap.get(menu.getPid());
            // 将当前节点存入父节点的children集合
            father.getChildren().add(menu);
        }
        // 经过上述算法，根节点就包含了整个树形结构，返回根节点就是返回整个树形结构
        return ResultEntity.successWithData(root);
    }

    // 这个方法因为用到了嵌套的for循环，时间复杂度极高，会消耗较多的资源，所以使用新方法替代
    public ResultEntity<Menu> getWholeTreeOld(){
        // 查询全部的Menu对象
        List<Menu> menuList = menuService.getAll();
        // 声明一个变量存储找到的根节点
        Menu root = null;
        // 遍历menuList
        for(Menu menu:menuList){
            // 获取当前menu的pid属性
            Integer pid = menu.getPid();
            // 如果pid为null，说明当前节点没有父节点，是根节点
            if (pid == null){
                // 把当前menu赋值给root
                root = menu;
                // 跳过本次循环，进入下次循环
                continue;
            }
            // 如果pid不为null，说明当前节点有父节点，找到父节点就可以进行组装，建立父子关系
            for (Menu maybeFather:menuList){
                // 获取maybeFather的id属性
                Integer id = maybeFather.getId();
                // 比较当前节点的pid和疑似是父节点的id
                if (Objects.equals(id,pid)){
                    // 二者相等则将子节点存入父节点的children集合
                    maybeFather.getChildren().add(menu);
                    // 停止整个循环
                    break;
                }
            }
        }
        // 最后将组装好的树形结构，即根节点对象，返回给浏览器
        return ResultEntity.successWithData(root);
    }
}
