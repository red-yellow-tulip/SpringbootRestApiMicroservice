package base.web;

import base.datasource.memDb.RedisService;
import base.datasource.sqlDb.DatabaseService;
import base.utils.logging.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.Resource;

public abstract class BaseController {

    @Autowired
    protected DatabaseService databaseService;

    @Autowired
    protected RedisService redisService;

    @Resource
    private LoggerService loggerService;

    protected UserDetails getCurrentUser(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return  (UserDetails) authentication.getPrincipal();
    }

    protected void logCurrentUserDetails() {
        UserDetails user = getCurrentUser();
        loggerService.log().info(user);
    }



}
