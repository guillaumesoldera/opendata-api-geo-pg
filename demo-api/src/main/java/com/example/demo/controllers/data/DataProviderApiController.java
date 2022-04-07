package com.example.demo.controllers.data;

import com.example.demo.controllers.BaseController;
import com.example.demo.models.data.DataProvider;
import com.example.demo.repositories.data.DataProviderRepository;
import com.example.demo.utils.json.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
public class DataProviderApiController extends BaseController {

    private final DataProviderRepository dataProviderRepository;

    @Autowired
    public DataProviderApiController(DataProviderRepository dataProviderRepository) {
        this.dataProviderRepository = dataProviderRepository;
    }

    @Async
    @GetMapping(path = "/api/v1/data/providers", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CompletableFuture<String> all() {
        CompletableFuture<List<DataProvider>> dataProviderListFuture = dataProviderRepository.findAll();
        return dataProviderListFuture.thenApply(dataProviderList -> JsonUtils.toJsonArray(dataProviderList).toString());
    }

    @Async
    @GetMapping(path = "/api/v1/data/providers/{id}", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CompletableFuture<String> byId(@PathVariable String id, HttpServletResponse response) {
        CompletableFuture<Optional<DataProvider>> dataProviderOptFuture = dataProviderRepository.findById(id);
        return dataProviderOptFuture.thenApply(dataProviderOpt -> processOptionalJsonable(dataProviderOpt, response));
    }

}
