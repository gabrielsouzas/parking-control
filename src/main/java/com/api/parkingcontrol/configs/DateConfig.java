package com.api.parkingcontrol.configs;

import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

// Essa anotação serve para dizer ao Spring que essa é uma classe de configuração, ou seja, ao iniciar o projeto ela será solicitada
@Configuration
public class DateConfig {

    // Formatação global para a data (Z = UTC)
    public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    // Seta no serializer o datetime definido
    public static LocalDateTimeSerializer LOCAL_DATETIME_SERIALIZER = new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATETIME_FORMAT));

    /* Por default o Sprint utiliza do ObjectMapper sempre que tiver serializações, assim faz o spring assumir esse tipo de data passado como padrão
     * Como o ObjectMapper é uma classe externa da aplicação, é necessário informar pela anotação @Bean que esse método é um produtor
     * A anotação @Primary é necessária por questões de prioridade, caso sejam gerados outros beans para ObjectMapper
    */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(LOCAL_DATETIME_SERIALIZER);
        return new ObjectMapper()
                .registerModule(module);
    }

}
