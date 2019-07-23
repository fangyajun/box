package com.kuose.box.wx.login.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * box_user
 * </p>
 *
 * @author fangyajun
 * @since 2019-07-09
 */
@ApiModel(value="用户对象模型")
public class BoxUser extends Model<BoxUser> {

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value="id",example = "123")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名称
     */
    @ApiModelProperty(value="用户账号" ,required=true)
    private String username;

    /**
     * 用户密码
     */
    @ApiModelProperty(value="密码" ,required=true)
    private String password;

    /**
     * 性别：0 未知， 1男， 1 女
     */
    @ApiModelProperty(value="用户性别" ,required=true, example = "123")
    private Integer gender;

    /**
     * 生日
     */
    private Long birthday;

    /**
     * 最近一次登录时间
     */
    private Long lastLoginTime;

    /**
     * 最近一次登录IP地址
     */
    private String lastLoginIp;

    /**
     * 用户昵称或网络名称
     */
    private String nickname;

    /**
     * 用户手机号码
     */
    private String mobile;

    /**
     * 用户头像图片
     */
    private String userpic;

    /**
     * 微信登录openid
     */
    private String weixinOpenid;

    /**
     * 微信登录会话KEY
     */
    private String sessionKey;

    /**
     * 0 可用, 1 禁用, 2 注销
     */
    private Integer status;

    public Long getBirthday() {
        return birthday;
    }

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    public Long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 创建时间 
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 逻辑删除
     */
    private Integer deleted;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    public String getWeixinOpenid() {
        return weixinOpenid;
    }

    public void setWeixinOpenid(String weixinOpenid) {
        this.weixinOpenid = weixinOpenid;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "BoxUser{" +
        "id=" + id +
        ", username=" + username +
        ", password=" + password +
        ", gender=" + gender +
        ", birthday=" + birthday +
        ", lastLoginTime=" + lastLoginTime +
        ", lastLoginIp=" + lastLoginIp +
        ", nickname=" + nickname +
        ", mobile=" + mobile +
        ", userpic=" + userpic +
        ", weixinOpenid=" + weixinOpenid +
        ", sessionKey=" + sessionKey +
        ", status=" + status +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", deleted=" + deleted +
        "}";
    }
}
