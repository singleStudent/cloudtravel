package com.cloudtravel.producer.controller;

import com.cloudtravel.producer.dao.IBaseUserDao;
import com.cloudtravel.producer.dao.IBaseUserEsDao;
import com.cloudtravel.producer.model.BaseUserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("es")
@ResponseBody
public class EsTestController {

    @Autowired
    IBaseUserEsDao baseUserEsDao;

    @Autowired
    IBaseUserDao baseUserDao;

    @GetMapping("select")
    public Object selectUserByEs() {
        List<BaseUserModel> baseUserModelList = baseUserDao.selectAll();
        //同步
        baseUserEsDao.saveAll(baseUserModelList);
        return "1";
    }

    @GetMapping("selectAll")
    public Object selectAllFromEs() {
        Iterable<BaseUserModel> list  =  baseUserEsDao.findAll();
        return list;
    }

    @PostMapping
    public String delete(@RequestParam Long id) {
        baseUserDao.deleteByPrimaryKey(id);
        //同步
        baseUserEsDao.deleteById(id);
        return "1";
    }
}
