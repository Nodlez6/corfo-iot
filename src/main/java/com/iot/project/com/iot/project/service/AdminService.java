package com.iot.project.com.iot.project.service;

import com.iot.project.com.iot.project.entity.Admin;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    //AdminRepository adminRepository;

    public Admin getAdmins(){
        //return adminRepository.getAdmins(//admin?)
        return null;
    }

    public Admin getAdminById(long id){
        //return adminRepository.getAdminByid(id);
        return null;
    }

    public void authenticateAdmin(String username, String password){
        //validar admin
        // if( si ese admin no se autentifica ){
        //  throw new UnauthorizedException("Invalid credentials");
        //}
    }
}
