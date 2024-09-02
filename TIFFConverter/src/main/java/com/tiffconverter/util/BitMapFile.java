/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiffconverter.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marcus Lessa
 */
public class BitMapFile {
    public short signature = 0x4D42;
    public int fileSize;
    public int dataOffSet;
    public int size = 0x28;
    public int width;
    public int height;
    public short planes;
    public short bitsPerPixel;
    public int compression;
    public int imageSize;
    public int xPixelsPerM;
    public int yPixelsPerM;
    public int colorsUsed;
    public int importantColors;
    public byte[][] colorTable;
    public byte[] pixelData;
    
    
    public byte[] toByteArray(){
        List<Byte> listaAuxiliar = new ArrayList<Byte>();
        List<Number> values = new ArrayList<Number>();        
        fileSize = pixelData.length + 14 + 40 + (colorTable!=null?colorTable.length*4:0);        
        this.dataOffSet = 54 + (colorTable!=null?colorTable.length*4:0);        
        
        values.add(signature);
        values.add(fileSize);
        values.add(0);
        values.add(dataOffSet);
        values.add(size);
        values.add(width);
        values.add(height);
        values.add(planes);
        values.add(bitsPerPixel);
        values.add(compression);
        values.add(imageSize);
        values.add(xPixelsPerM);
        values.add(yPixelsPerM);
        values.add(colorsUsed);
        values.add(importantColors);
        if (colorTable != null) {
            for (byte[] bs : colorTable) {
                for (byte b : bs) {
                    values.add(b);
                }
            }
        }

        for(Number number : values){
            addList(listaAuxiliar, from(number));
        }
        
        byte[] retorno = new byte[listaAuxiliar.size()+pixelData.length];
        for(int i=0;i<listaAuxiliar.size();i++)
            retorno[i] = listaAuxiliar.get(i);
        
        
        /*ByteBuffer buffer = ByteBuffer.allocate(fileSize);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort(signature);
        buffer.putInt(fileSize);
        buffer.putInt(0);
        buffer.putInt(dataOffSet);
        buffer.putInt(size);
        buffer.putInt(width);
        buffer.putInt(height);
        buffer.putShort(planes);
        buffer.putShort(bitsPerPixel);
        buffer.putInt(compression);
        buffer.putInt(imageSize);
        buffer.putInt(xPixelsPerM);
        buffer.putInt(yPixelsPerM);
        buffer.putInt(colorsUsed);
        buffer.putInt(importantColors);
        if(colorTable!=null){
            for(byte[] bs:colorTable){                
                buffer.put(bs);
            }
        }
        buffer.put(pixelData);
        return buffer.array();*/
        
        
        System.arraycopy(pixelData, 0, retorno, dataOffSet, pixelData.length);
        return retorno;
    }
    
    public static byte[] from(Number source){
        if(source instanceof Byte)
            return from(source.byteValue());
        else if(source instanceof Short)
            return from(source.shortValue());
        else if(source instanceof Integer)
            return from(source.intValue());
        return new byte[0];
    }
    
    public static byte[] from(Byte source){
        ByteBuffer buffer = ByteBuffer.allocate(Byte.BYTES);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(source);
        return buffer.array();
    }
    
    public static byte[] from(Integer source){
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(source);
        return buffer.array();
    }
    
    public static byte[] from(Short source){
        ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort(source);
        return buffer.array();
    }
    
    private static void addList(List<Byte> listaDestino, byte[] arrayOrigem){
        for(byte item : arrayOrigem){
            listaDestino.add(item);
        }
    }
   /* 
    public static void main(String args[]) throws Exception{
        File f = new File("\\\\paco100\\matriculas$\\Matriculas-v2\\Livro_02_20240715");
        //File f = new File("C:\\Livro_02");
        for(File arquivo : f.listFiles()){
            if(arquivo.isDirectory())
                renameArquivos(arquivo);
            else{
                renameArquivo(arquivo);                    
            }
        }
    }
    */
    
    public static void main2(String[] args) throws Exception{
        File arquivo = new File("D:\\arquivo1.bin");        
        FileInputStream fin = new FileInputStream(arquivo);
        BufferedInputStream buffin = new BufferedInputStream(fin);        
        //ByteArrayInputStream bais = new ByteArrayInputStream()
        byte[] bytes = new byte[(int)arquivo.length()];
        buffin.read(bytes);
        int h=1;
        PrintWriter out = new PrintWriter("D:\\arquivo2.txt");
        for(byte b : bytes){
            String bin = String.format("%8s",Integer.toBinaryString(Byte.toUnsignedInt(b))).replace(' ', '0');
            out.write(bin);
            
        }
        out.flush();
        out.close();        
        buffin.close();
    }
    
    public static void main(String[] args) throws Exception{
        File arquivo = new File("d:\\arquivo1.bin");        
        FileInputStream fin = new FileInputStream(arquivo);
        BufferedInputStream buffin = new BufferedInputStream(fin);        
        //ByteArrayInputStream bais = new ByteArrayInputStream()
        byte[] bytes = new byte[(int)arquivo.length()];
        buffin.read(bytes);
        int index = 0;
        for(byte b : bytes){
            System.out.println(String.format("%d: %02X", index++,Byte.toUnsignedInt(b)));
        }
    }
    
    
    public static void renameArquivos(File directory){
        for(File arquivo : directory.listFiles()){
            if(arquivo.isDirectory())
                renameArquivos(arquivo);
            else{
                renameArquivo(arquivo);                    
            }
        }
    }
    
    public static void renameArquivo(File arquivo){
                String newName = arquivo.getName();
                if(!newName.endsWith(".tif")){
                    arquivo.renameTo(new File(arquivo.getParent(),newName+".tif"));
                }
    }
    
}
