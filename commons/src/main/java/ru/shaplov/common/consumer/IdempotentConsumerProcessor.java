package ru.shaplov.common.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.UUID;

@Slf4j
public abstract class IdempotentConsumerProcessor<T> {

    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;

    protected IdempotentConsumerProcessor(JdbcTemplate jdbcTemplate,
                                          TransactionTemplate transactionTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
    }

    public final void processMessage(String key,
                                     T payload,
                                     String id,
                                     String orderEventType) {
        UUID messageId = UUID.fromString(id);
        EventHandler<T> processorService = eventProcessorService(orderEventType);
        if (processorService == null) {
            //сервис не обрабатывает данный тип события
            return;
        }
        try {
            transactionTemplate.executeWithoutResult(status -> {
                //если сообщение уже обработано, ничего не делаем
                if (deduplicate(messageId)) {
                    return;
                }
                processorService.processEvent(key, payload, orderEventType);
            });
        } catch (Exception e) {
            //если выполняется компенсирующая транзакция, она должна быть выполнена
            if (!processorService.hasErrorProcessing()) {
                throw e;
            }
            transactionTemplate.executeWithoutResult(status -> {
                //на всякий случай проверяем обработку сообщения
                //вдруг с ненулевой вероятностью мы не получили ответ на
                // удачный commit от предыдущего блока
                if (deduplicate(messageId)) {
                    return;
                }
                processorService.handleError(e, key, payload, orderEventType);
            });
        }
    }

    protected abstract EventHandler<T> eventProcessorService(String orderEventType);

    private boolean deduplicate(UUID messageId) {
        try {
            jdbcTemplate.update("INSERT INTO processed_messages (message_id) VALUES (?)", messageId);
        } catch (DuplicateKeyException e) {
            log.debug("Duplicate message received: {}", messageId);
            return true;
        }
        return false;
    }
}
