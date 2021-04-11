package base.web;

import base.datasource.DatabaseService;
import base.utils.logging.LoggerAspectConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public abstract class BaseController {

    @Autowired
    protected DatabaseService databaseService;

    protected static final Logger log = LogManager.getLogger(BaseController.class.getName());

    protected UserDetails getCurrentUser(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return  (UserDetails) authentication.getPrincipal();
    }

    protected void logCurrentUserDetails() {
        UserDetails user = getCurrentUser();
        log.info(user);
    }



}
