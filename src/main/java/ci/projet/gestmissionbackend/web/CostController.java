package ci.projet.gestmissionbackend.web;

import ci.projet.gestmissionbackend.Component.MSG;
import ci.projet.gestmissionbackend.dtos.AssignmentDTO;
import ci.projet.gestmissionbackend.dtos.CostsDTO;
import ci.projet.gestmissionbackend.exceptions.assignmentNotFoundException;
import ci.projet.gestmissionbackend.services.CostService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;


@RestController
@Slf4j
@AllArgsConstructor
@CrossOrigin
public class CostController {
    private CostService costService;
    private MSG msg;
    public static final String DIRECTORY = "./download";
    @Autowired
    private ServletContext context;
    @PostMapping("/costs")
    public CostsDTO saveCost(@RequestBody CostsDTO costsDTO , HttpServletResponse response) throws IOException {

        CostsDTO save = this.costService.save(costsDTO);
        Path filePath = get(DIRECTORY).toAbsolutePath().normalize().resolve(save.getCostsId().toString());

        costService.printCost(save);
        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "inline; filename=" + save.getCostsId() + ".pdf" );

        File downloadDir = new File(msg.getMessage("application.mission.downloaddir"));
        InputStream is = Files.newInputStream(Paths.get(downloadDir.getPath() + "/" + save.getCostsId() + ".pdf"));

        org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
        response.flushBuffer();
        return save ;
    }
   /* @GetMapping("/costs")
    public List<CostsDTO> costs(){return this.costService.findAll();}*/

    @PutMapping("/costs/assignment")
    public CostsDTO setCostWithAssignment(@RequestBody AssignmentDTO assignmentDTO){
        return this.costService.setDecompteWithMission(assignmentDTO);
    }

    @GetMapping("/costs/download/{costName}")
    public ResponseEntity<Resource> download(@PathVariable("costName") String costname) throws IOException {
       Path filePath = get(DIRECTORY).toAbsolutePath().normalize().resolve(costname);
       if(!Files.exists(filePath)){

           throw new FileNotFoundException(costname + " was not found on the server");
       }
       Resource resource = new UrlResource(filePath.toUri());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("File-Name",costname);
        httpHeaders.add(CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());
        System.out.println();
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                .headers(httpHeaders).body(resource);
    }
   /* @GetMapping("/pdf/generateCost/{costId}")
    public void printAssignment(@PathVariable ("costId") Long costId , HttpServletResponse response) throws IOException, assignmentNotFoundException {


       // AssignmentDTO assignmentDTO = assignmentService.findAssignmentById(assignId);
        CostsDTO costsDTO = costService.findCostById(costId);
        // Path filePath = get(DIRECTORY).toAbsolutePath().normalize().resolve(assignmentDTO.getUuid());

        costService.printCost(costsDTO);
        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "inline; filename=" + costsDTO.getCostsId() + ".pdf" );

        File downloadDir = new File(msg.getMessage("application.mission.downloaddir"));
        InputStream is = Files.newInputStream(Paths.get(downloadDir.getPath() + "/" + costsDTO.getCostsId() + ".pdf"));

        org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
        response.flushBuffer();
    }*/

    

    @GetMapping("/costs/calculi")
    public CostsDTO calculTotal(){
        return this.costService.calculTotal();
    }
    @GetMapping("/costs/cost")
    public CostsDTO getCost(){
        return this.costService.getCost();
    }
    @DeleteMapping("/costs")
    public void deleteCost(@RequestBody CostsDTO costsDTO){
        this.costService.delete(costsDTO);
    }
}
