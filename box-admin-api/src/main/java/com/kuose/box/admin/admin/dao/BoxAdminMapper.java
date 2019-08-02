package com.kuose.box.admin.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuose.box.admin.admin.entity.BoxAdmin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 管理员表 Mapper 接口
 * </p>
 *
 * @author fangyajun
 * @since 2019-07-22
 */
public interface BoxAdminMapper extends BaseMapper<BoxAdmin> {

    List<BoxAdmin> listAdmins(String username);

    void updateAdminById(BoxAdmin admin);

    IPage<BoxAdmin> listAdminsPage(Page<BoxAdmin> adminPage, @Param("username") String username);

    void saveBoxAdmin(BoxAdmin admin);
}
