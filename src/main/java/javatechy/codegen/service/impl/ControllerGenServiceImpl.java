package javatechy.codegen.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javatechy.codegen.common.Common;
import javatechy.codegen.controller.CodeGenController;
import javatechy.codegen.dto.Controller;
import javatechy.codegen.dto.Request;
import javatechy.codegen.service.ControllerGenService;
import javatechy.codegen.service.FileUtilService;

@Service
public class ControllerGenServiceImpl implements ControllerGenService {
    private Logger logger = Logger.getLogger(CodeGenController.class);

    @Autowired
    private FileUtilService fileUtilService;

    @Override
    public void generateControllers(Request request) throws IOException {


    }
}
