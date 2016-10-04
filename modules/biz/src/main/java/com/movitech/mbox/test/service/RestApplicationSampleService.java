package com.movitech.mbox.test.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alex.Chen
 */
@Service
@Transactional
public class RestApplicationSampleService {

    public String createToken(){
        return "123456";
    }
}
