package com.zhk.examonline.viewmodel;

import com.zhk.examonline.utility.ModelMapperSingle;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class BaseVM {
    protected static ModelMapper modelMapper = ModelMapperSingle.Instance();


}
