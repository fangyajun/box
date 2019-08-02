package com.kuose.box.admin.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuose.box.admin.admin.entity.BoxAdmin;
import com.kuose.box.admin.admin.dao.BoxAdminMapper;
import com.kuose.box.admin.admin.service.BoxAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuose.box.common.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author fangyajun
 * @since 2019-07-22
 */
@Service
public class BoxAdminServiceImpl extends ServiceImpl<BoxAdminMapper, BoxAdmin> implements BoxAdminService {

    @Autowired
    private BoxAdminMapper boxAdminMapper;


    @Override
    public IPage<BoxAdmin> listAdminsPage(Page<BoxAdmin> adminPage, String username) {
        IPage<BoxAdmin> boxAdminIPage = boxAdminMapper.listAdminsPage(adminPage, username);
        return boxAdminIPage;
    }

    @Override
    public List<BoxAdmin> listAdmins(String username) {
        return boxAdminMapper.listAdmins(username);
    }

    @Override
    public void updateAdminById(BoxAdmin admin) {
        boxAdminMapper.updateAdminById(admin);
    }

    @Override
    public void saveBoxAdmin(BoxAdmin admin) {
        boxAdminMapper.saveBoxAdmin(admin);
    }
}
