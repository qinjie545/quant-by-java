/**
 * ymm56.com Inc.
 * Copyright (c) 2013-2022 All Rights Reserved.
 */
package com.marketboxer.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jamesqin mail:qinjie@amh-group.com
 * @version : HelloWorldController.java, v 0.1 2022-09-08 11:51 jamesqin Exp $$
 */
@RestController("/hello")
public class HelloWorldController {

    @GetMapping("/")
    public String helloworld(){
        return "Hello welcome!";
    }

    @GetMapping(path = "{name}")
    public String helloworld(@PathVariable("name") String name){
        return String.format("%s %s", "Hello", name);
    }


}