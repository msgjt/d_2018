package ro.msg.edu.jbugs.bugmanagement.business.dto;


import ro.msg.edu.jbugs.bugmanagement.persistence.entity.Bug;
import ro.msg.edu.jbugs.bugmanagement.persistence.entity.Severity;

import java.util.List;
import java.util.stream.Collectors;


public class BugDTOHelper {



    private BugDTOHelper() {
        //hiding the default constructor.
    }

    public static BugDTO fromEntity(Bug bug) {

        BugDTO bugDTO = new BugDTO();
        bugDTO.setId(bug.getId());
        bugDTO.setTitle(bug.getTitle());
        bugDTO.setDescription(bug.getDescription());
        bugDTO.setVersion(bug.getVersion());
        bugDTO.setTargetDate(bug.getTargetDate());
        bugDTO.setStatus(bug.getStatus());
        bugDTO.setFixedVersion(bug.getFixedVersion());
        bugDTO.setSeverity(bug.getSeverity());
        NameIdDTO createdBy = new NameIdDTO();

        createdBy.setId(bug.getCreatedByUser().getId());
        createdBy.setUsername(bug.getCreatedByUser().getUsername());
        bugDTO.setCreatedByUser(createdBy);

        NameIdDTO assignedTo = new NameIdDTO();

        assignedTo.setId(bug.getAssignedTo().getId());
        assignedTo.setUsername(bug.getAssignedTo().getUsername());
        bugDTO.setAssignedTo(assignedTo);

        if (!bug.getAttachments().isEmpty()) {
            bugDTO.setFileName(bug.getAttachments().get(0).getName());
        }

        return bugDTO;

    }

    public static List<BugDTO> fromBugList(List<Bug> bugList) {
        return bugList
                .stream()
                .map(BugDTOHelper::fromEntity)
                .collect(Collectors.toList());
    }


    public static Bug updateBugWithDTO(Bug bug, BugDTO bugDTO){
        bug.setId(bugDTO.getId());
        bug.setTitle(bugDTO.getTitle());
        bug.setDescription(bugDTO.getDescription());
        bug.setVersion(bugDTO.getVersion());
        bug.setFixedVersion(bugDTO.getFixedVersion());
        bug.setTargetDate(bugDTO.getTargetDate());
        bug.setSeverity(Severity.valueOf(bugDTO.getSeverityString()));

        return bug;
    }


    public static Bug toEntity(BugDTO bugDTO) {
        Bug bug = new Bug();

        bug.setTitle(bugDTO.getTitle());
        bug.setDescription(bugDTO.getDescription());
        bug.setTargetDate(bugDTO.getTargetDate());
        bug.setStatus(bugDTO.getStatus());
        bug.setFixedVersion(bugDTO.getFixedVersion());
        bug.setSeverity(bugDTO.getSeverity());

        return bug;
    }



}
