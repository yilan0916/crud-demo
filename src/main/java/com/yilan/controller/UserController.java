package com.yilan.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yilan.common.HttpCodeEnum;
import com.yilan.common.ResponseResult;
import com.yilan.domain.entity.User;
import com.yilan.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "用户控制器", tags = "User的crud接口")
@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "根据id查询User", notes = "Restful格式读取id")
    @GetMapping("/get/{id}")
    public ResponseResult<?> getById(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        return ResponseResult.okResult(user);
    }

    @GetMapping("/list")
    public ResponseResult<?> list() {
        List<User> users = userService.list();
        return ResponseResult.okResult(users);
    }

    @GetMapping("/list/ids")
    public ResponseResult<?> listByIds(@RequestParam("ids") List<Long> ids) {
        List<User> users = userService.listByIds(ids);
        return ResponseResult.okResult(users);
    }

    @GetMapping("/count")
    public ResponseResult<?> count() {
        long count = userService.count();
        return ResponseResult.okResult(count);
    }

    @GetMapping("/count/name")
    public ResponseResult<?> countByName(@RequestParam("name") String name) {
        Long count = userService.countByName(name);
        return ResponseResult.okResult(count);
    }

    @GetMapping("/page")
    public ResponseResult<?> page(@RequestParam("pageNum") int pageNum,
                                  @RequestParam("pageSize") int paseSize) {
        Page<User> page = userService.page(new Page<>(pageNum, paseSize));
        return ResponseResult.okResult(page);
    }

    @GetMapping("/page/search")
    public ResponseResult<?> pageBySearch(@RequestParam("pageNum") int pageNum,
                                          @RequestParam("pageSize") int paseSize,
                                          @RequestParam(value = "search", required = false) String search) {
        Page<User> page = userService.pageBySearch(pageNum, paseSize, search);
        return ResponseResult.okResult(page);
    }

    @PostMapping("/save")
    ResponseResult<?> save(@RequestBody User user) {
        boolean save = userService.saveOrUpdate(user);
        return save ? ResponseResult.okResult() : ResponseResult.errorResult(HttpCodeEnum.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/remove/{id}")
    ResponseResult<?> remove(@PathVariable Long id) {
        boolean remove = userService.removeById(id);
        return remove ? ResponseResult.okResult() : ResponseResult.errorResult(HttpCodeEnum.INTERNAL_SERVER_ERROR);

    }
}
