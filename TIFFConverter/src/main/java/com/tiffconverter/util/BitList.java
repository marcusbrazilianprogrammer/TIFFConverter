package com.tiffconverter.util;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Marcus Lessa
 */
public class BitList{
    
    private byte[] source;
    private boolean[] bitList;
    
    public BitList(byte[] source){
        this.source = new byte[source.length];
        System.arraycopy(source,0,this.source,0,this.source.length);
        bitList = new boolean[source.length*8];
        BitIterator iterator = new BitIterator(source);
        int index = 0;
        while(iterator.hasNext()){
            bitList[index++] = (iterator.next()==1);
        }
    }
    
    public int size() {
        return bitList.length;
    }
    
    public boolean isEmpty() {
        return bitList.length>0;
    }
        
    public Iterator<Byte> iterator() {
        return new BitIterator(this.source);
    }
    
    public Byte get(int i) {
        return bitList[i]?(byte)1:0;
    }
    
}
