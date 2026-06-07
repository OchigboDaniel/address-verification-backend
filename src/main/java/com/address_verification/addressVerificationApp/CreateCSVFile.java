package com.address_verification.addressVerificationApp;

import com.address_verification.addressVerificationApp.dto.response.AddressResponse;

import java.util.List;

public class CreateCSVFile {


    public  static String setCSVString(List<AddressResponse> listAddressResponses){

        StringBuilder sb = new StringBuilder();

        //create the headings
        sb.append("Address,State,Country,Email\n");


        //Insert the body
        for(AddressResponse addressResponse : listAddressResponses){
            sb.append(addressResponse.getFormattedAddress()).append(",");
            sb.append(addressResponse.getState()).append(",");
            sb.append(addressResponse.getCountry()).append(",");
            sb.append(addressResponse.getOwnerEmail()).append(",");
        }

        return sb.toString();

    }
}
