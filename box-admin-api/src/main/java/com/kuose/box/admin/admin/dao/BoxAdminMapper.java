package com.kuose.box.admin.admin.dao;

import com.kuose.box.admin.admin.entity.BoxAdmin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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
}
