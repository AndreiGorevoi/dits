/*
@author Andrei Gorevoi
*/
package com.controller.literature;

import com.model.Literature;
import com.service.literature.LiteratureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("literature")
public class LiteratureRestController {
    private final LiteratureService literatureService;

    @Autowired
    public LiteratureRestController(LiteratureService literatureService) {
        this.literatureService = literatureService;
    }

    @PutMapping(value = "/")
    public Literature addLiterature(@RequestBody Literature literature){
        return literatureService.addLiterature(literature);
    }
    @DeleteMapping(value = "/{id}")
    public void deleteLiterature(@PathVariable int id){
        literatureService.deleteLiteratureById(id);
    }
    @GetMapping(value = "/")
    public List<Literature> getAllLiterature(){
        return literatureService.getAllLiterature();
    }
    @PostMapping(value = "/{id}")
    public Literature getLiteratureById(@PathVariable int id){
        return literatureService.getLiteratureById(id);
    }
    @PostMapping(value = "/byquestion/{id}")
    public List<Literature> getLiteratureForQuestion(@PathVariable int id){
        return literatureService.getAllLiteratureByQuestionId(id);
    }

}
