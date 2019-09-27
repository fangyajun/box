package com.kuose.box.db.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * box_user
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-04
 */
@Data
public class BoxUser extends Model<BoxUser> {

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名称
     */
    @ApiModelProperty(value="用户名称")
    private String username;

    /**
     * 用户密码
     */
    @ApiModelProperty(value="用户密码" , hidden = true)
    private String password;

    /**
     * 性别：0 未知， 1男， 1 女
     */
    @ApiModelProperty(value="性别：0 未知， 1男， 1 女")
    private Integer gender;

    /**
     * 生日
     */
    @ApiModelProperty(value="生日")
    private Long birthday;

    /**
     * 最近一次登录时间
     */
    @ApiModelProperty(value="最近一次登录时间")
    private Long lastLoginTime;

    /**
     * 最近一次登录IP地址
     */
    @ApiModelProperty(value="最近一次登录IP地址")
    private String lastLoginIp;

    /**
     * 用户昵称或网络名称
     */
    @ApiModelProperty(value="用户昵称或网络名称")
    private String nickname;

    /**
     * 用户手机号码
     */
    @ApiModelProperty(value="用户手机号码")
    private String mobile;

    /**
     * 用户头像图片
     */
    @ApiModelProperty(value="用户头像图片")
    private String userpic;

    /**
     * 微信登录openid
     */
    @ApiModelProperty(value="微信登录openid")
    private String weixinOpenid;

    /**
     * 微信登录会话KEY
     */
    @ApiModelProperty(value="微信登录会话KEY")
    private String sessionKey;

    /**
     * 0 可用, 1 禁用, 2 注销
     */
    @ApiModelProperty(value="0 可用, 1 禁用, 2 注销")
    private Integer status;

    /**
     * 创建时间 
     */
    @ApiModelProperty(value="创建时间", hidden = true)
    private Long createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间", hidden = true)
    private Long updateTime;

    /**
     * 逻辑删除
     */
    @ApiModelProperty(value="逻辑删除", hidden = true)
    private Integer deleted;

}
