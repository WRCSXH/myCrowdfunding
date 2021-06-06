package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.vo.DetailProjectVO;
import com.atguigu.crowd.entity.vo.PortalTypeVO;
import com.atguigu.crowd.entity.vo.ProjectVO;

import java.text.ParseException;
import java.util.List;

public interface ProjectService {
    void saveProject(ProjectVO projectVO, Integer memberid);
    List<PortalTypeVO> getPortalTypeVOList();
    DetailProjectVO getDetailProjectVO(Integer projectId) throws ParseException;
}
