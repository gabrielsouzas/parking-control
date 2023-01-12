package com.api.parkingcontrol.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.parkingcontrol.dtos.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.services.ParkingSpotService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {
    
    final ParkingSpotService parkingSpotService;

    public ParkingSpotController(ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }

    // Não foi definida uma URI pois já está definida a nivel de classe
    // Ou seja, quando o cliente solicitar o metodo POST com a URI /parking-spot ele será direcionado para o método saveParkingSpot
    // Recebe como parâmetro um parkingSpotDto passado pelo cliente
    // @RequestBody é usado por se tratar de uma API que retorna um JSON
    // @Valid é necessário para que as validações do Dto sejam feitas
    @PostMapping
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto parkingSpotDto){
        // Define um objeto do tipo ParkingSpotModel (A partir da JDK 9 - o var identifica o tipo da variavel passada, como no javascript)
        var parkingSpotModel = new ParkingSpotModel();
        // Conversão do Dto passado pelo cliente para Model, que vai para o banco
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
        // Data que não foi passada pelo cliente
        parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        // Retorno com os dados do metodo save dados do registro inserido)
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpotModel));
    }

}
