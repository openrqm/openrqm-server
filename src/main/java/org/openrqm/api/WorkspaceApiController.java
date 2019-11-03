package org.openrqm.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import javax.validation.Valid;
import static org.openrqm.api.WorkspaceApi.log;
import org.openrqm.mapper.WorkspaceRowMapper;
import org.openrqm.model.RQMWorkspace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class WorkspaceApiController implements WorkspaceApi {
    private static final Logger logger = LoggerFactory.getLogger(WorkspaceApiController.class);

    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    public WorkspaceApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @Override
    public Optional<ObjectMapper> getObjectMapper() {
        return Optional.ofNullable(objectMapper);
    }

    @Override
    public Optional<HttpServletRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<RQMWorkspace> getWorkspace() {
        try {
            Long id = new Long(1);
            RQMWorkspace workspace = jdbcTemplate.queryForObject("SELECT * FROM workspace WHERE id = ?;", new Object[] { id } , new WorkspaceRowMapper());
            return new ResponseEntity<>(workspace, HttpStatus.OK);
        } catch (DataAccessException ex) {
            logger.error(ex.getLocalizedMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Override
    public ResponseEntity<RQMWorkspace> postWorkspace(@ApiParam(value = "The workspace to create") @Valid @RequestBody RQMWorkspace workspace) {
        try {
            jdbcTemplate.update("INSERT INTO workspace(id, name, workspace_id) VALUES (?, ?, ?);",
                    0, workspace.getName(), workspace.getWorkspaceId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DataAccessException ex) {
            logger.error(ex.getLocalizedMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}