package ci.projet.gestmissionbackend;

import ci.projet.gestmissionbackend.dtos.*;
import ci.projet.gestmissionbackend.entities.*;
import ci.projet.gestmissionbackend.enums.*;
import ci.projet.gestmissionbackend.exceptions.*;
import ci.projet.gestmissionbackend.mappers.PersonalMapperImpl;
import ci.projet.gestmissionbackend.repositories.*;
import ci.projet.gestmissionbackend.services.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Stream;

@SpringBootApplication
//@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class GestmissionbackendApplication {



    public static void main(String[] args) {
        SpringApplication.run(GestmissionbackendApplication.class, args);
    }


    //@Bean
    CommandLineRunner commandLineRunner(PersonalService personalService, GradeService gradeService) {
        return args -> {


            for(PersonalRole personalRole: PersonalRole.values()){
                PersonalProfileDTO personalProfileDTO = new PersonalProfileDTO();
                personalProfileDTO.setType(personalRole);
                personalService.saveProfile(personalProfileDTO);
            }

            List<GradeDTO> gradeDTOS = new ArrayList<>();
            for(GradeType gradeType: GradeType.values()){
                GradeDTO gradeDTO = new GradeDTO();
                gradeDTO.setType(gradeType);
                gradeDTO.setLabel(gradeType.getLabel());
                gradeDTOS.add(gradeDTO);
                gradeService.saveGrade(gradeDTO);

            }




           };

        };

       // @Bean
        CommandLineRunner start (PersonalRepository personalRepository,
                AssignementRepository assignementRepository,
                VehicleRepository vehicleRepository,
                WorksiteRepository worksiteRepository,
                ServiceRepository serviceRepository,
                TownRepository townRepository,
                BankAccountRepository bankAccountRepository, PersonalService personalService, GradeService gradeService, PersonalMapperImpl personalMapper,
                                 ServiceService service
                                 ){
            return args -> {
                GradeDTO dto = new GradeDTO();
                List<GradeDTO> gradeDTOS = gradeService.searchGrade("Assistant");
                for (GradeDTO gradeDTO:gradeDTOS) {
                    if (gradeDTO.getType() == GradeType.ASSISTANT){

                        dto.setType(gradeDTO.getType());
                        dto.setId(gradeDTO.getId());
                        dto.setLabel(gradeDTO.getLabel());
                    }
                }
                /*GradeDTO gradeDTO =personalMapper.fromGrade( gradeService.getChefGrade());
                ServiceDTO serviceDTO = new ServiceDTO();
                serviceDTO.setServiceName("Finance");*/


                PersonalDTO personalDTO = new PersonalDTO();
                personalDTO.setPersonalName("Esther");
                personalDTO.setPersonalFullName("KOUADIO");
                personalDTO.setEmail("cmohamededouard@gmail.com");
                personalDTO.setPersonalMatricule("1604");
                personalDTO.setServiceDTO(service.findServiceById(1L));
                personalDTO.setGradeDTO(dto);
                personalDTO.setDirector(false);
                personalDTO.setFonction("Assistante");
                personalDTO.setCivilite(Civilite.MME);
                personalDTO.setPersonalClass(PersonalClass.C);
                personalService.addNewPersonal(personalDTO);

                /*Stream.of("Cisse", "Affou", "Moussa").forEach(name -> {
                    Personal personal = new Personal();
                    personal.setPersonalName(name);
                    personal.setPersonalFullName(name + " prenom");
                    personalRepository.save(personal);
                });
                Stream.of("Technique", "Comptabilite", "Commercial").forEach(serv -> {
                    Service service = new Service();
                    service.setServiceName(serv);
                    serviceRepository.save(service);
                });
                personalRepository.findAll().forEach(personal -> {
                    BankAccount bankAccount = new BankAccount();
                    bankAccount.setBankAccountRib("12333666666");
                    bankAccount.setAccountType(AccountType.CURRENT);
                    bankAccount.setPersonal(personal);
                    bankAccountRepository.save(bankAccount);
                });*/

            };
        }
    }

