package ro.msg.edu.jbugs.bugManagement.business.control;

import ro.msg.edu.jbugs.bugManagement.business.dto.BugDTO;
import ro.msg.edu.jbugs.bugManagement.business.exceptions.BusinessException;

import java.util.List;

public interface BugManagement {

    /**
     * @return a list of DTOs containing information about bugs.
     */
    List<BugDTO> getAllBugs();

    /**
     * Returns a bug entity with the matching id wrapped in an optional.
     * If none exist, returns an empty Optional Object
     *
     * @param id : Long containing the id.
     * @return : Optional, containing a bug entity.
     */
    BugDTO getBugById(Long id) throws BusinessException;
}
