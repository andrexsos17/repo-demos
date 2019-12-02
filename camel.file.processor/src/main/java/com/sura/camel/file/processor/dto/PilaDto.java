package com.sura.camel.file.processor.dto;

import lombok.Data;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@Data
@CsvRecord(separator = ";", crlf = "UNIX", skipFirstLine = true)
public class PilaDto {
    @DataField(pos = 1)
    private String tipo;
    @DataField(pos = 2)
    private String nro;
    @DataField(pos = 3)
    private Double aporte;
    @DataField(pos = 4)
    private Double saldo;
}