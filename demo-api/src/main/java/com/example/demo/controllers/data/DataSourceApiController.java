package com.example.demo.controllers.data;

import com.example.demo.controllers.BaseController;
import com.example.demo.models.data.DataSource;
import com.example.demo.repositories.data.DataSourceRepository;
import com.example.demo.utils.json.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
public class DataSourceApiController extends BaseController {

    private final DataSourceRepository dataSourceRepository;

    @Autowired
    public DataSourceApiController(DataSourceRepository dataSourceRepository) {
        this.dataSourceRepository = dataSourceRepository;
    }

    @GetMapping(path = "/api/v1/data/sources", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String all() {
        List<DataSource> dataSourceList = dataSourceRepository.findAll();
        return JsonUtils.toJsonArray(dataSourceList).toString();
    }

    @GetMapping(path = "/api/v1/data/sources/{id}", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String byId(@PathVariable String id, HttpServletResponse response) {
        Optional<DataSource> dataSourceOpt = dataSourceRepository.findById(id);
        return processOptionalJsonable(dataSourceOpt, response);
    }

}
