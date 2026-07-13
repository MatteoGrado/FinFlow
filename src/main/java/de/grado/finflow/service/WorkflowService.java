package de.grado.finflow.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkflowService
{
    private final RabbitTemplate rabbitTemplate;

    public void releasePayment()
    {
    }
}
