package com.zhk.examonline.base;



import com.zhk.examonline.context.WebContext;
import com.zhk.examonline.domain.User;
import com.zhk.examonline.utility.ModelMapperSingle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseApiController {
    protected final static String DEFAULT_PAGE_SIZE = "10";
    protected final static ModelMapper modelMapper = ModelMapperSingle.Instance();
    @Autowired
    protected WebContext webContext;

    protected User getCurrentUser() {
        return webContext.getCurrentUser();
    }
}
