package de.grado.finflow.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig
{
    @Bean
    public Queue workflowQueue(
            @Value("${workflow.queue}") String queueName,
            @Value("${workflow.durable}") boolean durable
    )
    {
        return new Queue(queueName, durable);
    }

    @Bean
    public DirectExchange workflowExchange(
            @Value("${workflow.exchange}") String exchangeName,
            @Value("${workflow.durable}") boolean durable
    )
    {
        return new DirectExchange(exchangeName, durable, false);
    }

    @Bean
    public Binding workflowBinding(
            Queue workflowQueue,
            DirectExchange workflowExchange,
            @Value("${workflow.routing-key}") String routingKey
    )
    {
        return BindingBuilder.bind(workflowQueue).to(workflowExchange).with(routingKey);
    }
}
