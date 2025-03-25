package com.iot.project.com.iot.project.service;

import com.iot.project.com.iot.project.exception.UnauthorizedException;
import org.springframework.stereotype.Service;

@Service
public class SensorService {
    //repository de sensor
    public long authenticateSensorApiKey(String sensorApiKey){
        //sensorRepository.find o algo asi,
       // if( si ese sensorapikey no existe para algun sensor ){
          //  throw new UnauthorizedException("Invalid sensor api key");
        //}
        //AQUI RETORNAR el id del sensor
        return 1;
    }
}
