package com.uf.stock.data.exception;

public class DataSyncException extends Exception{
public DataSyncException(String msg,Throwable throwable){
  super(msg,throwable);
}
public DataSyncException(String msg){
  super(msg);
}
}
