package com.kuose.box.wx.login.controller;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.*;
import com.kuose.box.db.user.entity.BoxUser;
import com.kuose.box.wx.common.notify.NotifyService;
import com.kuose.box.wx.common.notify.NotifyType;
import com.kuose.box.wx.common.service.CaptchaCodeManager;
import com.kuose.box.wx.common.service.UserTokenManager;
import com.kuose.box.wx.login.service.BoxUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * box_user 前端控制器
 * </p>
 *
 * @author fangyajun
 * @since 2019-07-09
 */
@Api(tags = {"用户登录,微信登录，手机验证码登录接口文档"})
@RestController
@RequestMapping("/boxUser")
public class BoxUserController {

    @Autowired
    private BoxUserService boxUserService;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private WxMaService wxService;


    /**
     * 请求注册验证码
     *
     * TODO
     * 这里需要一定机制防止短信验证码被滥用
     *
     * @param body 手机号码 { mobile }
     * @return
     */
    @ApiOperation(value="请求注册验证码")
    @ApiImplicitParam(name = "phoneNumber", value = "手机号码", required = true, dataType = "String", paramType = "path", example = "15000273684")
    @PostMapping("regCaptcha")
    public Result registerCaptcha(@RequestBody String body) {
        String phoneNumber = JacksonUtil.parseString(body, "phoneNumber");
        if (StringUtils.isEmpty(phoneNumber)) {
            return Result.failure("参数为空");
        }
        if (!RegexUtil.isMobileExact(phoneNumber)) {
            return Result.failure(601,"请填写正确的手机号码");
        }

        if (!notifyService.isSmsEnable()) {
            return Result.failure(602,"小程序后台验证码服务不支持");
        }

        String code = CharUtil.getRandomNum(6);
        notifyService.notifySmsTemplate(phoneNumber, NotifyType.CAPTCHA, new String[]{code,"1"});
        boolean successful = CaptchaCodeManager.addToCache(phoneNumber, code);
        if (!successful) {
            return Result.failure(603,"验证码未超时1分钟，不能发送");
        }
        System.out.println();
        return Result.success();
    }

