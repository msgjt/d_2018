package ro.msg.edu.jbugs.bugmanagement.business.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ro.msg.edu.jbugs.bugmanagement.business.dto.BugDTO;
import ro.msg.edu.jbugs.bugmanagement.business.dto.BugDTOHelper;
import ro.msg.edu.jbugs.bugmanagement.business.dto.NameIdDTO;
import ro.msg.edu.jbugs.bugmanagement.business.exceptions.BusinessException;
import ro.msg.edu.jbugs.bugmanagement.business.exceptions.ExceptionCode;
import ro.msg.edu.jbugs.bugmanagement.persistence.dao.BugPersistenceManager;
import ro.msg.edu.jbugs.bugmanagement.persistence.entity.Bug;
import ro.msg.edu.jbugs.bugmanagement.persistence.entity.Severity;
import ro.msg.edu.jbugs.bugmanagement.persistence.entity.Status;
import ro.msg.edu.jbugs.usermanagement.persistence.dao.UserPersistenceManager;
import ro.msg.edu.jbugs.usermanagement.persistence.entity.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BugManagementTest {


    @Mock
    private UserPersistenceManager userPersistenceManager;

    @Mock
    private BugPersistenceManager bugPersistenceManager;

    @InjectMocks
    private BugManagementService bugManagementController;


    @Test
    public void createBug_ExpectedOK() {
        User userUsed = new User();
        userUsed.setId(1L);
        userUsed.setUsername("ionion");
        when(userPersistenceManager.getUserByUsername(any(String.class)))
                .thenReturn(Optional.of(userUsed));
        BugDTO bugDTO = new BugDTO();
        bugDTO.setTitle("ceva");
        bugDTO.setTargetDateString("2018-12-25");
        bugDTO.setDescription("A wonderful serenity has taken possession of my entire soul, like these sweet mornings of spring which I enjoy with my whole heart. I am alone, and feel the charm of existence in this spot, which was created for the bliss of souls like mine. I am so ha");
        bugDTO.setVersion("1aa.2bb.3cc");
        bugDTO.setFixedVersion("1.2.3");
        NameIdDTO user= new NameIdDTO();
        user.setId(1L);
        user.setUsername("ionion");
        bugDTO.setCreatedByUser(user);
        bugDTO.setAssignedTo(user);
        BugDTO bug2DTO = new BugDTO();
        bug2DTO.setTitle("ceva22");
        bug2DTO.setTargetDateString("2018-12-25");
        bug2DTO.setDescription("A wonderful serenity has taken possession of my entire soul, like these sweet mornings of spring which I enjoy with my whole heart. I am alone, and feel the charm of existence in this spot, which was created for the bliss of souls like mine. I am so ha");
        bug2DTO.setVersion("1aa.2bb.3cc");
        bug2DTO.setFixedVersion("1.2.3");
        bug2DTO.setCreatedByUser(user);
        bug2DTO.setAssignedTo(user);
        when(bugPersistenceManager.createBug(any(Bug.class)))
                .thenReturn(new Bug());
        when(bugPersistenceManager.createBug(any(Bug.class)))
                .thenReturn(new Bug());
        try {
            assertEquals(bugDTO, bugManagementController.createBug(bugDTO));
            assertEquals(bug2DTO, bugManagementController.createBug(bug2DTO));
        } catch (BusinessException e) {
            fail("Should not reach this point");
        }


    }

    @Test
    public void getAllBugs_ExpectedOK() {
        User userUsed= new User();
        userUsed.setId(1L);
        userUsed.setUsername("ionion");
        Bug bug=new Bug();
        bug.setId(1L);
        bug.setAssignedTo(userUsed);
        bug.setCreatedByUser(userUsed);
        when(userPersistenceManager.getUserById(any(Long.class)))
                .thenReturn(Optional.of(new User()));
        when(bugPersistenceManager.getAllBugs())
                .thenReturn(Arrays.asList(bug,bug));
        assertEquals(2, bugManagementController.getAllBugs().size());
    }


    @Test
    public void getBugsWithId() {
        User userUsed= new User();
        userUsed.setId(1L);
        userUsed.setUsername("ionion");
        Bug bug=new Bug();
        bug.setId(1L);
        bug.setAssignedTo(userUsed);
        bug.setCreatedByUser(userUsed);
        Bug bug2=new Bug();
        bug2.setId(2L);
        bug2.setAssignedTo(userUsed);
        bug2.setCreatedByUser(userUsed);
        when(userPersistenceManager.getUserById(any(Long.class)))
                .thenReturn(Optional.of(new User()));
        when(bugPersistenceManager.createBug(bug)).thenReturn(bug);
        when(bugPersistenceManager.getAllBugs())
                .thenReturn(Arrays.asList(bug,bug2));
        ArrayList<Long> titles=new ArrayList<>();
        titles.add(1L);
        assertEquals(1,bugManagementController.getBugsWithId(titles).size());
    }

    @Test
    public void getBugWithId_ExpectedException() {

        when(bugPersistenceManager.getBugById(any(Long.class))).thenReturn(Optional.empty());
        try {
            bugManagementController.getBugById(100L);
            fail("Should not reach this point");
        } catch (BusinessException e) {
            assertEquals(ExceptionCode.BUG_NOT_EXIST,e.getExceptionCode());
        }

    }

    @Test
    public void sort() {
        User userUsed= new User();
        userUsed.setId(1L);
        userUsed.setUsername("ionion");
        Bug bug=new Bug();
        bug.setId(1L);
        bug.setTitle("ceva");
        bug.setVersion("1.2.3");
        bug.setAssignedTo(userUsed);
        bug.setCreatedByUser(userUsed);
        Bug bug2=new Bug();
        bug2.setId(2L);
        bug2.setTitle("ceva2");
        bug2.setVersion("1.2.3");
        bug2.setAssignedTo(userUsed);
        bug2.setCreatedByUser(userUsed);
        when(bugPersistenceManager.sort(any(boolean.class),any(boolean.class)))
                .thenReturn(Arrays.asList(bug,bug2));
        BugDTO bugDTO=BugDTOHelper.fromEntity(bug);
        bugManagementController.setUsersDTO(bugDTO,bug);
        BugDTO bugDTO2=BugDTOHelper.fromEntity(bug2);
        bugManagementController.setUsersDTO(bugDTO2,bug2);
        assertEquals(bugDTO.getId(),bugManagementController.sort(true, true).get(0).getId());
        assertEquals(bugDTO2.getId(),bugManagementController.sort(true, true).get(1).getId());
    }


    @Test
    public void filterAllFields_ExpectedOK() {
        User userUsed= new User();
        userUsed.setId(1L);
        userUsed.setUsername("ionion");
        Bug bug=new Bug();
        bug.setTitle("ceva");
        bug.setDescription("A wonderful serenity has taken possession of my entire soul, like these sweet mornings of spring which I enjoy with my whole heart. I am alone, and feel the charm of existence in this spot, which was created for the bliss of souls like mine. I am so ha");
        bug.setVersion("1aa.2bb.3cc");
        bug.setFixedVersion("1.2.3");
        bug.setSeverity(Severity.HIGH);
        bug.setStatus(Status.IN_PROGRESS);
        bug.setAssignedTo(userUsed);
        bug.setCreatedByUser(userUsed);
        bug.setId(1L);
        when(bugPersistenceManager.filter(any(String.class),any(String.class),any(Status.class),any(Severity.class),any(Long.class))).thenReturn( Arrays.asList(bug));
        BugDTO bugDTO=BugDTOHelper.fromEntity(bug);
        bugManagementController.setUsersDTO(bugDTO,bug);
        assertEquals(bugDTO.getId(),bugManagementController.filter("ceva","A wonderful serenity has taken possession of my entire soul, like these sweet mornings of spring which I enjoy with my whole heart. I am alone, and feel the charm of existence in this spot, which was created for the bliss of souls like mine. I am so ha",Status.IN_PROGRESS,Severity.HIGH,0,25,1L).get(0).getId());

    }

    @Test
    public void testvalidateDescription_ExpectedException()  {
        try{
            bugManagementController.validateDescription("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. A");
            fail("Should not reach this point");
        }
        catch( BusinessException e)
        {
            assertEquals(ExceptionCode.DESCRIPTION_TOO_SHORT,e.getExceptionCode());
        }
    }

    @Test
    public void testValidateVersion_ExpectedOK() {
        try {
            assertTrue(bugManagementController.validateVersion("1ccc.2bbb.3aaa"));
        } catch (BusinessException e) {
            fail("Should not reach this point");
        }
    }

    @Test
    public void testValidateVersion_ExpectedException() {
        try{
            bugManagementController.validateVersion("//////");
            fail("Should not reach this point");
        }
        catch( BusinessException e)
        {
            assertEquals(ExceptionCode.VERSION_NOT_VALID,e.getExceptionCode());
        }
    }



    @Test
    public void testValidateBug_ExpectedOK()  {
        Bug bug=new Bug();
        bug.setTitle("ceva");
        bug.setDescription("A wonderful serenity has taken possession of my entire soul, like these sweet mornings of spring which I enjoy with my whole heart. I am alone, and feel the charm of existence in this spot, which was created for the bliss of souls like mine. I am so ha");
        bug.setVersion("1aa.2bb.3cc");
        bug.setFixedVersion("1.2.3");
        bug.setTargetDate(new Date());
        bug.setSeverity(Severity.HIGH);
        bug.setStatus(Status.IN_PROGRESS);
        User user=new User();
        user.setFirstName("Ion");
        user.setLastName("Ion");
        user.setEmail("quer@msggroup.com");
        user.setPassword("Parola12.34");
        user.setPhoneNumber("0002220001");
        bug.setAssignedTo(user);
        bug.setCreatedByUser(user);
        try {
            assertTrue(bugManagementController.isBugValid(bug));
        } catch (BusinessException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testValidateBug_ExpectedFail(){
        Bug bug=new Bug();
        bug.setTitle("ceva");
        bug.setDescription("of spring which I enjoy with my whole heart. I am alone, and feel the charm of existence in this spot, which was created for the bliss of souls like mine. I am so ha");
        bug.setVersion("1");
        bug.setFixedVersion("1.2.3");
        bug.setTargetDate(new Date());
        bug.setSeverity(Severity.HIGH);
        bug.setStatus(Status.IN_PROGRESS);
        User user=new User();
        user.setFirstName("Ion");
        user.setLastName("Ion");
        user.setEmail("quer@msggroup.com");
        user.setPassword("Parola12.34");
        user.setPhoneNumber("0002220001");
        bug.setAssignedTo(user);
        bug.setCreatedByUser(user);
        try{
            bugManagementController.isBugValid(bug);
            fail("Should not reach this point");
        }
        catch( BusinessException e)
        {
            assertEquals(ExceptionCode.DESCRIPTION_TOO_SHORT,e.getExceptionCode());
        }

    }

    @Test
    public void testvalidateDescription_ExpectedOK() {
        try {
            assertEquals(true, bugManagementController.validateDescription("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium q"));
        } catch (BusinessException e) {
            fail("Should not reach this point");
        }
    }
}