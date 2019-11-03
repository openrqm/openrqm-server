package org.openrqm.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import javax.validation.Valid;
import org.openrqm.model.RQMElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ElementApiController implements ElementApi {
    private static final Logger logger = LoggerFactory.getLogger(ElementApiController.class);

    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    public ElementApiController(ObjectMapper objectMapper, HttpServletRequest request) {
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
    public ResponseEntity<Void> deleteElement(@ApiParam(value = "The element to update") @Valid @RequestBody RQMElement element) {
        try {
            jdbcTemplate.update("DELETE FROM element WHERE id = ?;", element.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DataAccessException ex) {
            logger.error(ex.getLocalizedMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> patchElement(@ApiParam(value = "The element to update") @Valid @RequestBody RQMElement element) {
        try {
            jdbcTemplate.update("UPDATE element SET element_type_id = ?, content = ?, rank = ?, parent_element_id = ? WHERE id = ?;",
                element.getElementTypeId(), element.getContent(), element.getRank(), element.getParentElementId(), element.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DataAccessException ex) {
            logger.error(ex.getLocalizedMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<RQMElement> postElement(@ApiParam(value = "The element to create") @Valid @RequestBody RQMElement element) {
        try {
            jdbcTemplate.update("INSERT INTO element(id, document_id, element_type_id, content, rank, parent_element_id) VALUES (?, ?, ?, ?, ?, ?);",
                0, element.getDocumentId(), element.getElementTypeId(), element.getContent(), element.getRank(), element.getParentElementId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DataAccessException ex) {
            logger.error(ex.getLocalizedMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> putElement(@ApiParam(value = "The element to update") @Valid @RequestBody RQMElement element) {
        try {
            jdbcTemplate.update("UPDATE element SET element_type_id = ?, content = ?, rank = ?, parent_element_id = ? WHERE id = ?;",
                element.getElementTypeId(), element.getContent(), element.getRank(), element.getParentElementId(), element.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DataAccessException ex) {
            logger.error(ex.getLocalizedMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}