    /**
     * 手机验证码登录
     *
     * @param body
     * @return 登录结果
     */
    @ApiOperation(value="手机验证码登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phoneNumber", value = "手机号码", required = true, dataType = "String", paramType = "path", example = "15000273684"),
            @ApiImplicitParam(name = "authCode", value = "验证码", required = true, dataType = "String", paramType = "path", example = "540021")
    })
    @PostMapping("phoneNumberLogin")
    public Result phoneNumberLogin(@RequestBody String body, HttpServletRequest request) {
        String phoneNumber = JacksonUtil.parseString(body, "phoneNumber");
        String authCode = JacksonUtil.parseString(body, "authCode");

        if (StringUtil.isBlank(phoneNumber ) || StringUtil.isBlank(authCode)) {
            return Result.failure("参数不能为空");
        }

        String cachedCaptcha = CaptchaCodeManager.getCachedCaptcha(phoneNumber);
        if (StringUtil.isBlank(cachedCaptcha) || !authCode.equals(cachedCaptcha)) {
            return Result.failure(601,"请填写正确的验证码");
        }

        //
        QueryWrapper<BoxUser> boxUserQueryWrapper = new QueryWrapper<>();
        List<BoxUser> boxUserList = boxUserService.list(boxUserQueryWrapper.eq("mobile", phoneNumber));
        BoxUser user = null;
        if (boxUserList.size() > 1) {
            return Result.failure(500,"系统内部错误");
        } else if (boxUserList.size() == 0) {
            // 新增用户
            user = new BoxUser();
            user.setLastLoginTime(System.currentTimeMillis());
            user.setLastLoginIp(IpUtil.getIpAddr(request));
            user.setMobile(phoneNumber);
            user.setCreateTime(System.currentTimeMillis());
            user.setUpdateTime(System.currentTimeMillis());
            // 0 可用, 1 禁用, 2 注销',
            user.setStatus(0);

            if (!boxUserService.save(user)) {
                return Result.failure(501,"登录失败");
            }

            // TODO 新用户发送优惠券
        } else {
            user = boxUserList.get(0);
            // 更新登录情况
            user.setLastLoginTime(System.currentTimeMillis());
            user.setLastLoginIp(IpUtil.getIpAddr(request));
            if (!boxUserService.updateById(user)) {
                return Result.failure(501,"更新数据失败");
            }
        }
        // token
        String token = UserTokenManager.generateToken(user.getId());

        Map<Object, Object> result = new HashMap();
        result.put("token", token);
        result.put("boxUser", user);
        return Result.success().setData("result", result);
    }

    /**
     * 微信手机号码绑定
     *
     * @param
     * @param
     * @return
     */
    @ApiOperation(value="微信手机号码绑定")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phoneNumber", value = "手机号码", required = true, dataType = "String", paramType = "path", example = "15000273684"),
            @ApiImplicitParam(name = "authCode", value = "验证码", required = true, dataType = "String", paramType = "path", example = "540021"),
            @ApiImplicitParam(name = "gender", value = "性别：0 未知，1男，1 女", required = true, dataType = "int", paramType = "path", example = "0"),
            @ApiImplicitParam(name = "userPic", value = "用户头像url", required = true, dataType = "String", paramType = "path", example = "http://xxxxxx/xx/xxx.jpg"),
            @ApiImplicitParam(name = "nickName", value = "微信昵称", required = true, dataType = "String", paramType = "path", example = "魔舞清华"),
            @ApiImplicitParam(name = "code", value = "微信登录凭证code", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("bindPhone")
    public Result bindPhone(@RequestBody String body ,HttpServletRequest request) {
        String phoneNumber = JacksonUtil.parseString(body, "phoneNumber");
        String authCode = JacksonUtil.parseString(body, "authCode");
        Integer gender = JacksonUtil.parseInteger(body, "gender");
        String userPic = JacksonUtil.parseString(body, "userPic");
        String nickName = JacksonUtil.parseString(body, "nickName");
        String code = JacksonUtil.parseString(body, "code");

        if (StringUtil.isBlank(phoneNumber) || gender == null || StringUtil.isBlank(userPic) ||  StringUtil.isBlank(nickName) || StringUtil.isBlank(code)) {
            return Result.failure("缺少必传参数");
        }

        String cachedCaptcha = CaptchaCodeManager.getCachedCaptcha(phoneNumber);
        if (StringUtil.isBlank(cachedCaptcha) || !authCode.equals(cachedCaptcha)) {
            return Result.failure(601,"请填写正确的验证码");
        }

        String sessionKey = null;
        String openId = null;
        try {
            WxMaJscode2SessionResult result = this.wxService.getUserService().getSessionInfo(code);
            sessionKey = result.getSessionKey();
            openId = result.getOpenid();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (sessionKey == null || openId == null) {
            return Result.failure("错误，未能获取到用户的openid");
        }

        BoxUser boxUser = boxUserService.getOne(new QueryWrapper<BoxUser>().eq("mobile", phoneNumber));
        if (boxUser == null ) {
            // 新增
            boxUser = new BoxUser();
            // 0 可用, 1 禁用, 2 注销',
            boxUser.setStatus(0);
            boxUser.setUpdateTime(System.currentTimeMillis());
            boxUser.setCreateTime(System.currentTimeMillis());
            boxUser.setLastLoginTime(System.currentTimeMillis());
            boxUser.setMobile(phoneNumber);
            boxUser.setLastLoginIp(IpUtil.getIpAddr(request));
            // 性别：0 未知， 1男， 1 女',
            boxUser.setGender(gender);
            boxUser.setNickname(nickName);
            boxUser.setUserpic(userPic);
            boxUser.setSessionKey(sessionKey);
            boxUser.setWeixinOpenid(openId);

            if (!boxUserService.save(boxUser)) {
                return Result.failure(501,"绑定失败");
            }
            // TODO 新用户发送优惠券
        } else {
            // 更新
            boxUser.setSessionKey(sessionKey);
            boxUser.setWeixinOpenid(openId);
            boxUser.setNickname(nickName);
            boxUser.setUserpic(userPic);
            boxUser.setGender(gender);
            boxUser.setLastLoginTime(System.currentTimeMillis());
            boxUser.setLastLoginIp(IpUtil.getIpAddr(request));
            boxUser.setUpdateTime(System.currentTimeMillis());

            if (!boxUserService.updateById(boxUser)) {
                return Result.failure(501,"绑定失败");
            }
        }

        // token
        String token = UserTokenManager.generateToken(boxUser.getId());

        Map<Object, Object> result = new HashMap<Object, Object>();
        result.put("token", token);
        result.put("boxUser", boxUser);
        return Result.success().setData("result", result);
    }

    /**
     * 微信登录
     *
     * @param
     * @param request     请求对象
     * @return 登录结果
     */
    @ApiOperation(value="微信登录", notes = "微信登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "微信登录凭证code", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "gender", value = "性别：0 未知，1男，1 女", required = true, dataType = "int", paramType = "path", example = "0"),
            @ApiImplicitParam(name = "userPic", value = "用户头像url", required = true, dataType = "String", paramType = "path", example = "http://xxxxxx/xx/xxx.jpg"),
            @ApiImplicitParam(name = "nickName", value = "微信昵称", required = true, dataType = "String", paramType = "path", example = "魔舞清华")
    })
    @PostMapping("login_by_weixin")
    public Result loginByWeixin(@RequestBody String body, HttpServletRequest request) {
        String code = JacksonUtil.parseString(body, "code");
        Integer gender = JacksonUtil.parseInteger(body, "gender");
        String userPic = JacksonUtil.parseString(body, "userPic");
        String nickName = JacksonUtil.parseString(body, "nickName");
        if (StringUtil.isBlank(code) || gender == null || StringUtil.isBlank(userPic) || StringUtil.isBlank(nickName)) {
            return Result.failure("缺少必传参数");
        }

        String sessionKey = null;
        String openId = null;
        try {
            WxMaJscode2SessionResult result = this.wxService.getUserService().getSessionInfo(code);
            sessionKey = result.getSessionKey();
            openId = result.getOpenid();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (sessionKey == null || openId == null) {
            return Result.failure("错误，未能获取到用户的openid");
        }

        BoxUser boxUser = boxUserService.getOne(new QueryWrapper<BoxUser>().eq("weixin_openid", openId));
        if (boxUser == null || StringUtil.isBlank(boxUser.getMobile())) {
            return Result.failure(601,"此微信号未绑定手机号，请先绑定");
        }

        // token
        String token = UserTokenManager.generateToken(boxUser.getId());

        Map<Object, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("boxUser", boxUser);
        return Result.success().setData("result" ,result);
    }


}

