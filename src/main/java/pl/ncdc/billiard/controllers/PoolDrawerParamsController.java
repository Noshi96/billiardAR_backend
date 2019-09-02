package pl.ncdc.billiard.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.ncdc.billiard.entities.PoolDrawerParamsEntity;
import pl.ncdc.billiard.mappers.PoolDrawerParamsMapper;
import pl.ncdc.billiard.service.PoolDrawerParamsService;

@RestController
@RequestMapping("/poolDrawerParams")
@CrossOrigin(value = "*")
public class PoolDrawerParamsController {

    private final PoolDrawerParamsService poolDrawerParamsService;
    private final PoolDrawerParamsMapper poolDrawerParamsMapper;

    @Autowired
    public PoolDrawerParamsController(PoolDrawerParamsService poolDrawerParamsService, PoolDrawerParamsMapper poolDrawerParamsMapper) {
        this.poolDrawerParamsService = poolDrawerParamsService;
        this.poolDrawerParamsMapper = poolDrawerParamsMapper;
    }

    @GetMapping("/get")
    public PoolDrawerParamsEntity getPoolDrawerParams() {
        return poolDrawerParamsMapper.toEntity(poolDrawerParamsService.getPoolDrawerParams());
    }

    @PutMapping("/update")
    public PoolDrawerParamsEntity getPoolDrawerParams(@RequestBody PoolDrawerParamsEntity poolDrawerParams) {
        return poolDrawerParamsMapper.toEntity(poolDrawerParamsService.save(poolDrawerParamsMapper.toModel(poolDrawerParams)));
    }
}
