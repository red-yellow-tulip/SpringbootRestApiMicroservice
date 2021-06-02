package base.web;

import base.datasource.DatabaseService;
import base.logging.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

public abstract class BaseController {

    @Autowired
    protected DatabaseService databaseService;

    @Resource
    private LoggerService loggerService;


}
