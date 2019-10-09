package com.kuose.box.wx.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuose.box.db.order.dao.BoxOrderCommentMapper;
import com.kuose.box.db.order.entity.BoxOrderComment;
import com.kuose.box.wx.order.service.BoxOrderCommentService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单评论表 服务实现类
 * </p>
 *
 * @author 魔舞清华
 * @since 2019-10-07
 */
@Service
public class BoxOrderCommentServiceImpl extends ServiceImpl<BoxOrderCommentMapper, BoxOrderComment> implements BoxOrderCommentService {

}
