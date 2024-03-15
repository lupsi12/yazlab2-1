package com.example.yazlab2_1.Controllers;

import com.example.yazlab2_1.Services.SequenceGeneratorService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/sequence")
@AllArgsConstructor
@NoArgsConstructor
public class SequenceController {
    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

    @RequestMapping(method = RequestMethod.DELETE)
    @DeleteMapping
    public void deleteAllSequence(){
        sequenceGeneratorService.deleteAllSequence();
    }
}
