package com.kuose.box.admin.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuose.box.admin.admin.entity.BoxAdmin;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 * @author fangyajun
 * @since 2019-07-22
 */
public interface BoxAdminService extends IService<BoxAdmin> {

    IPage<BoxAdmin> listAdminsPage(Page<BoxAdmin> adminPage, String username);

    List<BoxAdmin> listAdmins(String username);

    void updateAdminById(BoxAdmin admin);

    void saveBoxAdmin(BoxAdmin admin);
}
