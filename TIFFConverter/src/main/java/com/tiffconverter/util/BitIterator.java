/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiffconverter.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 *
 * @author Marcus Lessa
 */
public class BitIterator implements java.util.Iterator<Byte>{
    
    private byte[] source;
    private int bitsRead = -1;
    private static int INITIALBITMASK = 0X80; // 128 - 10000000 em binario
    
    
    public BitIterator(byte[] source){
        this.source = source;
    }   

    
    public static void main(String args[]) throws Exception{
        File arquivo = new File("D:\\arquivo2.bin");
        FileInputStream fin = new FileInputStream(arquivo);
        BufferedInputStream buffin = new BufferedInputStream(fin);
        //ByteArrayInputStream bais = new ByteArrayInputStream()
        byte[] dados = new byte[(int) arquivo.length()];
        buffin.read(dados);
        BitIterator iterator = new BitIterator(dados);
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }
        System.out.println(iterator.bitsRead);
        System.out.println(dados.length*8);
        System.out.println(Byte.toUnsignedInt(dados[dados.length-1]));
    }
    
    public void jumpToNextByte(){
        int currentBitIndex = bitsRead%8;
        bitsRead += (7 - currentBitIndex);
    }

    @Override
    public boolean hasNext() {
        return (bitsRead+1)<(source.length*8);
    }

    @Override
    public Byte next() {
        bitsRead++;
        int currentByteIndex = bitsRead / 8;
        int currentBitIndex = bitsRead % 8;
        int value = Byte.toUnsignedInt(source[currentByteIndex]);
        int mask = INITIALBITMASK;
        mask >>= currentBitIndex;
        value = value & mask;
        value >>= (7 - currentBitIndex);
        return (byte) value;
    }
    
}
