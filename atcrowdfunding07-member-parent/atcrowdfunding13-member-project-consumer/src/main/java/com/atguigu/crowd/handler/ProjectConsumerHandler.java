package com.atguigu.crowd.handler;

import com.atguigu.crowd.api.MysqlRemoteService;
import com.atguigu.crowd.config.OSSProperties;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.vo.*;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProjectConsumerHandler {

    @Autowired
    private OSSProperties ossProperties;

    @Autowired
    private MysqlRemoteService mysqlRemoteService;

    @RequestMapping("/get/detail/projectvo/{projectId}")
    public String getDetailProjectVO(ModelMap modelMap, @PathVariable("projectId") Integer projectId,HttpSession session){
        try{
            ResultEntity<DetailProjectVO> detailProjectVOResultEntity = mysqlRemoteService.getDetailProjectVORemote(projectId);
            String result = detailProjectVOResultEntity.getResult();
            if (ResultEntity.SUCCESS.equals(result)){
                DetailProjectVO detailProjectVO = detailProjectVOResultEntity.getData();
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_DETAIL_PROJECT_DATA,detailProjectVO);
                return "project-detail";
            } else {
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_DETAIL_PROJECT_DATA_MISSING);
                return "project-detail";
            }
        } catch (Exception e) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,e.getMessage());
            return "project-detail";
        }
    }

    @RequestMapping("/save/confirm")
    public String saveConfirm(HttpSession session, MemberConfirmInfoVO memberConfirmInfoVO,ModelMap modelMap){
        // 1.从 session域中读取之前临时存储的 projectVO对象
        ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLATE_PROJECT);
        // 2.判断 projectVO 是否为空
        if (projectVO == null){
            throw new RuntimeException(CrowdConstant.MESSAGE_TEMPLATE_PROJECT_MISSING);
        }
        // 3.将确认信息数据存入 projectVO对象
        projectVO.setMemberConfirmInfoVO(memberConfirmInfoVO);
        // 4.从 session 域中读取当前登录的用户信息
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        // 5.获取 memberid
        Integer memberid = memberLoginVO.getId();
        // 6.调用 mysql 远程方法保存 projectVO 对象
        ResultEntity<String> saveProjectVOResultEntity = mysqlRemoteService.saveProjectVORemote(projectVO, memberid);
        // 7.判断远程的保存操作是否成功
        String result = saveProjectVOResultEntity.getResult();
        if (ResultEntity.FAILED.equals(result)){
            // 8.保存失败返回原表单页面并携带错误消息
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,saveProjectVOResultEntity.getMessage());
            return "project-confirm";
        }
        // 9.从 session 域中移出临时的 projectVO 对象
        session.removeAttribute(CrowdConstant.ATTR_NAME_TEMPLATE_PROJECT);
        // 10.远程保存成功，重定向到最终完成页面
        return "redirect:http://localhost/project/to/success/page.html";
    }

    @ResponseBody
    @RequestMapping("/save/return.json")
    public ResultEntity<String> saveReturn(ReturnVO returnVO,HttpSession session){
        try{
            // 1.从session域中读取之前缓存的 ProjectVO对象
            ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLATE_PROJECT);
            // 2.判断 projectVO是否为空
            if (projectVO == null){
                return ResultEntity.failed(CrowdConstant.MESSAGE_TEMPLATE_PROJECT_MISSING);
            }
            // 3.从 projectVO对象中获取存储回报信息的集合returnVOList
            List<ReturnVO> returnVOList = projectVO.getReturnVOList();
            // 4.判断 returnVOList是否有效
            if (returnVOList == null||returnVOList.size() == 0){
                // 5.创建集合对象对 returnVOList集合初始化
                returnVOList = new ArrayList<>();
                // 6.为了以后可以正常使用该集合，存回 projectVO
                projectVO.setReturnVOList(returnVOList);
            }
            // 7.将收集了表单数据的 returnVO对象存入 returnVOList集合
            returnVOList.add(returnVO);
            // 8.把数据有变化的 projectVO 对象重新存入 session 域，确保最新的数据最终存入 redis
            session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLATE_PROJECT,projectVO);
            // 9.所有操作成功后返回成功结果
            return ResultEntity.successWithoutData();
        }catch (Exception e){
            // 10.报异常返回异常信息
            return ResultEntity.failed(e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping("/upload/return/picture.json")
    public ResultEntity<String> uploadReturnPicture(
            // 接收用户上传的文件
            @RequestParam("returnPicture") MultipartFile returnPicture
    ) throws IOException {
        // 1.执行文件上传
        ResultEntity<String> uploadReturnPictureResultEntity = CrowdUtil.uploadFileToOss(
                ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                returnPicture.getInputStream(),
                ossProperties.getBucketName(),
                ossProperties.getBucketDomain(),
                returnPicture.getOriginalFilename()
        );
        // 2.返回文件上传的结果
        return uploadReturnPictureResultEntity;
    }

    @RequestMapping("/save/project/basic/info")
    public String saveProjectBasicInfo(
            // 接收除了上传图片之外的其它普通数据
            ProjectVO projectVO,
            // 接收上传的头图
            MultipartFile headerPicture,
            // 接收上传的详情图
            List<MultipartFile> detailPictureList,
            // 用于将收集了一部分数据的 ProjectVO 对象存入 session 域
            HttpSession session,
            // 用于当前操作失败后返回上一个表单页面时携带提示消息
            ModelMap modelMap
            ) throws IOException {

        // 一、完成头图上传
        // 1.获取当前 headPicture 对象是否为空
        boolean headerPictureEmpty = headerPicture.isEmpty();
        if (headerPictureEmpty){
            // 2.若未上传头图，则返回表单页显示错误消息
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_HEADER_PICTURE_EMPTY);
            return "project-launch";
        }
        // 3.若用户确实上传了头图，执行上传
        ResultEntity<String> uploadHeaderPictureResultEntity = CrowdUtil.uploadFileToOss(
                ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                headerPicture.getInputStream(),
                ossProperties.getBucketName(),
                ossProperties.getBucketDomain(),
                headerPicture.getOriginalFilename()
        );
        // 4.判断头图是否上传成功
        String uploadHeaderPictureResult = uploadHeaderPictureResultEntity.getResult();
        if (ResultEntity.SUCCESS.equals(uploadHeaderPictureResult)){
            // 5.若上传成功，则从返回的数据中获取图片访问路径
            String headerPicturePath = uploadHeaderPictureResultEntity.getData();
            // 6.存入 projectVO 对象中
            projectVO.setHeaderPicturePath(headerPicturePath);
        } else {
            // 7.若上传失败，则返回表单页显示错误消息
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_HEADER_PICTURE_UPLOAD_FAILED);
            return "project-launch";
        }

        // 二、上传详情图
        // 1.创建一个用于存放详情图路径的集合
        List<String> detailPicturePathList = new ArrayList<>();
        // 2.检查 detailPictureList 是否有效
        if (detailPictureList == null || detailPictureList.size() == 0){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_DETAIL_PICTURE_EMPTY);
            return "project-launch";
        }
        // 3.遍历 detailPictureList 集合
        for (MultipartFile detailPicture:detailPictureList){

            // 4.判断当前 detailPicture 是否为空
            if (detailPicture == null){
                // 5.检测到详情图片集中有一张详情图为空，也回去返回错误消息
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_DETAIL_PICTURE_EMPTY);
                return "project-launch";
            }
        }
        // 6.执行上传
        ResultEntity<String> uploadDetailPictureResultEntity = CrowdUtil.uploadFileToOss(
                ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                headerPicture.getInputStream(),
                ossProperties.getBucketName(),
                ossProperties.getBucketDomain(),
                headerPicture.getOriginalFilename()
        );
        // 7.判断上传详情图是否成功
        String uploadDetailPictureResult = uploadDetailPictureResultEntity.getResult();
        if (ResultEntity.SUCCESS.equals(uploadDetailPictureResult)){
            // 8.若上传成功，则从返回的数据中收集刚上传的图片访问路径
            String detailPicturePath = uploadDetailPictureResultEntity.getData();
            detailPicturePathList.add(detailPicturePath);
        } else {
            // 9.若上传失败，返回表单页显示错误消息
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_DETAIL_PICTURE_UPLOAD_FAILED);
            return "project-launch";
        }
        // 10.将存放了详情图访问路径的集合存入 projectVO 对象中
        projectVO.setDetailPicturePathList(detailPicturePathList);

        // 三、后续操作
        // 1.将 projectVO 对象存入 session 域
        session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLATE_PROJECT, projectVO);
        // 2.以完整的访问路径前往下一个收集回报信息的页面
        return "redirect:http://localhost/project/to/return/page.html";
    }
}
