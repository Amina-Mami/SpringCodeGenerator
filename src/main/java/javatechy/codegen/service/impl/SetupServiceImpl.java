package javatechy.codegen.service.impl;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javatechy.codegen.service.SetupService;

/**
 * Service implementation of the {@link SetupService}
 * 
 * @author deepak.kumar
 *
 */
@Service
public class SetupServiceImpl implements SetupService {

    public static Logger logger = Logger.getLogger(SetupServiceImpl.class);

    /**
     * {@inheritDoc}
     */
    @PostConstruct
    @Override
    public void postSetup() {
    }
}
