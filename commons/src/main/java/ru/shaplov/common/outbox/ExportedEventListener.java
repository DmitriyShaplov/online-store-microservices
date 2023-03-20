package ru.shaplov.common.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.postgresql.util.PGobject;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ExportedEventListener {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public ExportedEventListener(JdbcTemplate jdbcTemplate,
                                 ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    @EventListener
    public void handleExportedEvent(ExportedEvent<?> exportedEvent) {
        String paylaod = objectMapper.writeValueAsString(exportedEvent.getPayload());
        PGobject jsonPayload = new PGobject();
        jsonPayload.setType("json");
        jsonPayload.setValue(paylaod);
        jdbcTemplate.update("""
                        INSERT INTO outbox (id, aggregatetype, aggregateid, type, payload) VALUES
                        (?, ?, ?, ?, ?)
                        """,
                UUID.randomUUID(),
                exportedEvent.getAggregateType(),
                exportedEvent.getAggregateId(),
                exportedEvent.getType(),
                jsonPayload);
    }
}
