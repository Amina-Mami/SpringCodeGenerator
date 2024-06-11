package javatechy.codegen.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javatechy.codegen.dto.Request;
import javatechy.codegen.service.DatabaseService;
import javatechy.codegen.service.FileUtilService;

@Service
public class DatabaseServiceImpl implements DatabaseService {

    @Autowired
    private FileUtilService fileUtilService;

    @Override
    public void addDatabaseProps(Request request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addDatabaseDto(Request request) {
        // TODO Auto-generated method stub

    }

}
