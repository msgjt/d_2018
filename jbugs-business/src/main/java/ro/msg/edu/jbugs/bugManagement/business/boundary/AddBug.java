package ro.msg.edu.jbugs.bugManagement.business.boundary;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import ro.msg.edu.jbugs.bugManagement.business.control.BugManagement;
import ro.msg.edu.jbugs.bugManagement.business.dto.BugDTO;
import ro.msg.edu.jbugs.bugManagement.business.dto.NameIdDTO;
import ro.msg.edu.jbugs.bugManagement.business.dto.BugDTO;
import ro.msg.edu.jbugs.bugManagement.business.dto.BugDTOHelper;
import ro.msg.edu.jbugs.bugManagement.business.dto.NameIdDTO;
import ro.msg.edu.jbugs.bugManagement.persistence.entity.Severity;
import ro.msg.edu.jbugs.bugManagement.persistence.entity.Status;
import ro.msg.edu.jbugs.userManagement.business.control.UserManagementController;
import ro.msg.edu.jbugs.userManagement.business.control.UserManagementController;
import ro.msg.edu.jbugs.userManagement.business.dto.UserDTO;
import ro.msg.edu.jbugs.userManagement.business.exceptions.BusinessException;
import ro.msg.edu.jbugs.userManagement.persistence.entity.User;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

@Path("/add-bug")
public class AddBug {

    @EJB
    private BugManagement bugManagement;

    @EJB
    private UserManagementController userManagement;

    @POST
    @Produces("application/json")
    @Consumes("application/x-www-form-urlencoded")
    public Response addBug(@FormParam("title") String title,
                           @FormParam("description") String description,
                           @FormParam("version") String version,
                           @FormParam("fixedVersion") String fixedVersion,
                           @FormParam("targetDate") String targetDate,
                           @FormParam("severity") String severity,
                           @FormParam("assignedTo") String assignedTo,
                           @FormParam("createdBy") String createdBy



    ){
        try {
            System.out.println("intra aici");
            User user=userManagement.getUserForUsername(assignedTo);
            User user2=userManagement.getUserForUsername(createdBy);
            NameIdDTO assignedUser=new NameIdDTO();
            assignedUser.setId(user.getId());
            assignedUser.setUsername(assignedTo);
            System.out.println("am format un user"+assignedUser.getId());
            NameIdDTO createdUser=new NameIdDTO();
            createdUser.setId(user2.getId());
            createdUser.setUsername(createdBy);
            System.out.println("am format al doilea user"+createdUser.getId());
            BugDTO bugDTO=new BugDTO();
            bugDTO.setTitle(title);
            bugDTO.setDescription(description);
            bugDTO.setVersion(version);
            bugDTO.setFixedVersion(fixedVersion);
            bugDTO.setAssignedTo(assignedUser);
            bugDTO.setStatus(Status.NEW);
            Date date = new java.sql.Date(new SimpleDateFormat("yyyy-mm-dd").parse(targetDate).getTime());
            bugDTO.setTargetDate(date);
            bugDTO.setSeverity(Severity.valueOf(severity));
            System.out.println("severity "+bugDTO.getSeverity());
            bugDTO.setCreatedByUser(createdUser);
            System.out.println("am format bug-ul");

            bugManagement.createBug(bugDTO);

            return Response.status(Response.Status.CREATED).build();

        } catch (BusinessException e) {
            e.printStackTrace();
        } catch (ro.msg.edu.jbugs.bugManagement.business.exceptions.BusinessException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Response.status(Response.Status.CREATED).build();
        }

}

