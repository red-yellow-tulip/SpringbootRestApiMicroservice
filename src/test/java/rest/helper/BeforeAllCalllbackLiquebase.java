package rest.helper;

import liquibase.integration.spring.SpringLiquibase;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.annotation.Autowired;

public class BeforeAllCalllbackLiquebase implements BeforeAllCallback {

    @Autowired
    private SpringLiquibase springLiquibase;

    @Override
    public void beforeAll(ExtensionContext extensionContext)  {


    }

}
