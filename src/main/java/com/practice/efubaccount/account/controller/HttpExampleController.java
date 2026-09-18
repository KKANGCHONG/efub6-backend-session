package com.practice.efubaccount.account.controller;

import com.practice.efubaccount.account.dto.ExampleDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("hello")
public class HttpExampleController {


    @GetMapping("/obj")
    public ExampleDto hello(@RequestParam("name") String name,
                            @RequestParam("id") Long id,
                            @RequestParam("password") String password) {
        ExampleDto exampleDto = new ExampleDto();

        exampleDto.setName(name);
        exampleDto.setId(id);
        exampleDto.setPassword(password);

        return exampleDto;
    }
}