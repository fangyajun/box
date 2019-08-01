package com.kuose.box.admin.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 管理员表
 * </p>
 *
 * @author fangyajun
 *
 * @since 2019-07-22
 */
@Data
@ApiModel(value="管理员用户信息")
public class BoxAdmin extends Model<BoxAdmin> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="id,添加时不需要，修改和删除的时必传", dataType = "Integer")
    private Integer id;

    /**
     * 管理员名称
     */
    @ApiModelProperty(value="用户名", example = "admin123")
    private String username;

    /**
     * 管理员密码
     */
    @ApiModelProperty(value="密码",  example = "admin123")
    private String password;

    /**
     * 最近一次登录IP地址
     */
    @ApiModelProperty(hidden = true)
    private String lastLoginIp;

    /**
     * 最近一次登录时间
     */
    @ApiModelProperty(hidden = true)
    private Long lastLoginTime;

    /**
     * 头像图片
     */
    @ApiModelProperty(value="头像图片")
    private String avatar;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Long addTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(hidden = true)
    private Long updateTime;

    /**
     * 逻辑删除
     */
    @ApiModelProperty(hidden = true)
    private Integer deleted;

    /**
     * 角色列表
     */
    @ApiModelProperty(hidden = true)
    private Integer[] roleIds;

}
