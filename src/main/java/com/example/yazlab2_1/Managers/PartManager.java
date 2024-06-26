package com.example.yazlab2_1.Managers;

import com.example.yazlab2_1.Entities.Part;
import com.example.yazlab2_1.Repository.PartRepo;
import com.example.yazlab2_1.Requests.PartUpdateRequest;
import com.example.yazlab2_1.SearchAndScrapper;
import com.example.yazlab2_1.Services.PartService;
import com.example.yazlab2_1.Services.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PartManager implements PartService {
    private PartRepo partRepo;
    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    public PartManager(PartRepo partRepo, SequenceGeneratorService sequenceGeneratorService) {
        this.partRepo = partRepo;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    @Override
    public List<Part> getAllPart() {
        List<Part> partList = partRepo.findAll();
        return partList;
    }

    @Override
    public Part getPartById(Long partId) {
        Part part = partRepo.findById(partId).orElse(null);
        return part;
    }

    @Override
    public Part addPart(Part part) {
        Part newPart = new Part();
        newPart.setId(sequenceGeneratorService.getSquenceNumber(part.SEQUENCE_NAME));
        newPart.setKelime(part.getKelime());
        newPart.setDuzelenKelime(part.getDuzelenKelime());
        newPart.setAutoPdf(part.isAutoPdf());
        newPart.setHazir(part.isHazir());
        newPart.setEnableSerpAPI(part.isEnableSerpAPI());
        newPart.setDownloadPdfRequest(part.isDownloadPdfRequest());
        newPart.setMaxArticleCount(part.getMaxArticleCount());
        newPart.setFoundArticleCount(part.getFoundArticleCount());

        Part savedPart = partRepo.save(newPart);
        //SearchAndScrapper.SearchAndScrap(savedPart);
        new Thread(new SearchAndScrapper(savedPart)).start();

        return savedPart;
    }

    @Override
    public void deletePart(Long partId) {
        partRepo.deleteById(partId);
    }

    @Override
    public void deleteAllPart() {
       partRepo.deleteAll();
    }

    @Override
    public Part updatePart(Long id, PartUpdateRequest partUpdateRequest) {
        Optional<Part> part = partRepo.findById(id);
        if(part.isPresent()){
            Part foundPart = part.get();
            foundPart.setKelime(partUpdateRequest.getKelime());
            foundPart.setDuzelenKelime(partUpdateRequest.getDuzelenKelime());
            foundPart.setAutoPdf(partUpdateRequest.isAutoPdf());
            foundPart.setHazir(partUpdateRequest.isHazir());
            foundPart.setEnableSerpAPI(partUpdateRequest.isEnableSerpAPI());
            foundPart.setDownloadPdfRequest(partUpdateRequest.isDownloadPdfRequest());
            foundPart.setMaxArticleCount(partUpdateRequest.getMaxArticleCount());
            foundPart.setFoundArticleCount(partUpdateRequest.getFoundArticleCount());

            partRepo.save(foundPart);
            return foundPart;
        }else
            return null;
    }
}
