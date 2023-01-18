package com.api.parkingcontrol.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
        
        // Verificações antes de cadastrar um novo registro
        // Verifica a se a Placa do carro já existe
        if(parkingSpotService.existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Plate Car is already in use!");
        }
        // Verifica se o Númeto da vaga esta em uso
        if(parkingSpotService.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is already in use!");
        }
        // Verifica se o apartamento e o bloco já estão registrados
        if(parkingSpotService.existsByApartmentAndBlock(parkingSpotDto.getApartment(), parkingSpotDto.getBlock())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot already registered for this apartment/block!");
        }
        
        // Define um objeto do tipo ParkingSpotModel (A partir da JDK 9 - o var identifica o tipo da variavel passada, como no javascript)
        var parkingSpotModel = new ParkingSpotModel();
        // Conversão do Dto passado pelo cliente para Model, que vai para o banco
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
        // Data que não foi passada pelo cliente
        parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        // Retorno com os dados do metodo save dados do registro inserido)
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpotModel));
    }

    // Retorna uma listagem de ParkingSpotModel
    @GetMapping
    public ResponseEntity<List<ParkingSpotModel>> getAllParkingSpots(){
        // Aciona o método findAll() no retorno
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findAll());
    }

    // Retorna um ParkingSpotModel (Object) pelo ID (URI: parking-spot/id)
    /* Passa como parâmetro uma variável do tipo UUID com a anotação @PathVariable que recebe um value do mesmo nome que foi passado entre as chaves na URI (id)*/
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneParkingSpot(@PathVariable(value = "id") UUID id){
        /* A variável do tipo Optional<ParkingSpotModel recebe o retorno da busca no banco de dados> */
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
        // É verificado se dentro dessa variavel contém algum dado
        if(!parkingSpotModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }
        // Aqui o .get é utilizado para obter o dado da variavel Optional
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModelOptional.get());
    }

    // Método que remove um parkingSpot pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteParkingSpot(@PathVariable(value = "id") UUID id){
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
        if(!parkingSpotModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }
        // Chama o método delete do parkingSpotService passando o objeto retornado pela pesquisa pelo findById()
        parkingSpotService.delete(parkingSpotModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Parking Spot deleted successfully");
    }

    // Método que atualiza um parkingSpot passando um ID e um body válido no formato JSON com os campos a serem atualizados (parkingSpotDto)
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id") UUID id, 
                                                    @RequestBody @Valid ParkingSpotDto parkingSpotDto){
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
        if(!parkingSpotModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }

        /* Primeiro jeito de atualizar os dados */

        /* Insere os novos dados passados por parametro (parkingSpotDto) nos dados retornados do banco pelo ID (parkingSpotModelOptional) */
        /*
        var parkingSpotModel = parkingSpotModelOptional.get();
        parkingSpotModel.setParkingSpotNumber(parkingSpotDto.getParkingSpotNumber());
        parkingSpotModel.setLicensePlateCar(parkingSpotDto.getLicensePlateCar());
        parkingSpotModel.setModelCar(parkingSpotDto.getModelCar());
        parkingSpotModel.setBrandCar(parkingSpotDto.getBrandCar());
        parkingSpotModel.setColorCar(parkingSpotDto.getColorCar());
        parkingSpotModel.setResponsibleName(parkingSpotDto.getResponsibleName());
        parkingSpotModel.setApartment(parkingSpotDto.getApartment());
        parkingSpotModel.setBlock(parkingSpotDto.getBlock());
        */

        /* Segundo jeito de atualizar os dados */

        // Cria uma variavel do Model
        var parkingSpotModel = new ParkingSpotModel();
        // Faz a cópia do Dto passado por parametro para o Model
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
        // Seta o ID retornado do banco que não vai ter alteração
        parkingSpotModel.setId(parkingSpotModelOptional.get().getId());
        // Seta a data do registro retornada do banco no ModelOptional que também não será alterada
        parkingSpotModel.setRegistrationDate(parkingSpotModelOptional.get().getRegistrationDate());

        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.save(parkingSpotModel));
    }
}
