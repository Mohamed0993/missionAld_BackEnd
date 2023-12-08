package ci.projet.gestmissionbackend.web;

import ci.projet.gestmissionbackend.Component.MSG;
import ci.projet.gestmissionbackend.dtos.AssignmentDTO;
import ci.projet.gestmissionbackend.dtos.PersonalDTO;
import ci.projet.gestmissionbackend.exceptions.PersonalNotFoundException;
import ci.projet.gestmissionbackend.exceptions.assignmentNotFoundException;
import ci.projet.gestmissionbackend.exceptions.personalHasPlannedMissionException;
import ci.projet.gestmissionbackend.exceptions.worksiteNotFoundException;
import ci.projet.gestmissionbackend.services.AssignmentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static ci.projet.gestmissionbackend.web.CostController.DIRECTORY;
import static java.nio.file.Paths.get;

@RestController
@Slf4j
@AllArgsConstructor
@CrossOrigin()
public class AssignmentController {
    private AssignmentService assignmentService;

    private MSG msg;

    @PostMapping("/assignments")
    @PostAuthorize("hasAuthority('USER')")
    public AssignmentDTO saveAssignment(@RequestBody AssignmentDTO assignmentDTO) throws worksiteNotFoundException, personalHasPlannedMissionException, PersonalNotFoundException, assignmentNotFoundException {
        return this.assignmentService.addNewAssignment(assignmentDTO);
    }

    @GetMapping("/assignments")
    @PostAuthorize("hasAuthority('USER')")
    public List<AssignmentDTO> assignments(){
        return assignmentService.listAssignment();
    }


    @GetMapping("/assignmentsPers")
    @PostAuthorize("hasAuthority('USER')")
    public List<AssignmentDTO> assignmentsPer() throws PersonalNotFoundException {
        return assignmentService.getMissionsForEMP();
    }
    @GetMapping("/assignmentsChef")
    @PostAuthorize("hasAuthority('USER')")
    public List<AssignmentDTO> assignmentsChef() throws PersonalNotFoundException {
        return assignmentService.getMissionsForCHEF();
    }
    @GetMapping("/assignmentsDG")
    @PostAuthorize("hasAuthority('USER')")
    public List<AssignmentDTO> assignmentsDG() throws PersonalNotFoundException {
        return assignmentService.getMissionsForDG();
    }
    @GetMapping("/assignmentsDAF")
    @PostAuthorize("hasAuthority('USER')")
    public List<AssignmentDTO> assignmentsDAF() throws PersonalNotFoundException {
        return assignmentService.getMissionsForDAF();
    }
    @GetMapping("/assignmentsAll")
    @PostAuthorize("hasAuthority('USER')")
    public List<AssignmentDTO> findAll(boolean mine) throws PersonalNotFoundException {
        return assignmentService.findAll(mine);
    }
    @GetMapping("/assignments/{id}")
    @PostAuthorize("hasAuthority('USER')")
    public AssignmentDTO getAssignment(@PathVariable(name = "id") Long assignmentId) throws assignmentNotFoundException {
        return assignmentService.findAssignmentById(assignmentId);
    }
    @PutMapping ("/assignments/validate")
    @PostAuthorize("hasAuthority('USER')")
    public AssignmentDTO validateAssignment(@RequestBody AssignmentDTO assignmentDTO){
        return  assignmentService.validateAssignment(assignmentDTO);
    }
    @GetMapping("/assignments/validateByUuid")
    @PostAuthorize("hasAuthority('USER')")
    public boolean validateAssignmentUuid ( @RequestParam(name = "uuid")  String uuid, @RequestParam(name = "secret")  String secret){
        return assignmentService.validateAssignmentByUuid(uuid,secret);
    }

    @PostMapping("/assignments/cancel")
    public void cancelAssignment(@RequestBody AssignmentDTO assignmentDTO){
        assignmentService.canceledAssignment(assignmentDTO);
    }

    @PostMapping("/assignments/reject")
    public void rejectAssignment( @RequestBody AssignmentDTO assignmentDTO){
        assignmentService.rejectMission(assignmentDTO);
    }

    @GetMapping("/assignments/rejectByUuid")
    public boolean rejectAssignmentUUID(@RequestParam String uuid, @RequestParam String secret){
        return assignmentService.rejectAssignmentByUuid(uuid, secret);
    }
    @PostMapping("/assignments/resend")
    public void resendAssignment(@RequestBody AssignmentDTO assignmentDTO){
        assignmentService.resendAssignment(assignmentDTO);
    }

    @GetMapping("/assignments/principal")
    public PersonalDTO getPrincipal() throws PersonalNotFoundException {
        return this.assignmentService.getPrincipal();
    }

    @GetMapping("/pdf/generate/{assignId}")
    public void printAssignment(@PathVariable ("assignId") Long assignId , HttpServletResponse response) throws IOException, assignmentNotFoundException {


        AssignmentDTO assignmentDTO = assignmentService.findAssignmentById(assignId);
       // Path filePath = get(DIRECTORY).toAbsolutePath().normalize().resolve(assignmentDTO.getUuid());

        assignmentService.printMission(assignmentDTO);
        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "inline; filename=" + assignmentDTO.getUuid() + ".pdf" );

        File downloadDir = new File(msg.getMessage("application.mission.downloaddir"));
        InputStream is = Files.newInputStream(Paths.get(downloadDir.getPath() + "/" + assignmentDTO.getUuid() + ".pdf"));

        org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
        response.flushBuffer();





        /*Path filePath = get(DIRECTORY).toAbsolutePath().normalize().resolve(assignmentDTO.getUuid());
       *//* if(!Files.exists(filePath)){

            throw new FileNotFoundException(assignmentDTO.getUuid()+".pdf" + " was not found on the server");
        }*//*
        //Resource resource = new UrlResource(filePath.toUri());
        ByteArrayInputStream bais = ;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("File-Name",assignmentDTO.getUuid());
        httpHeaders.add(CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());
        System.out.println();
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                .headers(httpHeaders).body(assignmentService.printMission(assignmentDTO));*/
    }


}
