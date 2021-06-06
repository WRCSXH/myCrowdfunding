package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.mapper.*;
import com.atguigu.crowd.entity.po.MemberConfirmInfoPO;
import com.atguigu.crowd.entity.po.MemberLaunchInfoPO;
import com.atguigu.crowd.entity.po.ProjectPO;
import com.atguigu.crowd.entity.po.ReturnPO;
import com.atguigu.crowd.service.api.ProjectService;
import com.atguigu.crowd.entity.vo.*;
import com.atguigu.crowd.util.CrowdUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ReturnPOMapper returnPOMapper;

    @Autowired
    private MemberConfirmInfoPOMapper memberConfirmInfoPOMapper;

    @Autowired
    private MemberLaunchInfoPOMapper memberLaunchInfoPOMapper;

    @Autowired
    private ProjectItemPicPOMapper projectItemPicPOMapper;

    @Autowired
    private ProjectPOMapper projectPOMapper;

    @Transactional(readOnly = false,propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    @Override
    public void saveProject(ProjectVO projectVO, Integer memberid) {
        // 一、保存 projectPO 对象
        // 1.创建空的 projectPO 对象
        ProjectPO projectPO = new ProjectPO();
        // 2.把 projectVO 中的属性复制到 projectPO 中
        BeanUtils.copyProperties(projectVO,projectPO);
        // 设置主键
        projectPO.setMemberid(memberid);
        // 设置创建时间
        String createdate = CrowdUtil.getSysTime();
        projectPO.setCreatedate(createdate);
        // 设置状态 0 表示即将开始
        Integer status = 0;
        projectPO.setStatus(status);
        // 3.保存 projectPO
        projectPOMapper.insertSelective(projectPO);
        // 为了能够获取到 projectPO 保存后的自增主键 projectId，需要到 ProjectPOMapper.xml 中进行相关的设置
        // <insert id="insertSelective" useGeneratedKeys="true" keyProperty="id" parameterType="com.atguigu.crowd.po.ProjectPO" >
        // 4.从 projectPO 对象中获取自增主键
        Integer projectId = projectPO.getId();

        // 二、保存项目/分类的关联关系信息
        // 1.从 projectVO 中获取 typeIdList
        List<Integer> typeIdList = projectVO.getTypeIdList();
        // 2.保存到数据库中
        projectPOMapper.insertTypeRelationship(typeIdList,projectId);

        // 三、保存项目/标签的关联关系信息
        // 1.从 projectVO 中获取 tagIdList
        List<Integer> tagIdList = projectVO.getTagIdList();
        // 2.保存到数据库中
        projectPOMapper.insertTagRelationship(tagIdList,projectId);

        // 四、保存项目/详情图片路径信息
        List<String> detailPicturePathList = projectVO.getDetailPicturePathList();
        projectItemPicPOMapper.insertDetailPicturePathList(detailPicturePathList,projectId);

        // 五、保存项目/发起人信息
        MemberLaunchInfoVO memberLaunchInfoVO = projectVO.getMemberLaunchInfoVO();
        MemberLaunchInfoPO memberLaunchInfoPO = new MemberLaunchInfoPO();
        BeanUtils.copyProperties(memberLaunchInfoVO,memberLaunchInfoPO);
        // 设置准备好的 memberid
        memberLaunchInfoPO.setMemberid(memberid);
        memberLaunchInfoPOMapper.insertSelective(memberLaunchInfoPO);

        // 六、保存项目/回报信息
        List<ReturnVO> returnVOList = projectVO.getReturnVOList();
        List<ReturnPO> returnPOList = new ArrayList<>();
        for (ReturnVO returnVO:returnVOList){
            ReturnPO returnPO = new ReturnPO();
            BeanUtils.copyProperties(returnVO,returnPO);
            returnPOList.add(returnPO);
        }
        returnPOMapper.insertReturnPOBatch(returnPOList,projectId);

        // 七、保存项目/确认信息
        MemberConfirmInfoVO memberConfirmInfoVO = projectVO.getMemberConfirmInfoVO();
        MemberConfirmInfoPO memberConfirmInfoPO = new MemberConfirmInfoPO();
        BeanUtils.copyProperties(memberConfirmInfoVO,memberConfirmInfoPO);
        memberConfirmInfoPO.setMemberid(memberid);
        memberConfirmInfoPOMapper.insertSelective(memberConfirmInfoPO);
    }

    @Override
    public List<PortalTypeVO> getPortalTypeVOList() {
        return projectPOMapper.selectPortalTypeVOList();
    }

    @Override
    public DetailProjectVO getDetailProjectVO(Integer projectId) throws ParseException {
        DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(projectId);
        String deployDateStr = detailProjectVO.getDeployDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date deployDate = simpleDateFormat.parse(deployDateStr);    // 众筹发布时间
        Date nowDate = new Date();  // 系统当前时间
        long deployDateTime = deployDate.getTime(); // 众筹发布时间毫秒数
        long nowDateTime = nowDate.getTime(); // 系统当前时间毫秒数
        long pastDays = (nowDateTime - deployDateTime)/1000/60/60/24; // 众筹发布后过去的天数
        long totalDays = detailProjectVO.getDay();  // 众筹持续的天数
        long lastDays = totalDays - pastDays; // 众筹剩余的天数
        detailProjectVO.setLastDays(lastDays);

        int status = detailProjectVO.getStatus();
        String statusText = "";
        switch (status){
            case 0:
                statusText = "即将开始";
                break;
            case 1:
                statusText = "众筹中";
                break;
            case 2:
                statusText = "众筹成功";
                break;
            case 3:
                statusText = "众筹失败";
                break;
            default:
                break;
        }
        detailProjectVO.setStatusText(statusText);
        return detailProjectVO;
    }
}
