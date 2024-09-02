/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiffconverter.compression;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author marcuslf
 */
public class PackBits {
       
    
    public byte[] decompress(byte[] source){
        List<Byte> output = new ArrayList<Byte>();
        for(int i=0; i<source.length;i++){
            byte hex = source[i];
            if(Byte.toUnsignedInt(hex)>Byte.toUnsignedInt((byte)0x80) ){
                int diff = 0x100 - Byte.toUnsignedInt(hex);
                for(int j=0; j<=diff;++j)
                    output.add(source[i+1]);
                i++;
            }else if(Byte.toUnsignedInt(hex)<0x80){
                int j=0;                
                for(;j<=Byte.toUnsignedInt(hex);j++)
                    output.add(source[i+j+1]);
                i+=j;
            }
        }
        byte[] returned = new byte[output.size()];
        for(int i=0;i<output.size();i++)
            returned[i] = output.get(i);
        return returned;
    }
    
}
