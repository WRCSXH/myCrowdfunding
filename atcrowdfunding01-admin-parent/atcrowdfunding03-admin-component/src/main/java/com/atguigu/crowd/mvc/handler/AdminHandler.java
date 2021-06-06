package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.service.api.AdminService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * 管理员控制器
 */
@Controller
public class AdminHandler {

    @Autowired
    private AdminService adminService;

    /**
     * 登录
     * @param loginAcct
     * @param userPswd
     * @param session
     * @return
     */
    @RequestMapping("/admin/do/login.html")
    public String doLogin(String loginAcct, String userPswd, HttpSession session){
        // 调用service方法执行登录检查
        // 如果这个方法能够返回admin对象表示登录成功，如果账号或密码不正确则抛出异常
        Admin admin = adminService.getAdminByLoginAcct(loginAcct,userPswd);
        // 将登录成功返回的admin对象存入session域
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN, admin);
        // 登录成功就重定向到后台主页面
        // 为了避免跳转到后台主页面再刷新浏览器导致重复提交登录表单，重定向到目标页面
        return "redirect:/admin/to/main/page.html";
    }

    /**
     * 退出登录
     * @param session
     * @return
     */
    @RequestMapping("/admin/do/logout.html")
    public String doLogout(HttpSession session){
        // 强制session失效
        session.invalidate();
        return "redirect:/admin/to/login/page.html";
    }

    /**
     * 分页查询
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param modelMap
     * @return
     */
    @RequestMapping("/admin/get/page.html")
    public String getPageInfo(
            // 使用@RequestParam注解的defaultValue属性指定默认值，当请求中没有携带对应的参数时，使用默认值
            // keyword默认值使用空字符串，和sql语句配合实现两种情况适配
            @RequestParam(value = "keyword",defaultValue = "") String keyword,
            // pageNum默认值使用"1"，默认展示首页
            @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
            // pageSize默认值使用"5"，默认每页展示5条记录
            @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize,
            /*
             ModelMap与Model、ModelAndView的作用类似，都可以发送信息给结果页面，
             只不过ModelAndView是返回值，Model/ModelMap是参数，
             ModelAndView使用addObject()方法保存信息
             Model/ModelMap使用addAttribute()方法保存信息
             */
            ModelMap modelMap){
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);
        modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO,pageInfo);
        return "admin-page";
    }

    /**
     * 删除管理员
     * @param adminId
     * @param pageNum
     * @param keyword
     * @return
     */
    @RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public String remove(
            // @PathVariable是spring3.0的一个新功能：接收请求路径中占位符的值，占位符如{adminId}
                        @PathVariable("adminId") Integer adminId,
                        @PathVariable("pageNum") Integer pageNum,
                        @PathVariable("keyword") String keyword){
        // 执行删除
        adminService.remove(adminId);

        // 页面跳转，回到分页页面
        // 重定向到 /admin/get/page.html地址
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }

    /**
     * 新增管理员
     * @param admin
     * @return
     */
    @RequestMapping("/admin/save.html")
    public String save(Admin admin){
        adminService.save(admin);
        return "redirect:/admin/get/page.html?pageNum="+Integer.MAX_VALUE;
    }

    /**
     * 跳转到更新管理员的页面
     * @param adminId
     * @param modelMap
     * @return
     */
    @RequestMapping("/admin/to/edit/page.html")
    public String toEditPage(@RequestParam("adminId") Integer adminId,
                            ModelMap modelMap){
        Admin admin = adminService.getAdminByAdminId(adminId);
        modelMap.addAttribute("admin",admin);
        return "admin-edit";
    }

    /**
     * 更新管理员
     * @param admin
     * @param pageNum
     * @param keyword
     * @return
     */
    @RequestMapping("/admin/update.html")
    public String update(Admin admin,
                        @RequestParam("pageNum") Integer pageNum,
                         @RequestParam("keyword") String keyword){
        adminService.update(admin);
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }
}
