package com.kuose.box.admin.user.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuose.box.admin.user.entity.BoxUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * box_user Mapper 接口
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-04
 */
public interface BoxUserMapper extends BaseMapper<BoxUser> {

    IPage<BoxUser> listUser(Page<BoxUser> boxUserPage, @Param("mobile") String mobile,  @Param("weixinOpenid") String weixinOpenid);
}
