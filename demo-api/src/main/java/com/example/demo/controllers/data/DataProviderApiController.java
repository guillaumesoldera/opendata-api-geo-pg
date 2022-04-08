package com.example.demo.controllers.data;

import com.example.demo.controllers.BaseController;
import com.example.demo.models.data.DataProvider;
import com.example.demo.repositories.data.DataProviderRepository;
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
public class DataProviderApiController extends BaseController {

    private final DataProviderRepository dataProviderRepository;

    @Autowired
    public DataProviderApiController(DataProviderRepository dataProviderRepository) {
        this.dataProviderRepository = dataProviderRepository;
    }

    @GetMapping(path = "/api/v1/data/providers", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String all() {
        List<DataProvider> dataProviderList = dataProviderRepository.findAll();
        return JsonUtils.toJsonArray(dataProviderList).toString();
    }

    @GetMapping(path = "/api/v1/data/providers/{id}", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String byId(@PathVariable String id, HttpServletResponse response) {
        Optional<DataProvider> dataProviderOpt = dataProviderRepository.findById(id);
        return processOptionalJsonable(dataProviderOpt, response);
    }

}
