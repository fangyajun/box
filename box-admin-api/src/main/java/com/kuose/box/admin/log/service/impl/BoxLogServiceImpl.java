package com.kuose.box.admin.log.service.impl;

import com.kuose.box.admin.log.entity.BoxLog;
import com.kuose.box.admin.log.dao.BoxLogMapper;
import com.kuose.box.admin.log.service.BoxLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志表 服务实现类
 * </p>
 *
 * @author fangyajun
 * @since 2019-07-23
 */
@Service
public class BoxLogServiceImpl extends ServiceImpl<BoxLogMapper, BoxLog> implements BoxLogService {

}
