/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiffconverter.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author marcuslf
 */
public class IFDRecord {
    
    public List<IFDField> fields = new ArrayList<>();
    
    public IFDField getFieldByName(String name){
        for(IFDField field : fields){
            if(field.fieldName.equalsIgnoreCase(name))
                return field;
        }
        return null;
    }
    
}
